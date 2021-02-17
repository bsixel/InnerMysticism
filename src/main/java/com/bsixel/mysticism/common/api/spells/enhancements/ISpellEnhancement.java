package com.bsixel.mysticism.common.api.spells.enhancements;

import com.bsixel.mysticism.common.api.spells.ISpellComponent;
import com.bsixel.mysticism.common.api.spells.instances.ISpellInstance;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;

public interface ISpellEnhancement extends ISpellComponent { // Radius, duration, strength, etc

    /**
     * How many of this enhancement can be on a particular component? Default to 1 for safety.
     * @param actionToEnhance The class of component this is enhancing, in case the max count is dependent. Can be null if the class doesn't matter
      */
    default int maxCount(Class<ISpellComponent> actionToEnhance) {
        return 1;
    }

    default void modify(Class<ISpellComponent> actionToEnhance, ISpellInstance instance) {} // Honestly not sure what you'd ever use this one for, but just in case we need it

    default ItemStack modifyAndReturn(Class<? extends ISpellComponent> actionToEnhance, ISpellInstance instance, ItemStack stack) {
        return stack;
    }

    default double modifyAndReturn(Class<? extends ISpellComponent> actionToEnhance, ISpellInstance instance, double value) {
        return value;
    }

    default Entity modifyAndReturn(Class<? extends ISpellComponent> actionToEnhance, ISpellInstance instance, Entity target) {
        return target;
    }

}
