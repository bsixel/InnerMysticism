package com.bsixel.mysticism.common.api.spells;

import com.bsixel.mysticism.MysticismMod;
import com.bsixel.mysticism.common.api.spells.actions.ISpellAction;
import com.bsixel.mysticism.common.api.spells.casttypes.ISpellCastType;
import com.bsixel.mysticism.common.api.spells.enhancements.ISpellEnhancement;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.event.world.BlockEvent;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Collectors;

public class SpellHelper {

    private static final Map<ResourceLocation, ISpellComponent> registeredSpellComponents = new HashMap<>();

    public static int registerSpellComponent(ISpellComponent component) { // TODO: Every component we want to actually appear *anywhere* in game should be registered here
        registeredSpellComponents.put(component.getResourceLocation(), component);
        return registeredSpellComponents.size();
    }

    public static boolean isComponentActive(ResourceLocation component) {
        return registeredSpellComponents.get(component).isActive();
    }

    public static List<ISpellComponent> getActiveComponents() {
        return registeredSpellComponents.values().stream().filter(ISpellComponent::isActive).collect(Collectors.toList());
    }

    public static Collection<ISpellComponent> getAllComponents() {
        return registeredSpellComponents.values();
    }

    /**
     * Returns the registered spell component given a resource location
     * @param componentLocation The resource location of the component you're hoping to find
     * @return The registered ISpellComponent of the desired type, or null if no match is found for the given resource location
     */
    public static ISpellComponent getRegisteredComponent(@Nonnull ResourceLocation componentLocation) {
        return registeredSpellComponents.getOrDefault(componentLocation, null);
    }

    public static ISpellComponent getRegisteredComponent(@Nonnull String componentLocation) {
        return registeredSpellComponents.getOrDefault(new ResourceLocation(componentLocation), null);
    }

    public static List<ISpellCastType> getCastTypes() {
        return registeredSpellComponents.values().stream().filter(component -> component instanceof ISpellCastType).map(component -> (ISpellCastType) component).collect(Collectors.toList());
    }

    public static List<ISpellAction> getActions() {
        return registeredSpellComponents.values().stream().filter(component -> component instanceof ISpellAction).map(component -> (ISpellAction) component).collect(Collectors.toList());
    }

    public static List<ISpellEnhancement> getEnhancements() {
        return registeredSpellComponents.values().stream().filter(component -> component instanceof ISpellEnhancement).map(component -> (ISpellEnhancement) component).collect(Collectors.toList());
    }

    public static boolean canEntityBreakPos(World world, LivingEntity breaker, BlockPos pos) { // Check if it's spawn protected or otherwise... unbreakable or whatever. Or we're "clientside". Breaking is done serverside
        // Neat, forge has built in way. Presumably this works better than vanilla new FakePlayer
        if (!(world instanceof ServerWorld)) {
            return false;
        } else return !MinecraftForge.EVENT_BUS.post(new BlockEvent.BreakEvent(world, pos, world.getBlockState(pos), (breaker instanceof PlayerEntity) ? (PlayerEntity) breaker : FakePlayerFactory.get((ServerWorld) world, new GameProfile(MysticismMod.FAKE_PLAYER_UUID, "Mysticism"))));
    }

}
