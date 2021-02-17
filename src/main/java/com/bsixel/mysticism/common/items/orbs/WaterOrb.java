package com.bsixel.mysticism.common.items.orbs;

import com.bsixel.mysticism.common.api.capability.mana.Force;

public class WaterOrb extends ForceOrb {

    // TODO:
    public WaterOrb() { //  TODO: Maybe we just have one orb object with multiple states? Regardless, orb should slightly attune the user more to that Force
        super();
        this.force = Force.WATER;
    }

}
