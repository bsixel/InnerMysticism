package com.bsixel.mysticism.common.api.math;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/*
 Based loosely on http://www.cs.unc.edu/techreports/89-034.pdf
 */

public class Tree<T extends IPositionableTreeNode<T>> { // TODO: How the heck do we draw lines between the points
    private int xTopAdjustment;
    private int yTopAdjustment;

    public enum TreeDirection { HORIZONTAL, TOP_DOWN, BOTTOM_UP }

    private T rootNode;
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
        this.addNode(null, rootNode);
    }

    public T addNode(@Nullable T parentNode, @Nonnull T node) {
        int depth = parentNode != null ? parentNode.depth()+1 : 0; // Zero if this is a root node
        if (depth == 0) {
            this.rootNode = node;
        }
        if (parentNode != null) {
            parentNode.addChild(node);
        }
        if (!depthMap.containsKey(depth)) {
            depthMap.put(depth, new ArrayList<>());
        }
        List<T> depthList = depthMap.get(depth);
        node.setDepth(depth);
//        node.setChildIndex(calculateAddedChildIndex(node));
        node.setChildIndex(parentNode != null ? parentNode.getChildren().size()-1 : 0); // The index of the child in *just* the sibling list
        depthList.add(node);
//        shiftRightSiblings(node);
        this.positionTree(this.rootNode);
//        depthMap.forEach((dpth, list) -> {
//            list.forEach(nd -> this.fixNodePosition(nd, true));
//        });
        return node;
    }

    public void fixNodePosition(T node, boolean calcBasedOnAllChildren) {
        List<T> depthList = depthMap.get(node.depth());
        int relativeXParentPos = node.getParent() != null ? node.getParent().getX() : this.startingX;
        int totalXSpaceEach = this.nodeSize + this.minSpacing;
        double distanceFromCenterChild = depthList.size() == 0 ? 1 : Math.ceil((double)node.childIndex() - ((double)depthList.size() / 2D));
        int acrossPos = (int) ((distanceFromCenterChild * totalXSpaceEach) + relativeXParentPos);
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

        T parentNode = node.getParent();
        if (parentNode == null) {
            return;
        }
        parentNode.getChildren().forEach(sib -> {
            if (sib.childIndex() >= node.childIndex() && ! node.equals(sib)) {
                sib.shiftRight();
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
        this.depthMap.forEach((depthIndex, list) -> list.forEach(action));
    }

    public void bottomUpForEach(Consumer<T> action) {
        for (int depth = depthMap.size()-1; depth >= 0; depth--) {
            List<T> depthList = depthMap.get(depth);
            for (int idx = depthList.size()-1; depth >= 0; depth--) {
                action.accept(depthList.get(idx));
            }
        }
    }

    public int getNodeSize() {
        return nodeSize;
    }

    private int totalSpacing() {
        return this.nodeSize + this.minSpacing;
    }

    public void positionTree(T node) {
        if (node != null) {
            node.setPos(this.startingX, this.startingY);
            initPreviousNodeList();
            firstWalk(node, 0);
            this.xTopAdjustment = node.getX() - node.getPrelimX();
            this.yTopAdjustment = node.getY();

            secondWalk(node, 0, 0);
        }
    }

    private final Map<Integer, T> prevNodeAtLevel = new HashMap<>();
    private T getPrevNodeAtLevel(T current) {
        T prev = prevNodeAtLevel.get(current.depth());
        return current.equals(prev) ? null : prev;
    }

    private void firstWalk(T node, int depth) {
        node.setLeftNeighbor(getPrevNodeAtLevel(node));
        prevNodeAtLevel.put(depth, node);
        node.setModifier(0);
        if (node.getChildren().size() == 0) { // We're a leaf
            T lSib = getLeftSibling(node);
            if (lSib != null) {
                node.setPrelimX(lSib.getPrelimX() + totalSpacing() + meanNodeSize(lSib, node));
            } else {
                node.setPrelimX(0);
            }
        } else {
            T leftMost = node.firstChild();
            T rightMost = node.firstChild();
            firstWalk(leftMost, depth+1);
            while (getRightSibling(rightMost) != null) {
                rightMost = getRightSibling(rightMost);
                firstWalk(rightMost, depth+1);
            }
            int midpoint = ((leftMost != null ? leftMost.getPrelimX() : 0) + (rightMost != null ? rightMost.getPrelimX() : 0)) / 2;
            T leftSibling = getLeftSibling(node);
            if (leftSibling != null) {
                node.setPrelimX(leftSibling.getPrelimX() + totalSpacing() + this.nodeSize); // OLD: meanNodeSize
                node.setModifier(node.getPrelimX() - midpoint);
                apportion(node, depth);
            } else {
                node.setPrelimX(midpoint);
            }
        }
    }

    private void secondWalk(T node, int depth, int modSum) {
        int xTemp = this.xTopAdjustment + node.getPrelimX() + modSum;
        int yTemp = this.yTopAdjustment + (node.depth() * totalSpacing());
        node.setPos(xTemp, yTemp);
        if (node.firstChild() != null) {
            secondWalk(node.firstChild(), depth+1, modSum + node.getModifier());
        }
        if (getRightSibling(node) != null) {
            secondWalk(getRightSibling(node), depth+1, modSum);
        }
    }

    private T getLeftSibling(T node) {
        if (node == null || node.getParent() == null) { // No parent, root node or malformed
            return null;
        }
        int currentNodeIndex = node.childIndex();
        if (currentNodeIndex == 0) { // First in the child list or (somehow??) this parent doesn't contain the node
            return null;
        }
        return node.getParent().getChildren().get(currentNodeIndex - 1);
    }

    private T getRightSibling(T node) {
        if (node == null || node.getParent() == null) { // No parent, root node or malformed
            return null;
        }
        int currentNodeIndex = node.childIndex();
        if (node.getParent().getChildren().size()-1 < currentNodeIndex + 1) { // Last child in sibling list
            return null;
        }
        return node.getParent().getChildren().get(currentNodeIndex + 1);
    }

    private void apportion(T node, int depth) {
        T leftMost = node.firstChild();
        T neighbor = leftMost != null ? leftMost.getLeftNeighbor() : null;
        int compareDepth = 1;
        int depthToStop = this.depthMap.size()-1-depth;
        while (leftMost != null && neighbor != null && compareDepth <= depthToStop) {
            int leftModSum = 0;
            int rightModSum = 0;
            T leftMostAncestor = leftMost;
            T neighborAncestor = neighbor;
            for (int i = 0; i <= compareDepth; i++) { // TODO: IF THINGS BREAK MAKE SURE THIS IS THE RIGHT THING ( <= vs <)
                leftMostAncestor = leftMostAncestor != null ? leftMostAncestor.getParent() : null;
                neighborAncestor = neighborAncestor.getParent();
                rightModSum = rightModSum + (leftMostAncestor != null ? leftMostAncestor.getModifier() : 0);
                leftModSum = leftModSum + (neighborAncestor != null ? neighborAncestor.getModifier() : 0);
            }
            int moveDistance = (neighbor.getPrelimX() + leftModSum + totalSpacing() + this.nodeSize) - (leftMost.getPrelimX() + rightModSum); // OLD: meanNodeSize
            if (moveDistance > 0) {
                T temp = node;
                int leftSiblings = 0;
                while (temp != null && !temp.equals(neighborAncestor)) {
                    leftSiblings++;
                    temp = getLeftSibling(temp);
                }
                if (temp != null) {
                    int portion = leftSiblings == 0 ? 0 : (moveDistance / leftSiblings);
                    temp = node;
                    while (temp.equals(neighborAncestor)) {
                        temp.setPrelimX(temp.getPrelimX() + moveDistance);
                        temp.setModifier(temp.getModifier() + moveDistance);
                        moveDistance -= portion;
                        temp = getLeftSibling(temp);
                    }
                } else {
                    return;
                }
            }
            compareDepth = compareDepth + 1;
            if (leftMost.firstChild() == null) { // It's a leaf
                leftMost = getLeftMost(node, 0, compareDepth);
            } else {
                leftMost = leftMost.firstChild();
            }
        }
    }

    private T getLeftMost(T node, int level, int depth) {
        if (level >= depth) {
            return node;
        } else if (node.firstChild() == null) {
            return null;
        } else {
            T rightmost = node.firstChild();
            T leftmost = getLeftMost(rightmost, level+1, depth);
            while (leftmost != null && getRightSibling(rightmost) != null) {
                rightmost = getRightSibling(rightmost);
                leftmost = getLeftMost(rightmost, level+1, depth);
            }
            return leftmost;
        }
    }

    private int meanNodeSize(T leftNode, T rightNode) { // TODO: Use if we ever have variable node size
//        int nodeSize = 0;
//        if (leftNode != null) {
////            nodeSize += rightSize(leftNode);
//            nodeSize += this.nodeSize;
//        }
//        if (rightNode != null) {
////            nodeSize += leftSize(rightNode);
//            nodeSize += this.nodeSize;
//        }
//        return nodeSize / 2;
        return this.nodeSize;
    }

    private void initPreviousNodeList() { // TODO: What the heck is this for?
        T temp = this.rootNode;
        while (temp != null) {
            temp.setPrevNode(null);
            temp = temp.getNextLevel();
        }
    }

    private T getPrevNodeAtLevel(int depth) {
        T temp = this.rootNode;
        int i = 0;
        while (temp != null) {
            if (i == depth) {
                return temp.getPrevNode();
            }
            temp = temp.getNextLevel();
            i++;
        }
        return null;
    }

    private void setPrevNodeAtLevel(int depth, T node) { // I think we can replace this with our depthMap; each first node in the depthList of each level
        T temp = this.rootNode;
        int i = 0;
        while (temp != null) {
            if (i == depth) {
                temp.setPrevNode(node);
                return;
            } else if (temp.getNextLevel() == null) {
                T newNode = null; // ????? What the heck is supposed to be here
//                newNode.setPrevNode(null);
//                newNode.setNextLevel(null);
                temp.setNextLevel(newNode);
            }
            temp = temp.getNextLevel();
            i++;
        }
    }

}
