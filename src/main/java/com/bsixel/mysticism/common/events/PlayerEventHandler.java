package com.bsixel.mysticism.common.events;

import com.bsixel.mysticism.common.api.capability.mana.IManaHolder;
import com.bsixel.mysticism.common.api.capability.mana.ManaCapability;
import com.bsixel.mysticism.common.networking.NetworkManager;
import com.bsixel.mysticism.common.networking.packets.MysticismManaPacket;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

public class PlayerEventHandler { // NOTE: These should all be serverside

    private static void updatePlayerMana(PlayerEntity player) {
        player.getCapability(ManaCapability.mana_cap, null).ifPresent(playerMana -> { // Update remote client's player mana/etc stats on login
            updatePlayerMana(player, playerMana);
        });
    }

    public static void updatePlayerMana(PlayerEntity player, IManaHolder playerMana) {
        NetworkManager.channel.sendToSpecPlayer(player, new MysticismManaPacket(playerMana));
    }

    public static void onPlayerJoin(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) event.getEntity();
            updatePlayerMana(player);
        }
    }

    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        //  TODO: Add config for mana rates, etc. Do we still need to manually send mana data every time there's a mana action? Probably should to avoid desync... Should add manaTooLow event or something
        if (event.player.getEntityWorld().isRemote || event.player.getEntityWorld().getGameTime() % 2 != 0) { // Only update mana every 2 ticks
            return;
        }

        int manaRegenRate = 5;
        event.player.getCapability(ManaCapability.mana_cap, null).ifPresent(playerMana -> { // TODO: Figure out how to add delay to mana regen so you're not regenerating as you cast...
            if (playerMana.isRefillable() && (playerMana.getManaLastUsed() + 1300 < System.currentTimeMillis())) { // If mana is full obviously we shouldn't regen
                playerMana.addMana(manaRegenRate);
                updatePlayerMana(event.player, playerMana);
            }
        });


    }

}
