package com.bsixel.mysticism.common.entities.projectiles;

import com.bsixel.mysticism.common.api.spells.Spell;
import com.bsixel.mysticism.common.api.spells.SpellComponentInstance;
import com.bsixel.mysticism.common.api.spells.actions.ISpellAction;
import com.bsixel.mysticism.common.api.spells.instances.SpellInstance;
import com.bsixel.mysticism.common.init.registries.EntityRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nonnull;

public class SpellProjectileEntity extends ArrowEntity implements IAnimatable { // NOTE: func_230299_a_ in ProjectileEntity is basically onBlockHit, I think

    private AnimationFactory animationFactory = new AnimationFactory(this);

    private SpellComponentInstance wrapper;
    private SpellInstance spellInstance;
    private int lifespan = 100; // Projectile lasts for 100 ticks (5 seconds) unless altered
    private Vector3d preservedMotion;

    public SpellProjectileEntity(SpellInstance spellInstance, SpellComponentInstance wrapper) {
        super(spellInstance.getCaster().world, spellInstance.getCaster());
        this.spellInstance = spellInstance;
        this.wrapper = wrapper;
        this.pickupStatus = AbstractArrowEntity.PickupStatus.DISALLOWED; // Obviously you can't pick up spells
        this.setNoGravity(true); // TODO: Handle if we add a gravity changing enhancement
        this.preservedMotion = new Vector3d(this.getMotion().x, this.getMotion().y, this.getMotion().z);
    }

    public SpellProjectileEntity(EntityType<SpellProjectileEntity> entityEntityType, World world) {
        super(entityEntityType, world);
    }

    public SpellProjectileEntity(World world) {
        super(EntityRegistry.SPELL_PROJECTILE.get(), world);
    }

    @Override
    @Nonnull
    public EntityType<SpellProjectileEntity> getType() {
        return EntityRegistry.SPELL_PROJECTILE.get();
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void readAdditional(@Nonnull CompoundNBT nbt) {
        super.deserializeNBT(nbt);
        this.lifespan = nbt.getInt("myst_lifespan");
        this.spellInstance = new SpellInstance(this.getCaster(), Spell.deserialize((CompoundNBT) nbt.get("myst_spell")));
        this.wrapper = SpellComponentInstance.deserialize(this.spellInstance.getSpell(), (CompoundNBT) nbt.get("myst_wrapper"));
        this.preservedMotion = new Vector3d(nbt.getDouble("myst_motionX"), nbt.getDouble("myst_motionY"), nbt.getDouble("myst_motionZ"));
    }

    @Override
    public void writeAdditional(@Nonnull CompoundNBT nbt) {
        super.writeAdditional(nbt);
        nbt.putInt("myst_lifespan", this.lifespan);
        nbt.put("myst_spell", this.spellInstance.getSpell().serialize());
        nbt.put("myst_wrapper", this.wrapper.serialize());
        nbt.putDouble("myst_motionX", this.preservedMotion.x);
        nbt.putDouble("myst_motionY", this.preservedMotion.y);
        nbt.putDouble("myst_motionZ", this.preservedMotion.z);
    }

    public LivingEntity getCaster() {
        return (LivingEntity) this.func_234616_v_();
    }

    private float velocity() {
        return 1.5F;
    }

    public void launch() {
        super.func_234612_a_(spellInstance.getCaster(), spellInstance.getCaster().rotationPitch, spellInstance.getCaster().rotationYaw, 0.0F, velocity(), 0);
        this.preservedMotion = new Vector3d(this.getMotion().x, this.getMotion().y, this.getMotion().z);
    }

    @Override
    public void tick() {
        if (this.preservedMotion == null) { // Just in case, really
            this.preservedMotion = new Vector3d(this.getMotion().x, this.getMotion().y, this.getMotion().z);
        }

        this.lifespan--;
        if (lifespan <= 0) { // Just in case we miss a tick somehow I guess
            this.remove();
            return;
        }

        // Make sure we maintain motion through liquids etc
        this.setMotion(this.preservedMotion.x, this.preservedMotion.y, this.preservedMotion.z);

        super.tick();
    }

    @Override
    protected void doBlockCollisions() {
        super.doBlockCollisions();
    }

    @Override
    protected void onInsideBlock(BlockState state) {
        super.onInsideBlock(state);
    }

    @Override
    public boolean canRenderOnFire() {
        return false;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    protected void onEntityHit(EntityRayTraceResult rayTraceResult) { // TODO: Is this called automatically now?? I sure hope so. Do I need both this *and* onImpact?
        /*
         * See {@net.minecraft.entity.projectile.AbstractArrowEntity.onEntityHit} for some interesting examples
         */
//        super.onEntityHit(rayTraceResult); // We probably don't want that? Maybe?
    }

    @Override
    protected void onImpact(@Nonnull RayTraceResult result) {
        if (!this.world.isRemote) {
            wrapper.getWrappedChildActionComponents().forEach(child -> {
                ISpellAction childAction = (ISpellAction) child.getComponent();
                // TODO: Add another statement for touch-affecting enhancements - range fluid touching etc
                if (result.getType() == RayTraceResult.Type.BLOCK) {
                    BlockRayTraceResult correctedResult = (BlockRayTraceResult) result; // I think this is actually safe?
                    childAction.applyToBlock(this.world, correctedResult, this.spellInstance, child); // TODO: Clean
                } else if (result.getType() == RayTraceResult.Type.ENTITY) {
                    EntityRayTraceResult correctedResult = (EntityRayTraceResult) result;
                    childAction.applyToEntity(this.world, correctedResult, this.spellInstance, child);
                }
            });
        }
        this.remove(); // TODO: Check piercing sorta modifier first to make sure we're not puncturing more stuff. Or maybe handle that differently?
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.bike.idle", true));
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.animationFactory;
    }
}
