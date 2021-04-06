package com.bsixel.mysticism.common.networking.packets;

import com.bsixel.mysticism.common.api.capability.spellcasting.SpellcasterCapability;
import com.bsixel.mysticism.common.events.PlayerEventHandler;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MysticismClientChangedSpellPacket {

    private int slotChange;

    public MysticismClientChangedSpellPacket(PacketBuffer buffer) {
        this.slotChange = buffer.readInt();
    }

    public MysticismClientChangedSpellPacket() { // Default: Spell slot increased once clientside
        this(1);
    }

    public MysticismClientChangedSpellPacket(int slotChange) {
        this.slotChange = slotChange;
    }

    public static MysticismClientChangedSpellPacket decode(PacketBuffer buffer) {
        return new MysticismClientChangedSpellPacket(buffer);
    }

    public void encode(PacketBuffer buffer) {
        buffer.writeInt(this.slotChange);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {

        ctx.get().enqueueWork(() -> { // TODO: This should always be received on the server side.
            ServerPlayerEntity player = ctx.get().getSender();
            if (player != null) { // Shouldn't ever be null but you never know
                player.getCapability(SpellcasterCapability.spellcaster_cap, null).ifPresent(playerCasting -> {
                    if (playerCasting.getKnownSpells().size() > 0) {
                        playerCasting.changeSpellslot(this.slotChange);
                        PlayerEventHandler.updatePlayerSpellcasting(player);
                        player.sendMessage(new StringTextComponent("Switched to spell: " + playerCasting.getCurrentSpell().getName()).mergeStyle(TextFormatting.BLUE), Util.DUMMY_UUID);
                    }
                });
            }
        });
        ctx.get().setPacketHandled(true);
    }

}
