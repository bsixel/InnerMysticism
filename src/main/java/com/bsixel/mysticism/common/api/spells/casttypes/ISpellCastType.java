package com.bsixel.mysticism.common.api.spells.casttypes;

import com.bsixel.mysticism.common.api.spells.ISpellComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

public interface ISpellCastType extends ISpellComponent { // Self, sustained (children: beam, chain, self sus), projectile, touch, rune etc. TODO: Will need subclasses

    void cast(LivingEntity caster);
    void cast(BlockPos sourcePos, Vector3d lookVector);

}
