package com.bsixel.mysticism.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static com.bsixel.mysticism.client.gui.GuiHelper.parseAlpha;

@OnlyIn(Dist.CLIENT)
public abstract class AbstractMystGui extends AbstractGui {
    // TODO: IngameGui.java L557 func_238454_b_ seems to render xp bar; copy that approach for mana bar, maybe magic xp bar

    protected void renderRectangle(MatrixStack pane, int leftX, int length, int topY, int height, String hexColor) {
        fill(pane, leftX, topY, leftX+length, topY+height, parseAlpha(hexColor));
    }

    protected void renderOutlinedRectangle(MatrixStack pane, int leftX, int length, int topY, int height, int borderWidth, String hexColor, String hexBorderColor) {
        fill(pane, leftX-borderWidth, topY-borderWidth, leftX+length+borderWidth, topY+height+borderWidth, parseAlpha(hexBorderColor));
        fill(pane, leftX, topY, leftX+length, topY+height, parseAlpha(hexColor));
    }

    protected void renderOutlinedGradient(MatrixStack pane, int leftX, int length, int topY, int height, int totalLength, int borderWidth, String hexFromColor, String hexToColor, String hexBorderColor) {
        fill(pane, leftX-borderWidth, topY-borderWidth, leftX+totalLength+borderWidth, topY+height+borderWidth, parseAlpha(hexBorderColor));
        fillGradient(pane, leftX, topY, leftX+length, topY+height, parseAlpha(hexFromColor), parseAlpha(hexToColor));
    }

    protected void renderGradientHorizontal(MatrixStack stack, int left, int top, int right, int bottom, int startColor, int endColor) {
        Matrix4f matrix = stack.getLast().getMatrix();
        float f = (float) (startColor >> 24 & 255) / 255.0F;
        float f1 = (float) (startColor >> 16 & 255) / 255.0F;
        float f2 = (float) (startColor >> 8 & 255) / 255.0F;
        float f3 = (float) (startColor & 255) / 255.0F;
        float f4 = (float) (endColor >> 24 & 255) / 255.0F;
        float f5 = (float) (endColor >> 16 & 255) / 255.0F;
        float f6 = (float) (endColor >> 8 & 255) / 255.0F;
        float f7 = (float) (endColor & 255) / 255.0F;
        GlStateManager.disableTexture();
        GlStateManager.enableBlend();
        GlStateManager.disableAlphaTest();
        GlStateManager.blendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder worldrenderer = tessellator.getBuffer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldrenderer.pos(matrix, right, top, this.getBlitOffset()).color(f1, f2, f3, f).endVertex();
        worldrenderer.pos(matrix, left, top, this.getBlitOffset()).color(f5, f6, f7, f4).endVertex();
        worldrenderer.pos(matrix, left, bottom, this.getBlitOffset()).color(f5, f6, f7, f4).endVertex();
        worldrenderer.pos(matrix, right, bottom, this.getBlitOffset()).color(f1, f2, f3, f).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlphaTest();
        GlStateManager.enableTexture();
    }

    protected  void renderOutlinedGradientHorizontal(MatrixStack pane, int leftX, int length, int topY, int height, int totalLength, int borderWidth, String hexFromColor, String hexToColor, String hexBorderColor) {
        fill(pane, leftX-borderWidth, topY-borderWidth, leftX+totalLength+borderWidth, topY+height+borderWidth, parseAlpha(hexBorderColor));
        renderGradientHorizontal(pane, leftX, topY, leftX + length, topY + height, parseAlpha(hexFromColor), parseAlpha(hexToColor));

    }

}
