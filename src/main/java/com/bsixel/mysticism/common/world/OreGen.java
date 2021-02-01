package com.bsixel.mysticism.common.world;

import com.bsixel.mysticism.MysticismMod;
import com.bsixel.mysticism.init.registries.BlockRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.template.RuleTest;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.placement.TopSolidRangeConfig;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class OreGen {

    private static final Logger logger = LogManager.getLogger(MysticismMod.MOD_ID);

    public static void generateOres(BiomeLoadingEvent event) { // NOTE: We could load up other generation stuff in here but I'd like to keep all worldgen split up by type
        logger.info("Initializing Mysticism ore generation");

        if (!event.getCategory().equals(Biome.Category.NETHER) && !event.getCategory().equals(Biome.Category.THEEND)) { // Overworld only oregen
            genOverworldOres(event);
        } else if (event.getCategory().equals(Biome.Category.NETHER)) { // Nether oregen
            genNetherOres(event);
        } else if (event.getCategory().equals(Biome.Category.THEEND)) { // End oregen
            genEndOres(event);
        }

    }

    private static void genStandardOre(BiomeLoadingEvent event, RuleTest fillerType, BlockState state, int veinSize, int minHeight, int maxHeight, int maxPerChunk) {
        event.getGeneration().withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(fillerType, state, veinSize))
                .withPlacement(Placement.RANGE.configure(new TopSolidRangeConfig(minHeight, 0, maxHeight)).square().func_242731_b(maxPerChunk)));
    }

    private static void genOverworldOres(BiomeLoadingEvent event) {
        logger.info("Initializing Overworld ores...");
        RuleTest overworld_stones = OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD; // TODO: Maybe change where the ores appear. Different heights? Biomes? Etc
        genStandardOre(event, overworld_stones, BlockRegistry.WATER_FORCE_ORE_BLOCK.get().getDefaultState(), 4, 1, 256, 15);
        genStandardOre(event, overworld_stones, BlockRegistry.FIRE_FORCE_ORE_BLOCK.get().getDefaultState(), 4, 1, 256, 15);
        genStandardOre(event, overworld_stones, BlockRegistry.AIR_FORCE_ORE_BLOCK.get().getDefaultState(), 4, 1, 256, 15);
        genStandardOre(event, overworld_stones, BlockRegistry.EARTH_FORCE_ORE_BLOCK.get().getDefaultState(), 4, 1, 256, 15);
        genStandardOre(event, overworld_stones, BlockRegistry.LIFE_FORCE_ORE_BLOCK.get().getDefaultState(), 4, 1, 256, 15);
        genStandardOre(event, overworld_stones, BlockRegistry.DEATH_FORCE_ORE_BLOCK.get().getDefaultState(), 4, 1, 256, 15);
        genStandardOre(event, overworld_stones, BlockRegistry.BALANCE_FORCE_ORE_BLOCK.get().getDefaultState(), 4, 1, 256, 15);
    }

    private static void genNetherOres(BiomeLoadingEvent event) { // TODO: Fire force ore here more commonly
        logger.info("Initializing Nether ores...");
        RuleTest nether_stones = OreFeatureConfig.FillerBlockType.BASE_STONE_NETHER;
    }

    private static void genEndOres(BiomeLoadingEvent event) { // TODO: Death force ore here more commonly
        logger.info("Initializing End ores...");
//        RuleTest end_stones = new TagMatchRuleTest(BlockTags.); // TODO: Figure out how we match end stone varieties.
    }



}
