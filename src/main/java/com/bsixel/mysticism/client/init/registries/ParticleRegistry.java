package com.bsixel.mysticism.client.init.registries;

import com.bsixel.mysticism.MysticismMod;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ParticleRegistry {

    private static final Logger logger = LogManager.getLogger(MysticismMod.MOD_ID);

    private static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, MysticismMod.MOD_ID);

    public static void init(IEventBus lifecycleBus){
        logger.info("Registering particles...");
        PARTICLES.register(lifecycleBus);
    }

    public static final RegistryObject<BasicParticleType> BASIC_SPELL_PARTICLE = PARTICLES.register("spell_particle", () -> new BasicParticleType(true));

}
