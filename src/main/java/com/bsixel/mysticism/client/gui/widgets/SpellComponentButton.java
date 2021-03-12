package com.bsixel.mysticism.client.gui.widgets;

import com.bsixel.mysticism.client.gui.GuiHelper;
import com.bsixel.mysticism.client.gui.screens.SpellcraftingGui;
import com.bsixel.mysticism.common.api.math.tree.IPositionable;
import com.bsixel.mysticism.common.api.spells.ISpellComponent;
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
    private SpellcraftingGui parentScreen;
    private final ItemRenderer itemRenderer;
    private final ISpellComponent component;
    private final List<SpellComponentButton> children = new ArrayList<>();
    private SpellComponentButton parent;
    private static int buttonWidth = 16;
    private static int buttonHeight = 16;

    public SpellComponentButton(int x, int y, int width, int height, ITextComponent title, @Nonnull ISpellComponent component, @Nonnull SpellcraftingGui parentScreen, @Nonnull ItemRenderer itemRenderer) {
        super(x, y, width, height, title, b -> {}, (v,b,n,m) -> {});
        this.component = component;
        this.parentScreen = parentScreen;
        this.itemRenderer = itemRenderer;
    }

    @Override
    public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        if (this.visible) {
            this.itemRenderer.renderItemIntoGUI(this.component.getIcon(), this.x, this.y);
            if (this.isHovered()) {
                List<ITextComponent> tooltip = new ArrayList<>();
//                GuiHelper.fixStringLength(this.component.getName(), 110).get(0) // TODO: Figure out how to use this instead
                tooltip.add(this.component.getName());
                tooltip.add(this.component.getDescription());
                GuiHelper.drawHoveringText(matrixStack, tooltip, mouseX, mouseY, this.parentScreen.getSizeX(), this.parentScreen.getSizeY(), 120, this.parentScreen.getFontRenderer());
//                this.renderToolTip(matrixStack, mouseX, mouseY);
            }
        }
    }

    @Override
    public void onPress() {
//        SpellComponentButton newButton = ;
//        newButton = new SpellComponentButton(0, 0, buttonWidth, buttonHeight, component.getName(), , this.parentScreen, this.itemRenderer,
//                (p_onTooltip_1_, p_onTooltip_2_, p_onTooltip_3_, p_onTooltip_4_) -> this.parentScreen.renderTooltip(p_onTooltip_2_, Minecraft.getInstance().fontRenderer.trimStringToWidth(new TranslationTextComponent(newAbility.getName().getString() + ": " + newAbility.getDescription().getString() + " Parent: " + this.ability.getName().getString()), Math.max(this.width / 2 - 43, 170)), p_onTooltip_3_, p_onTooltip_4_));
//        this.parentScreen.addSpellComponentButton(this, newButton);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
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

    public ISpellComponent getComponent() {
        return this.component;
    }

    private void setParent(SpellComponentButton parent) {
        this.parent = parent;
    }

    public SpellComponentButton getParent() {
        return this.parent;
    }

    public List<SpellComponentButton> getChildren() {
        return this.children;
    }

    public void addChild(SpellComponentButton child) {
        child.setParent(this);
        this.getChildren().add(child);
    }
}
