package com.bsixel.mysticism.common.api.spells.casttypes;

import com.bsixel.mysticism.MysticismMod;
import com.bsixel.mysticism.common.api.capability.mana.Force;
import com.bsixel.mysticism.common.api.spells.BaseSpellComponent;
import com.bsixel.mysticism.common.api.spells.ISpellComponent;
import com.bsixel.mysticism.common.api.spells.Spell;
import com.bsixel.mysticism.common.api.spells.SpellComponentInstance;
import com.bsixel.mysticism.common.api.spells.actions.ISpellAction;
import com.bsixel.mysticism.common.api.spells.enhancements.SpellEnhancementPower;
import com.bsixel.mysticism.common.api.spells.instances.SpellInstanceTouch;
import javafx.scene.paint.Color;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;

public class SpellCastTypeTouch extends BaseSpellComponent implements ISpellCastType {
    private static final ResourceLocation location = new ResourceLocation(MysticismMod.MOD_ID, "spellcomponent.touch");

    public SpellCastTypeTouch() {}

    @Override
    public boolean isActive() {
        return true; // TODO: These should be loaded from configs
    }

    @Override
    public boolean cast(LivingEntity caster, SpellComponentInstance wrapper) { // TODO: We'll need to do something different here if they're going AOE touch
        RayTraceResult traceResult = caster.pick(this.calculateRange(caster), 0, this.canTouchFluids());
        return wrapper.getWrappedChildActionComponents().stream().allMatch(child -> {
            ISpellAction childAction = (ISpellAction) child.getComponent();
            // TODO: Add another statement for touch-affecting enhancements - range fluid touching etc
            if (traceResult.getType() == RayTraceResult.Type.BLOCK) {
                BlockRayTraceResult correctedResult = (BlockRayTraceResult) traceResult; // I think this is actually safe?
                return childAction.applyToBlock(caster.getEntityWorld(), correctedResult, new SpellInstanceTouch(caster.getEntityId(), caster.getPosition(), caster.getLookVec(), wrapper.getParentSpell()), child); // TODO: Clean
            } else if (traceResult.getType() == RayTraceResult.Type.ENTITY) {
                EntityRayTraceResult correctedResult = (EntityRayTraceResult) traceResult;
                return childAction.applyToEntity(caster.getEntityWorld(), correctedResult, new SpellInstanceTouch(caster.getEntityId(), caster.getPosition(), caster.getLookVec(), wrapper.getParentSpell()), child);
            }
            return false;
        });
    }

    @Override
    public boolean cast(BlockPos sourcePos, Vector3d lookVector, SpellComponentInstance wrapper) {
        // Hold for now
        return false;
    }

    @Override
    public TranslationTextComponent getName() {
        return new TranslationTextComponent("spell.component.touch.name");
    }

    @Override
    public TranslationTextComponent getDescription() {
        return new TranslationTextComponent("spell.component.touch.description");
    }

    @Override
    public ResourceLocation getResourceLocation() {
        return location;
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(Items.OAK_BUTTON); // TODO
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
