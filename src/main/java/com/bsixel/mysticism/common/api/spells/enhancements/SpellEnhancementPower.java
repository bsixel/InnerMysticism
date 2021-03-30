package com.bsixel.mysticism.common.api.spells.enhancements;

import com.bsixel.mysticism.MysticismMod;
import com.bsixel.mysticism.common.api.capability.mana.Force;
import com.bsixel.mysticism.common.api.spells.BaseSpellComponent;
import com.bsixel.mysticism.common.api.spells.ISpellComponent;
import com.bsixel.mysticism.common.api.spells.instances.SpellInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.HashMap;
import java.util.Map;

public class SpellEnhancementPower extends BaseSpellComponent implements ISpellEnhancement {

    private static final ResourceLocation location = new ResourceLocation(MysticismMod.MOD_ID, "spellcomponent.power");

    private static final Map<Class<? extends ISpellComponent>, Double> powerMapping = new HashMap<>();

    /*
        TODO: Maybe something like
         Put stuff like
        private static final Map<Class<? extends ISpellComponent>, TriConsumer<Class<ISpellComponent>, ISpellInstance, ItemStack>> stackEffectMap = new HashMap<>();
        stackEffectMap.put(DigAction.class, (Class<ISpellComponent> actionToEnhance, ISpellInstance instance, ItemStack stack) -> {
            stack.setCount(5);
        });
     */

    public void registerForEnhancement(Class<? extends ISpellComponent> componentClass, double modifier) {
        powerMapping.put(componentClass, modifier);
    }

    @Override
    public boolean isActive() {
        return true; // TODO: From config
    }

    @Override
    public TranslationTextComponent getName() {
        return new TranslationTextComponent("spell.component.power.name");
    }

    @Override
    public TranslationTextComponent getDescription() {
        return new TranslationTextComponent("spell.component.power.description");
    }

    @Override
    public ResourceLocation getResourceLocation() {
        return location;
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(Items.DIAMOND);
    }

    @Override
    public double getAttenuationToForce(Force force) {
        return force == Force.BALANCE ? 10 : 0; // TODO
    }

    @Override
    public double getCost() {
        return 10;
    }

    @Override
    public double getSustainedCost() {
        return 5;
    }

    @Override
    public int maxCount(Class<ISpellComponent> actionToEnhance) {
        return 10; // TODO: This will need changing, probably dependent on class
    }

    @Override
    public double modifyAndReturn(Class<? extends ISpellComponent> actionToEnhance, SpellInstance instance, double value) {
        double baseModifierForClass = powerMapping.getOrDefault(actionToEnhance, 1.2);
        // TODO: Look for opposite modifier
        return baseModifierForClass;
    }
}
