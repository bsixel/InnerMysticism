package com.bsixel.mysticism.client.gui.screens;

import com.bsixel.mysticism.common.api.capability.mana.Force;
import com.google.common.collect.Maps;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class ForceAbilityScreen extends ScreenBase {

    private static final ResourceLocation WINDOW = new ResourceLocation("textures/gui/advancements/window.png");
    private static final ResourceLocation TABS = new ResourceLocation("textures/gui/advancements/tabs.png");

    private final Map<Force, ForceAbilityTabGui> tabs = Maps.newLinkedHashMap();
    private ForceAbilityTabGui currentTab;

    private boolean isScrolling;

    protected ForceAbilityScreen(ITextComponent title) {
        super(title);
    }

    public void rootAdvancementAdded(Force force) {
        ForceAbilityTabGui forceAbilityTabGui = ForceAbilityTabGui.create(this, this.tabs.size(), force);
        this.tabs.put(force, forceAbilityTabGui);
    }

}
