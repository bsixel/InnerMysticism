package com.bsixel.mysticism.client.gui.screens;

import com.bsixel.mysticism.MysticismMod;
import com.bsixel.mysticism.client.gui.AbstractMystGui;
import com.bsixel.mysticism.client.gui.widgets.SpellComponentButton;
import com.bsixel.mysticism.common.api.ability.Ability;
import com.bsixel.mysticism.common.api.capability.mana.Force;
import com.bsixel.mysticism.common.api.math.tree.PositionableTree;
import com.bsixel.mysticism.common.api.math.tree.PositionableTreeConfig;
import com.bsixel.mysticism.common.api.math.tree.PositionableTreeLayout;
import com.bsixel.mysticism.common.api.spells.Spell;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
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

    private final ForceAbilityScreen screen;
    private final int max = 8;
    private final int index;
    private final Force force;
    private final ItemStack icon;
    private static final int nodeDistance = 24;
    private int rootNodeX;
    private int rootNodeY;
    private int dragX = 0;
    private int dragY = 0;
    private double scrollX;
    private double scrollY;
    private Spell activeSpell;

    private SpellComponentButton root;
    private PositionableTree<SpellComponentButton> testTree;
    private FixedNodeExtentProvider<SpellComponentButton> extentProvider;
    private PositionableTreeConfig<SpellComponentButton> treeConfiguration;
    private PositionableTreeLayout<SpellComponentButton> treeLayout;

    public SpellcraftingGui(ForceAbilityScreen parentScreen, int index, Force force) {
        super(new StringTextComponent(force.getName()));
        this.screen = parentScreen;
        this.index = index;
        this.force = force;
        this.icon = new ItemStack(Items.IRON_PICKAXE); // TODO: Figure out how to icon dynamically; Do we want to load it from a file? Just case it?

        this.sizeX = 247;
        this.sizeY = 165;
    }

    public static SpellcraftingGui create(ForceAbilityScreen screen, int tabIndex, Force force) {
        return new SpellcraftingGui(screen, tabIndex, force);
    }

    @Override
    protected void init() {
        super.init();
        this.guiLeft = (this.width - this.sizeX) / 2; // Quarter from left
        this.guiTop = (this.height - this.sizeY) / 2; // Quarter from top
        this.rootNodeX = this.guiLeft + 124 - (SpellComponentButton.getButtonWidth() / 2);
        this.rootNodeY = this.guiTop + 7;

        // TODO: Initialize tree on selecting spell
//        this.testTree = new PositionableTree<>();
//        this.extentProvider = new FixedNodeExtentProvider<>(SpellComponentButton.getButtonHeight(), SpellComponentButton.getButtonHeight());
//        this.treeConfiguration = new PositionableTreeConfig<>(nodeDistance, nodeDistance, Configuration.Location.Top, Configuration.AlignmentInLevel.AwayFromRoot, this.rootNodeX, this.rootNodeY);
//        this.treeLayout = new PositionableTreeLayout<>(this.testTree, this.extentProvider, this.treeConfiguration);
//        this.treeLayout = new PositionableTreeLayout<>(this.testTree, this.extentProvider, this.treeConfiguration);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private static final ResourceLocation example_loc = new ResourceLocation(MysticismMod.MOD_ID, "textures/blocks/water-rune.png");

    public void addSpellComponentButton(SpellComponentButton parent, SpellComponentButton child) {
        this.addButton(child);
        this.testTree.addChild(parent, child);
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
        /* matrixStack, startLeftPos, startRightPos, zIndex, textureSheetStartX, textureSheetStartY, textureSizeXCrop, textureSizeYCrop, textureSizeXScale, textureSizeYScale */
        blit(matrixStack, this.guiLeft, this.guiTop, 0, 0, this.sizeX, this.sizeY);

        // Render side buttons
        this.minecraft.getTextureManager().bindTexture(AbstractMystGui.STATS_ICON_LOCATION);
        blit(matrixStack, this.guiLeft - 11, this.guiTop, 22, 6, 10, 6); // Up arrow
        blit(matrixStack, this.guiLeft - 11, this.guiTop + this.sizeY, 40, 5, 10, 6); // Down arrow
        blit(matrixStack, this.guiLeft - 11, this.guiTop - 20, 21, 20, 11, 13); // Anvil icon

        this.renderTree(matrixStack, mouseX, mouseY, partialTicks);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    private void renderTree(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        // Stop rendering of any buttons outside of the GUI
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        double scale = getGuiScaleFactor();
        GL11.glScissor((int) (this.guiLeft * scale), (int) (this.guiTop * scale), (int) (this.sizeX * scale), (int) (this.sizeY * scale));

        // Draw connecting lines
        this.buttons.forEach(btn -> {
            int buttonSize = SpellComponentButton.getButtonWidth();
            int halfButton = buttonSize / 2;
            btn.visible = isPointInsideGui(btn.x + halfButton, btn.y + halfButton);
            if (btn instanceof SpellComponentButton) {
                SpellComponentButton abilityBtn = (SpellComponentButton) btn;
                List<SpellComponentButton> children = this.testTree.getChildrenList(abilityBtn);
                if (!children.isEmpty()) {
                    int horizLineLevel = (int) (btn.y + this.treeConfiguration.getGapBetweenLevels(0));
                    int lineColor = parseAlpha("#3068c2");
                    int buttonBottom = btn.y + buttonSize;
                    int buttonMiddle = btn.x + halfButton;
                    SpellComponentButton firstChild = this.testTree.getFirstChild(abilityBtn);
                    SpellComponentButton lastChild = this.testTree.getLastChild(abilityBtn);
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

        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

    public static void open() {
        Minecraft.getInstance().displayGuiScreen(new SpellcraftingGui(null, 0, Force.BALANCE));
    }

}
