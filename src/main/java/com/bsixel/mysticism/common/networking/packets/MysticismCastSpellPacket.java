package com.bsixel.mysticism.common.networking.packets;

import com.bsixel.mysticism.common.api.capability.mana.IManaHolder;
import com.bsixel.mysticism.common.api.capability.mana.ManaCapability;
import com.bsixel.mysticism.common.api.capability.spellcasting.SpellcasterCapability;
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
                    player.getCapability(SpellcasterCapability.spellcaster_cap, null).ifPresent(playerCasting -> {
                        if (playerCasting.getKnownSpells().size() > 0) {
                            if (playerCasting.getCurrentSpellIndex() < 0 || playerCasting.getCurrentSpellIndex() >= playerCasting.getKnownSpells().size()) { // They don't have a valid index, just default it to zero
                                playerCasting.incrementSpellslot();
                                PlayerEventHandler.updatePlayerSpellcasting(player);
                            }
                            double spellCost = playerCasting.getCurrentSpell().getCost();
                            if (playerMana.getCurrentMana() >= spellCost || player.isCreative()) { // Player can cast whatever they want in creative
                                playerMana.addMana(player.isCreative() ? 0 : -spellCost); // Don't expend mana in creative
                                playerCasting.getCurrentSpell().cast(player);
                                playerMana.setManaLastUsed(); // Reset regen cooldown
                                PlayerEventHandler.updatePlayerMana(player, playerMana);
                            } else {
                                // TODO: Something interesting if they try to expend mana they don't have, maybe cause damage? Random effect? Probably affected by player's abilities
                            }
                        }
                    });
                });
            }
        });
        ctx.get().setPacketHandled(true);
    }

}
