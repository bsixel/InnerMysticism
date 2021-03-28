package com.bsixel.mysticism.client.gui.screens;

import com.bsixel.mysticism.MysticismMod;
import com.bsixel.mysticism.client.gui.GuiHelper;
import com.bsixel.mysticism.client.gui.widgets.AbilityButton;
import com.bsixel.mysticism.common.api.ability.Ability;
import com.bsixel.mysticism.common.api.capability.mana.Force;
import com.bsixel.mysticism.common.api.math.tree.PositionableTree;
import com.bsixel.mysticism.common.api.math.tree.PositionableTreeConfig;
import com.bsixel.mysticism.common.api.math.tree.PositionableTreeLayout;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
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
public class ForceAbilityTabGui extends ScreenBase {
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

    private AbilityButton root;
    private PositionableTree<AbilityButton> testTree;
    private FixedNodeExtentProvider<AbilityButton> extentProvider;
    private PositionableTreeConfig<AbilityButton> treeConfiguration;
    private PositionableTreeLayout<AbilityButton> treeLayout;

    public ForceAbilityTabGui(ForceAbilityScreen parentScreen, int index, Force force) {
        super(new StringTextComponent(force.getName()));
        this.screen = parentScreen;
        this.index = index;
        this.force = force;
        this.icon = new ItemStack(Items.IRON_PICKAXE); // TODO: Figure out how to icon dynamically; Do we want to load it from a file? Just case it?

        this.sizeX = 247;
        this.sizeY = 165;
    }

    public static ForceAbilityTabGui create(ForceAbilityScreen screen, int tabIndex, Force force) {
        return new ForceAbilityTabGui(screen, tabIndex, force);
    }

    @Override
    protected void init() {
        super.init();
        this.guiLeft = (this.width - this.sizeX) / 2; // Quarter from left
        this.guiTop = (this.height - this.sizeY) / 2; // Quarter from top
        this.rootNodeX = this.guiLeft + 124 - (AbilityButton.getButtonWidth() / 2);
        this.rootNodeY = this.guiTop + 7;
        Ability rootAbility = AbilityButton.abilityFactory("ability_root", "Root", "First test!");
        this.root = new AbilityButton(this.guiLeft, this.guiTop, AbilityButton.getButtonWidth(), AbilityButton.getButtonHeight(), rootAbility.getName(), rootAbility, this, this.itemRenderer,
                (p_onTooltip_1_, p_onTooltip_2_, p_onTooltip_3_, p_onTooltip_4_) -> this.renderTooltip(p_onTooltip_2_, this.minecraft.fontRenderer.trimStringToWidth(new TranslationTextComponent(rootAbility.getName().getString() + ": " + rootAbility.getDescription().getString()), Math.max(this.width / 2 - 43, 170)), p_onTooltip_3_, p_onTooltip_4_));
        initializeTestTree();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private static final ResourceLocation example_loc = new ResourceLocation(MysticismMod.MOD_ID, "textures/blocks/water-rune.png");
    private int addedNodes = 4;
    private void initializeTestTree() {
        this.testTree = new PositionableTree<>(this.root);
        this.addButton(this.root);
        this.extentProvider = new FixedNodeExtentProvider<>(AbilityButton.getButtonHeight(), AbilityButton.getButtonHeight());
        this.treeConfiguration = new PositionableTreeConfig<>(nodeDistance, nodeDistance, Configuration.Location.Top, Configuration.AlignmentInLevel.AwayFromRoot, this.rootNodeX, this.rootNodeY);
        this.treeLayout = new PositionableTreeLayout<>(this.testTree, this.extentProvider, this.treeConfiguration);
        this.treeLayout = new PositionableTreeLayout<>(this.testTree, this.extentProvider, this.treeConfiguration);
    }

    public void addAbilityButton(AbilityButton parent, AbilityButton child) {
        this.addButton(child);
        this.testTree.addChild(parent, child);
        this.treeLayout.recalculate();
    }

    private boolean isPointInsideGui(int pointX, int pointY) {
        return pointX >= this.guiLeft && pointX <= this.guiLeft + this.sizeX && pointY >= this.guiTop && pointY <= this.guiTop + this.sizeY;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (isPointInsideGui((int) mouseX, (int) mouseY)) {
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
        this.minecraft.getTextureManager().bindTexture(GuiHelper.wide_background);
        /* matrixStack, startLeftPos, startRightPos, zIndex, textureSheetStartX, textureSheetStartY, textureSizeXCrop, textureSizeYCrop, textureSizeXScale, textureSizeYScale */
        blit(matrixStack, this.guiLeft, this.guiTop, 0, 0, this.sizeX, this.sizeY);

        // Stop rendering of any buttons outside of the GUI
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        double scale = getGuiScaleFactor();
        GL11.glScissor((int) (this.guiLeft * scale), (int) (this.guiTop * scale), (int) (this.sizeX * scale), (int) (this.sizeY * scale));

        this.buttons.forEach(btn -> {
            int buttonSize = AbilityButton.getButtonWidth();
            int halfButton = buttonSize / 2;
            btn.visible = isPointInsideGui(btn.x + halfButton, btn.y + halfButton);
            if (btn instanceof AbilityButton) {
                AbilityButton abilityBtn = (AbilityButton) btn;
                List<AbilityButton> children = this.testTree.getChildrenList(abilityBtn);
                if (!children.isEmpty()) {
                    int horizLineLevel = (int) (btn.y + this.treeConfiguration.getGapBetweenLevels(0));
                    int lineColor = parseAlpha("#3068c2");
                    int buttonBottom = btn.y + buttonSize;
                    int buttonMiddle = btn.x + halfButton;
                    AbilityButton firstChild = this.testTree.getFirstChild(abilityBtn);
                    AbilityButton lastChild = this.testTree.getLastChild(abilityBtn);
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

        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    public static void open() {
        Minecraft.getInstance().displayGuiScreen(new ForceAbilityTabGui(null, 0, Force.BALANCE));
    }

}
