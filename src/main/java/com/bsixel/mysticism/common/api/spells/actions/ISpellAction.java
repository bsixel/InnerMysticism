package com.bsixel.mysticism.common.api.spells.actions;

import com.bsixel.mysticism.common.api.spells.ISpellComponent;
import com.bsixel.mysticism.common.api.spells.instances.ISpellInstance;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.world.World;

public interface ISpellAction extends ISpellComponent { // Break, hurt, heal, grow etc, subclasses

    boolean applyToEntity(World world, EntityRayTraceResult trace, ISpellInstance spellInstance); // TODO: Each component should handle self-casts separately inside here with an entity comparison check
    boolean applyToBlock(World world, BlockRayTraceResult trace, ISpellInstance spellInstance);

}
