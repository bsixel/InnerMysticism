package com.bsixel.mysticism.common.blocks.tileentities;

import com.bsixel.mysticism.common.capability.mana.Force;
import com.bsixel.mysticism.common.capability.mana.IManaHolder;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

import java.util.HashMap;
import java.util.Map;

public abstract class MysticismManaTileEntity extends TileEntity implements IManaHolder {

    private int mysticismLevel = 0;
    private float mysticismXp = 0;
    private float maxMana = 1000;
    private float currentMana = 0;
    public boolean isLocked = false;
    private String owner = null; // By default things have no owner
    private Force force = Force.BALANCE; // By default, the world is bound to balance
    private Map<Force, Float> attenuation = new HashMap<>();

    public MysticismManaTileEntity(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    @Override
    public int getMysticismLevel() {
        return this.mysticismLevel;
    }

    @Override
    public void setMysticismLevel(int level) {
        this.mysticismLevel = level;
    }

    @Override
    public float getMysticismXp() {
        return this.mysticismXp;
    }

    @Override
    public void setMysticismXp(float xp) {
        this.mysticismXp = xp;
    }

    @Override
    public float getMaxMana() {
        return this.maxMana;
    }

    @Override
    public void setMaxMana(float amt) {
        this.maxMana = amt;
    }

    @Override
    public float addMaxMana(float amt) {
        this.maxMana += amt;
        return this.maxMana;
    }

    @Override
    public float getCurrentMana() {
        if (this.currentMana > this.maxMana) {
            this.currentMana = this.maxMana;
        } else if (this.currentMana < 0) {
            this.currentMana = 0;
        }
        return this.currentMana;
    }

    @Override
    public boolean isRefillable() { // TODO: Overwrite this for some blocks and items which can't be refilled once depleted
        return this.currentMana < this.maxMana;
    }

    @Override
    public void setMana(float amt) {
        this.currentMana = amt;
    }

    public float addMana(float amount) { // TODO: I guess this also works to remove mana, maybe rename?
        this.currentMana += amount;
        markDirty();
        return this.getCurrentMana();
    }

    public boolean hasOwner() {
        return this.owner != null;
    }

    public String getOwner() {
        return this.owner;
    }

    @Override
    public void setPrimaryForce(Force force) {
        this.force = force;
    }

    @Override
    public Force getPrimaryForce() {
        return this.force;
    }

    @Override
    public Map<Force, Float> getAttenuations() {
        return this.attenuation;
    }

    @Override
    public void setForceAttenuation(Force force, float amount) {
        markDirty();
        this.attenuation.put(force, amount);
    }

    @Override
    public void initializeAttenuations() {
        // Shouldn't ever be necessary but it's required by the interface, so...
        if (this.attenuation == null) {
            this.attenuation = new HashMap<>();
        }
    }

    @Override
    public float getForceAttenuation(Force force) {
        return this.attenuation.getOrDefault(force, 0f); // By default things have no attenuation to any particular force
    }

    public boolean toggleLocked() {
        return this.isLocked = !this.isLocked;
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        return super.write(compound);
    }

    @Override
    public LivingEntity getAttachedEntity() {
        return null; // No entity by default; However a tile could be setup to draw a player's mana, or a mob's. Some collectors will likely do this if I ever get around to it TODO
    }
}
