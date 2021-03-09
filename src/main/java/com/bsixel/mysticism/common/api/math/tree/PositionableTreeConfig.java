package com.bsixel.mysticism.common.api.math.tree;

import org.abego.treelayout.util.DefaultConfiguration;

public class PositionableTreeConfig<Node extends IPositionable> extends DefaultConfiguration<Node> {

    private int initialX;
    private int initialY;

    public PositionableTreeConfig(double gapBetweenLevels, double gapBetweenNodes, Location location, AlignmentInLevel alignmentInLevel, int initialX, int initialY) {
        super(gapBetweenLevels, gapBetweenNodes, location, alignmentInLevel);
        this.initialX = initialX;
        this.initialY = initialY;
    }

    public PositionableTreeConfig(double gapBetweenLevels, double gapBetweenNodes, Location location) {
        super(gapBetweenLevels, gapBetweenNodes, location);
    }

    public PositionableTreeConfig(double gapBetweenLevels, double gapBetweenNodes) {
        super(gapBetweenLevels, gapBetweenNodes);
    }

    public void setInitialX(int initialX) {
        this.initialX = initialX;
    }

    public void setInitialY(int initialY) {
        this.initialY = initialY;
    }

    public int getInitialX() {
        return initialX;
    }

    public int getInitialY() {
        return initialY;
    }
}
