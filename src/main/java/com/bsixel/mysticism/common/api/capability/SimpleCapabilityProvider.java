package com.bsixel.mysticism.common.api.capability;

import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

// See https://github.com/Choonster-Minecraft-Mods/TestMod3/blob/1.16.x/src/main/java/choonster/testmod3/capability/SimpleCapabilityProvider.java for basis
public class SimpleCapabilityProvider<HANDLER> implements ICapabilityProvider { // No longer SerializeableCapability or whatever, serializing will be handled by capability's Storage. This allows us to have only one generic provider

    private final HANDLER handlerInstance;
    private final Capability<HANDLER> cap;
    private final Direction facing;

    private final LazyOptional<HANDLER> handlerInstanceOptional;

    public SimpleCapabilityProvider(@Nonnull final Capability<HANDLER> cap, @Nullable final Direction facing, @Nullable final HANDLER handlerInstance) {
        this.cap = cap;
        this.handlerInstance = handlerInstance;
        this.facing = facing;
        this.handlerInstanceOptional = this.handlerInstance == null ? LazyOptional.empty() : LazyOptional.of(() -> this.handlerInstance);
    }

    public final Capability<HANDLER> getCapability() {
        return this.cap;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap) {
        return getCapability().orEmpty(cap, this.handlerInstanceOptional);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return getCapability(cap);
    }

    protected HANDLER getHandlerInstance() {
        return this.handlerInstance;
    }

    protected Direction getFacing() {
        return this.facing;
    }

}
