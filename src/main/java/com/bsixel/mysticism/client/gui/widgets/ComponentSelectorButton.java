package com.bsixel.mysticism.client.gui.widgets;

import com.bsixel.mysticism.MysticismMod;
import com.bsixel.mysticism.client.gui.GuiHelper;
import com.bsixel.mysticism.client.gui.screens.SpellcraftingGui;
import com.bsixel.mysticism.common.api.spells.ISpellComponent;
import com.bsixel.mysticism.common.api.spells.SpellHelper;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.util.text.StringTextComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ComponentSelectorButton extends Button { // TODO: These three buttons should probably all extend something else, they're so similar

    private static final Logger logger = LogManager.getLogger(MysticismMod.MOD_ID);

    private final SpellcraftingGui parentScreen;
    private final ItemRenderer itemRenderer;
    private final int componentSlot;

    public ComponentSelectorButton(int x, int y, int width, int height, SpellcraftingGui parentScreen, ItemRenderer itemRenderer, int componentSlot, IPressable pressedAction, ITooltip tooltip) {
        super(x, y, width, height, new StringTextComponent(""), pressedAction, tooltip); // We never use the "title" anyways
        this.parentScreen = parentScreen;
        this.itemRenderer = itemRenderer;
        this.componentSlot = componentSlot;
    }

    @Override
    public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        if (this.visible && this.componentSlot < this.parentScreen.getComponentCount()) {
            Minecraft.getInstance().getTextureManager().bindTexture(GuiHelper.gui_pieces);
            blit(matrixStack, this.x, this.y, 69, 77, 24, 24);
            this.itemRenderer.renderItemIntoGUI(this.getComponent().getIcon(), this.x + 3, this.y + 4);
        }
    }

    public ISpellComponent getComponent() { // TODO: Switch to only the user's known components
        if (this.componentSlot < this.parentScreen.getComponentCount()) {
            return this.parentScreen.getComponentForIndex(this.componentSlot);
        } else {
            return null;
        }
    }

}
