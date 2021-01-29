package com.bsixel.mysticism.common.capability.mana;

import net.minecraft.entity.LivingEntity;

import java.util.Map;

public interface IManaHolder {
    default float getMaxMana() { return 0f; };
    default float getCurrentMana() { return 0f; };
    boolean isRefillable();
    float addMana(float amt);
    void setMana(float amt);
    float addMaxMana(float amt);
    void setMaxMana(float amt);
    Force getPrimaryForce();
    void setPrimaryForce(Force force);
    void initializeAttenuations();
    Map<Force, Float> getAttenuations();
    float getForceAttenuation(Force force);
    void setForceAttenuation(Force force, float amt);
    int getMysticismLevel();
    void setMysticismLevel(int level);
    float getMysticismXp();
    void setMysticismXp(float xp);

    LivingEntity getAttachedEntity();
}
