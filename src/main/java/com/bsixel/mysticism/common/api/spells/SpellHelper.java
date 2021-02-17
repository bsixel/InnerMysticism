package com.bsixel.mysticism.common.api.spells;

import com.bsixel.mysticism.MysticismMod;
import com.bsixel.mysticism.common.api.spells.instances.ISpellInstance;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.event.world.BlockEvent;

import java.util.*;
import java.util.stream.Collectors;

public class SpellHelper {

    private static final Map<Class<? extends ISpellComponent>, ISpellComponent> registeredSpellComponents = new HashMap<>();

    public static int registerSpellComponent(ISpellComponent component) { // TODO: Every component we want to actually appear *anywhere* in game should be registered here
        registeredSpellComponents.put(component.getClass(), component);
        return registeredSpellComponents.size();
    }

    public static boolean isComponentActive(Class<? extends ISpellComponent> component) {
        return registeredSpellComponents.get(component).isActive();
    }

    public static List<Class<? extends ISpellComponent>> getActiveComponents() {
        return registeredSpellComponents.keySet().stream().filter(SpellHelper::isComponentActive).collect(Collectors.toList());
    }

    public static Set<Class<? extends ISpellComponent>> getAllComponents() {
        return registeredSpellComponents.keySet();
    }

    public static boolean canEntityBreakPos(World world, LivingEntity breaker, BlockPos pos) { // Check if it's spawn protected or otherwise... unbreakable or whatever. Or we're "clientside". Breaking is done serverside
        // Neat, forge has built in way. Presumably this works better than vanilla new FakePlayer
        if (!(world instanceof ServerWorld)) {
            return false;
        } else return !MinecraftForge.EVENT_BUS.post(new BlockEvent.BreakEvent(world, pos, world.getBlockState(pos), (breaker instanceof PlayerEntity) ? (PlayerEntity) breaker : FakePlayerFactory.get((ServerWorld) world, new GameProfile(MysticismMod.FAKE_PLAYER_UUID, "Mysticism"))));
    }

}
