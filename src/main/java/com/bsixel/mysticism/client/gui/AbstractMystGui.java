package com.bsixel.mysticism.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.AbstractGui;

public abstract class AbstractMystGui extends AbstractGui {

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

    protected static int parseAlpha(String color) { // Provide hex color code for standard's sake please! Ex. B7CBB3 , no #
        return 0xFF000000 | Integer.parseInt(color, 16);
    }
}
