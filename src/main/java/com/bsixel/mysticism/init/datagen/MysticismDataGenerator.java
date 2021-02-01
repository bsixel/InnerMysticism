package com.bsixel.mysticism.init.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

public class MysticismDataGenerator {

    @SubscribeEvent
    public static void gather(GatherDataEvent event) {
        DataGenerator dataGenerator = event.getGenerator();
        ExistingFileHelper helper = event.getExistingFileHelper();

        if (event.includeServer()) { //TODO: Spell parts? Perks/Abilities (what are we calling these? not feats) Loot tables, recipes, tags, advancements

        }
        if (event.includeClient()) { // Textures, models, BlockStates etc
            dataGenerator.addProvider(new ItemsProvider(dataGenerator, helper));
        }

    }

}
