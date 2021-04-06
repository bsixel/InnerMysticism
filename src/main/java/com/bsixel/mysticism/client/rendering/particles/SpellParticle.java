package com.bsixel.mysticism.client.rendering.particles;

import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.BasicParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class SpellParticle extends SpriteTexturedParticle {

    protected SpellParticle(ClientWorld world, double x, double y, double z) {
        super(world, x, y, z);
    }

    protected SpellParticle(ClientWorld world, double x, double y, double z, double motionX, double motionY, double motionZ) {
        super(world, x, y, z, motionX, motionY, motionZ);

    }

    private void init() {
        this.particleRed = 0.73f;
        this.particleGreen = 0.58f;
        this.particleBlue = 0.76f;
        this.particleAlpha = 0.73f;

        this.setSize(0.02f, 0.02f);
        this.particleScale *= this.rand.nextFloat() * 1.035f;
        this.motionX *= 0.015 * this.rand.nextDouble();
        this.motionY *= 0.015 * this.rand.nextDouble();
        this.motionZ *= 0.015 * this.rand.nextDouble();
        this.maxAge = (int) (20 / this.rand.nextDouble());

    }

    @Override
    @Nonnull
    public IParticleRenderType getRenderType() {
        return IParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public void tick() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        if (this.maxAge-- <= 0) {
            this.setExpired();
        } else {
            this.move(this.motionX, this.motionY, this.motionZ);
            this.motionX *= this.rand.nextDouble();
            this.motionY *= this.rand.nextDouble();
            this.motionZ *= this.rand.nextDouble();
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class Factory implements IParticleFactory<BasicParticleType> {
        private final IAnimatedSprite spriteSet;

        public Factory(IAnimatedSprite spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Nullable
        @Override
        public Particle makeParticle(@Nonnull BasicParticleType typeIn, @Nonnull ClientWorld worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            SpellParticle particle = new SpellParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed);
            particle.setColor(1, 1, 1);
            particle.selectSpriteRandomly(this.spriteSet);
            return particle;
        }
    }

}
