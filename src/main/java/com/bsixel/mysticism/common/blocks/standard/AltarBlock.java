package com.bsixel.mysticism.common.blocks.standard;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Stream;

public class AltarBlock extends BaseHorizontalRotatableBlock {

    private static final VoxelShape HITBOX = Stream.of(
            Block.makeCuboidShape(7, 2, 6, 11, 6, 10),
            Block.makeCuboidShape(5, 4, 4, 11, 5, 12),
            Block.makeCuboidShape(5, 0, 4, 7, 2, 6),
            Block.makeCuboidShape(11, 0, 10, 13, 2, 12),
            Block.makeCuboidShape(11, 0, 4, 13, 2, 6),
            Block.makeCuboidShape(5, 0, 10, 7, 2, 12),
            Block.makeCuboidShape(10, 3, 9, 12, 5, 11),
            Block.makeCuboidShape(6, 3, 9, 8, 5, 11),
            Block.makeCuboidShape(6, 3, 5, 8, 5, 7),
            Block.makeCuboidShape(10, 3, 5, 12, 5, 7),
            Block.makeCuboidShape(2, 6, 7, 4, 12, 9),
            Block.makeCuboidShape(2, 6, 0, 3, 16, 1),
            Block.makeCuboidShape(0, 6, 4, 1, 16, 5),
            Block.makeCuboidShape(0, 6, 11, 1, 16, 12),
            Block.makeCuboidShape(2, 6, 15, 3, 16, 16),
            Block.makeCuboidShape(9, 6, 14, 10, 16, 15),
            Block.makeCuboidShape(9, 6, 1, 10, 16, 2)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR)).get();

    public AltarBlock() {
        super(Properties.create(Material.ROCK).sound(SoundType.STONE).hardnessAndResistance(1.5f, 3).setLightLevel((n)-> 7).harvestTool(ToolType.PICKAXE).setRequiresTool());
        this.calculateHitbox(HITBOX);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(new TranslationTextComponent("tooltip.mysticism.altar.description"));
    }

    @SuppressWarnings({"deprecation", "NullableProblems"})
    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPES.get(this).get(state.get(HORIZONTAL_FACING));
    }

}
