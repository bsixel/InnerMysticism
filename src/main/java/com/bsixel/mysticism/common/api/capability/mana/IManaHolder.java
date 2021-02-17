package com.bsixel.mysticism.common.api.capability.mana;

import net.minecraft.entity.LivingEntity;

import java.util.Map;

public interface IManaHolder {
    default double getMaxMana() { return 0; };
    default double getCurrentMana() { return 0; };
    boolean isRefillable();
    double addMana(double amt);
    void setMana(double amt);
    double addMaxMana(double amt);
    void setMaxMana(double amt);
    Force getPrimaryForce();
    void setPrimaryForce(Force force);
    void initializeAttenuations();
    Map<Force, Double> getAttenuations();
    double getForceAttenuation(Force force);
    void setForceAttenuation(Force force, double amt);
    int getMysticismLevel();
    void setMysticismLevel(int level);
    double getMysticismXp();
    void setMysticismXp(double xp);

    LivingEntity getAttachedEntity();
}
