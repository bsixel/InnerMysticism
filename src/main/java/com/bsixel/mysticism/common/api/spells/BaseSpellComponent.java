package com.bsixel.mysticism.common.api.spells;

import com.bsixel.mysticism.common.api.spells.ISpellComponent;
import com.bsixel.mysticism.common.api.spells.Spell;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTier;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.crafting.Ingredient;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseSpellComponent implements ISpellComponent {

    protected static final List<Class<? extends ISpellComponent>> allowedChildren = new ArrayList<>();

    protected Spell parentSpell;
    protected ISpellComponent parentComponent;
    protected final List<ISpellComponent> children = new ArrayList<>();

    /**
     * This should only be used for registering the component!
     */
    public BaseSpellComponent() {}

    public BaseSpellComponent(Spell parentSpell, @Nullable ISpellComponent parentComponent) {
        this.parentSpell = parentSpell;
        this.parentComponent = parentComponent;
    }

    @Override
    public List<ISpellComponent> getChildren() {
        return this.children;
    }

    @Override
    public Spell getParentSpell() {
        return this.parentSpell;
    }

    @Override
    public ISpellComponent getParentSpellComponent() {
        return this.parentComponent;
    }

    @Override
    public boolean isChildAllowed(Class<? extends ISpellComponent> childPart) {
        return allowedChildren.contains(childPart); // TODO: We might want an easy way of adding "all" or groups or something
    }

    @Override
    public void addAllowedChild(Class<? extends ISpellComponent> childPart) {
        allowedChildren.add(childPart);
    }

}
