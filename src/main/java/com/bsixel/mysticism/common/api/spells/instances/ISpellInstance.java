package com.bsixel.mysticism.common.api.spells.instances;

import com.bsixel.mysticism.common.api.spells.Spell;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

public interface ISpellInstance {

    int getCasterId();
    BlockPos getInitialCastLocation();
    Vector3d getInitialCastDirection();
    Spell getSpell();

    void onInteractWithEntity();
    void onInteractWithBlock();

}
