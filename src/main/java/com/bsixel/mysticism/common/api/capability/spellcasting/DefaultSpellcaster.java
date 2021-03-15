package com.bsixel.mysticism.common.api.capability.spellcasting;

import com.bsixel.mysticism.MysticismMod;
import com.bsixel.mysticism.common.api.spells.Spell;
import com.bsixel.mysticism.common.api.spells.SpellComponentInstance;
import com.bsixel.mysticism.common.api.spells.SpellHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class DefaultSpellcaster implements ISpellcaster {

    private List<Spell> knownSpells = new ArrayList<>();
    private int currentSpellIndex = 0;

    public DefaultSpellcaster() {
        knownSpells.clear();
        this.addSpell(buildTestSpell1());
        this.addSpell(buildTestSpell2());
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

    private static Spell buildTestSpell1() { // Bad example, most of the time the resource locations won't be newly created
        Spell spell = new Spell("", new BlockPos(0,67,0), "minecraft:overworld");
        spell.root = new SpellComponentInstance(spell, SpellHelper.getRegisteredComponent(new ResourceLocation(MysticismMod.MOD_ID, "spellcomponent.touch")));
        spell.root.getChildren().add(new SpellComponentInstance(spell, SpellHelper.getRegisteredComponent(new ResourceLocation(MysticismMod.MOD_ID, "spellcomponent.dig"))));
        spell.setName("Basic Dig");
        spell.setIcon(new ItemStack(Items.IRON_PICKAXE));
        return spell;
    }

    private static Spell buildTestSpell2() { // Bad example, most of the time the resource locations won't be newly created
        Spell spell = new Spell("", new BlockPos(0,67,0), "minecraft:overworld");
        spell.root = new SpellComponentInstance(spell, SpellHelper.getRegisteredComponent(new ResourceLocation(MysticismMod.MOD_ID, "spellcomponent.touch")));
        SpellComponentInstance dig = new SpellComponentInstance(spell, SpellHelper.getRegisteredComponent(new ResourceLocation(MysticismMod.MOD_ID, "spellcomponent.dig")));
        spell.root.getChildren().add(dig);
        dig.addChild(new SpellComponentInstance(spell, SpellHelper.getRegisteredComponent(new ResourceLocation(MysticismMod.MOD_ID, "spellcomponent.power"))));
        dig.addChild(new SpellComponentInstance(spell, SpellHelper.getRegisteredComponent(new ResourceLocation(MysticismMod.MOD_ID, "spellcomponent.power"))));
        dig.addChild(new SpellComponentInstance(spell, SpellHelper.getRegisteredComponent(new ResourceLocation(MysticismMod.MOD_ID, "spellcomponent.power"))));
        spell.setName("Bigger Badder Dig");
        spell.setIcon(new ItemStack(Items.DIAMOND_PICKAXE));
        return spell;
    }

}
