package com.bsixel.mysticism.common.api.capability.spellcasting;

import com.bsixel.mysticism.MysticismMod;
import com.bsixel.mysticism.common.api.capability.mana.DefaultManaHolder;
import com.bsixel.mysticism.common.api.capability.mana.IManaHolder;
import com.bsixel.mysticism.common.api.capability.mana.ManaCapability;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;

public class SpellcasterCapability {
    private static final Logger logger = LogManager.getLogger();

    @CapabilityInject(ISpellcaster.class)
    public static Capability<ISpellcaster> spellcaster_cap = null;

    public static final ResourceLocation loc = new ResourceLocation(MysticismMod.MOD_ID, "mana_holder");

    public static void register() {
        CapabilityManager.INSTANCE.register(ISpellcaster.class, new SpellcasterStorage(), DefaultSpellcaster::new);
    }

    public static class SpellcasterStorage implements Capability.IStorage<ISpellcaster> {

        @Nullable
        @Override
        public INBT writeNBT(Capability<ISpellcaster> capability, ISpellcaster instance, Direction side) {
            CompoundNBT nbt = new CompoundNBT();
            
            return null;
        }

        @Override
        public void readNBT(Capability<ISpellcaster> capability, ISpellcaster instance, Direction side, INBT nbt) {

        }
    }

}
