package com.bsixel.mysticism.common.networking;

import com.bsixel.mysticism.MysticismMod;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MysticismChannel { // TODO: All these MSG generics should probably be at least *somewhat* typed

    private static final Logger logger = LogManager.getLogger(MysticismMod.MOD_ID);
    private final SimpleChannel channel;


    public MysticismChannel(SimpleChannel channel) {
        this.channel = channel;
    }

    public SimpleChannel getChannel() {
        return this.channel;
    }

    public <MSG> void send(MSG message, PacketDistributor.PacketTarget target) { // End all
        this.channel.send(target, message);
    }

    public <MSG> void sendToNearby(MSG message, PacketDistributor.TargetPoint origin) {
        this.send(message, PacketDistributor.NEAR.with(() -> origin));
    }

    /*public <MSG> void sendToOfDimension(MSG message, Dimension dim) { TODO
        this.send(message, PacketDistributor.DIMENSION.with());
    }*/

    public <MSG> void sendToAll(MSG message) {
        this.send(message, PacketDistributor.ALL.noArg());
    }

    public <MSG> void sendToServer(MSG message) {
        this.channel.sendToServer(message);
    }

    public <MSG> void sendToSpecPlayer(PlayerEntity player, MSG message) {
        if (player instanceof ServerPlayerEntity) {
            this.channel.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), message);
        }
    }

}
