package com.bsixel.mysticism.common.api.spells.instances;

import com.bsixel.mysticism.common.api.spells.Spell;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

public class SpellInstanceTouch extends SpellInstanceBase implements ISpellInstance {

    public SpellInstanceTouch(LivingEntity caster, Spell spell) {
        this(caster.getEntityId(), caster.getPosition(), caster.getLookVec(), spell);
    }

    public SpellInstanceTouch(int casterId, BlockPos initialCastPos, Vector3d initialCastDirection, Spell spell) {
        this.casterId = casterId;
        this.initialCastPos = initialCastPos;
        this.initialCastDirection = initialCastDirection;
        this.spell = spell;
    }

    @Override
    public void onInteractWithEntity() {
        // Not really sure what this means for a touch spell tbh
    }

    @Override
    public void onInteractWithBlock() {
        // Not really sure what this means for a touch spell tbh
    }
}
