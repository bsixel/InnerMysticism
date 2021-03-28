package com.bsixel.mysticism.client.gui.widgets;

import com.bsixel.mysticism.client.gui.GuiHelper;
import com.bsixel.mysticism.client.gui.screens.SpellcraftingGui;
import com.bsixel.mysticism.common.api.math.tree.IPositionable;
import com.bsixel.mysticism.common.api.spells.SpellComponentInstance;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.Nonnull;

public class SpellComponentButton extends Button implements IPositionable {

    private static int added = 1;
    private final SpellcraftingGui parentScreen;
    private final ItemRenderer itemRenderer;
    private final SpellComponentInstance wrapper;
    private static final int buttonWidth = 24;
    private static final int buttonHeight = 24;

    public SpellComponentButton(int x, int y, @Nonnull SpellComponentInstance wrapper, @Nonnull SpellcraftingGui parentScreen, @Nonnull ItemRenderer itemRenderer, IPressable pressed, ITooltip tooltip) {
        super(x, y, buttonWidth, buttonHeight, new StringTextComponent(""), pressed, tooltip);
        this.wrapper = wrapper;
        this.parentScreen = parentScreen;
        this.itemRenderer = itemRenderer;
    }

    @Override
    public void renderButton(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        if (this.visible) {
            Minecraft.getInstance().getTextureManager().bindTexture(GuiHelper.gui_pieces);
            this.parentScreen.enableScissorToMainArea();
            if (this.parentScreen.getSelectedComponent() == this.wrapper) { // Currently selected component
                if (this.getWrapper().getComponent() == null) { // This is a noncomplete wrapper, it doesn't have an actual component
                    blit(matrixStack, this.x, this.y, 69, 102, 24, 24);
                } else {
                    blit(matrixStack, this.x, this.y, 69, 52, 24, 24);
                }
            } else { // Not selected/active
                if (this.getWrapper().getComponent() == null) { // This is a noncomplete wrapper, it doesn't have an actual component
                    blit(matrixStack, this.x, this.y, 69, 127, 24, 24);
                } else {
                    blit(matrixStack, this.x, this.y, 69, 77, 24, 24);
                }
            }
            if (this.wrapper.getComponent() != null) {
//                GL11.glDisable(GL11.GL_LIGHTING); // TODO MAYBE
                this.itemRenderer.renderItemIntoGUI(this.wrapper.getComponent().getIcon(), this.x + 4, this.y + 4);
            }
            this.parentScreen.disableScissor();
        }
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
