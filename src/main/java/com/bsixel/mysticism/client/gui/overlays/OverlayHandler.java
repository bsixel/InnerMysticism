package com.bsixel.mysticism.client.gui.overlays;

import com.bsixel.mysticism.MysticismMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@OnlyIn(Dist.CLIENT)
public class OverlayHandler {

    private static final Logger logger = LogManager.getLogger(MysticismMod.MOD_ID);
    private static final Minecraft minecraft = Minecraft.getInstance();

    public static OverlayHandler instance = new OverlayHandler();

    private static ManaOverlay mana_overlay = new ManaOverlay();

    // NOTE: Player's mana will update on backend server, then be sent to us clientside to do with here
    public void onRenderGUI(RenderGameOverlayEvent event) { // Trying to stay under the f3 menu
        if (event.getType() != RenderGameOverlayEvent.ElementType.EXPERIENCE) { // Render at the same time as XP to not get in the way of other things
            return;
        }
        ClientPlayerEntity player = minecraft.player;
        if (player != null) {
            mana_overlay.renderManaHud(minecraft, player, event.getMatrixStack());
        }
    }

}
