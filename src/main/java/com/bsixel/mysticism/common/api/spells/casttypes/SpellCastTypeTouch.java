package com.bsixel.mysticism.common.api.spells.casttypes;

import com.bsixel.mysticism.common.api.capability.mana.Force;
import com.bsixel.mysticism.common.api.spells.ISpellComponent;
import com.bsixel.mysticism.common.api.spells.Spell;
import com.bsixel.mysticism.common.api.spells.actions.ISpellAction;
import com.bsixel.mysticism.common.api.spells.BaseSpellComponent;
import com.bsixel.mysticism.common.api.spells.instances.SpellInstanceTouch;
import javafx.scene.paint.Color;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;

import javax.annotation.Nullable;

public class SpellCastTypeTouch extends BaseSpellComponent implements ISpellCastType {

    public SpellCastTypeTouch() {}

    public SpellCastTypeTouch(Spell parentSpell, @Nullable ISpellComponent parentComponent) {
        this.parentSpell = parentSpell;
        this.parentComponent = parentComponent;
    }

    @Override
    public boolean isActive() {
        return true; // TODO: These should be loaded from configs
    }

    @Override
    public boolean cast(LivingEntity caster) { // TODO: We'll need to do something different here if they're going AOE touch
        RayTraceResult traceResult = caster.pick(this.calculateRange(caster), 0, this.canTouchFluids());
        return this.children.stream().allMatch(child -> {
            if (child instanceof ISpellAction) {
                if (traceResult.getType() == RayTraceResult.Type.BLOCK) {
                    BlockRayTraceResult correctedResult = (BlockRayTraceResult) traceResult; // I think this is actually safe?
                    return ((ISpellAction) child).applyToBlock(caster.getEntityWorld(), correctedResult, new SpellInstanceTouch(caster.getEntityId(), caster.getPosition(), caster.getLookVec(), parentSpell));
                } else if (traceResult.getType() == RayTraceResult.Type.ENTITY) {
                    EntityRayTraceResult correctedResult = (EntityRayTraceResult) traceResult;
                    return ((ISpellAction) child).applyToEntity(caster.getEntityWorld(), correctedResult, new SpellInstanceTouch(caster.getEntityId(), caster.getPosition(), caster.getLookVec(), parentSpell));
                }
            }
            return false;
        });
    }

    @Override
    public boolean cast(BlockPos sourcePos, Vector3d lookVector) {
        // Hold for now
        return false;
    }

    @Override
    public String getName() {
        return "Touch";
    }

    @Override
    public String getDescription() {
        return "Really more like melee, but who's tracking?";
    }

    @Override
    public double getAttenuationToForce(Force force) {
        return force == Force.EARTH ? 10 : 0; // TODO: Heck if I know what these numbers should actually be right now
    }

    @Override
    public double getCost() {
        return 10;
    }

    @Override
    public double getSustainedCost() {
        return 10;
    }

    @Override
    public Color getPrimaryColor() {
        return Color.web("#3e2c12"); // Some sorta brown, idk
    }

    private double calculateRange(LivingEntity caster) { // TODO: This should take range enhancements into effect
        return 4.5; // TODO: Figure out how the heck we check the player's reach distance, for now be generous
    }

    private boolean canTouchFluids() {
        return false; // TODO: Check for some sorta fluid toucher enhancement so we can freeze water and such
    }

}
