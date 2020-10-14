package library.gpLibrary.specialisations.ADF;

import library.gpLibrary.models.highOrder.implementation.NodeTree;
import library.gpLibrary.models.primitives.nodes.abstractClasses.Node;
import library.gpLibrary.specialisations.ADF.infrastructure.ADFRoot;

import java.util.List;

public class ADFTree<T> extends NodeTree<T> {

    private ADFRoot<T> root;

    public ADFTree(int maxFuncDepth, int maxFuncBreadth,int maxMainDepth, int maxMainBreadth) {
        super();

        root = new ADFRoot<>(maxFuncDepth,maxFuncBreadth,maxMainDepth,maxMainBreadth);
        //Must handle breadth and depth calculations manually
        _maxNodes = root.getMaxNodes();
    }

    public ADFTree(NodeTree<T> other) {
        super(other);
    }

    @Override
    public void addNode(Node<T> newNode) throws Exception {
        if (isFull())
            throw new Exception("Tree full");

        root.addNode(newNode);
        numberOfNodes++;
    }

    @Override
    protected void setRoot(Node<T> node) {
        throw new RuntimeException("No reason to use set root on ADF Tree");
    }

    @Override
    public NodeTree<T> getCopy() {
        return new ADFTree<>(this);
    }

    @Override
    public boolean isFull() {
        return root.isFull();
    }

    @Override
    public boolean requiresTerminals() {
        return root.requiresTerminals();
    }

    @Override
    public Node<T> getRoot() {
        throw new RuntimeException("Get root not defined for ADF trees");
    }

    @Override
    public boolean acceptsNode(Node<T> nodeToAdd) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void addNodes(List<? extends Node<T>> nodesToLoad) {
        root.addNodes(nodesToLoad);
    }


}
