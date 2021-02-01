package com.bsixel.mysticism.init.registries;

import com.bsixel.mysticism.MysticismMod;
import com.bsixel.mysticism.common.items.crystals.*;
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
    public static final RegistryObject<Item> WATER_CRYSTAL_ITEM = ITEMS.register("water_crystal", WaterCrystal::new);
    public static final RegistryObject<Item> FIRE_CRYSTAL_ITEM = ITEMS.register("fire_crystal", FireCrystal::new);
    public static final RegistryObject<Item> AIR_CRYSTAL_ITEM = ITEMS.register("air_crystal", AirCrystal::new);
    public static final RegistryObject<Item> EARTH_CRYSTAL_ITEM = ITEMS.register("earth_crystal", EarthCrystal::new);
    public static final RegistryObject<Item> LIFE_CRYSTAL_ITEM = ITEMS.register("life_crystal", LifeCrystal::new);
    public static final RegistryObject<Item> DEATH_CRYSTAL_ITEM = ITEMS.register("death_crystal", DeathCrystal::new);
    public static final RegistryObject<Item> BALANCE_CRYSTAL_ITEM = ITEMS.register("balance_crystal", BalanceCrystal::new);

    // BLOCK ITEMS
    public static final RegistryObject<BlockItem> WATER_FORCE_ORE_BLOCK = ITEMS.register("water_force_ore_block", () -> new BlockItem(BlockRegistry.WATER_FORCE_ORE_BLOCK.get(), new Item.Properties().group(MysticismMod.item_group)));
    public static final RegistryObject<BlockItem> FIRE_FORCE_ORE_BLOCK = ITEMS.register("fire_force_ore_block", () -> new BlockItem(BlockRegistry.FIRE_FORCE_ORE_BLOCK.get(), new Item.Properties().group(MysticismMod.item_group)));
    public static final RegistryObject<BlockItem> AIR_FORCE_ORE_BLOCK = ITEMS.register("air_force_ore_block", () -> new BlockItem(BlockRegistry.AIR_FORCE_ORE_BLOCK.get(), new Item.Properties().group(MysticismMod.item_group)));
    public static final RegistryObject<BlockItem> EARTH_FORCE_ORE_BLOCK = ITEMS.register("earth_force_ore_block", () -> new BlockItem(BlockRegistry.EARTH_FORCE_ORE_BLOCK.get(), new Item.Properties().group(MysticismMod.item_group)));
    public static final RegistryObject<BlockItem> LIFE_FORCE_ORE_BLOCK = ITEMS.register("life_force_ore_block", () -> new BlockItem(BlockRegistry.LIFE_FORCE_ORE_BLOCK.get(), new Item.Properties().group(MysticismMod.item_group)));
    public static final RegistryObject<BlockItem> DEATH_FORCE_ORE_BLOCK = ITEMS.register("death_force_ore_block", () -> new BlockItem(BlockRegistry.DEATH_FORCE_ORE_BLOCK.get(), new Item.Properties().group(MysticismMod.item_group)));
    public static final RegistryObject<BlockItem> BALANCE_FORCE_ORE_BLOCK = ITEMS.register("balance_force_ore_block", () -> new BlockItem(BlockRegistry.BALANCE_FORCE_ORE_BLOCK.get(), new Item.Properties().group(MysticismMod.item_group)));
    public static final RegistryObject<BlockItem> ALTAR_BLOCK = ITEMS.register("altar_block", () -> new BlockItem(BlockRegistry.ALTAR_BLOCK.get(), new Item.Properties().group(MysticismMod.item_group)));

}
