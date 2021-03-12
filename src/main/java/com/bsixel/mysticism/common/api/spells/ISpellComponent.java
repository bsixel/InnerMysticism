package com.bsixel.mysticism.common.api.spells;

import com.bsixel.mysticism.common.api.capability.mana.Force;
import com.bsixel.mysticism.common.api.spells.actions.ISpellAction;
import javafx.scene.paint.Color;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.List;

public interface ISpellComponent {

    // Whether the spell component can be used in game; Note that the spell component won't actually be removed, but rather it
    // will not be evaluated in (non-creative) casts and you won't be able to add it to new spells
    boolean isActive();

    TranslationTextComponent getName();
    TranslationTextComponent getDescription();
    ResourceLocation getResourceLocation();
    ItemStack getIcon();
    double getAttenuationToForce(Force force);
    double getCost();
    double getSustainedCost(); // TODO: This should maybe be a mult?
    boolean isChildAllowed(Class<? extends ISpellComponent> childPart);
    void addAllowedChild(Class<? extends ISpellComponent> childPart);

    default Color getPrimaryColor() {
        if (this instanceof ISpellAction) {
            return this.getPrimaryColor();
        }
        return null;
    }

}
