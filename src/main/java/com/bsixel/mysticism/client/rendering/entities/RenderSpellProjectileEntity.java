package com.bsixel.mysticism.client.rendering.entities;

import com.bsixel.mysticism.MysticismMod;
import com.bsixel.mysticism.common.entities.projectiles.SpellProjectileEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;

import javax.annotation.Nonnull;

public class RenderSpellProjectileEntity extends GeoProjectilesRenderer<SpellProjectileEntity> {

//    private static final ResourceLocation tex = new ResourceLocation(MysticismMod.MOD_ID, "textures/entity/spell_projectile.png"); // TODO: Honestly, not sure we want a static texture. Is there a way to have some sorta math based rendering thing?
    private static final ResourceLocation tex = new ResourceLocation(MysticismMod.MOD_ID, "textures/blocks/water-rune.png"); // For now it just looks like an arrow idk

    public RenderSpellProjectileEntity(EntityRendererManager renderManager) {
        super(renderManager);
    }

    @Override
    public void render(@Nonnull SpellProjectileEntity entityIn, float entityYaw, float partialTicks, @Nonnull MatrixStack matrixStackIn, @Nonnull IRenderTypeBuffer bufferIn, int packedLightIn) {
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);

//        net.minecraft.particles.ItemParticleData // TODO: Use this for some interesting particle rendering
    }

    @Nonnull
    @Override
    public ResourceLocation getEntityTexture(@Nonnull SpellProjectileEntity spellProjectileEntity) {
        return tex;
    }
}
