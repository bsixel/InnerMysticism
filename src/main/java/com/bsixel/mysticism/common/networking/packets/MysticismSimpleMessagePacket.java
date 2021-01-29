package com.bsixel.mysticism.common.networking.packets;

import com.bsixel.mysticism.MysticismMod;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;

public class MysticismSimpleMessagePacket {

    private static final Logger logger = LogManager.getLogger(MysticismMod.MOD_ID);

    private String message;

    public MysticismSimpleMessagePacket(PacketBuffer buffer) {
        this.message = buffer.readString();
    }

    public static MysticismSimpleMessagePacket decode(PacketBuffer buffer) {
        return new MysticismSimpleMessagePacket(buffer);
    }

    public void encode(PacketBuffer buffer) {
        buffer.writeString(this.message);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> { // TODO: Switch handle whatever message we get
            logger.info("Got generic info packet for Mysticism");
        });
        ctx.get().setPacketHandled(true);
    }
}
