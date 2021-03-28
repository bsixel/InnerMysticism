package com.bsixel.mysticism.common.api.spells;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseSpellComponent implements ISpellComponent {

    protected static final List<Class<? extends ISpellComponent>> allowedChildren = new ArrayList<>();

    @Override
    public boolean isChildAllowed(Class<? extends ISpellComponent> childPart) {
//        return allowedChildren.contains(childPart); // TODO: We might want an easy way of adding "all" or groups or something
        return true;
    }

    @Override
    public void addAllowedChild(Class<? extends ISpellComponent> childPart) {
        allowedChildren.add(childPart);
    }

}
