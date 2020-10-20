package library.gpLibrary.models.primitives.nodes.implementation;

import library.gpLibrary.models.primitives.nodes.abstractClasses.Node;

public class EmptyNode extends Node<Double> {

    public EmptyNode() {
        super("Empty");
    }

    @Override
    public boolean isFull() {
        return true;
    }

    @Override
    protected Node<Double> getCopy() {
        return new EmptyNode();
    }

    @Override
    public boolean canTakeMoreChildren() {
        return false;
    }

    @Override
    public boolean requiresTerminals(int maxDepth) {
        return false;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public int countLeaves() {
        return 1;
    }
}
