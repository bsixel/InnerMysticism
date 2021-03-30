package com.bsixel.mysticism.common.api.spells.instances;

import com.bsixel.mysticism.common.api.spells.Spell;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

public class SpellInstance {

    // TODO: Maybe add initial cast world?
    private final LivingEntity caster;
    private final Spell spell;
    private final int casterId;
    private final BlockPos initialCastPos;
    private final Vector3d initialCastDirection;

    public SpellInstance(LivingEntity caster, Spell spell) {
        this.caster = caster;
        this.casterId = caster.getEntityId();
        this.spell = spell;
        this.initialCastPos = caster.getPosition();
        this.initialCastDirection = caster.getLookVec();
    }

    public LivingEntity getCaster() {
        return caster;
    }

    public Spell getSpell() {
        return spell;
    }

    public int getCasterId() {
        return casterId;
    }

    public BlockPos getInitialCastLocation() {
        return initialCastPos;
    }

    public Vector3d getInitialCastDirection() {
        return initialCastDirection;
    }

}
