package com.bsixel.mysticism.init;

import com.bsixel.mysticism.MysticismMod;
import com.bsixel.mysticism.client.gui.OverlayHandler;
import com.bsixel.mysticism.client.keybindings.KeyHandler;
import com.bsixel.mysticism.client.keybindings.Keybindings;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = MysticismMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD) TODO: Probably replaced by higher registration?
public class ClientProxy extends CommonProxy { // TODO: Register textures etc

    private static final Logger logger = LogManager.getLogger(MysticismMod.MOD_ID);

    @Override
    public void init(IEventBus lifecycleBus, IEventBus ingameBus) {
        logger.info("Initializing clientside for Mysticism");
        super.init(lifecycleBus, ingameBus); // Register shared-side things
        // Register clientside-only things: textures, rendering, GUIs etc
        this.attachStartupLifecycleBus(lifecycleBus);
        this.attachNormalEventBus(ingameBus);
    }

    private void attachStartupLifecycleBus(IEventBus lifecycleBus) {
        // onServerStart/stop watchers etc
        lifecycleBus.addListener(this::registerKeybindings);
    }

    private void attachNormalEventBus(IEventBus eventBus) {
        // onEntityDeath watchers etc - apparently gui rendering also go here? Seems odd
        eventBus.addListener(EventPriority.LOW, OverlayHandler.instance::onRenderGUI);
        eventBus.addListener(KeyHandler::handleKeyEvent);
    }

    private void registerKeybindings(final FMLClientSetupEvent event) {
        Keybindings.register();
    }

}
