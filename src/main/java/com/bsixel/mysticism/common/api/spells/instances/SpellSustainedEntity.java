package com.bsixel.mysticism.common.api.spells.instances;

import com.bsixel.mysticism.common.api.spells.Spell;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

public class SpellSustainedEntity extends AreaEffectCloudEntity implements ISpellInstance {

    private final Spell spell;
    private final int casterId;
    private final BlockPos initialCastLocation;
    private final Vector3d initialCastDirection;

    public SpellSustainedEntity(LivingEntity caster, Spell spell) {
        super(caster.world, caster.getPosX(), caster.getPosY(), caster.getPosZ());
        this.spell = spell;
        this.casterId = caster.getEntityId();
        this.initialCastLocation = caster.getPosition();
        this.initialCastDirection = caster.getLookVec();
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
        spell.initialCastType.cast((LivingEntity) world.getEntityByID(getCasterId())); // TODO: FIX
    }

    @Override
    public void onInteractWithBlock() {

    }
}
