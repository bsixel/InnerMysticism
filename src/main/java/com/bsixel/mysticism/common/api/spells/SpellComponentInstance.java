package com.bsixel.mysticism.common.api.spells;

import com.bsixel.mysticism.common.api.spells.actions.ISpellAction;
import com.bsixel.mysticism.common.api.spells.enhancements.ISpellEnhancement;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SpellComponentInstance {

    private ISpellComponent component;
    private List<SpellComponentInstance> children = new ArrayList<>();
    private Spell parentSpell;

    public SpellComponentInstance(Spell parentSpell, ISpellComponent component) {
        this.parentSpell = parentSpell;
        this.component = component;
    }

    public SpellComponentInstance(Spell parentSpell, ISpellComponent component, List<SpellComponentInstance> children) {
        this(parentSpell, component);
        this.children = children;
    }

    public CompoundNBT serialize() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putString("component", this.component.getResourceLocation().toString());
        ListNBT childrenNBT = new ListNBT();
        for (int i = 0; i < this.children.size(); i++) {
            childrenNBT.addNBTByIndex(i, this.children.get(i).serialize());
        }
        nbt.put("children", childrenNBT);
        return nbt;
    }

    public static SpellComponentInstance deserialize(Spell parentSpell, CompoundNBT nbt) {
        SpellComponentInstance instance = new SpellComponentInstance(parentSpell, SpellHelper.getRegisteredComponent(new ResourceLocation(nbt.getString("component"))));
        nbt.getList("children", Constants.NBT.TAG_LIST).forEach(childNbt -> instance.addChild(deserialize(parentSpell, (CompoundNBT) childNbt)));
        return instance;
    }

    public ISpellComponent getComponent() {
        return this.component;
    }

    public List<SpellComponentInstance> getChildren() {
        return this.children;
    }

    public List<ISpellComponent> getChildrenComponents() {
        return this.children.stream().map(SpellComponentInstance::getComponent).collect(Collectors.toList());
    }

    public List<ISpellAction> getChildActionComponents() {
        return this.children.stream().filter(child -> child.component instanceof ISpellAction).map(child -> (ISpellAction)child.getComponent()).collect(Collectors.toList());
    }

    public List<ISpellEnhancement> getChildEnhancementComponents() {
        return this.children.stream().filter(child -> child.component instanceof ISpellEnhancement).map(child -> (ISpellEnhancement)child.getComponent()).collect(Collectors.toList());
    }

    public void addChild(SpellComponentInstance child) {
        this.children.add(child);
    }

    public void removeChild(SpellComponentInstance child) {
        this.children.remove(child);
    }

    public Spell getParentSpell() {
        return this.parentSpell;
    }

    public double getCost() {
        return this.component.getCost() + this.children.stream().mapToDouble(SpellComponentInstance::getCost).sum();
    }

}
