package com.bsixel.mysticism.client.gui.widgets;

import com.bsixel.mysticism.MysticismMod;
import com.bsixel.mysticism.client.gui.GuiHelper;
import com.bsixel.mysticism.client.gui.screens.ForceAbilityTabGui;
import com.bsixel.mysticism.client.gui.screens.SpellcraftingGui;
import com.bsixel.mysticism.common.api.ability.Ability;
import com.bsixel.mysticism.common.api.capability.mana.Force;
import com.bsixel.mysticism.common.api.math.tree.IPositionable;
import com.bsixel.mysticism.common.api.spells.Spell;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class SpellButton extends Button {

    private static final Logger logger = LogManager.getLogger(MysticismMod.MOD_ID);

    private final SpellcraftingGui parentScreen;
    private final ItemRenderer itemRenderer;
    private final int spellSlot;

    public SpellButton(int x, int y, int width, int height, SpellcraftingGui parentScreen, ItemRenderer itemRenderer, int spellSlot, IPressable pressedAction, ITooltip tooltip) {
        super(x, y, width, height, new StringTextComponent(""), pressedAction, tooltip); // We never use the "title" anyways
        this.parentScreen = parentScreen;
        this.itemRenderer = itemRenderer;
        this.spellSlot = spellSlot;
    }

    @Override
    public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        if (this.visible && this.spellSlot < this.parentScreen.getNumSpells()) {
            this.itemRenderer.renderItemIntoGUI(this.getSpell().getIcon(), this.x + 4, this.y + 4);
            if (this.isHovered()) {
                this.renderToolTip(matrixStack, mouseX, mouseY);
            }
        }
    }

    public Spell getSpell() {
        return this.parentScreen.getSpellInSlot(this.spellSlot);
    }

    public int getSpellSlot() {
        return this.spellSlot;
    }

}
