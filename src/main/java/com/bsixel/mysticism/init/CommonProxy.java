package com.bsixel.mysticism.init;

import com.bsixel.mysticism.MysticismMod;
import com.bsixel.mysticism.common.capability.mana.ManaCapability;
import com.bsixel.mysticism.common.commands.CommandRegistrar;
import com.bsixel.mysticism.common.events.PlayerEventHandler;
import com.bsixel.mysticism.common.networking.NetworkManager;
import com.bsixel.mysticism.init.registries.BlockRegistry;
import com.bsixel.mysticism.init.registries.ItemRegistry;
import com.bsixel.mysticism.init.registries.TileEntityRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CommonProxy { // TODO: Maybe break out into separate initializers for blocks, items etc

    private static final Logger logger = LogManager.getLogger(MysticismMod.MOD_ID);

    // TODO: Register new damage types. Reuse existing ones if possible to avoid damage type clutter! Maybe check if other mods with damage types exist and use theirs?

    // TODO: Register new armor types/materials using IArmorMaterial



    // Register common things here - blocks, biomes, items, entities etc
    private static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, MysticismMod.MOD_ID);
    private static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, MysticismMod.MOD_ID);
//    private static final DeferredRegister<> DIMENSIONS = DeferredRegister.create(ForgeRegistries.BLOCKS, MysticismMod.MOD_ID); TODO: Do we want a neat dimension? Probably someday, no rush

    public void init(IEventBus lifecycleBus, IEventBus ingameBus) {
        logger.info("Initializing commonside for Mysticism");

        this.attachStartupLifecycleBus(lifecycleBus); // Startup events
        this.attachNormalEventBus(ingameBus); // In-game events

        ItemRegistry.init(lifecycleBus);
        BlockRegistry.init(lifecycleBus);
        TileEntityRegistry.init(lifecycleBus);
        // TODO: Move what's left to their own
        CONTAINERS.register(lifecycleBus);
        ENTITIES.register(lifecycleBus);

    }

    private void attachStartupLifecycleBus(IEventBus lifecycleBus) {
        // onServerStart/stop watchers etc
        lifecycleBus.addListener(this::registerCommonSetup);
    }

    private void attachNormalEventBus(IEventBus eventBus) {
        // onEntityDeath watchers etc - apparently commands also go here? Seems odd
        eventBus.addListener(this::registerCommands);
        eventBus.addListener(PlayerEventHandler::onPlayerJoin);
        eventBus.addListener(PlayerEventHandler::onPlayerTick);
    }

    private void registerCommonSetup(FMLCommonSetupEvent event) {
        ManaCapability.register();
        NetworkManager.register();
    }

    private void registerCommands(RegisterCommandsEvent event) {
        CommandRegistrar.register(event.getDispatcher());
    }

}
