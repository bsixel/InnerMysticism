package com.bsixel.mysticism.common.api.spells.instances;

import com.bsixel.mysticism.common.api.spells.Spell;
import com.bsixel.mysticism.common.api.spells.SpellHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class SpellProjectileEntity extends ArrowEntity implements ISpellInstance { // NOTE: func_230299_a_ in ProjectileEntity is basically onBlockHit, I think

    private final Spell spell;
    private final int casterId;
    private final BlockPos initialCastLocation;
    private final Vector3d initialCastDirection;

    public SpellProjectileEntity(World worldIn, LivingEntity shooter, Spell spell) {
        super(worldIn, shooter);
        this.spell = spell;
        this.casterId = shooter.getEntityId();
        this.pickupStatus = AbstractArrowEntity.PickupStatus.DISALLOWED; // Obviously you can't pick up spells
        this.initialCastLocation = shooter.getPosition();
        this.initialCastDirection = shooter.getLookVec();
    }

    @SuppressWarnings("NullableProblems")
    @Override
    protected void onEntityHit(EntityRayTraceResult rayTraceResult) { // TODO: Is this called automatically now?? I sure hope so
        /**
         * See {@net.minecraft.entity.projectile.AbstractArrowEntity.onEntityHit} for some interesting examples
         */
//        super.onEntityHit(rayTraceResult); We probably don't want that? Maybe?
    }

    @Override
    protected void onImpact(RayTraceResult result) {
        super.onImpact(result);
    }

    @Override
    public int getCasterId() {
        return this.casterId;
    }

    @Override
    public BlockPos getInitialCastLocation() {
        return this.initialCastLocation;
    }

    @Override
    public Vector3d getInitialCastDirection() {
        return this.initialCastDirection;
    }

    @Override
    public Spell getSpell() {
        return this.spell;
    }

    @Override
    public void onInteractWithEntity() {
        spell.cast((LivingEntity) world.getEntityByID(getCasterId())); // TODO: FIX
    }

    @Override
    public void onInteractWithBlock() {

    }
}
