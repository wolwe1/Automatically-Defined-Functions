package library.gpLibrary.models.primitives.nodes.implementation;

import library.gpLibrary.models.primitives.nodes.abstractClasses.Node;
import library.gpLibrary.models.primitives.nodes.abstractClasses.ValueNode;

public class EmptyNode extends ValueNode<Double>{

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
        //throw new RuntimeException("Terminal asked if required terminals");
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

    @Override
    public void setChildAtFirstTerminal(Node<Double> nodeInMain) {
        throw new RuntimeException("Terminal cannot set terminal");
    }

    @Override
    public void cutNodes(int i) {
        return;
    }

    @Override
    public Double getValue() {
        throw new RuntimeException("Empty node has no value");
    }

    @Override
    public Double getBaseValue() {
        throw new RuntimeException("Empty node has no value");
    }
}
