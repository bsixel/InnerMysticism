package com.bsixel.mysticism.common.api.capability.mana;

import com.bsixel.mysticism.MysticismMod;
import com.bsixel.mysticism.common.api.capability.SerializableCapabilityProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;

public class ManaCapability {
    private static final Logger logger = LogManager.getLogger();

    @CapabilityInject(IManaHolder.class)
    public static Capability<IManaHolder> mana_cap = null;

    public static final Direction default_facing = null;

    public static final ResourceLocation loc = new ResourceLocation(MysticismMod.MOD_ID, "mana_holder");

    public static void register() {
        CapabilityManager.INSTANCE.register(IManaHolder.class, new ManaStorage(), () -> new DefaultManaHolder(null)); // Honestly no idea what this last param is
    }

    public static class ManaStorage implements Capability.IStorage<IManaHolder> {
        private static final String attKey = "attenuations";
        @Nullable
        @Override
        public INBT writeNBT(Capability<IManaHolder> capability, IManaHolder instance, Direction side) {
            CompoundNBT nbt = new CompoundNBT();
            nbt.putInt("mystLevel", instance.getMysticismLevel());
            nbt.putDouble("mystXp", instance.getMysticismXp());
            nbt.putDouble("maxMana", instance.getMaxMana());
            nbt.putDouble("mana", instance.getCurrentMana());
            nbt.putString("primaryForce", instance.getPrimaryForce().name());
            // Serialize attenuations
            CompoundNBT attenuationNbt = new CompoundNBT();
            for (Force force : instance.getAttenuations().keySet()) {
                attenuationNbt.putDouble(force.name(), instance.getForceAttenuation(force)); // name(), not getName() ! Very important
            }
            nbt.put(attKey, attenuationNbt);
            return nbt;
        }

        @Override
        public void readNBT(Capability<IManaHolder> capability, IManaHolder instance, Direction side, INBT nbt) {
            if (nbt instanceof CompoundNBT) {
                CompoundNBT read = (CompoundNBT) nbt;
                instance.setMysticismLevel(read.getInt("mystLevel"));
                instance.setMysticismXp(read.getDouble("mystXp"));
                instance.setMaxMana(read.getDouble("maxMana")); // Max first in case implementation specifically caps
                instance.setMana(read.getDouble("mana"));
                Force primaryForce;
                primaryForce = tryForceFromName(read.getString("primaryForce"));
                instance.setPrimaryForce(primaryForce != null ? primaryForce : Force.BALANCE);
                // Always initialize first
                instance.initializeAttenuations();
                if (read.get(attKey) instanceof CompoundNBT) {
                    CompoundNBT attNbt = (CompoundNBT) read.get(attKey);
                    for (String forceName : attNbt.keySet()) {
                        instance.setForceAttenuation(tryForceFromName(forceName), attNbt.getDouble(forceName));
                    }
                }
                // TODO: Do we need refillable here? I don't think so
            }
        }

        private Force tryForceFromName(String name) {
            try { // May get exceptions if somehow they have invalid force names
                return Force.valueOf(name);
            } catch (IllegalArgumentException ex) {
                logger.error("Error! Attempted to load Mysticism nonexistant force " + name);
                return null;
            }
        }

    }

    @Mod.EventBusSubscriber(modid = MysticismMod.MOD_ID) // Leave this one, it's handled separately from proxy registration to keep it specifically registered
    private static class ManaCapabilityHandler  {

        @SubscribeEvent
        // All living things have mana and can have attenuation
        public static void addManaCap(final AttachCapabilitiesEvent<Entity> event) { // TODO: Maybe randomize entity attenuation based on spawn biome?
            if (event.getObject() instanceof PlayerEntity || event.getObject() instanceof MobEntity) { // I'd use LivingEntity but for some reason that includes armor stands
                event.addCapability(loc, new SerializableCapabilityProvider<>(mana_cap, null, new DefaultManaHolder((LivingEntity) event.getObject())));
            }
        }

        //Make sure players keep mana on death, I'm not that crazy
        @SubscribeEvent
        public static void reAddDeadPlayerMana(final PlayerEvent.Clone event) {
            if (event.isWasDeath()) { // Who named this thing, really. End return data should be taken care of by default in this version of MC.TODO: Address if I'm wrong
                // Apparently there's no better way to do this?
                event.getOriginal().getCapability(mana_cap, null).ifPresent(ogPlayerMana -> {
                    event.getPlayer().getCapability(mana_cap, null).ifPresent(revivedPlayerMana -> { // TODO: Maybe make a copy method somewhere so we don't have to do this, we might need it elsewhere too
                        revivedPlayerMana.setMysticismLevel(ogPlayerMana.getMysticismLevel());
                        revivedPlayerMana.setMysticismXp(ogPlayerMana.getMysticismXp());
                        revivedPlayerMana.setMaxMana(ogPlayerMana.getMaxMana());
                        revivedPlayerMana.setMana(ogPlayerMana.getCurrentMana());
                        revivedPlayerMana.initializeAttenuations();
                        revivedPlayerMana.setPrimaryForce(ogPlayerMana.getPrimaryForce());
                        for (Force force : ogPlayerMana.getAttenuations().keySet()) {
                            revivedPlayerMana.setForceAttenuation(force, ogPlayerMana.getForceAttenuation(force));
                        }
                    });
                });
            }
        }

    }

}
