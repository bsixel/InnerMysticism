package com.bsixel.mysticism.common.api.spells.instances;

import com.bsixel.mysticism.common.api.spells.Spell;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

public abstract class SpellInstanceBase implements ISpellInstance {

    // TODO: Maybe add initial cast world?
    protected int casterId;
    protected BlockPos initialCastPos;
    protected Vector3d initialCastDirection;
    protected Spell spell;

    @Override
    public int getCasterId() {
        return casterId;
    }

    @Override
    public BlockPos getInitialCastLocation() {
        return initialCastPos;
    }

    @Override
    public Vector3d getInitialCastDirection() {
        return initialCastDirection;
    }

    @Override
    public Spell getSpell() {
        return spell;
    }

}
