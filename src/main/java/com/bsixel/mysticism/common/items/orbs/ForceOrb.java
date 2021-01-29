package com.bsixel.mysticism.common.items.orbs;

import com.bsixel.mysticism.common.capability.mana.Force;
import com.bsixel.mysticism.common.capability.mana.ManaCapability;
import com.bsixel.mysticism.init.registries.ItemRegistry;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public abstract class ForceOrb extends Item {
    Force force = Force.BALANCE;

    public ForceOrb() {
        super(ItemRegistry.getDefaultProperties().maxStackSize(1));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        CompoundNBT mystTag = stack.getChildTag("mysticism");
        if (mystTag != null) {
            tooltip.add(new TranslationTextComponent("A mystical orb bound to the forces of " + force.getName()));
            tooltip.add(new TranslationTextComponent("Mana: " + mystTag.getInt("currentMana") + "/" + mystTag.getInt("maxMana")).mergeStyle(TextFormatting.AQUA));
        }
    }

    @Override
    public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
        World world = context.getWorld();
        if (stack.getTag() == null || stack.getChildTag("mysticism") == null) { // Item doesn't yet have any NBT tag with the data we need
            CompoundNBT baseTag = new CompoundNBT();
            baseTag.put("mysticism", new CompoundNBT());
            stack.setTag(baseTag);
        }
        CompoundNBT mystTag = stack.getChildTag("mysticism");
        int maxMana = world.rand.nextInt(1000-250) + 250;
        mystTag.putInt("maxMana", maxMana);
        mystTag.putInt("currentMana", world.rand.nextInt(maxMana));
        return super.onItemUseFirst(stack, context);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity player, Hand handUsed) {
        // Without additional info, give between 250 and 1k and don't use world seed etc
        ItemStack stack = player.getHeldItem(handUsed);
        player.getCapability(ManaCapability.mana_cap).ifPresent(playerMana -> {
            if (stack.hasTag()) {
                float missingPlayerMana = playerMana.getMaxMana()-playerMana.getCurrentMana();
                float manaToAdd = missingPlayerMana > 100 ? 100 : missingPlayerMana;
                CompoundNBT mystTag = stack.getChildTag("mysticism");
                int currentOrbMana = mystTag.getInt("currentMana");
                if (currentOrbMana <= manaToAdd) { // Discard orb if we drain it entirely
                    playerMana.addMana(currentOrbMana);
                    stack.setCount(0);
                } else {
                    playerMana.addMana(manaToAdd);
                    currentOrbMana-=manaToAdd;
                    mystTag.putInt("currentMana", currentOrbMana);
                }
            }
        });
        return super.onItemRightClick(worldIn, player, handUsed);
    }

}
