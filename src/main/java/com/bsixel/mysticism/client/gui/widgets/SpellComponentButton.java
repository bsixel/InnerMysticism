package com.bsixel.mysticism.client.gui.widgets;

import com.bsixel.mysticism.client.gui.GuiHelper;
import com.bsixel.mysticism.client.gui.screens.SpellcraftingGui;
import com.bsixel.mysticism.common.api.math.tree.IPositionable;
import com.bsixel.mysticism.common.api.spells.SpellComponentInstance;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.util.text.ITextComponent;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class SpellComponentButton extends Button implements IPositionable {

    private static int added = 1;
    private final SpellcraftingGui parentScreen;
    private final ItemRenderer itemRenderer;
    private final SpellComponentInstance wrapper;
    private static final int buttonWidth = 16;
    private static final int buttonHeight = 16;

    public SpellComponentButton(int x, int y, @Nonnull SpellComponentInstance wrapper, @Nonnull SpellcraftingGui parentScreen, @Nonnull ItemRenderer itemRenderer, ITooltip tooltip) {
        super(x, y, buttonWidth, buttonHeight, wrapper.getComponent().getName(), b -> {}, tooltip);
        this.wrapper = wrapper;
        this.parentScreen = parentScreen;
        this.itemRenderer = itemRenderer;
    }

    @Override
    public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        if (this.visible) {
            this.itemRenderer.renderItemIntoGUI(this.wrapper.getComponent().getIcon(), this.x, this.y);
            if (this.isHovered()) {
                this.renderToolTip(matrixStack, mouseX, mouseY);
            }
        }
    }

    @Override
    public void onPress() { // Button clicked
//        SpellComponentButton newButton = ;
//        newButton = new SpellComponentButton(0, 0, buttonWidth, buttonHeight, component.getName(), , this.parentScreen, this.itemRenderer,
//                (p_onTooltip_1_, p_onTooltip_2_, p_onTooltip_3_, p_onTooltip_4_) -> this.parentScreen.renderTooltip(p_onTooltip_2_, Minecraft.getInstance().fontRenderer.trimStringToWidth(new TranslationTextComponent(newAbility.getName().getString() + ": " + newAbility.getDescription().getString() + " Parent: " + this.ability.getName().getString()), Math.max(this.width / 2 - 43, 170)), p_onTooltip_3_, p_onTooltip_4_));
//        this.parentScreen.addSpellComponentButton(this, newButton);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) { // Key pressed
        if (keyCode == GLFW.GLFW_KEY_BACKSPACE || keyCode == GLFW.GLFW_KEY_DELETE) { // Delete this ability
            this.parentScreen.removeComponent(this);
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
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

    public SpellComponentInstance getWrapper() {
        return this.wrapper;
    }

}
