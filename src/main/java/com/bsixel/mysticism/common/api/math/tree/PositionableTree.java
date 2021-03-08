package com.bsixel.mysticism.common.api.math.tree;

import org.abego.treelayout.util.DefaultTreeForTreeLayout;

public class PositionableTree<Node extends IPositionable> extends DefaultTreeForTreeLayout<Node> {

    /**
     * Creates a new instance with a given node as the root
     *
     * @param root the node to be used as the root.
     */
    public PositionableTree(Node root) {
        super(root);
    }

}
