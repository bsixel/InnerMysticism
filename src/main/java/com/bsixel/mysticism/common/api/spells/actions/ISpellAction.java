package com.bsixel.mysticism.common.api.spells.actions;

import com.bsixel.mysticism.common.api.spells.ISpellComponent;
import com.bsixel.mysticism.common.api.spells.SpellComponentInstance;
import com.bsixel.mysticism.common.api.spells.instances.SpellInstance;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.world.World;

public interface ISpellAction extends ISpellComponent { // Break, hurt, heal, grow etc, subclasses

    boolean applyToEntity(World world, EntityRayTraceResult trace, SpellInstance spellInstance, SpellComponentInstance wrapper); // TODO: Each component should handle self-casts separately inside here with an entity comparison check
    boolean applyToBlock(World world, BlockRayTraceResult trace, SpellInstance spellInstance, SpellComponentInstance wrapper);

}
