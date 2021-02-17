package com.bsixel.mysticism.common.items.crystals;

import com.bsixel.mysticism.common.api.capability.mana.Force;
import com.bsixel.mysticism.init.registries.ItemRegistry;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public abstract class ForceCrystal extends Item { // TODO: Different crystals can have weights for how effective they are in ritual purification + cost etc

    Force force = Force.BALANCE;

    public ForceCrystal() {
        super(ItemRegistry.getDefaultProperties());
    }

    public ForceCrystal(Properties properties) {
        super(ItemRegistry.getDefaultProperties());
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("tooltip.mysticism.force_crystal_tooltip", force.getName()));
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }
}
