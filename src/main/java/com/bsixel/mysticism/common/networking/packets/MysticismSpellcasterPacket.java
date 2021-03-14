package com.bsixel.mysticism.common.networking.packets;

import com.bsixel.mysticism.common.api.capability.mana.Force;
import com.bsixel.mysticism.common.api.capability.mana.IManaHolder;
import com.bsixel.mysticism.common.api.capability.mana.ManaCapability;
import com.bsixel.mysticism.common.api.capability.spellcasting.ISpellcaster;
import com.bsixel.mysticism.common.api.capability.spellcasting.SpellcasterCapability;
import com.bsixel.mysticism.common.api.spells.Spell;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

// TODO: Useful note for later: PacketBuffer can .writeItemStack and .writeCompoundTag (CompoundNBT)
// TODO: Maybe add an abstract class or interface above most of our packets if we have good reason to
public class MysticismSpellcasterPacket {

    private CompoundNBT casterData;

    public MysticismSpellcasterPacket(PacketBuffer buffer) {
        this.casterData = buffer.readCompoundTag();
    }

    public MysticismSpellcasterPacket(ISpellcaster spellcasterData) {
        this.casterData = spellcasterData.serialize();
    }

    public static MysticismSpellcasterPacket decode(PacketBuffer buffer) {
        return new MysticismSpellcasterPacket(buffer);
    }

    public void encode(PacketBuffer buffer) {
        buffer.writeCompoundTag(this.casterData);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> { // TODO: This should only be processed on the clientside but make sure that's true maybe
            ClientPlayerEntity player = Minecraft.getInstance().player;
            if (player != null) { // Shouldn't ever be null but you never know
                player.getCapability(SpellcasterCapability.spellcaster_cap, null).ifPresent(spellcasterCap -> {
                    spellcasterCap.getKnownSpells().clear();
                    ListNBT spellsNbt = (ListNBT) this.casterData.get("spells");
                    spellsNbt.forEach(singleNbt -> spellcasterCap.addSpell(Spell.deserialize((CompoundNBT) singleNbt)));
                    spellcasterCap.setCurrentSpellIndex(this.casterData.getInt("current"));
                });
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
