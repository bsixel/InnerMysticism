package com.bsixel.mysticism.common.api.capability.mana;

import net.minecraft.entity.LivingEntity;

import java.util.HashMap;
import java.util.Map;

public class DefaultManaHolder implements IManaHolder {

    private final LivingEntity attachedEntity;
    private int mysticismLevel = 0;
    private double mysticismXp = 0;
    private double maxMana = 1000;
    private double currentMana = 0;
    private Force force = Force.BALANCE; // By default, the world is bound to balance
    private Map<Force, Double> attenuation = new HashMap<>();

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
    public double getMysticismXp() {
        return this.mysticismXp;
    }

    @Override
    public void setMysticismXp(double xp) {
        this.mysticismXp = xp;
    }

    @Override
    public double getMaxMana() {
        return this.maxMana;
    }

    @Override
    public void setMaxMana(double amt) {
        this.maxMana = amt;
    }

    @Override
    public double addMaxMana(double amt) {
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
    public double getCurrentMana() {
        this.fixCurrentMana();
        return this.currentMana;
    }

    @Override
    public boolean isRefillable() { // TODO: Overwrite this for some blocks and items which can't be refilled once depleted
        return this.currentMana < this.maxMana;
    }

    @Override
    public void setMana(double amt) {
        this.currentMana = amt;
        this.fixCurrentMana();
    }

    public double addMana(double amount) { // TODO: I guess this also works to remove mana, maybe rename?
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
    public Map<Force, Double> getAttenuations() {
        return this.attenuation;
    }

    @Override
    public void setForceAttenuation(Force force, double amount) {
        this.attenuation.put(force, amount);
    }

    @Override
    public double getForceAttenuation(Force force) {
        return this.attenuation.getOrDefault(force, 0D); // By default things have no attenuation to any particular force
    }
}
