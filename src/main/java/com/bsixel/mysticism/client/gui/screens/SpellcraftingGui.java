package com.bsixel.mysticism.client.gui.screens;

import com.bsixel.mysticism.MysticismMod;
import com.bsixel.mysticism.client.gui.widgets.PlusButton;
import com.bsixel.mysticism.client.gui.widgets.ComponentSelectorButton;
import com.bsixel.mysticism.client.gui.widgets.SpellButton;
import com.bsixel.mysticism.client.gui.widgets.SpellComponentButton;
import com.bsixel.mysticism.common.api.capability.spellcasting.ISpellcaster;
import com.bsixel.mysticism.common.api.capability.spellcasting.SpellcasterCapability;
import com.bsixel.mysticism.common.api.math.tree.PositionableTree;
import com.bsixel.mysticism.common.api.math.tree.PositionableTreeConfig;
import com.bsixel.mysticism.common.api.math.tree.PositionableTreeLayout;
import com.bsixel.mysticism.common.api.spells.ISpellComponent;
import com.bsixel.mysticism.common.api.spells.Spell;
import com.bsixel.mysticism.common.api.spells.SpellComponentInstance;
import com.bsixel.mysticism.common.api.spells.SpellHelper;
import com.bsixel.mysticism.common.networking.NetworkManager;
import com.bsixel.mysticism.common.networking.packets.MysticismSpellcasterPacketFromClient;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.util.InputMappings;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.abego.treelayout.Configuration;
import org.abego.treelayout.util.FixedNodeExtentProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.bsixel.mysticism.client.gui.GuiHelper.parseAlpha;

@OnlyIn(Dist.CLIENT)
public class SpellcraftingGui extends ScreenBase {
    private static final Logger logger = LogManager.getLogger(MysticismMod.MOD_ID);
    private static final ResourceLocation background = new ResourceLocation(MysticismMod.MOD_ID, "textures/gui/spellcrafting-background.png");

    private static final int nodeDistance = 32;
    private int rootNodeX;
    private int rootNodeY;
    private int dragX = 0;
    private int dragY = 0;
    private Spell activeSpell = null;
    private int activeSpellIndex = -1;
    private TextFieldWidget componentSearchField;
    private TextFieldWidget spellNameField;
    private List<ISpellComponent> filteredComponents = new ArrayList<>();
    private int activeComponentIndex = -1;
    private ISpellcaster playerSpellData;

    private int bottomSpell = 0;
    private int bottomComponent = 0;

    private SpellComponentInstance selectedComponent = null;

    private SpellComponentButton root;
    private PositionableTree<SpellComponentButton> componentButtonTree;
    private FixedNodeExtentProvider<SpellComponentButton> extentProvider;
    private PositionableTreeConfig<SpellComponentButton> treeConfiguration;
    private PositionableTreeLayout<SpellComponentButton> treeLayout;

    public SpellcraftingGui() {
        super(new TranslationTextComponent("gui.spellcrafting.name"));
        // Setup the player's spell data
        this.refreshPlayerSpellData();

        this.sizeX = 256;
        this.sizeY = 198;
    }

    private void refreshPlayerSpellData() {
        MysticismMod.getInstance().getSideSafeProxy().getPlayer().getCapability(SpellcasterCapability.spellcaster_cap, null).ifPresent(spellcasterCap -> this.playerSpellData = spellcasterCap);
    }


    @Override
    protected void init() {
        super.init();
        this.guiLeft = (this.width - this.sizeX) / 2; // Quarter from left
        this.guiTop = (this.height - this.sizeY) / 2; // Quarter from top
        this.rootNodeX = this.guiLeft + 124 - (SpellComponentButton.getButtonWidth() / 2);
        this.rootNodeY = this.guiTop + 7;
        this.filteredComponents = SpellHelper.getActiveComponents(); // TODO: Only whatever components the user can access

        for (int i = 0; i < 5; i++) { // Render each spell selector button within view
            SpellButton spellButton = new SpellButton(this.guiLeft + 6, this.guiTop + 8 + (i * 26), 20, 20, this, this.itemRenderer, i, this::pressSpellButton, this::renderSpellTooltip);
            this.addButton(spellButton);
        }

        for (int i = 0; i < 7; i++) { // Render each component selector button within view
            ComponentSelectorButton selectorButton = new ComponentSelectorButton(this.getLeftMainBorder() + 6 + (i * 26), this.getBottomMainBorder() + 6, 20, 20, this, this.itemRenderer, i, this::pressComponentSelectorButton, this::renderComponentSelectorButtonTooltip);
            this.addButton(selectorButton);
        }

        // To add spells
        PlusButton addSpellButton = new PlusButton(this.guiLeft + 6, this.getBottomMainBorder() + 6, btn -> this.addSpell(), this::renderAddSpellTooltip);
        this.addButton(addSpellButton);

        // Naming spells TODO: Translations everywhere
        this.spellNameField = new TextFieldWidget(this.font, this.getLeftMainBorder(), this.guiTop + 3, 100, 10, new TranslationTextComponent(""));
        this.spellNameField.setText("");
        this.spellNameField.setResponder(str -> {
            this.activeSpell.setName(str);
            this.updateRemoteSpellData();
        });
        this.spellNameField.visible = false;
        this.addButton(this.spellNameField);

        // Searching for usable components
        this.componentSearchField = new TextFieldWidget(this.font, this.getLeftMainBorder(), this.guiTop + this.sizeY + 1, 100, 10, new TranslationTextComponent(""));
        this.componentSearchField.setText("");
        this.componentSearchField.setResponder(str -> {
            this.filteredComponents = SpellHelper.getActiveComponents().stream().filter(comp -> comp.getName().getString().toLowerCase().contains(str.toLowerCase())).collect(Collectors.toList());
        });
        this.addButton(this.componentSearchField);

    }

    public ISpellComponent getComponentForIndex(int index) {
        return this.filteredComponents.get(index);
    }

    public int getComponentCount() {
        return this.filteredComponents.size();
    }

    private void renderAddSpellTooltip(Button button, MatrixStack ms, int mouseX, int mouseY) {
        this.renderTooltip(ms, new StringTextComponent("Add Spell"), mouseX, mouseY);
    }

    private void addSpell() {
        Spell newSpell = new Spell(MysticismMod.getInstance().getSideSafeProxy().getPlayer());
        newSpell.root = new SpellComponentInstance(newSpell, SpellHelper.getCastTypes().get(0));
        newSpell.setIcon(newSpell.root.getComponent().getIcon());
        newSpell.setName(" New Spell " + (this.playerSpellData.getKnownSpells().size() + 1));
        this.playerSpellData.addSpell(newSpell);
        this.setActiveSpell(this.playerSpellData.getKnownSpells().size()-1);
        this.updateRemoteSpellData();
    }

    private void setActiveSpell(int slot) {
        this.activeSpellIndex = slot;
        this.activeSpell = getSpellInSlot(slot);
        this.spellNameField.setText(this.activeSpell.getName());
        this.spellNameField.visible = true;
        this.realizeTree();
    }

    private void pressSpellButton(Button spellButton) { // TODO: Move
        int slot = ((SpellButton) spellButton).getSpellSlot();
        if (slot < this.getNumSpells()) {
            this.setActiveSpell(slot);
        }
    }

    private void pressComponentSelectorButton(Button button) { // TODO: Move
        if (this.selectedComponent != null) {
            ComponentSelectorButton componentSelectorButton = (ComponentSelectorButton) button;
            if (componentSelectorButton.getComponent() != null) {
                this.selectedComponent.setComponent(componentSelectorButton.getComponent());
                this.updateRemoteSpellData();
            }
        }
    }

    private void updateRemoteSpellData() {
        NetworkManager.channel.sendToServer(new MysticismSpellcasterPacketFromClient(this.playerSpellData));
    }

    private void renderSpellTooltip(Button button, MatrixStack ms, int mouseX, int mouseY) { // TODO: Move
        if (((SpellButton) button).getSpellSlot() < this.getNumSpells()) {
            this.renderTooltip(ms, Minecraft.getInstance().fontRenderer.trimStringToWidth(new TranslationTextComponent(((SpellButton) button).getSpell().getName()), Math.max(this.width / 2 - 43, 80)), mouseX, mouseY);
        }
    }

    private void renderComponentSelectorButtonTooltip(Button button, MatrixStack ms, int mouseX, int mouseY) { // TODO: Move
        if (((ComponentSelectorButton) button).getComponent() != null) {
            this.renderTooltip(ms, Minecraft.getInstance().fontRenderer.trimStringToWidth(new TranslationTextComponent(((ComponentSelectorButton) button).getComponent().getName().getString() + ": " + ((ComponentSelectorButton) button).getComponent().getDescription().getString()), Math.max(this.width / 2 - 43, 80)), mouseX, mouseY);
        }
    }

    private void realizeTree() {
        this.buttons.removeIf(btn -> btn instanceof SpellComponentButton); // Apparently we have to remove it from both the buttons/widgets list and the listeners list, odd but alright
        this.children.removeIf(btn -> btn instanceof SpellComponentButton);
        if (this.activeSpell != null && this.activeSpell.root != null) {
            // TODO: There's almost certainly a more performant way to do this but considering how I don't expect spells to get that large, that's a later day task
            SpellComponentButton rootButton = new SpellComponentButton(this.guiLeft, this.guiTop, this.activeSpell.root, this, this.itemRenderer, this::pressComponentButton, this::renderComponentTooltip);
            this.addButton(rootButton);
            this.componentButtonTree = new PositionableTree<>(rootButton);
            rootButton.getWrapper().getChildren().forEach(subChild -> this.registerComponentTreeNode(rootButton, subChild));
            this.extentProvider = new FixedNodeExtentProvider<>(SpellComponentButton.getButtonHeight(), SpellComponentButton.getButtonHeight());
            this.treeConfiguration = new PositionableTreeConfig<>(nodeDistance, nodeDistance, Configuration.Location.Top, Configuration.AlignmentInLevel.AwayFromRoot, this.rootNodeX, this.rootNodeY);
            this.treeLayout = new PositionableTreeLayout<>(this.componentButtonTree, this.extentProvider, this.treeConfiguration);
        }
    }

    private void renderComponentTooltip(Button button, MatrixStack ms, int mouseX, int mouseY) { // TODO: Move
        SpellComponentButton compButton = (SpellComponentButton) button;
        if (compButton.visible && compButton.getWrapper() != null && compButton.getWrapper().getComponent() != null) {
            this.renderTooltip(ms, Minecraft.getInstance().fontRenderer.trimStringToWidth(new TranslationTextComponent(compButton.getWrapper().getComponent().getName().getString() + ": " + compButton.getWrapper().getComponent().getDescription().getString()), Math.max(this.width / 2 - 43, 80)), mouseX, mouseY);
        }
    }

    private void registerComponentTreeNode(SpellComponentButton parent, SpellComponentInstance child) {
        SpellComponentButton childButton = new SpellComponentButton(this.guiLeft, this.guiTop, child, this, this.itemRenderer, this::pressComponentButton, this::renderComponentTooltip);
        this.componentButtonTree.addChild(parent, childButton);
        this.addButton(childButton);
        child.getChildren().forEach(subChild -> this.registerComponentTreeNode(childButton, subChild));
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private void pressComponentButton(Button button) {
        if (InputMappings.isKeyDown(Minecraft.getInstance().getMainWindow().getHandle(), GLFW.GLFW_KEY_LEFT_SHIFT)) { // Add another if we're holding shift when we click
            SpellComponentInstance parent = ((SpellComponentButton)button).getWrapper();
            SpellComponentInstance newComponent = new SpellComponentInstance(this.activeSpell, null);
            parent.addChild(newComponent);
            this.selectedComponent = newComponent;
            this.realizeTree(); // Don't update spell data - we need them to select a valid component first
        } else {
            this.selectedComponent = ((SpellComponentButton)button).getWrapper();
        }
    }

    public SpellComponentInstance getSelectedComponent() {
        return this.selectedComponent;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) { // Key pressed
        if (!this.spellNameField.isFocused() && !this.componentSearchField.isFocused() && this.selectedComponent != null && (keyCode == GLFW.GLFW_KEY_BACKSPACE || keyCode == GLFW.GLFW_KEY_DELETE)) { // Delete the currently selected ability
            for (Widget button : this.buttons) {
                if (button instanceof SpellComponentButton && ((SpellComponentButton) button).getWrapper() == this.selectedComponent) {
                    if (this.activeSpell.root == this.selectedComponent) { // This is the last/root node, just delete the whole spell
                        this.playerSpellData.getKnownSpells().remove(this.activeSpell);
                        this.activeSpell = null;
                        this.spellNameField.visible = false;
                    } else {
                        this.componentButtonTree.getParent((SpellComponentButton) button).getWrapper().removeChild(this.selectedComponent);
                    }
                    this.realizeTree();
                    this.updateRemoteSpellData();
                    return true;
                }
            }
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (this.treeConfiguration != null && this.treeLayout != null && isPointInsideMainGui((int) mouseX, (int) mouseY)) {
            this.dragX += dragX;
            this.dragY += dragY;
            this.treeConfiguration.setInitialX(this.rootNodeX + this.dragX);
            this.treeConfiguration.setInitialY(this.rootNodeY + this.dragY);
            this.treeLayout.recalculate();
        }
        return true;
    }

    @Override
    public void render(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getInstance().getTextureManager().bindTexture(background);
        /* matrixStack, startLeftPos, startTopPos, zIndex, textureSheetStartX, textureSheetStartY, textureSizeXCrop, textureSizeYCrop, textureSizeXScale, textureSizeYScale */
        blit(matrixStack, this.guiLeft, this.guiTop, 0, 0, this.sizeX, this.sizeY);

        if (this.activeSpell != null) {
            this.renderTree(matrixStack, mouseX, mouseY, partialTicks);
        }

        // Render buttons themselves, except the text fields which should be rendered last
        this.buttons.forEach(btn -> {
            if (!(btn instanceof TextFieldWidget)) {
                btn.render(matrixStack, mouseX, mouseY, partialTicks);
            }
        });

        this.buttons.forEach(btn -> {
            if (btn instanceof TextFieldWidget) {
                btn.render(matrixStack, mouseX, mouseY, partialTicks);
            }
        });

        // Render button tooltips
        this.buttons.forEach(btn -> {
            if (btn.visible && btn.isHovered()) {
                btn.renderToolTip(matrixStack, mouseX, mouseY);
            }
        });
    }

    private boolean isPointInsideMainGui(int pointX, int pointY) {
        return pointX >= this.getLeftMainBorder() && pointX <= this.guiLeft + this.sizeX - 3 && pointY >= this.guiTop && pointY <= this.guiTop + this.sizeY - 37;
    }

    public void enableScissorToMainArea() {
        // Stop rendering of any buttons outside of the GUI
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        double scale = getGuiScaleFactor();
        // TODO: Wtf is going on here? Apparently this unlike everything else starts from bottom left.
        GL11.glScissor((int) (this.getLeftMainBorder() * getGuiScaleFactor()), (int) ((this.guiTop + 37) * getGuiScaleFactor()), (int) ((this.sizeX - 37) * getGuiScaleFactor()), (int) ((this.sizeY - 40) * getGuiScaleFactor()));
    }

    public void disableScissor() {
        // Re-enable rendering stuff outside the bounds of the main GUI area so we can draw other things
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

    private void renderTree(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.enableScissorToMainArea();

        // Draw connecting lines
        this.buttons.forEach(btn -> {
            if (btn instanceof SpellComponentButton) { // ONLY DO ALL THIS FOR COMPONENT BUTTONS!
                int buttonSize = SpellComponentButton.getButtonWidth();
                int halfButton = buttonSize / 2;
                btn.visible = isPointInsideMainGui(btn.x + halfButton, btn.y + halfButton);
                SpellComponentButton abilityBtn = (SpellComponentButton) btn;
                List<SpellComponentButton> children = this.componentButtonTree.getChildrenList(abilityBtn);
                if (!children.isEmpty()) {
                    int horizLineLevel = (int) (btn.y + this.treeConfiguration.getGapBetweenLevels(0));
                    int lineColor = parseAlpha("#3068c2");
                    int buttonBottom = btn.y + buttonSize;
                    int buttonMiddle = btn.x + halfButton;
                    SpellComponentButton firstChild = this.componentButtonTree.getFirstChild(abilityBtn);
                    SpellComponentButton lastChild = this.componentButtonTree.getLastChild(abilityBtn);
                    if (children.size() == 1) { // There's only one child node, just draw line straight down
                        this.vLine(matrixStack, buttonMiddle, buttonBottom, firstChild.y - 1, lineColor);
                    } else {
                        this.vLine(matrixStack, buttonMiddle, buttonBottom, horizLineLevel, lineColor);
                        this.hLine(matrixStack, firstChild.x + halfButton, lastChild.x + halfButton, horizLineLevel, lineColor);
                        children.forEach(child -> this.vLine(matrixStack, child.x + halfButton, horizLineLevel, child.y - 1, lineColor));
                    }
                }
            }
        });
        this.disableScissor();
    }

    public static void open() {
        Minecraft.getInstance().displayGuiScreen(new SpellcraftingGui());
    }

    public int getRightMainBorder() {
        return this.guiLeft + this.sizeX;
    }

    public int getLeftMainBorder() {
        return this.guiLeft + 38;
    }

    public int getBottomMainBorder() {
        return this.guiTop + this.sizeY - 37;
    }

    public int getActiveSpellIndex() {
        return this.activeSpellIndex;
    }

    public int getNumSpells() {
        return this.playerSpellData.getKnownSpells().size();
    }

    public Spell getSpellInSlot(int slot) {
        return this.playerSpellData.getKnownSpells().get(slot + this.bottomSpell);
    }

}
