package com.bsixel.mysticism.common.api.capability.spellcasting;

import com.bsixel.mysticism.MysticismMod;
import com.bsixel.mysticism.common.api.capability.SerializableCapabilityProvider;
import com.bsixel.mysticism.common.api.capability.mana.DefaultManaHolder;
import com.bsixel.mysticism.common.api.capability.mana.Force;
import com.bsixel.mysticism.common.api.spells.Spell;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;

public class SpellcasterCapability {
    private static final Logger logger = LogManager.getLogger();

    @CapabilityInject(ISpellcaster.class)
    public static Capability<ISpellcaster> spellcaster_cap = null;

    public static final ResourceLocation loc = new ResourceLocation(MysticismMod.MOD_ID, "spellcaster");

    public static void register() {
        CapabilityManager.INSTANCE.register(ISpellcaster.class, new SpellcasterStorage(), DefaultSpellcaster::new);
    }

    public static class SpellcasterStorage implements Capability.IStorage<ISpellcaster> {

        @Nullable
        @Override
        public INBT writeNBT(Capability<ISpellcaster> capability, ISpellcaster instance, Direction side) {
            return instance.serialize();
        }

        @Override
        public void readNBT(Capability<ISpellcaster> capability, ISpellcaster instance, Direction side, INBT nbt) {
            if (nbt instanceof CompoundNBT) {
                CompoundNBT read = (CompoundNBT) nbt;
                instance.getKnownSpells().clear();
                ListNBT spellsNbt = (ListNBT) read.get("spells");
                spellsNbt.forEach(spellNbt -> instance.addSpell(Spell.deserialize((CompoundNBT) spellNbt)));
                instance.setCurrentSpellIndex(read.getInt("current"));
            }
        }
    }

    @Mod.EventBusSubscriber(modid = MysticismMod.MOD_ID) // Leave this one, it's handled separately from proxy registration to keep it specifically registered
    private static class ManaCapabilityHandler  {

        @SubscribeEvent
        // All living things have mana and can have attenuation
        public static void addSpellcastingCap(final AttachCapabilitiesEvent<Entity> event) {
            if (event.getObject() instanceof PlayerEntity) { // TODO: Attach to spellcasting mobs? Interesting idea: Mob that casts whatever spells you cast at it, plus some defaults
                event.addCapability(loc, new SerializableCapabilityProvider<>(spellcaster_cap, null, new DefaultSpellcaster()));
            }
        }

        //Make sure players keep mana on death, I'm not that crazy
        @SubscribeEvent
        public static void repopulateSpellcasterPostDeath(final PlayerEvent.Clone event) {
            if (event.isWasDeath()) { // Who named this thing, really. End return data should be taken care of by default in this version of MC.TODO: Address if I'm wrong
                // Apparently there's no better way to do this?
                event.getOriginal().getCapability(spellcaster_cap, null).ifPresent(ogPlayerCasting -> {
                    event.getPlayer().getCapability(spellcaster_cap, null).ifPresent(revivedPlayerCasting -> {
                        revivedPlayerCasting.getKnownSpells().clear();
                        ogPlayerCasting.getKnownSpells().forEach(revivedPlayerCasting::addSpell);
                        revivedPlayerCasting.setCurrentSpellIndex(ogPlayerCasting.getCurrentSpellIndex());
                    });
                });
            }
        }

    }

}
