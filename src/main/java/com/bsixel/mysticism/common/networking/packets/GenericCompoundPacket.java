package com.bsixel.mysticism.common.networking.packets;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class GenericCompoundPacket {

    private CompoundNBT data;

    public GenericCompoundPacket() {}
    public GenericCompoundPacket(CompoundNBT data) {
        this.data = data;
    }

    public GenericCompoundPacket(PacketBuffer buffer) {
        this.data = buffer.readCompoundTag();
    }

    public static GenericCompoundPacket decode(PacketBuffer buffer) {
        return new GenericCompoundPacket(buffer);
    }

    public void encode(PacketBuffer buffer) {
        buffer.writeCompoundTag(this.data);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) { // TODO

    }

}
