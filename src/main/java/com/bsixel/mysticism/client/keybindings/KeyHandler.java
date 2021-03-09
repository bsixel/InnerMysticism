package com.bsixel.mysticism.client.keybindings;

import com.bsixel.mysticism.MysticismMod;
import com.bsixel.mysticism.client.gui.screens.ForceAbilityTabGui;
import com.bsixel.mysticism.common.networking.NetworkManager;
import com.bsixel.mysticism.common.networking.packets.MysticismCastSpellPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraftforge.client.event.InputEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

public class KeyHandler {

    private static final Logger logger = LogManager.getLogger(MysticismMod.MOD_ID);

    private static final Minecraft minecraft = Minecraft.getInstance();

    public static void handleKeyEvent(InputEvent.KeyInputEvent event) {
        ClientPlayerEntity player = minecraft.player;
        if (player == null || !minecraft.isGameFocused() || minecraft.isGamePaused() || minecraft.currentScreen != null) {
            return;
        }

        if (event.getKey() == Keybindings.PEW.getKey().getKeyCode() && event.getAction() == GLFW.GLFW_PRESS) { // TODO: Figure out how to stop key propagation
            handleSpellCast(event);
        } else if (event.getKey() == Keybindings.MENU.getKey().getKeyCode() && event.getAction() == GLFW.GLFW_PRESS) {
            ForceAbilityTabGui.open();
        }
    }

    private static void handleSpellCast(InputEvent.KeyInputEvent event) { // TODO: This should maybe be elsewhere, move once we've got actual spells going back into the casting package? Although this should really just trigger serverside spell execution
        NetworkManager.channel.sendToServer(new MysticismCastSpellPacket());
        logger.info("HEARD KEYPRESS OF PEW! MYSTICISM AWAY!");
    }

}
