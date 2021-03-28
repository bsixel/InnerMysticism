package com.bsixel.mysticism.common.api.spells.actions;

import com.bsixel.mysticism.MysticismMod;
import com.bsixel.mysticism.common.api.capability.mana.Force;
import com.bsixel.mysticism.common.api.spells.BaseSpellComponent;
import com.bsixel.mysticism.common.api.spells.SpellComponentInstance;
import com.bsixel.mysticism.common.api.spells.SpellHelper;
import com.bsixel.mysticism.common.api.spells.enhancements.ISpellEnhancement;
import com.bsixel.mysticism.common.api.spells.enhancements.SpellEnhancementPower;
import com.bsixel.mysticism.common.api.spells.instances.ISpellInstance;
import com.bsixel.mysticism.common.utilities.ItemHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.common.util.FakePlayerFactory;

import javax.annotation.Nonnull;
import java.util.List;

public class DigAction extends BaseSpellComponent implements ISpellAction { // TODO: Blacklist blocks you can't break somehow

    private static final ResourceLocation location = new ResourceLocation(MysticismMod.MOD_ID, "spellcomponent.dig");

    @Override
    public boolean applyToEntity(World world, EntityRayTraceResult trace, ISpellInstance spellInstance, SpellComponentInstance wrapper) {
        return false; // This can't be applied to entities
    }

    @Override // TODO / NOTE: This whole thing should be called for each block; We're not handling AoE here. I don't think... The cast type itself will handle multi-hits.
    public boolean applyToBlock(World world, @Nonnull BlockRayTraceResult trace, ISpellInstance spellInstance, SpellComponentInstance wrapper) {
        if (trace.getType() == RayTraceResult.Type.BLOCK) {
            LivingEntity caster = (LivingEntity) world.getEntityByID(spellInstance.getCasterId());
            BlockState initialTargetState = world.getBlockState(trace.getPos()); // Ignore this warning, block trace result should always be non null
            Block hitBlock = initialTargetState.getBlock();
            TileEntity tile = world.getTileEntity(trace.getPos()); // This may very well be null, but it shouldn't matter
            int miningLevel = 1; // By default start at 1; Meaning you can break iron ore; Effectively a stone pickaxe, any lower is nearly useless, but maybe downgrade to avoid skipping stone age
            miningLevel = (int) (miningLevel + wrapper.getChildren().stream().filter(child -> child.getComponent() instanceof SpellEnhancementPower).count());
            ItemStack mockBreakerStack = ItemHelper.getToolOrMock(caster, initialTargetState, miningLevel);
            miningLevel = Math.max(miningLevel, mockBreakerStack.getHarvestLevel(ToolType.PICKAXE, null, null));

            // TODO: Maybe we register with an enhancement what it should do for our class???
            // TODO: Apply enhancements to item to apply enchantments or whatever. Also figure out how to autosmelt
            // ex: https://github.com/MinecraftForge/MinecraftForge/blob/813f3d630de31b3f0ce20955ff6e6c335bc0c0cd/src/test/java/net/minecraftforge/debug/gameplay/loot/GlobalLootModifiersTest.java#L146
            for (ISpellEnhancement child : wrapper.getChildEnhancementComponents()) {
                mockBreakerStack = child.modifyAndReturn(DigAction.class, spellInstance, mockBreakerStack); // Stuff like a fortune enhancement applying fortune to the itemstack
            }

            if (initialTargetState.getBlockHardness(world, trace.getPos()) != -1 && miningLevel >= initialTargetState.getHarvestLevel() && SpellHelper.canEntityBreakPos(world, caster, trace.getPos())) { // If we're clientside or the block is... safetied by forge or spawn or something, we won't break it
                // If we have silk touch or fortune, it'll be handled on their end - altering/returning a different itemstack. Maybe? Ugh I have no idea how drops work in this version
                List<ItemStack> initialDrops = Block.getDrops(initialTargetState, (ServerWorld) world, trace.getPos(), tile, caster, mockBreakerStack);
                if (caster instanceof PlayerEntity) {
                    hitBlock.onBlockHarvested(world, trace.getPos(), initialTargetState, (PlayerEntity) caster);
                } else { // Yes I know I could just use a ternary instead, this is slightly more legible
                    hitBlock.onBlockHarvested(world, trace.getPos(), initialTargetState, FakePlayerFactory.getMinecraft((ServerWorld) world));
                }
                world.destroyBlock(trace.getPos(), false, caster); // False because spell is handling drops, world doesn't need to // TODO: When Ultimine adds API, update this so we don't void stuff

                initialDrops.forEach(itemStack -> {
                    ItemEntity ent = new ItemEntity(world, Math.floor(trace.getPos().getX())+0.5, Math.floor(trace.getPos().getY()), Math.floor(trace.getPos().getZ())+0.5, itemStack); // TODO: Randomize this position a little so it doesn't look so damn unnatural
                    for (ISpellEnhancement child : wrapper.getChildEnhancementComponents()) {
                        ent = (ItemEntity) child.modifyAndReturn(DigAction.class, spellInstance, ent);
                    }
                    ent.setVelocity(0, 0, 0);
                    world.addEntity(ent);
                });
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
    public TranslationTextComponent getName() {
        return new TranslationTextComponent("spell.component.dig.name");
    }

    @Override
    public TranslationTextComponent getDescription() {
        return new TranslationTextComponent("spell.component.dig.description");
    }

    @Override
    public ResourceLocation getResourceLocation() {
        return location;
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(Items.IRON_SHOVEL);
    }

    @Override
    public double getAttenuationToForce(Force force) {
        return force == Force.EARTH ? this.getCost() : 0;
    }

    @Override
    public double getCost() {
        return 10;
    }

    @Override
    public double getSustainedCost() { // If it's used in a beam or cloud or something
        return 10;
    }
}
