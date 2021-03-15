package com.bsixel.mysticism.client.gui.screens;

import com.bsixel.mysticism.MysticismMod;
import com.bsixel.mysticism.client.gui.widgets.SpellButton;
import com.bsixel.mysticism.client.gui.widgets.SpellComponentButton;
import com.bsixel.mysticism.common.api.capability.mana.Force;
import com.bsixel.mysticism.common.api.capability.spellcasting.ISpellcaster;
import com.bsixel.mysticism.common.api.capability.spellcasting.SpellcasterCapability;
import com.bsixel.mysticism.common.api.math.tree.PositionableTree;
import com.bsixel.mysticism.common.api.math.tree.PositionableTreeConfig;
import com.bsixel.mysticism.common.api.math.tree.PositionableTreeLayout;
import com.bsixel.mysticism.common.api.spells.Spell;
import com.bsixel.mysticism.common.api.spells.SpellComponentInstance;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.abego.treelayout.Configuration;
import org.abego.treelayout.util.FixedNodeExtentProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import java.util.List;

import static com.bsixel.mysticism.client.gui.GuiHelper.parseAlpha;

@OnlyIn(Dist.CLIENT)
public class SpellcraftingGui extends ScreenBase {
    private static final Logger logger = LogManager.getLogger(MysticismMod.MOD_ID);

    private static final int nodeDistance = 24;
    private int rootNodeX;
    private int rootNodeY;
    private int dragX = 0;
    private int dragY = 0;
    private Spell activeSpell;
    private ISpellcaster playerSpellData;

    private int bottomSpell = 0;
    private int bottomComponent = 0;
    private boolean selectingComponent = false;

    private SpellComponentButton root;
    private PositionableTree<SpellComponentButton> componentButtonTree;
    private FixedNodeExtentProvider<SpellComponentButton> extentProvider;
    private PositionableTreeConfig<SpellComponentButton> treeConfiguration;
    private PositionableTreeLayout<SpellComponentButton> treeLayout;

    // TODO:
    public SpellcraftingGui() {
        super(new StringTextComponent("Spellcrafting")); // TODO: Translation text

        // Setup the player's spell data
        MysticismMod.getInstance().getSideSafeProxy().getPlayer().getCapability(SpellcasterCapability.spellcaster_cap, null).ifPresent(spellcasterCap -> this.playerSpellData = spellcasterCap);

        this.sizeX = 248;
        this.sizeY = 186;
    }

    @Override
    protected void init() {
        super.init();
        this.guiLeft = (this.width - this.sizeX) / 2; // Quarter from left
        this.guiTop = (this.height - this.sizeY) / 2; // Quarter from top
        this.rootNodeX = this.guiLeft + 124 - (SpellComponentButton.getButtonWidth() / 2);
        this.rootNodeY = this.guiTop + 7;

        for (int i = 0; i < 7; i++) { // Render each spell within view
            SpellButton spellButton = new SpellButton(this.guiLeft - 27, this.guiTop + 4 + (i * 26), 20, 20, this, this.itemRenderer, i, this::pressSpellButton, this::renderSpellTooltip);
            this.addButton(spellButton);
        }
    }

    private void pressSpellButton(Button spellButton) {
        int slot = ((SpellButton) spellButton).getSpellSlot();
        if (slot < this.getNumSpells()) {
            this.activeSpell = getSpellInSlot(slot);
            this.realizeTree();
        } else {
            this.activeSpell = null;
        }
    }

    private void renderSpellTooltip(Button button, MatrixStack ms, int mouseX, int mouseY) {
        this.renderTooltip(ms, Minecraft.getInstance().fontRenderer.trimStringToWidth(new TranslationTextComponent(((SpellButton) button).getSpell().getName()), Math.max(this.width / 2 - 43, 80)), mouseX, mouseY);
    }

    private void realizeTree() {
        this.buttons.removeIf(btn -> {
            return btn instanceof SpellComponentButton;
        });
        // TODO: There's almost certainly a more performant way to do this but considering how I don't expect spells to get that large, that's a later day task
        SpellComponentButton rootButton = new SpellComponentButton(this.guiLeft, this.guiTop, this.activeSpell.root, this, this.itemRenderer, this::renderComponentTooltip);
        this.buttons.add(rootButton);
        this.componentButtonTree = new PositionableTree<>(rootButton);
        rootButton.getWrapper().getChildren().forEach(subChild -> this.registerComponentTreeNode(rootButton, subChild));
        this.extentProvider = new FixedNodeExtentProvider<>(SpellComponentButton.getButtonHeight(), SpellComponentButton.getButtonHeight());
        this.treeConfiguration = new PositionableTreeConfig<>(nodeDistance, nodeDistance, Configuration.Location.Top, Configuration.AlignmentInLevel.AwayFromRoot, this.rootNodeX, this.rootNodeY);
        this.treeLayout = new PositionableTreeLayout<>(this.componentButtonTree, this.extentProvider, this.treeConfiguration);
    }

    private void renderComponentTooltip(Button button, MatrixStack ms, int mouseX, int mouseY) {
        SpellComponentButton compButton = (SpellComponentButton) button;
        this.renderTooltip(ms, Minecraft.getInstance().fontRenderer.trimStringToWidth(new TranslationTextComponent(compButton.getWrapper().getComponent().getName().getString() + ": " + compButton.getWrapper().getComponent().getDescription().getString()), Math.max(this.width / 2 - 43, 80)), mouseX, mouseY);
    }

    private void registerComponentTreeNode(SpellComponentButton parent, SpellComponentInstance child) {
        SpellComponentButton childButton = new SpellComponentButton(this.guiLeft, this.guiTop, child, this, this.itemRenderer, this::renderComponentTooltip);
        this.componentButtonTree.addChild(parent, childButton);
        this.buttons.add(childButton);
        child.getChildren().forEach(subChild -> this.registerComponentTreeNode(childButton, subChild));
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    public int getNumSpells() {
        return this.playerSpellData.getKnownSpells().size();
    }

    public Spell getSpellInSlot(int slot) {
        return this.playerSpellData.getKnownSpells().get(slot + this.bottomSpell);
    }

    private static final ResourceLocation example_loc = new ResourceLocation(MysticismMod.MOD_ID, "textures/blocks/water-rune.png");

    public void addSpellComponentButton(SpellComponentButton parent, SpellComponentButton child) {
        this.addButton(child);
        this.componentButtonTree.addChild(parent, child);
        this.treeLayout.recalculate();
    }

    public void removeComponent(SpellComponentButton componentButton) {
//        this.testTree.remove(componentButton); // TODO: Apparently we have to implement a remove
    }

    private boolean isPointInsideGui(int pointX, int pointY) {
        return pointX >= this.guiLeft && pointX <= this.guiLeft + this.sizeX && pointY >= this.guiTop && pointY <= this.guiTop + this.sizeY;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (this.treeConfiguration != null && this.treeLayout != null && isPointInsideGui((int) mouseX, (int) mouseY)) {
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
        this.minecraft.getTextureManager().bindTexture(wide_background);
        /* matrixStack, startLeftPos, startTopPos, zIndex, textureSheetStartX, textureSheetStartY, textureSizeXCrop, textureSizeYCrop, textureSizeXScale, textureSizeYScale */
        blit(matrixStack, this.guiLeft, this.guiTop, 0, 0, this.sizeX, this.sizeY);

        // Render spell selector
        this.minecraft.getTextureManager().bindTexture(gui_pieces);
        blit(matrixStack, this.guiLeft - 32, this.guiTop, 55, 56, 33, 186); // Left sidebar - spell selection

        if (this.activeSpell != null) { // User doesn't currently have any spell selected to work on; make sure we render an "add spell" button
            this.renderTree(matrixStack, mouseX, mouseY, partialTicks);
        }
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    private void renderTree(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        // Stop rendering of any buttons outside of the GUI
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        double scale = getGuiScaleFactor();
        GL11.glScissor((int) (this.guiLeft * scale), (int) (this.guiTop * scale), (int) (this.sizeX * scale), (int) (this.sizeY * scale));

        // Draw connecting lines
        this.buttons.forEach(btn -> {
            if (btn instanceof SpellComponentButton) { // ONLY DO ALL THIS FOR COMPONENT BUTTONS!
                int buttonSize = SpellComponentButton.getButtonWidth();
                int halfButton = buttonSize / 2;
                btn.visible = isPointInsideGui(btn.x + halfButton, btn.y + halfButton);
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
                        this.vLine(matrixStack, buttonMiddle, buttonBottom, firstChild.y, lineColor);
                    } else {
                        this.vLine(matrixStack, buttonMiddle, buttonBottom, horizLineLevel, lineColor);
                        this.hLine(matrixStack, firstChild.x + halfButton, lastChild.x + halfButton, horizLineLevel, lineColor);
                        children.forEach(child -> this.vLine(matrixStack, child.x + halfButton, horizLineLevel, child.y, lineColor));
                    }
                }
            }
        });
        // Re-enable rendering stuff outside the bounds of the main GUI area so we can draw other things
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

    public static void open() {
        Minecraft.getInstance().displayGuiScreen(new SpellcraftingGui());
    }

}
