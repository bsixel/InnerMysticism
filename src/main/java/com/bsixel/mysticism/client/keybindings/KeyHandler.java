package com.bsixel.mysticism.client.keybindings;

import com.bsixel.mysticism.MysticismMod;
import com.bsixel.mysticism.client.gui.screens.SpellcraftingGui;
import com.bsixel.mysticism.common.networking.NetworkManager;
import com.bsixel.mysticism.common.networking.packets.MysticismCastSpellPacket;
import com.bsixel.mysticism.common.networking.packets.MysticismClientChangedSpellPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
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
            NetworkManager.channel.sendToServer(new MysticismCastSpellPacket());
            logger.info("HEARD KEYPRESS OF PEW! MYSTICISM AWAY!");
        } else if (event.getKey() == Keybindings.MENU.getKey().getKeyCode() && event.getAction() == GLFW.GLFW_PRESS) {
            SpellcraftingGui.open();
        } else if (event.getKey() == Keybindings.SELECT_PEW.getKey().getKeyCode() && event.getAction() == GLFW.GLFW_PRESS) {
            NetworkManager.channel.sendToServer(new MysticismClientChangedSpellPacket());
        }
    }

}
