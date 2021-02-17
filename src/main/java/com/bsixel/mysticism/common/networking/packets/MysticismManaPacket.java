package com.bsixel.mysticism.common.networking.packets;

import com.bsixel.mysticism.common.api.capability.mana.Force;
import com.bsixel.mysticism.common.api.capability.mana.IManaHolder;
import com.bsixel.mysticism.common.api.capability.mana.ManaCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

// TODO: Useful note for later: PacketBuffer can .writeItemStack and .writeCompoundTag (CompoundNBT)
// TODO: Maybe add an abstract class or interface above this if we have good reason to
public class MysticismManaPacket { // TODO: Buffer xp updates. XP: They break 20 blocks in a second, instead of sending 20 packets, send one with the combo of all 20 summed.

    private int mystLevel;
    private double mystXp;
    private double maxMana;
    private double currentMana;
    private Force primaryForce;
    private Map<Force, Double> attenuation = new HashMap<>();

    public MysticismManaPacket(PacketBuffer buffer) {
        this.mystLevel = buffer.readInt();
        this.mystXp = buffer.readDouble();
        this.maxMana = buffer.readDouble();
        this.currentMana = buffer.readDouble();
        this.primaryForce = buffer.readEnumValue(Force.class);
        CompoundNBT attTag = buffer.readCompoundTag();
        if (attTag != null) { // If it's null, don't set attenuation, we won't update it later
            for (String forceName : attTag.keySet()) {
                this.attenuation.put(Force.valueOf(forceName), attTag.getDouble(forceName));
            }
        }
    }

    public MysticismManaPacket(IManaHolder manaData) {
        this.mystLevel = manaData.getMysticismLevel();
        this.mystXp = manaData.getMysticismXp();
        this.maxMana = manaData.getMaxMana();
        this.currentMana = manaData.getCurrentMana();
        this.primaryForce = manaData.getPrimaryForce();
        this.attenuation = manaData.getAttenuations();
    }

    public MysticismManaPacket(int mystLevel, double mystXp, double maxMana, double currentMana, Force primaryForce, Map<Force, Double> attenuation) {
        this.mystLevel = mystLevel;
        this.mystXp = mystXp;
        this.maxMana = maxMana;
        this.currentMana = currentMana;
        this.primaryForce = primaryForce;
        this.attenuation = attenuation;
    }

    public static MysticismManaPacket decode(PacketBuffer buffer) {
        return new MysticismManaPacket(buffer);
    }

    public void encode(PacketBuffer buffer) {
        buffer.writeInt(this.mystLevel);
        buffer.writeDouble(this.mystXp);
        buffer.writeDouble(this.maxMana);
        buffer.writeDouble(this.currentMana);
        buffer.writeEnumValue(this.primaryForce);
        CompoundNBT attTag = new CompoundNBT();
        for (Force force : this.attenuation.keySet()) {
            attTag.putDouble(force.name(), this.attenuation.get(force));
        }
        buffer.writeCompoundTag(attTag);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> { // TODO: This should only be processed on the clientside but make sure that's true maybe
            ClientPlayerEntity player = Minecraft.getInstance().player;
            if (player != null) { // Shouldn't ever be null but you never know
                player.getCapability(ManaCapability.mana_cap).ifPresent(manaCap -> {
                    manaCap.setMysticismLevel(this.mystLevel);
                    manaCap.setMysticismXp(this.mystXp);
                    manaCap.setMaxMana(this.maxMana);
                    manaCap.setMana(this.currentMana);
                    manaCap.setPrimaryForce(this.primaryForce);
                    for (Force force : this.attenuation.keySet()) {
                        manaCap.setForceAttenuation(force, this.attenuation.get(force));
                    }
                });
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
