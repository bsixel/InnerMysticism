package com.bsixel.mysticism.client.gui;

public class GuiHelper {

    public static int parseAlpha(String color) { // Provide just hex color code for standard's sake please! Ex. B7CBB3 , no #. Or not I guess I can just strip it out
        String toUse = color.startsWith("#") ? color.substring(1) : color;
        return 0xFF000000 | Integer.parseInt(toUse, 16);
    }

}
