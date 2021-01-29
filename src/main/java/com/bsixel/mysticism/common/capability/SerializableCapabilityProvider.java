package com.bsixel.mysticism.common.capability;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;

public class SerializableCapabilityProvider<HANDLER> extends SimpleCapabilityProvider<HANDLER> implements INBTSerializable<INBT> {

    public SerializableCapabilityProvider(final Capability<HANDLER> cap, @Nullable final Direction facing) {
        this(cap, facing, cap.getDefaultInstance());
    }

    public SerializableCapabilityProvider(final Capability<HANDLER> cap, @Nullable final Direction facing, @Nullable final HANDLER instance) {
        super(cap, facing, instance);
    }

    @Nullable
    @Override
    public INBT serializeNBT() {
        final HANDLER instance = getHandlerInstance();
        return instance == null ? null : getCapability().writeNBT(instance, getFacing());
    }

    @Override
    public void deserializeNBT(final INBT nbt) {
        final HANDLER instance = getHandlerInstance();
        if (instance == null) {
            return;
        }
        getCapability().readNBT(instance, getFacing(), nbt);
    }
}
