package com.bsixel.mysticism.common.commands;

import com.bsixel.mysticism.MysticismMod;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

public class CommandRegistrar {

    public static void register(CommandDispatcher<CommandSource> dispatcher) { // TODO: Register other commands here
        LiteralCommandNode<CommandSource> cmdMysticism = dispatcher.register(Commands.literal(MysticismMod.MOD_ID)
                .then(ManaCommand.register(dispatcher))
                .then(SubManaCommand.register(dispatcher)));

        // EX: We want /myst to also do the above
        dispatcher.register(Commands.literal("myst").redirect(cmdMysticism));
    }

}
