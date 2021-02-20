package com.bsixel.mysticism.client.gui.screens;

import com.bsixel.mysticism.client.gui.AbstractMystGui;
import com.bsixel.mysticism.common.api.capability.mana.Force;
import com.google.common.collect.Maps;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.advancements.Advancement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.advancements.AdvancementEntryGui;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.Nullable;
import java.util.Map;

public class ForceAbilityTabGui extends AbstractMystGui {
    private final Minecraft minecraft;
    private final ForceAbilityScreen screen;
    private final int textureX = 0;
    private final int textureY = 0;
    private final int width = 28;
    private final int height = 32;
    private final int max = 8;
    private final int index;
    private final Force force;
    private final ItemStack icon;
    private final ITextComponent title;
    private final Map<Advancement, AdvancementEntryGui> guis = Maps.newLinkedHashMap();
    private double scrollX;
    private double scrollY;
    private int minX = Integer.MAX_VALUE;
    private int minY = Integer.MAX_VALUE;
    private int maxX = Integer.MIN_VALUE;
    private int maxY = Integer.MIN_VALUE;
    private float fade;
    private boolean centered;
    private int page;

    public ForceAbilityTabGui(Minecraft minecraft, ForceAbilityScreen parentScreen, int index, Force force) {
        this.minecraft = minecraft;
        this.screen = parentScreen;
        this.index = index;
        this.force = force;
        this.icon = new ItemStack(Items.IRON_PICKAXE); // TODO: Figure out how to icon dynamically; Do we want to load it from a file? Just case it?
        this.title = new StringTextComponent(force.getName());
    }

    @Nullable
    public static ForceAbilityTabGui create(Minecraft minecraft, ForceAbilityScreen screen, int tabIndex, Force force) {
        return new ForceAbilityTabGui(minecraft, screen, tabIndex, force);
    }

    /*public int getMax() {
        return this.max;
    }

    public void renderTabSelectorBackground(MatrixStack matrixStack, AbstractGui abstractGui, int offsetX, int offsetY, boolean isSelected, int index) {
        int i = this.textureX;
        if (index > 0) {
            i += this.width;
        }

        if (index == this.max - 1) {
            i += this.width;
        }

        int j = isSelected ? this.textureY + this.height : this.textureY;
        abstractGui.blit(matrixStack, offsetX + this.getX(index), offsetY + this.getY(index), i, j, this.width, this.height);
    }

    public void drawIcon(int offsetX, int offsetY, int index, ItemRenderer renderItemIn, ItemStack stack) {
        renderItemIn.renderItemAndEffectIntoGuiWithoutEntity(stack, offsetX + this.getX(index) + 6, offsetY + this.getY(index) + 9);
    }

    public int getX(int index) {
        return (this.width + 4) * index;
    }

    public int getY(int index) {
        return -this.height + 4;
    }

    public boolean inInsideTabSelector(int offsetX, int offsetY, int index, double mouseX, double mouseY) {
        int i = offsetX + this.getX(index);
        int j = offsetY + this.getY(index);
        return mouseX > (double)i && mouseX < (double)(i + this.width) && mouseY > (double)j && mouseY < (double)(j + this.height);
    }

    public void drawTabBackground(MatrixStack matrixStack) {
        if (!this.centered) {
            this.scrollX = (double)(117 - (this.maxX + this.minX) / 2);
            this.scrollY = (double)(56 - (this.maxY + this.minY) / 2);
            this.centered = true;
        }

        RenderSystem.pushMatrix();
        RenderSystem.enableDepthTest();
        RenderSystem.translatef(0.0F, 0.0F, 950.0F);
        RenderSystem.colorMask(false, false, false, false);
        fill(matrixStack, 4680, 2260, -4680, -2260, -16777216);
        RenderSystem.colorMask(true, true, true, true);
        RenderSystem.translatef(0.0F, 0.0F, -950.0F);
        RenderSystem.depthFunc(518);
        fill(matrixStack, 234, 113, 0, 0, -16777216);
        RenderSystem.depthFunc(515);
        ResourceLocation resourcelocation = this.display.getBackground();
        if (resourcelocation != null) {
            this.minecraft.getTextureManager().bindTexture(resourcelocation);
        } else {
            this.minecraft.getTextureManager().bindTexture(TextureManager.RESOURCE_LOCATION_EMPTY);
        }

        int i = MathHelper.floor(this.scrollX);
        int j = MathHelper.floor(this.scrollY);
        int k = i % 16;
        int l = j % 16;

        for(int i1 = -1; i1 <= 15; ++i1) {
            for(int j1 = -1; j1 <= 8; ++j1) {
                blit(matrixStack, k + 16 * i1, l + 16 * j1, 0.0F, 0.0F, 16, 16, 16, 16);
            }
        }

        this.root.drawConnectionLineToParent(matrixStack, i, j, true);
        this.root.drawConnectionLineToParent(matrixStack, i, j, false);
        this.root.drawAdvancement(matrixStack, i, j);
        RenderSystem.depthFunc(518);
        RenderSystem.translatef(0.0F, 0.0F, -950.0F);
        RenderSystem.colorMask(false, false, false, false);
        fill(matrixStack, 4680, 2260, -4680, -2260, -16777216);
        RenderSystem.colorMask(true, true, true, true);
        RenderSystem.translatef(0.0F, 0.0F, 950.0F);
        RenderSystem.depthFunc(515);
        RenderSystem.popMatrix();
    }

    public void drawTabTooltips(MatrixStack matrixStack, int mouseX, int mouseY, int width, int height) {
        RenderSystem.pushMatrix();
        RenderSystem.translatef(0.0F, 0.0F, 200.0F);
        fill(matrixStack, 0, 0, 234, 113, MathHelper.floor(this.fade * 255.0F) << 24);
        boolean flag = false;
        int i = MathHelper.floor(this.scrollX);
        int j = MathHelper.floor(this.scrollY);
        if (mouseX > 0 && mouseX < 234 && mouseY > 0 && mouseY < 113) {
            for(AdvancementEntryGui advancemententrygui : this.guis.values()) {
                if (advancemententrygui.isMouseOver(i, j, mouseX, mouseY)) {
                    flag = true;
                    advancemententrygui.drawAdvancementHover(matrixStack, i, j, this.fade, width, height);
                    break;
                }
            }
        }

        RenderSystem.popMatrix();
        if (flag) {
            this.fade = MathHelper.clamp(this.fade + 0.02F, 0.0F, 0.3F);
        } else {
            this.fade = MathHelper.clamp(this.fade - 0.04F, 0.0F, 1.0F);
        }

    }*/

}
