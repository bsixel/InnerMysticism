package com.bsixel.mysticism.common.api.spells;

import com.bsixel.mysticism.common.api.spells.casttypes.ISpellCastType;
import javafx.scene.paint.Color;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.List;

public class Spell { // NOTE: This hold the state of a spell, but doesn't represent the cast spell.
    // TODO: Serialization
    public ISpellCastType initialCastType; // Spells are trees that start with one ISpellCastType node, basically

    // Some additional that may be needed depending on what components we have in the spell
    private final String creatorUUID; // Who made this spell? Spells can be copied, maybe we need the original creator TODO: We need a fake player+UUID
    private final String creationDimension;
    private final BlockPos creationPosition;

    public Spell(LivingEntity creator, BlockPos pos, World dimension) { // Living entity should be safe, SafePlayer is a distant inheritor
        this.creatorUUID = creator.getUniqueID().toString();
        this.creationDimension = dimension.getDimensionKey().getRegistryName().getPath();
        this.creationPosition = pos;
    }

    // Normal casters like players and justiciars/enforcers
    public void cast(@Nonnull LivingEntity caster) {
        initialCastType.cast(caster);
    }

    // Artificial caster. Not sure what all this will include but probably some sorta block that casts. Magic traps? Might be needed for anchors? I don't think so.
    public void cast(@Nonnull BlockPos sourcePos, @Nonnull Vector3d lookVector) {
        initialCastType.cast(sourcePos, lookVector);
    }

    public Color getPrimaryColor() {
        return initialCastType.getPrimaryColor();
    }

    public String getCreatorUUID() {
        return creatorUUID;
    }

    public String getCreationDimension() {
        return creationDimension;
    }

    public BlockPos getCreationPosition() {
        return creationPosition;
    }
}
