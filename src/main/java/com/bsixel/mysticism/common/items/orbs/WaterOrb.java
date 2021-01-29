package com.bsixel.mysticism.common.items.orbs;

import com.bsixel.mysticism.common.capability.mana.Force;

public class WaterOrb extends ForceOrb {

    public WaterOrb() { //  TODO: Maybe we just have one orb object with multiple states
        super();
        this.force = Force.WATER;
    }

}
