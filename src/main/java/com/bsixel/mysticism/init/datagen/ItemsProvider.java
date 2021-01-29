package com.bsixel.mysticism.init.datagen;

import com.bsixel.mysticism.MysticismMod;
import com.bsixel.mysticism.init.registries.ItemRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.RegistryObject;

public class ItemsProvider extends ItemModelProvider {

    public ItemsProvider(DataGenerator generator, ExistingFileHelper helper) {
        super(generator, MysticismMod.MOD_ID, helper);
    }

    private void registerBasicItem(RegistryObject<Item> item, String path) {
        singleTexture(item.get().getRegistryName().getPath(), new ResourceLocation("item/handheld"), "layer0", new ResourceLocation("item/"+path));
    }

    @Override
    protected void registerModels() {
        registerBasicItem(ItemRegistry.WATER_ORB_ITEM, "water_orb");
    }
}
