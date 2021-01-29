package com.bsixel.mysticism.common.blocks.standard;

import com.bsixel.mysticism.common.blocks.MysticismBlock;
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
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;
import java.util.List;

public class ForceShardOreBlock extends MysticismBlock { // TODO: In force_ore_block.json we can set alt textures for each Force's ore based on block state

    public ForceShardOreBlock() { // Very soft crystal ore
        super(Properties.create(Material.ROCK).sound(SoundType.NETHER_ORE).hardnessAndResistance(1.5f, 3).setLightLevel((n)->3).harvestTool(ToolType.PICKAXE).setRequiresTool());
    }

    @Override
    public void dropXpOnBlockBreak(ServerWorld worldIn, BlockPos pos, int amount) {
        super.dropXpOnBlockBreak(worldIn, pos, 2);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("A crystalline ore containing shards of a Force, bound to the earth by some ancient process."));
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public void onPlayerDestroy(IWorld worldIn, BlockPos pos, BlockState state) {
        // TODO: Force specific stuff
        super.onPlayerDestroy(worldIn, pos, state);
    }
}
