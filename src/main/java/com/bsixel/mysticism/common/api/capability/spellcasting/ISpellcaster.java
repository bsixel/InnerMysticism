package com.bsixel.mysticism.common.api.capability.spellcasting;

import com.bsixel.mysticism.common.api.spells.Spell;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;

import java.util.List;

public interface ISpellcaster {

    CompoundNBT serialize();

    List<Spell> getKnownSpells();
    void setCurrentSpellIndex(int idx);
    int incrementSpellslot();
    int decrementSpellslot();
    int getCurrentSpellIndex();

    default Spell getCurrentSpell() {
        return getKnownSpells().get(getCurrentSpellIndex());
    }

    int addSpell(Spell spell);

}
