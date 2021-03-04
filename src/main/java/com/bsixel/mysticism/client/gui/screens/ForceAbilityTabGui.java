package com.bsixel.mysticism.client.gui.screens;

import com.bsixel.mysticism.MysticismMod;
import com.bsixel.mysticism.client.gui.widgets.AbilityButton;
import com.bsixel.mysticism.common.api.ability.Ability;
import com.bsixel.mysticism.common.api.capability.mana.Force;
import com.bsixel.mysticism.common.api.math.Tree;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.ToggleWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
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
    private double scrollX;
    private double scrollY;

    private Ability root;
    private Tree<Ability> testTree;

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

    private static Ability crappyTempAbilityFactory(String id, Ability parent, String name, String description) {
        return new Ability(new ResourceLocation(id), parent, new ItemStack(Items.NETHER_STAR), Force.BALANCE, 1, 0, new StringTextComponent(name), new StringTextComponent(description), null);
    }

    @Override
    protected void init() {
        super.init(); // TODO: Figure out what this.width and this.height actually are. Seem to be related to screen size? Maybe?
        this.guiLeft = (this.width - this.sizeX) / 2; // Quarter from left
        this.guiTop = (this.height - this.sizeY) / 2; // Quarter from top
        this.root = crappyTempAbilityFactory("ability_root", null, "Root", "First test!");
        initializeTestTree();
    }

    @Override
    public boolean isPauseScreen() {
        return true;
    }

    private static final ResourceLocation example_loc = new ResourceLocation(MysticismMod.MOD_ID, "textures/blocks/water-rune.png");
    private int addedNodes = 4;
    private void initializeTestTree() {
        this.testTree = new Tree<>(this.root, 16, 7, this.guiLeft, this.guiTop + 7, 12, Tree.TreeDirection.TOP_DOWN);
        Ability second = crappyTempAbilityFactory("ability_second", root, "Second", "Not first :(");
        testTree.addNode(root, second);
        Ability third = crappyTempAbilityFactory("ability_third", root, "Third", "Bippity Boppity Boo");
        testTree.addNode(root, third);
        Ability fourth = crappyTempAbilityFactory("ability_fourth", third, "Fourth", "42");
        testTree.addNode(third, fourth);
//            this.itemRenderer.renderItemIntoGUI(ability.getIcon(), ability.getX(), ability.getY());
        testTree.forEach(this::addAbilityButton);
    }

    private void addAbilityButton(Ability ability) {
        AbilityButton button = new AbilityButton(ability.getX(), ability.getY(), testTree.getNodeSize(), testTree.getNodeSize(), ability.getName(), press -> addAbilityButton(this.testTree.addNode(ability, crappyTempAbilityFactory("ability_"+this.addedNodes, ability, "Added " + addedNodes++, "Another"))),
                (p_onTooltip_1_, p_onTooltip_2_, p_onTooltip_3_, p_onTooltip_4_) -> this.renderTooltip(p_onTooltip_2_, this.minecraft.fontRenderer.trimStringToWidth(new TranslationTextComponent(ability.getName().getString() + ": " + ability.getDescription().getString()), Math.max(this.width / 2 - 43, 170)), p_onTooltip_3_, p_onTooltip_4_), ability, this.itemRenderer);
        addButton(button);
        this.buttons.forEach(btn -> {
            if (btn instanceof AbilityButton) {
                ((AbilityButton) btn).setPos(((AbilityButton) btn).getAbility().getX(), ((AbilityButton) btn).getAbility().getY());
            }
        });
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
        return mouseX >= ability.getX() && mouseX <= ability.getX() + testTree.getNodeSize() && mouseY >= ability.getY() && mouseY <= ability.getY() + testTree.getNodeSize();
    }

    public static void open() {
        Minecraft.getInstance().displayGuiScreen(new ForceAbilityTabGui(null, 0, Force.BALANCE));
    }

}
