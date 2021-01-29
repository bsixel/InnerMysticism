package com.bsixel.mysticism.common.capability.mana;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;

import java.util.HashMap;
import java.util.Map;

public class DefaultManaHolder implements IManaHolder {

    private final LivingEntity attachedEntity;
    private int mysticismLevel = 0;
    private float mysticismXp = 0;
    private float maxMana = 1000;
    private float currentMana = 0;
    private Force force = Force.BALANCE; // By default, the world is bound to balance
    private Map<Force, Float> attenuation = new HashMap<>();

    public DefaultManaHolder(LivingEntity entity) {
        this.attachedEntity = entity;
    }

    @Override
    public LivingEntity getAttachedEntity() {
        return this.attachedEntity;
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

    private void fixCurrentMana() {
        if (this.currentMana > this.maxMana) {
            this.currentMana = this.maxMana;
        } else if (this.currentMana < 0) {
            this.currentMana = 0;
        }
    }

    @Override
    public float getCurrentMana() {
        this.fixCurrentMana();
        return this.currentMana;
    }

    @Override
    public boolean isRefillable() { // TODO: Overwrite this for some blocks and items which can't be refilled once depleted
        return this.currentMana < this.maxMana;
    }

    @Override
    public void setMana(float amt) {
        this.currentMana = amt;
        this.fixCurrentMana();
    }

    public float addMana(float amount) { // TODO: I guess this also works to remove mana, maybe rename?
        this.currentMana += amount;
        return this.getCurrentMana();
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
    public void initializeAttenuations() {
        // Shouldn't ever be necessary but it's required by the interface, so...
        if (this.attenuation == null) {
            this.attenuation = new HashMap<>();
        }
    }

    @Override
    public Map<Force, Float> getAttenuations() {
        return this.attenuation;
    }

    @Override
    public void setForceAttenuation(Force force, float amount) {
        this.attenuation.put(force, amount);
    }

    @Override
    public float getForceAttenuation(Force force) {
        return this.attenuation.getOrDefault(force, 0f); // By default things have no attenuation to any particular force
    }
}
