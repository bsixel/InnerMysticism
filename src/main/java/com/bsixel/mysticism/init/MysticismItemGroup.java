package com.bsixel.mysticism.init;

import com.bsixel.mysticism.MysticismMod;
import com.bsixel.mysticism.init.registries.BlockRegistry;
import com.bsixel.mysticism.init.registries.ItemRegistry;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class MysticismItemGroup extends ItemGroup {

    public MysticismItemGroup() {
        super(MysticismMod.MOD_ID);
    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(ItemRegistry.WATER_ORB_ITEM.get());
    }
}
