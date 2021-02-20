package com.bsixel.mysticism.common.api.spells.actions;

import com.bsixel.mysticism.common.api.capability.mana.Force;
import com.bsixel.mysticism.common.api.spells.BaseSpellComponent;
import com.bsixel.mysticism.common.api.spells.ISpellComponent;
import com.bsixel.mysticism.common.api.spells.SpellHelper;
import com.bsixel.mysticism.common.api.spells.enhancements.ISpellEnhancement;
import com.bsixel.mysticism.common.api.spells.enhancements.SpellEnhancementPower;
import com.bsixel.mysticism.common.api.spells.instances.ISpellInstance;
import com.bsixel.mysticism.common.utilities.ItemHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nonnull;
import java.util.List;

public class DigAction extends BaseSpellComponent implements ISpellAction { // TODO: Blacklist blocks you can't break somehow

    @Override
    public boolean applyToEntity(World world, EntityRayTraceResult trace, ISpellInstance spellInstance) {
        return false; // This can't be applied to entities
    }

    @Override // TODO / NOTE: This whole thing should be called for each block; We're not handling AoE here. I don't think... The cast type itself will handle multi-hits.
    public boolean applyToBlock(World world, @Nonnull BlockRayTraceResult trace, ISpellInstance spellInstance) {
        if (trace.getType() == RayTraceResult.Type.BLOCK) {
            LivingEntity caster = (LivingEntity) world.getEntityByID(spellInstance.getCasterId());
            BlockState initialTargetState = world.getBlockState(trace.getPos()); // Ignore this warning, block trace result should always be non null
            Block hitBlock = initialTargetState.getBlock();
            TileEntity tile = world.getTileEntity(trace.getPos()); // This may very well be null, but it shouldn't matter
            int miningLevel = 1; // By default start at 1; Meaning you can break iron ore; Effectively a stone pickaxe, any lower is nearly useless, but maybe downgrade to avoid skipping stone age
            miningLevel = (int) (miningLevel + children.stream().filter(child -> child instanceof SpellEnhancementPower).count());
            ItemStack mockBreakerStack = ItemHelper.getToolOrMock(caster, initialTargetState, miningLevel);
            miningLevel = Math.max(miningLevel, mockBreakerStack.getHarvestLevel(ToolType.PICKAXE, null, null));

            // TODO: Maybe we register with an enhancement what it should do for our class???
            // TODO: Apply enhancements to item to apply enchantments or whatever. Also figure out how to autosmelt
            for (ISpellComponent child : children) {
                if (child instanceof ISpellEnhancement) {
                    mockBreakerStack = ((ISpellEnhancement) child).modifyAndReturn(DigAction.class, spellInstance, mockBreakerStack); // Stuff like a fortune enhancement applying fortune to the itemstack
                }
            }

            if (miningLevel >= initialTargetState.getHarvestLevel() && SpellHelper.canEntityBreakPos(world, caster, trace.getPos())) { // If we're clientside or the block is... safetied by forge or spawn or something, we won't break it
                // If we have silk touch or fortune, it'll be handled on their end - altering/returning a different itemstack. Maybe? Ugh I have no idea how drops work in this version
                List<ItemStack> initialDrops = Block.getDrops(initialTargetState, (ServerWorld) world, trace.getPos(), tile, caster, mockBreakerStack);
                initialDrops.forEach(itemStack -> {
                    ItemEntity ent = new ItemEntity(world, trace.getPos().getX(), trace.getPos().getY(), trace.getPos().getZ(), itemStack);
                    for (ISpellComponent child : children) {
                        if (child instanceof ISpellEnhancement) {
                            ent = (ItemEntity) ((ISpellEnhancement) child).modifyAndReturn(DigAction.class, spellInstance, ent);
                        }
                    }
                    world.addEntity(ent);
                });
                world.destroyBlock(trace.getPos(), false, caster); // False because spell is handling drops, world doesn't need to
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public String getName() {
        return "Dig";
    }

    @Override
    public String getDescription() {
        return "Break things! Dig em! Stick them in a hole! Wait, what?";
    }

    @Override
    public double getAttenuationToForce(Force force) {
        return force == Force.EARTH ? 10 : 0;
    }

    @Override
    public double getSustainedCost() { // If it's used in a beam or cloud or something
        return 5;
    }
}
