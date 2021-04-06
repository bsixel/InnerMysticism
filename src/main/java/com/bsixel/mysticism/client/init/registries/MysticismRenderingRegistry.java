package com.bsixel.mysticism.client.init.registries;

import com.bsixel.mysticism.MysticismMod;
import com.bsixel.mysticism.client.rendering.entities.RenderSpellProjectileEntity;
import com.bsixel.mysticism.common.init.registries.EntityRegistry;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MysticismRenderingRegistry {

    private static final Logger logger = LogManager.getLogger(MysticismMod.MOD_ID);

    public static void init(final FMLClientSetupEvent event){ // Keep that param in case we need it
        logger.info("Registering rendering...");

        initEntityRendering(event);
    }

    private static void initEntityRendering(final FMLClientSetupEvent event) {
        logger.info("Registering entity rendering...");

        ItemRenderer renderer = event.getMinecraftSupplier().get().getItemRenderer();
        RenderingRegistry.registerEntityRenderingHandler(EntityRegistry.SPELL_PROJECTILE.get(), RenderSpellProjectileEntity::new);
    }

    private static void initParticleRendering(final FMLClientSetupEvent event) {
        logger.info("Registering particle rendering...");

    }

}
