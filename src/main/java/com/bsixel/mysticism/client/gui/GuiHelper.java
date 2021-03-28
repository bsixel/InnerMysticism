package com.bsixel.mysticism.client.gui;

import com.bsixel.mysticism.MysticismMod;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.client.gui.GuiUtils;

import java.util.List;

public class GuiHelper extends GuiUtils {

    public static ResourceLocation wide_background = new ResourceLocation(MysticismMod.MOD_ID, "textures/gui/generic_background_248_186.png");
    public static ResourceLocation tall_background = new ResourceLocation(MysticismMod.MOD_ID, "textures/gui/generic_background_tall_256x256.png");
    public static ResourceLocation square_background = new ResourceLocation(MysticismMod.MOD_ID, "textures/gui/generic_background_square_256x256.png");
    public static ResourceLocation gui_pieces = new ResourceLocation(MysticismMod.MOD_ID, "textures/gui/gui-pieces.png");

    public static int parseAlpha(String color) { // Provide just hex color code for standard's sake please! Ex. B7CBB3 , no #. Or not I guess I can just strip it out
        String toUse = color.startsWith("#") ? color.substring(1) : color;
        return 0xFF000000 | Integer.parseInt(toUse, 16);
    }

    public static List<IReorderingProcessor> fixStringLength(ITextComponent text, int length) {
        return Minecraft.getInstance().fontRenderer.trimStringToWidth(text, length);
    }

    public static long getGlfwWindow() {
        return Minecraft.getInstance().getMainWindow().getHandle();
    }

}
