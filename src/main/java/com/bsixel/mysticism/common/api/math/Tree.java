package com.bsixel.mysticism.common.api.math;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Tree<T extends IPositionableTreeNode<T>> { // TODO: How the heck do we draw lines between the points

    public enum TreeDirection { HORIZONTAL, TOP_DOWN, BOTTOM_UP }

    private Map<Integer, List<T>> depthMap = new HashMap<>();
    private final int nodeSize;
    private final int minSpacing;
    private final int startingX;
    private final int startingY;
    private final int yPadPerUncle;
    private final TreeDirection direction;

    public Tree(int nodeSize, int minSpacing, int startingX, int startingY, int yPadPerUncle) {
        this.nodeSize = nodeSize;
        this.minSpacing = minSpacing;
        this.startingX = startingX;
        this.startingY = startingY;
        this.yPadPerUncle = yPadPerUncle;
        this.direction = TreeDirection.HORIZONTAL;
    }

    public Tree(int nodeSize, int minSpacing, int startingX, int startingY, int yPadPerUncle, TreeDirection direction) {
        this.nodeSize = nodeSize;
        this.minSpacing = minSpacing;
        this.startingX = startingX;
        this.startingY = startingY;
        this.yPadPerUncle = yPadPerUncle;
        this.direction = direction;
    }

    public Tree(T rootNode, int nodeSize, int minSpacing, int startingX, int startingY, int yPadPerUncle, TreeDirection direction) {
        this.nodeSize = nodeSize;
        this.minSpacing = minSpacing;
        this.startingX = startingX;
        this.startingY = startingY;
        this.yPadPerUncle = yPadPerUncle;
        this.direction = direction;
        this.addNode(rootNode);
    }

    public T addNode(@Nonnull T node) {
        T parentNode = node.getParent();
        int depth = parentNode != null ? parentNode.depth()+1 : 0; // Zero if this is a root node
        if (!depthMap.containsKey(depth)) {
            depthMap.put(depth, new ArrayList<>());
        }
        List<T> depthList = depthMap.get(depth);
        node.setDepth(depth);
        node.setChildIndex(calculateAddedChildIndex(node));
        depthList.add(node);
//        fixNodePosition(node); Should be covered by mass position fix below
        shiftRightSiblings(node);
        depthMap.forEach((dpth, list) -> {
                list.forEach(this::fixNodePosition);
        });
        return node;
    }

    public void fixNodePosition(T node) {
        List<T> depthList = depthMap.get(node.depth());
        int relativeXParentPos = node.getParent() != null ? node.getParent().getX() : this.startingX;
        int totalXSpaceEach = this.nodeSize + this.minSpacing;
        int distanceFromCenterChild = depthList.size() == 0 ? 1 : (node.childIndex() - (depthList.size() / 2));
        int acrossPos = (distanceFromCenterChild * totalXSpaceEach) + relativeXParentPos;
        if (depthList.size() % 2 == 0) {
            acrossPos += totalXSpaceEach / 2;
        }
        int childWidthMod = node.getChildren().size() * totalXSpaceEach;
        if (distanceFromCenterChild < 0) {
            acrossPos -= childWidthMod;
        } else {
            acrossPos += childWidthMod;
        }
        int downPos = (node.depth() * (this.nodeSize +this.minSpacing)) + this.startingY;
        switch (this.direction) {
            case HORIZONTAL:
                node.setPos(downPos, acrossPos);
                break;
            case BOTTOM_UP:
                node.setPos(acrossPos, -downPos);
                break;
            default:
                node.setPos(acrossPos, downPos);
                break;
        }
    }

    /* Really this whole thing could be replaced by
        return depthMap.get(node.depth()) == 0 ? 0 : getLargestSiblingBeforeNode(depthMap.get(node.depth()), node.getParent())+1
       but this is more readable
     */
    private int calculateAddedChildIndex(@Nonnull T node) {
        T parentNode = node.getParent();
        List<T> allSiblings = depthMap.get(node.depth());
        if (allSiblings.size() == 0 ||  parentNode == null) {
            return 0; // This is a root node, or it's the first at this level
        } else {
            return getLargestSiblingBeforeNode(allSiblings, parentNode)+1;
        }
    }

    private int getLargestSiblingBeforeNode(List<T> allSiblings, T parentNode) { // Why are collections so messy
        List<Integer> collected = allSiblings.stream().filter(sib -> sib.getParent().childIndex() <= parentNode.childIndex()).map(T::childIndex).collect(Collectors.toList());
        return collected.size() == 0 ? 0 : Collections.max(collected);
    }

    public void shiftRightSiblings(@Nonnull T node) {
        this.depthMap.get(node.depth()).forEach(sibling -> {
            if (sibling.childIndex() >= node.childIndex() && node != sibling) { // The sibling is at or to the right of where the added node now is, shift to the right; Can use == because we're just checking if the "sibling" is its own pointer
                sibling.shiftRight();
            }
        });
    }

    public T getNodeFromDepthAndIndex(int depth, int childIndex) {
        return this.depthMap.get(depth).get(childIndex);
    }

    /**
     * Traverses through each node doing the action, will traverse across a depth then move down - may eventually be rewritten to do things in a better order! Currently as basically implemented as possible...
     */
    public void forEach(Consumer<T> action) {
        this.depthMap.forEach((depthIndex, list) -> {
            list.forEach(action);
        });
    }

    public int getNodeSize() {
        return nodeSize;
    }
}
