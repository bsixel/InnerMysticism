package com.bsixel.mysticism.common.capability.mana;

public enum Force {
    AIR("Air"),
    EARTH("Earth"),
    FIRE("Fire"),
    WATER("Water"),
    LIFE("Life"),
    DEATH("Death"),
    BALANCE("Balance");

    static {
        AIR.antithesis = EARTH;
        EARTH.antithesis = AIR;
        FIRE.antithesis = WATER;
        WATER.antithesis = FIRE;
        LIFE.antithesis = DEATH;
        BALANCE.antithesis = null;
    }

    private String name;
    private Force antithesis;

    Force(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Force getAntithesis() {
        return antithesis;
    }
}
