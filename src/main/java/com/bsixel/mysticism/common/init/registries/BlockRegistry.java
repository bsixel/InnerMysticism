package com.bsixel.mysticism.common.init.registries;

import com.bsixel.mysticism.MysticismMod;
import com.bsixel.mysticism.common.blocks.standard.AltarBlock;
import com.bsixel.mysticism.common.blocks.standard.ores.*;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BlockRegistry {

    private static final Logger logger = LogManager.getLogger(MysticismMod.MOD_ID);

    public static void init(IEventBus lifecycleBus) {
        logger.info("Registering blocks...");
        BLOCKS.register(lifecycleBus);
    }

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MysticismMod.MOD_ID);

    public static AbstractBlock.Properties GENERIC_PLANT_PROPS = AbstractBlock.Properties.
            create(Material.PLANTS)
            .doesNotBlockMovement()
            .setAllowsSpawn((a,b,c,d) -> true)
            .sound(SoundType.PLANT)
            .harvestTool(ToolType.AXE)
            .setSuffocates((a,b,c) -> false);
    public static AbstractBlock.Properties GENERIC_MANA_MACHINE_PROPS = AbstractBlock.Properties
            .create(Material.ROCK)
            .harvestLevel(0)
            .harvestTool(ToolType.PICKAXE)
            .setLightLevel(v -> 4)
            .setAllowsSpawn((s,r,p,e) -> false)
            .sound(SoundType.STONE)
            .hardnessAndResistance(1.0f);

    public static final RegistryObject<AltarBlock> ALTAR_BLOCK = BLOCKS.register("altar_block", AltarBlock::new);
    public static final RegistryObject<ForceOreBlock> WATER_FORCE_ORE_BLOCK = BLOCKS.register("water_force_ore_block", WaterForceOreBlock::new);
    public static final RegistryObject<ForceOreBlock> FIRE_FORCE_ORE_BLOCK = BLOCKS.register("fire_force_ore_block", FireForceOreBlock::new);
    public static final RegistryObject<ForceOreBlock> AIR_FORCE_ORE_BLOCK = BLOCKS.register("air_force_ore_block", AirForceOreBlock::new);
    public static final RegistryObject<ForceOreBlock> EARTH_FORCE_ORE_BLOCK = BLOCKS.register("earth_force_ore_block", EarthForceOreBlock::new);
    public static final RegistryObject<ForceOreBlock> LIFE_FORCE_ORE_BLOCK = BLOCKS.register("life_force_ore_block", LifeForceOreBlock::new);
    public static final RegistryObject<ForceOreBlock> DEATH_FORCE_ORE_BLOCK = BLOCKS.register("death_force_ore_block", DeathForceOreBlock::new);
    public static final RegistryObject<ForceOreBlock> BALANCE_FORCE_ORE_BLOCK = BLOCKS.register("balance_force_ore_block", BalanceForceOreBlock::new);

}
