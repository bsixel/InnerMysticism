package com.bsixel.mysticism.common.networking.packets;

import com.bsixel.mysticism.MysticismMod;
import com.bsixel.mysticism.common.api.capability.mana.IManaHolder;
import com.bsixel.mysticism.common.api.capability.mana.ManaCapability;
import com.bsixel.mysticism.common.api.spells.Spell;
import com.bsixel.mysticism.common.api.spells.SpellComponentInstance;
import com.bsixel.mysticism.common.api.spells.SpellHelper;
import com.bsixel.mysticism.common.api.spells.actions.DigAction;
import com.bsixel.mysticism.common.api.spells.casttypes.SpellCastTypeTouch;
import com.bsixel.mysticism.common.events.PlayerEventHandler;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;

import javax.annotation.Nullable;
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
                Spell testSpell = buildTestSpell(player);
                player.getCapability(ManaCapability.mana_cap, null).ifPresent(playerMana -> {
                    double spellCost = testSpell.getCost();
                    if (playerMana.getCurrentMana() >= spellCost) {
                        player.heal(10f);
                        player.getFoodStats().setFoodLevel(20);
                        // TODO: Expend the spell's mana
                        playerMana.addMana(-spellCost);
                        testSpell.cast(player);
                        playerMana.setManaLastUsed(); // Reset regen cooldown
                        PlayerEventHandler.updatePlayerMana(player, playerMana);
                    } else {
                        // TODO: Something interesting if they try to expend mana they don't have
                    }
                });
            }
        });
        ctx.get().setPacketHandled(true);
    }

    private static Spell buildTestSpell(ServerPlayerEntity player) { // Bad example, most of the time the resource locations won't be newly created
        Spell spell = new Spell(player, player.getPosition(), player.world.getDimensionKey().getRegistryName().toString());
        spell.root = new SpellComponentInstance(spell, SpellHelper.getRegisteredComponent(new ResourceLocation(MysticismMod.MOD_ID, "spellcomponent.touch")));
        spell.root.getChildren().add(new SpellComponentInstance(spell, SpellHelper.getRegisteredComponent(new ResourceLocation(MysticismMod.MOD_ID, "spellcomponent.dig"))));
        return spell;
    }

}
