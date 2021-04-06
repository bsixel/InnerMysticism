package com.bsixel.mysticism.common.init.registries;

import com.bsixel.mysticism.MysticismMod;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TileEntityRegistry {

    private static final Logger logger = LogManager.getLogger(MysticismMod.MOD_ID);

    private static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, MysticismMod.MOD_ID);

    public static void init(IEventBus lifecycleBus) {
        logger.info("Registering tile entities...");
        TILE_ENTITIES.register(lifecycleBus);
    }
//    public static final RegistryObject<TileEntityType<AltarBlockTile>> ALTARBLOCK_TILE = TILE_ENTITIES.register("altar_block_tile", () -> TileEntityType.Builder.create(AltarBlockTile::new, BlockRegistry.ALTARBLOCK.get()).build(null));
}
