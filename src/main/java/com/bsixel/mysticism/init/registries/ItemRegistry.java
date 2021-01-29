package com.bsixel.mysticism.init.registries;

import com.bsixel.mysticism.MysticismMod;
import com.bsixel.mysticism.common.items.orbs.WaterOrb;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ItemRegistry {

    private static final Logger logger = LogManager.getLogger(MysticismMod.MOD_ID);

    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MysticismMod.MOD_ID);

    public static void init(IEventBus lifecycleBus){
        logger.info("Registering items...");
        ITEMS.register(lifecycleBus);
    }

    public static Item.Properties getDefaultProperties() {
        return new Item.Properties().group(MysticismMod.item_group);
    }

    public static final RegistryObject<Item> WATER_ORB_ITEM = ITEMS.register("water_orb", WaterOrb::new);

    // BLOCK ITEMS
    public static final RegistryObject<BlockItem> FORCE_ORE_BLOCK = ITEMS.register("force_ore_block", () -> new BlockItem(BlockRegistry.FORCE_ORE_BLOCK.get(), new Item.Properties().group(MysticismMod.item_group)));

}
