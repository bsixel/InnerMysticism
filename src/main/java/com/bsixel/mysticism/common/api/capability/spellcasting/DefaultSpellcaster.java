package com.bsixel.mysticism.common.api.capability.spellcasting;

import com.bsixel.mysticism.common.api.spells.Spell;

import java.util.ArrayList;
import java.util.List;

public class DefaultSpellcaster implements ISpellcaster {

    private List<Spell> knownSpells = new ArrayList<>();
    private int currentSpellIndex = 0;

    @Override
    public List<Spell> getKnownSpells() {
        return this.knownSpells;
    }

    @Override
    public void setCurrentSpellIndex(int idx) {
        this.currentSpellIndex = idx;
    }

    @Override
    public int getCurrentSpellIndex() {
        return currentSpellIndex;
    }
}
