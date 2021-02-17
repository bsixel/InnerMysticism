package com.bsixel.mysticism.common.networking.packets;

import com.bsixel.mysticism.common.api.capability.mana.IManaHolder;
import com.bsixel.mysticism.common.api.capability.mana.ManaCapability;
import com.bsixel.mysticism.common.events.PlayerEventHandler;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

// TODO: Useful note for later: PacketBuffer can .writeItemStack and .writeCompoundTag (CompoundNBT)
// TODO: Buffer xp updates. XP: They break 20 blocks in a second, instead of sending 20 packets, send one with the combo of all 20 summed.
// TODO: I have no idea what we're going to need to send in these packets...
public class MysticismCastSpellPacket {

    public MysticismCastSpellPacket(PacketBuffer buffer) {
    }

    public MysticismCastSpellPacket(IManaHolder manaData) {
    }

    public MysticismCastSpellPacket() {
    }

    public static MysticismCastSpellPacket decode(PacketBuffer buffer) {
        return new MysticismCastSpellPacket(buffer);
    }

    public void encode(PacketBuffer buffer) {
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> { // TODO: This should be received on the server side. Also creative players shouldn't expend mana, for science
            // For now to test we're just going to heal the player
            ServerPlayerEntity player = ctx.get().getSender();
            if (player != null) { // Shouldn't ever be null but you never know
                player.getCapability(ManaCapability.mana_cap, null).ifPresent(playerMana -> {
                    if (playerMana.getCurrentMana() >= 100) {
                        player.heal(10f);
                        player.getFoodStats().setFoodLevel(20);
                        playerMana.addMana(-100f);
                        PlayerEventHandler.updatePlayerMana(player, playerMana);
                    }
                });
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
