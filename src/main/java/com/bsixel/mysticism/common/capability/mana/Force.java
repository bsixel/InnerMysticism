package com.bsixel.mysticism.common.capability.mana;

public enum Force { // TODO: TOSSING EVENTUAL IDEAS PER FORCE IN HERE UNTIL I HAVE A BETTER PLACE
    /*
        Maybe we add elementals? Like blazes but everything.
     */
    AIR("Air"),
    EARTH("Earth"), //
    FIRE("Fire"), // Summon blazes
    WATER("Water"), // Summon... Guardians?? Can drown effect entities
    LIFE("Life"), // Summon Killer Bunny (bunnyType 99) Naturey stuff, can defuse creepers, maybe grow crops/animals, healing
    DEATH("Death"), // Summon skeletons
    BALANCE("Balance"); // Tough one, not really sure what to put in here...

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
