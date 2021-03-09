package com.bsixel.mysticism.client.gui.widgets;

import com.bsixel.mysticism.client.gui.screens.ForceAbilityTabGui;
import com.bsixel.mysticism.common.api.ability.Ability;
import com.bsixel.mysticism.common.api.capability.mana.Force;
import com.bsixel.mysticism.common.api.math.tree.IPositionable;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class AbilityButton extends Button implements IPositionable {

    private static int added = 1;
    private ForceAbilityTabGui parentScreen;
    private final ItemRenderer itemRenderer;
    private final Ability ability;
    private final List<AbilityButton> children = new ArrayList<>();
    private AbilityButton parent;
    private static int buttonWidth = 16;
    private static int buttonHeight = 16;

    public AbilityButton(int x, int y, int width, int height, ITextComponent title, @Nonnull Ability ability, @Nonnull ForceAbilityTabGui parentScreen, @Nonnull ItemRenderer itemRenderer, ITooltip onTooltip) {
        super(x, y, width, height, title, b -> {}, onTooltip);
        this.ability = ability;
        this.parentScreen = parentScreen;
        this.itemRenderer = itemRenderer;
    }

    @Override
    public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        if (this.visible) {
            this.itemRenderer.renderItemIntoGUI(this.ability.getIcon(), this.x, this.y);
            if (this.isHovered()) {
                this.renderToolTip(matrixStack, mouseX, mouseY);
            }
        }
    }

    public static Ability abilityFactory(String id, @Nonnull String name, String description) { // TODO
        return new Ability(new ResourceLocation(id), new ItemStack(Items.NETHER_STAR), Force.BALANCE, 1, 0, new StringTextComponent(name), new StringTextComponent(description), null);
    }

    @Override
    public void onPress() {
        AbilityButton newButton;
        Ability newAbility = abilityFactory("ability_"+added++, "Ability "+added, "The " +added+"nth added ability");
        newButton = new AbilityButton(0, 0, buttonWidth, buttonHeight, ability.getName(), newAbility, this.parentScreen, this.itemRenderer,
                (p_onTooltip_1_, p_onTooltip_2_, p_onTooltip_3_, p_onTooltip_4_) -> this.parentScreen.renderTooltip(p_onTooltip_2_, Minecraft.getInstance().fontRenderer.trimStringToWidth(new TranslationTextComponent(newAbility.getName().getString() + ": " + newAbility.getDescription().getString() + " Parent: " + this.ability.getName().getString()), Math.max(this.width / 2 - 43, 170)), p_onTooltip_3_, p_onTooltip_4_));
        this.addChild(newButton);
        this.parentScreen.addAbilityButton(this, newButton);
    }



    public void setPos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static int getButtonWidth() {
        return buttonWidth;
    }

    public static int getButtonHeight() {
        return buttonHeight;
    }

    public Ability getAbility() {
        return ability;
    }

    private void setParent(AbilityButton parent) {
        this.parent = parent;
    }

    public AbilityButton getParent() {
        return this.parent;
    }

    public List<AbilityButton> getChildren() {
        return this.children;
    }

    public void addChild(AbilityButton child) {
        child.setParent(this);
        this.getChildren().add(child);
    }

    public void addChildren(AbilityButton... children) {
        for (AbilityButton child : children) {
            this.addChild(child);
        }
    }

    public void removeChild(AbilityButton child) {
        this.getChildren().remove(child);
        child.setParent(null);
    }

    public void unParent() {
        this.parent.removeChild(this);
    }
}
