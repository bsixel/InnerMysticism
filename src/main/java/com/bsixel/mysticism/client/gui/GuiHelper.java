package com.bsixel.mysticism.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.client.gui.GuiUtils;

import java.util.List;

public class GuiHelper extends GuiUtils {

    public static int parseAlpha(String color) { // Provide just hex color code for standard's sake please! Ex. B7CBB3 , no #. Or not I guess I can just strip it out
        String toUse = color.startsWith("#") ? color.substring(1) : color;
        return 0xFF000000 | Integer.parseInt(toUse, 16);
    }

    public static List<IReorderingProcessor> fixStringLength(ITextComponent text, int length) {
        return Minecraft.getInstance().fontRenderer.trimStringToWidth(text, length);
    }

}
