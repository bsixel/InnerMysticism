package com.bsixel.mysticism.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockDisplayReader;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;

public abstract class MysticismBlock extends Block {

    private static final Properties DEFAULT_PROPS = Properties.create(Material.ROCK)
            .harvestLevel(0)
            .harvestTool(ToolType.PICKAXE)
            .setLightLevel(v -> 4)
            .setAllowsSpawn((s,r,p,e) -> false)
            .sound(SoundType.STONE)
            .hardnessAndResistance(1.0f);

    public MysticismBlock() {
        super(DEFAULT_PROPS);
    }

    public MysticismBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void dropXpOnBlockBreak(ServerWorld worldIn, BlockPos pos, int amount) {
        super.dropXpOnBlockBreak(worldIn, pos, amount);
    }
}
