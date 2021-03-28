package com.bsixel.mysticism.common.api.capability.spellcasting;

import com.bsixel.mysticism.common.api.spells.Spell;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;

import java.util.ArrayList;
import java.util.List;

public class DefaultSpellcaster implements ISpellcaster {

    private List<Spell> knownSpells = new ArrayList<>();
    private int currentSpellIndex = 0;

    public DefaultSpellcaster() {
        knownSpells.clear();
    }

    @Override
    public CompoundNBT serialize() {
        CompoundNBT nbt = new CompoundNBT();
        ListNBT spellListNbt = new ListNBT();
        for (int i = 0; i < this.getKnownSpells().size(); i++) {
            spellListNbt.addNBTByIndex(i, this.getKnownSpells().get(i).serialize());
        }
        nbt.put("spells", spellListNbt);
        nbt.putInt("current", this.getCurrentSpellIndex());
        return nbt;
    }

    @Override
    public List<Spell> getKnownSpells() {
        return this.knownSpells;
    }

    @Override
    public void setCurrentSpellIndex(int idx) {
        this.currentSpellIndex = idx;
    }

    @Override
    public int incrementSpellslot() {
        if (currentSpellIndex < knownSpells.size() - 1) {
            currentSpellIndex++;
        } else {
            this.currentSpellIndex = 0; // Reset to beginning if we'd overflow
        }
        return currentSpellIndex;
    }

    @Override
    public int decrementSpellslot() {
        if (currentSpellIndex > 0) {
            currentSpellIndex--;
        } else {
            this.currentSpellIndex = this.knownSpells.size() - 1; // Reset to end if we'd overflow
        }
        return currentSpellIndex;
    }

    @Override
    public int changeSpellslot(int amount) {
        for (int i = 0; i < Math.abs(amount); i++) { // Don't @ me I know it's weird but it guarantees we handle the cases the same either way
            if (amount >= 0) {
                this.incrementSpellslot();
            } else {
                this.decrementSpellslot();
            }
        }
        return currentSpellIndex;
    }

    @Override
    public int getCurrentSpellIndex() {
        return currentSpellIndex;
    }

    @Override
    public int addSpell(Spell spell) {
        getKnownSpells().add(spell);
        setCurrentSpellIndex(getKnownSpells().size()-1);
        return getCurrentSpellIndex();
    }

}
