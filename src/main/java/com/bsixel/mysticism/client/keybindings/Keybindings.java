package com.bsixel.mysticism.client.keybindings;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.glfw.GLFW;

public class Keybindings {

    private static final String KEY_CATEGORY = "Mysticism";

    // Public so we can reference them elsewhere
    // TODO: rename to less silly after we've had our fun
    public static final KeyBinding CAST_SPELL = new KeyBinding("Cast Spell", KeyConflictContext.IN_GAME, InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_C, KEY_CATEGORY); // CAST SPELL
    public static final KeyBinding SELECT_SPELL = new KeyBinding("Select Spell", KeyConflictContext.IN_GAME, InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_V, KEY_CATEGORY); // SELECT SPELL
    public static final KeyBinding MENU = new KeyBinding("Mysticism Main Menu", KeyConflictContext.IN_GAME, InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_K, KEY_CATEGORY); // OPEN MYSTICISM MENU

    public static void register() {
        ClientRegistry.registerKeyBinding(CAST_SPELL);
        ClientRegistry.registerKeyBinding(SELECT_SPELL);
        ClientRegistry.registerKeyBinding(MENU);
    }

}
