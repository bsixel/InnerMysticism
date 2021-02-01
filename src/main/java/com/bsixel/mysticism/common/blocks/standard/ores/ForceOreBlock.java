package com.bsixel.mysticism.common.blocks.standard.ores;

import com.bsixel.mysticism.common.blocks.MysticismBlock;
import com.bsixel.mysticism.common.capability.mana.Force;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;
import java.util.List;

public abstract class ForceOreBlock extends MysticismBlock {

    Force force = Force.BALANCE;

    public ForceOreBlock() { // Very soft crystal ore
        super(Properties.create(Material.ROCK).sound(SoundType.STONE).hardnessAndResistance(1.5f, 3).setLightLevel((n)-> 7).harvestTool(ToolType.PICKAXE).setRequiresTool());
    }

    @Override
    public void dropXpOnBlockBreak(ServerWorld worldIn, BlockPos pos, int amount) { // TODO: Figure out why this doesn't drop xp
        super.dropXpOnBlockBreak(worldIn, pos, RANDOM.nextInt(5)); // It's magic, drop some xp even if you used silk touch. I guess maybe that's a bad idea because of infinite xp gen but whatever, there's better ways.
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(new TranslationTextComponent("tooltip.mysticism.force_ore_block_description", this.force.getName()));
    }

    @Override
    // NOTE: Occurs once per side
    public void onPlayerDestroy(IWorld worldIn, BlockPos pos, BlockState state) {
        // TODO: Force specific stuff
        super.onPlayerDestroy(worldIn, pos, state);
    }
}
