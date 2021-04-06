package com.bsixel.mysticism.common.init.registries;

import com.bsixel.mysticism.MysticismMod;
import com.bsixel.mysticism.common.entities.projectiles.SpellProjectileEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityRegistry {

    private static final Logger logger = LogManager.getLogger(MysticismMod.MOD_ID);

    private static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, MysticismMod.MOD_ID);

    public static void init(IEventBus lifecycleBus){
        logger.info("Registering entities...");
        ENTITIES.register(lifecycleBus);
    }

    public static final RegistryObject<EntityType<SpellProjectileEntity>> SPELL_PROJECTILE = register("spell_projectile", SpellProjectileEntity::new, EntityClassification.MISC, 0.15f);

    private static <T extends Entity> RegistryObject<EntityType<T>> register(String id, EntityType.IFactory<T> factory, EntityClassification classification) {
        return ENTITIES.register(id, () -> EntityType.Builder.create(factory, classification).size(0.25f, 0.25f).build(MysticismMod.MOD_ID + ":" + id));
    }

    private static <T extends Entity> RegistryObject<EntityType<T>> register(String id, EntityType.IFactory<T> factory, EntityClassification classification, float width, float height) {
        return ENTITIES.register(id, () -> EntityType.Builder.create(factory, classification).size(width, height).build(MysticismMod.MOD_ID + ":" + id));
    }

    private static <T extends Entity> RegistryObject<EntityType<T>> register(String id, EntityType.IFactory<T> factory, EntityClassification classification, float size) {
        return ENTITIES.register(id, () -> EntityType.Builder.create(factory, classification).size(size, size).build(MysticismMod.MOD_ID + ":" + id));
    }

}
