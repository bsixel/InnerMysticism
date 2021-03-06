package com.bsixel.mysticism.client.gui.screens;

import com.bsixel.mysticism.MysticismMod;
import com.bsixel.mysticism.client.gui.widgets.AbilityButton;
import com.bsixel.mysticism.common.api.ability.Ability;
import com.bsixel.mysticism.common.api.capability.mana.Force;
import com.bsixel.mysticism.common.api.math.tree.*;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@OnlyIn(Dist.CLIENT)
public class ForceAbilityTabGui extends ScreenBase {
    private static final Logger logger = LogManager.getLogger(MysticismMod.MOD_ID);

    private final ForceAbilityScreen screen;
    private final int max = 8;
    private final int index;
    private final Force force;
    private final ItemStack icon;
//    private final Map<Advancement, AdvancementEntryGui> guis = Maps.newLinkedHashMap();
    private static final int nodeDistance = 24;
    private double scrollX;
    private double scrollY;

    private AbilityButton root;
    private Tree<AbilityButton> testTree;
    private FixedNodeExtentProvider<AbilityButton> extentProvider;
    private TreeConfiguration<AbilityButton> treeConfiguration;
    private TreeLayout<AbilityButton> treeLayout;

    public ForceAbilityTabGui(ForceAbilityScreen parentScreen, int index, Force force) {
        super(new StringTextComponent(force.getName()));
        this.screen = parentScreen;
        this.index = index;
        this.force = force;
        this.icon = new ItemStack(Items.IRON_PICKAXE); // TODO: Figure out how to icon dynamically; Do we want to load it from a file? Just case it?

//        this.sizeX = 179;
        this.sizeX = 179*2;
//        this.sizeY = 151;
        this.sizeY = 151*2;
    }

    public static ForceAbilityTabGui create(ForceAbilityScreen screen, int tabIndex, Force force) {
        return new ForceAbilityTabGui(screen, tabIndex, force);
    }

    @Override
    protected void init() {
        super.init(); // TODO: Figure out what this.width and this.height actually are. Seem to be related to screen size? Maybe?
        this.guiLeft = (this.width - this.sizeX) / 2; // Quarter from left
        this.guiTop = (this.height - this.sizeY) / 2; // Quarter from top
        Ability rootAbility = AbilityButton.abilityFactory("ability_root", "Root", "First test!");
        this.root = new AbilityButton(this.guiLeft, this.guiTop, AbilityButton.getButtonWidth(), AbilityButton.getButtonHeight(), rootAbility.getName(), rootAbility, this, this.itemRenderer);
        initializeTestTree();
    }

    @Override
    public boolean isPauseScreen() {
        return true;
    }

    private static final ResourceLocation example_loc = new ResourceLocation(MysticismMod.MOD_ID, "textures/blocks/water-rune.png");
    private int addedNodes = 4;
    private void initializeTestTree() {
        this.testTree = new Tree<>(this.root);
        this.addButton(this.root);
        this.extentProvider = new FixedNodeExtentProvider<>(AbilityButton.getButtonHeight(), AbilityButton.getButtonHeight());
        this.treeConfiguration = new TreeConfiguration<>(nodeDistance, nodeDistance, ITreeConfiguration.Location.Top, ITreeConfiguration.AlignmentInLevel.AwayFromRoot, this.guiLeft, this.guiTop);
        this.treeLayout = new TreeLayout<>(this.testTree, this.extentProvider, this.treeConfiguration);
    }

    public void addAbilityButton(AbilityButton parent, AbilityButton child) {
        this.addButton(child);
        this.testTree.addChild(parent, child);
        this.treeLayout.recalculate();
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(tall_background);
        // matrixStack, startLeftPos, startRightPos, zIndex, textureSheetStartX, textureSheetStartY, textureSizeXCrop, textureSizeYCrop, textureSizeXScale, textureSizeYScale
        blit(matrixStack, this.guiLeft, this.guiTop, -10, 0F, 0F, this.sizeX, this.sizeY, this.sizeY, this.sizeX);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    protected boolean isAbilityMouseOvered(int mouseX, int mouseY, Ability ability) {
        return false; // TODO
//        return mouseX >= ability.getX() && mouseX <= ability.getX() + testTree.getNodeSize() && mouseY >= ability.getY() && mouseY <= ability.getY() + testTree.getNodeSize();
    }

    public static void open() {
        Minecraft.getInstance().displayGuiScreen(new ForceAbilityTabGui(null, 0, Force.BALANCE));
    }

}
