package library.gpLibrary.specialisations.ADF.infrastructure;

import library.gpLibrary.models.highOrder.implementation.NodeTree;
import library.gpLibrary.models.primitives.nodes.abstractClasses.Node;
import library.gpLibrary.models.primitives.nodes.abstractClasses.ValueNode;

import java.util.List;

public class ADFMain<T> extends NodeTree<T> {

    ValueNode<T> root;

    public ADFMain(int maxDepth, int maxBreadth) {
        super(maxDepth, maxBreadth);
    }

    public ADFMain(ADFMain<T> other) {
        super(other);

        root = (ValueNode<T>) (other.getRoot().getCopy(true));
    }

    @Override
    protected void setRoot(Node<T> node) {
        root = (ValueNode<T>) node;
    }

    @Override
    public NodeTree<T> getCopy() {
        return new ADFMain<>(this);
    }

    @Override
    public boolean isFull() {
        if(root == null)
            return false;

        return !root.canTakeMoreChildren();
    }

    @Override
    public boolean requiresTerminals() {
        if(root == null)
            return false;

        return root.requiresTerminals(maxDepth - 1);
    }

    @Override
    public ValueNode<T> getRoot() {
        return root;
    }

    //Main accepts all nodes
    @Override
    public boolean acceptsNode(Node<T> nodeToAdd) {
        return true;
    }

    @Override
    public void addNodes(List<? extends Node<T>> nodesToLoad) {

        for (Node<T> node : nodesToLoad) {
            try {
                breadthFirstInsert(node);
            } catch (Exception e) {
                throw new RuntimeException("Unable to load nodes");
            }
        }
        numberOfNodes += nodesToLoad.size();
    }

}
