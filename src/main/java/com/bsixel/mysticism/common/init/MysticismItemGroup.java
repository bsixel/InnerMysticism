package com.bsixel.mysticism.common.init;

import com.bsixel.mysticism.MysticismMod;
import com.bsixel.mysticism.common.init.registries.ItemRegistry;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class MysticismItemGroup extends ItemGroup {

    public MysticismItemGroup() {
        super(MysticismMod.MOD_ID);
    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(ItemRegistry.WATER_ORB_ITEM.get()); // TODO: We should use one of the Balance items or something instead
    }
}
