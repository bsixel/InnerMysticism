package com.bsixel.mysticism.client.gui;

import com.bsixel.mysticism.common.api.capability.mana.ManaCapability;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;

public class ManaOverlay extends AbstractMystGui {

    public void renderManaHud(Minecraft minecraft, ClientPlayerEntity player, MatrixStack pane) { // TODO: Make it so the user can move this around!! Annoying to have locked GUIs...
        player.getCapability(ManaCapability.mana_cap).ifPresent(manaStats -> {
            int leftPad = 15; // TODO: load this from config;
            int topPad = 15; // Also load
            int barThickness = 10; // etc
            int totalLength = 100; // Do I even need this comment, you know what I'm going to say

            double fillLength = totalLength * (manaStats.getCurrentMana() / manaStats.getMaxMana());

            this.renderOutlinedGradient(pane, leftPad, (int)fillLength, topPad, barThickness, totalLength, 3, "6acdd4", "44bdc7", "435354");

        });
    }
}
