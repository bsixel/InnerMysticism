package com.bsixel.mysticism.common.api.capability.spellcasting;

import com.bsixel.mysticism.common.api.spells.Spell;

import java.util.List;

public interface ISpellcaster {

    List<Spell> getKnownSpells();
    void setCurrentSpellIndex(int idx);
    int getCurrentSpellIndex();

    default Spell getCurrentSpell() {
        return getKnownSpells().get(getCurrentSpellIndex());
    }

    default int addSpell(Spell spell) {
        getKnownSpells().add(spell);
        setCurrentSpellIndex(getKnownSpells().size()-1);
        return getCurrentSpellIndex();
    }

}
