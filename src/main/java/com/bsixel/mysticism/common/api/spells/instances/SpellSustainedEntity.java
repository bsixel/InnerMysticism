package com.bsixel.mysticism.common.api.spells.instances;

import com.bsixel.mysticism.common.api.spells.SpellComponentInstance;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

public class SpellSustainedEntity extends AreaEffectCloudEntity {

    private final SpellInstance spellInstance;
    private final int casterId;
    private final BlockPos initialCastLocation;
    private final Vector3d initialCastDirection;
    private SpellComponentInstance wrapper;

    public SpellSustainedEntity(LivingEntity caster, SpellInstance spellInstance, SpellComponentInstance wrapper) {
        super(caster.world, caster.getPosX(), caster.getPosY(), caster.getPosZ());
        this.spellInstance = spellInstance;
        this.wrapper = wrapper;
        this.casterId = caster.getEntityId();
        this.initialCastLocation = caster.getPosition();
        this.initialCastDirection = caster.getLookVec();
    }

}
