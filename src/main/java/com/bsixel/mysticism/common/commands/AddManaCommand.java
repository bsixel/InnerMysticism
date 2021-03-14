package com.bsixel.mysticism.common.commands;

import com.bsixel.mysticism.common.api.capability.mana.ManaCapability;
import com.bsixel.mysticism.common.networking.NetworkManager;
import com.bsixel.mysticism.common.networking.packets.MysticismManaPacket;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;

public class AddManaCommand implements Command<CommandSource> {
    private static final AddManaCommand cmd = new AddManaCommand();

    public static ArgumentBuilder<CommandSource, ?> register(CommandDispatcher<CommandSource> dispatcher) {
        return Commands.literal("submana").requires(cs -> cs.hasPermissionLevel(0)).executes(cmd);
    }

    @Override
    public int run(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
        ServerPlayerEntity player = ctx.getSource().asPlayer(); //TODO: Bad practice, be safe at some point
        player.getCapability(ManaCapability.mana_cap, null).ifPresent(playerMana -> {
            playerMana.addMana(100);
            NetworkManager.channel.sendToSpecPlayer(player, new MysticismManaPacket(playerMana));
        });

        return 0;
    }

}
