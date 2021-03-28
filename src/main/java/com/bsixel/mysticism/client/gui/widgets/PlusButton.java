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

public class PlusButton extends Button { // TODO: Seriously having all these tiny button classes is silly

    private static final Logger logger = LogManager.getLogger(MysticismMod.MOD_ID);

    public PlusButton(int x, int y, IPressable pressedAction, ITooltip tooltip) {
        super(x, y, 24, 24, new StringTextComponent(""), pressedAction, tooltip);
    }

    @Override
    public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        if (this.visible) {
            Minecraft.getInstance().getTextureManager().bindTexture(GuiHelper.gui_pieces);
            blit(matrixStack, this.x, this.y, 1, 1, 24, 24);
        }
    }

}
