package com.bsixel.mysticism.common.api.math;

import net.minecraft.util.math.vector.Vector2f;

import java.util.LinkedList;

public interface IPositionableTreeNode<T extends IPositionableTreeNode<T>> {

    T getParent(); // May be null if this is the root node of a tree
    T setLeftNeighbor(T node);
    T setRightNeighbor(T node);
    T getLeftNeighbor();
    T getRightNeighbor();
    T setPrevNode(T prevNode);
    T getPrevNode();
    T setNextLevel(T nextLevel);
    T getNextLevel();
    LinkedList<T> getChildren();
    void addChild(T child);
    T firstChild();


    int childIndex(); // The position of this node in the parent's child list
    void setChildIndex(int index);
    default int shiftRight() {
        this.setChildIndex(this.childIndex()+1);
        return childIndex();
    }
    int depth(); // How many levels deep this node is
    void setDepth(int depth);
    void setModifier(int modifier);
    int getModifier();
    void setPrelimX(int x);
    int getPrelimX();
    void setPos(int x, int y);
    int getX();
    int getY();
    Vector2f getPos();

}
