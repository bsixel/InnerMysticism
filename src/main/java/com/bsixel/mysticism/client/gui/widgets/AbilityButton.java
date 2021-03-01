package com.bsixel.mysticism.client.gui.widgets;

import com.bsixel.mysticism.common.api.ability.Ability;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nonnull;

public class AbilityButton extends Button {

    private final Ability ability;
    private final ItemRenderer itemRenderer;

    public AbilityButton(int x, int y, int width, int height, ITextComponent title, IPressable pressedAction, @Nonnull Ability ability, @Nonnull ItemRenderer itemRenderer) {
        super(x, y, width, height, title, pressedAction);
        this.ability = ability;
        this.itemRenderer = itemRenderer;
    }

    public AbilityButton(int x, int y, int width, int height, ITextComponent title, IPressable pressedAction, ITooltip onTooltip, @Nonnull Ability ability, ItemRenderer itemRenderer) {
        super(x, y, width, height, title, pressedAction, onTooltip);
        this.ability = ability;
        this.itemRenderer = itemRenderer;
    }

    @Override
    public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.itemRenderer.renderItemIntoGUI(this.ability.getIcon(), this.x, this.y);
        if (this.isHovered()) {
            this.renderToolTip(matrixStack, mouseX, mouseY);
        }
    }

    public void setPos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Ability getAbility() {
        return ability;
    }
}
