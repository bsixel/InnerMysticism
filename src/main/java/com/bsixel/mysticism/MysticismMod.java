package com.bsixel.mysticism;

import com.bsixel.mysticism.init.ClientProxy;
import com.bsixel.mysticism.init.CommonProxy;
import com.bsixel.mysticism.init.MysticismItemGroup;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.bernie.geckolib3.GeckoLib;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(MysticismMod.MOD_ID)
public class MysticismMod {
    public static final String MOD_ID = "mysticism";
    public static final ResourceLocation NET_CHANNEL = new ResourceLocation(MOD_ID + ":net_channel");
    public static final String ver = "1.0";
    public static final MysticismItemGroup item_group = new MysticismItemGroup();

    private static final Logger logger = LogManager.getLogger(MOD_ID);

    private static MysticismMod instance;
    private final CommonProxy sideSafeProxy;

    public MysticismMod() {
        GeckoLib.initialize(); // TODO: If GeckoLib ever isn't updated in time, make sure to get rid of this
        // Make sure we always distinguish between sided-safe initialization. Ex: Don't register GUIs on server, but register capabilities on both sides
        this.sideSafeProxy = DistExecutor.unsafeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);

        this.sideSafeProxy.init(FMLJavaModLoadingContext.get().getModEventBus(), MinecraftForge.EVENT_BUS);

        instance = this; // TODO: This should happen only once, make sure that's true
    }

    public CommonProxy getSideSafeProxy() {
        return this.sideSafeProxy;
    }

    public static MysticismMod getInstance() {
        return instance;
    }

}
