package library.gpLibrary.specialisations.ADF;

import library.gpLibrary.functionality.interfaces.ITreeVisitor;
import library.gpLibrary.models.highOrder.implementation.NodeTree;
import library.gpLibrary.models.primitives.nodes.abstractClasses.Node;
import library.gpLibrary.specialisations.ADF.infrastructure.ADFRoot;

public class ADFTree<T> extends NodeTree<T> {

    private ADFRoot<T> root;

    public ADFTree(int maxDepth, int maxBreadth) {
        super(maxDepth, maxBreadth);

        root = new ADFRoot<>();
    }

    public ADFTree(NodeTree<T> other) {
        super(other);
    }

    @Override
    public int getTreeSize() {
        return root.getSize();
    }

    @Override
    public void addNode(Node<T> node) throws Exception {
        root.addNode(node);
    }

    @Override
    public void visitTree(ITreeVisitor<T> visitor) {

    }

    @Override
    protected void breadthFirstInsert(Node<T> node) throws Exception {

    }

    @Override
    public void clearLeaves() {

    }

    @Override
    public NodeTree<T> getCopy() {
        return null;
    }

    @Override
    public void replaceNode(int nodeToReplace, Node<T> newNode) {

    }

    @Override
    public boolean IsFull() {
        return false;
    }

    @Override
    public boolean requiresTerminals() {
        return false;
    }

    @Override
    public boolean isValid() {
        return false;
    }
}
