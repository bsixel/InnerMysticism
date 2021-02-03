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
            Block.makeCuboidShape(5, 3, 4, 11, 8, 10),
            Block.makeCuboidShape(4, 11, 0, 12, 13, 9),
            Block.makeCuboidShape(10, 0, 3, 12, 4, 5),
            Block.makeCuboidShape(4, 0, 3, 6, 4, 5),
            Block.makeCuboidShape(4, 0, 9, 6, 4, 11),
            Block.makeCuboidShape(10, 0, 9, 12, 4, 11),
            Block.makeCuboidShape(2, 8, 4, 3, 15, 5),
            Block.makeCuboidShape(13, 8, 4, 14, 15, 5),
            Block.makeCuboidShape(14, 8, 8, 15, 15, 9),
            Block.makeCuboidShape(12, 8, 12, 13, 15, 13),
            Block.makeCuboidShape(7, 8, 12, 9, 15, 14),
            Block.makeCuboidShape(1, 8, 8, 2, 15, 9),
            Block.makeCuboidShape(3, 8, 12, 4, 15, 13)
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
