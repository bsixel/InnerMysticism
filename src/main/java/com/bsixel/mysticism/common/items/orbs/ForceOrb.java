package com.bsixel.mysticism.common.items.orbs;

import com.bsixel.mysticism.common.api.capability.mana.Force;
import com.bsixel.mysticism.common.api.capability.mana.ManaCapability;
import com.bsixel.mysticism.init.registries.ItemRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nullable;
import java.util.List;

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
            tooltip.add(new TranslationTextComponent("tooltip.mysticism.force_orb_description", force.getName()));
            if (InputMappings.isKeyDown(Minecraft.getInstance().getMainWindow().getHandle(), GLFW.GLFW_KEY_LEFT_SHIFT)) { // Only show mana contents on left shft held
                tooltip.add(new StringTextComponent("Mana: " + mystTag.getInt("currentMana") + "/" + mystTag.getInt("maxMana")).mergeStyle(TextFormatting.AQUA));
            } else {
                tooltip.add(new TranslationTextComponent("tooltip.mysticism.shift_for_more"));
            }
        }
    }

    @Override
    public void onCreated(ItemStack stack, World worldIn, PlayerEntity playerIn) {
        // Without additional info, give between 250 and 1k and don't use world seed etc
        if (stack.getTag() == null || stack.getChildTag("mysticism") == null) { // Item doesn't yet have any NBT tag with the data we need, initialize its stored mana TODO: Bake this into the item being spawned/dropped/etc
            CompoundNBT baseTag = new CompoundNBT();
            baseTag.put("mysticism", new CompoundNBT());
            stack.setTag(baseTag);
            CompoundNBT mystTag = stack.getChildTag("mysticism");
            int maxMana = worldIn.rand.nextInt(1000-250) + 250;
            mystTag.putInt("maxMana", maxMana);
            mystTag.putInt("currentMana", worldIn.rand.nextInt(maxMana));
        }
        super.onCreated(stack, worldIn, playerIn);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity player, Hand handUsed) {
        ItemStack stack = player.getHeldItem(handUsed);
        if (!player.getCooldownTracker().hasCooldown(this) && stack.hasTag()) {
            player.getCapability(ManaCapability.mana_cap, null).ifPresent(playerMana -> {
                double missingPlayerMana = playerMana.getMaxMana()-playerMana.getCurrentMana();
                double manaToAdd = missingPlayerMana > 100 ? 100 : missingPlayerMana;
                CompoundNBT mystTag = stack.getChildTag("mysticism");
                int currentOrbMana = mystTag.getInt("currentMana");
                if (manaToAdd > 0) {
                    if (currentOrbMana <= manaToAdd) { // Discard orb if we drain it entirely
                        playerMana.addMana(currentOrbMana);
                        stack.setCount(0);
                    } else {
                        playerMana.addMana(manaToAdd);
                        currentOrbMana-=manaToAdd;
                        mystTag.putInt("currentMana", currentOrbMana);
                    }
                    player.getCooldownTracker().setCooldown(this, 100);
                }
            });
        } else {
            return ActionResult.resultFail(player.getHeldItem(handUsed));
        }
        return ActionResult.resultSuccess(player.getHeldItem(handUsed));
    }

}
