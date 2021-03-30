package com.bsixel.mysticism.common.api.spells.instances;

import com.bsixel.mysticism.common.api.spells.Spell;
import com.bsixel.mysticism.common.api.spells.SpellComponentInstance;
import com.bsixel.mysticism.common.api.spells.SpellHelper;
import com.bsixel.mysticism.common.api.spells.actions.ISpellAction;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class SpellProjectileEntity extends ArrowEntity { // NOTE: func_230299_a_ in ProjectileEntity is basically onBlockHit, I think

    private final SpellComponentInstance wrapper;
    private final SpellInstance spellInstance;

    public SpellProjectileEntity(World worldIn, LivingEntity caster, SpellInstance spellInstance, SpellComponentInstance wrapper) {
        super(worldIn, caster);
        this.spellInstance = spellInstance;
        this.wrapper = wrapper;
        this.pickupStatus = AbstractArrowEntity.PickupStatus.DISALLOWED; // Obviously you can't pick up spells, obviously
    }

    @SuppressWarnings("NullableProblems")
    @Override
    protected void onEntityHit(EntityRayTraceResult rayTraceResult) { // TODO: Is this called automatically now?? I sure hope so. Do I need both this *and* onImpact?
        /*
         * See {@net.minecraft.entity.projectile.AbstractArrowEntity.onEntityHit} for some interesting examples
         */
//        super.onEntityHit(rayTraceResult); // We probably don't want that? Maybe?
    }

    @Override
    protected void onImpact(RayTraceResult result) {
        wrapper.getWrappedChildActionComponents().forEach(child -> {
            ISpellAction childAction = (ISpellAction) child.getComponent();
            // TODO: Add another statement for touch-affecting enhancements - range fluid touching etc
            if (result.getType() == RayTraceResult.Type.BLOCK) {
                BlockRayTraceResult correctedResult = (BlockRayTraceResult) result; // I think this is actually safe?
                childAction.applyToBlock(this.world, correctedResult, this.spellInstance, child); // TODO: Clean
            } else if (result.getType() == RayTraceResult.Type.ENTITY) {
                EntityRayTraceResult correctedResult = (EntityRayTraceResult) result;
                childAction.applyToEntity(this.world, correctedResult, this.spellInstance, child);
            }
        });
    }

}
