package com.bsixel.mysticism.common.api.spells.casttypes;

import com.bsixel.mysticism.common.api.spells.ISpellComponent;
import com.bsixel.mysticism.common.api.spells.SpellComponentInstance;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

public interface ISpellCastType extends ISpellComponent { // Self, sustained (children: beam, chain, self sus), projectile, touch, rune etc. TODO: Will need subclasses

    boolean cast(LivingEntity caster, SpellComponentInstance wrapper);
    boolean cast(BlockPos sourcePos, Vector3d lookVector, SpellComponentInstance wrapper);

}
