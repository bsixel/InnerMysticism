package com.bsixel.mysticism.client.gui.screens;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ScreenBase extends Screen {

    protected int guiLeft = 0; // Screens are weird, start top left, like reading a page I guess
    protected int guiTop = 0;

    protected int sizeX = 0;
    protected int sizeY = 0;

    protected ScreenBase(ITextComponent title, int width, int height) {
        super(title);
        this.width = width;
        this.height = height;
    }

    @Override
    protected void init() {
        super.init();
        this.guiLeft = (this.width - this.sizeX) / 2; // Quarter from left
        this.guiTop = (this.height - this.sizeY) / 2; // Quarter from top
    }

    public int getGuiLeft() {
        return guiLeft;
    }

    public int getGuiTop() {
        return guiTop;
    }

    public int getSizeX() {
        return sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }
}
