package library.gpLibrary.specialisations.classification;

import library.gpLibrary.models.highOrder.implementation.NodeTree;
import library.gpLibrary.models.primitives.nodes.abstractClasses.ChoiceNode;
import library.gpLibrary.models.primitives.nodes.abstractClasses.Node;

import java.util.ArrayDeque;
import java.util.Queue;

public class ClassificationTree<T> extends NodeTree<T> {

    private ChoiceNode<T> root;

    public ClassificationTree(int maxDepth, int maxBreadth) {
        super(maxDepth, maxBreadth);
    }

    public ClassificationTree(NodeTree<T> other) {
        super(other);
    }

    public T feedProblem(Problem<T> problem){
        return root.feed(problem);
    }

    public void setRoot(Node<T> node) {
        root = (ChoiceNode<T>) node;
    }

    public NodeTree<T> getCopy(){
        NodeTree<T> newTree = new ClassificationTree<T>(maxDepth, maxBreadth);
        try {
            newTree.addNode(root.getCopy(true));
        } catch (Exception e) {
            throw new RuntimeException("Unable to copy tree");
        }
        newTree.depth = depth;
        return newTree;
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
    public Node<T> getRoot() {
        return root;
    }

    /**
     * Enforces branch uniqueness
     * @param nodeToAdd The node attempting to be added
     * @return Whether or not the node is unique in the current addition branch
     */
    public boolean acceptsNode(Node<T> nodeToAdd) {
        Node<T> nodeThatWillAcceptChild = getNextNodeForInsert();
        return !nodeThatWillAcceptChild.hasAncestor(nodeToAdd);
    }

    private Node<T> getNextNodeForInsert() {

        Queue<Node<T>> queue = new ArrayDeque<>();
        Node<T> temp;

        queue.add(root);

        while (queue.size() != 0)
        {
            temp = queue.remove();

            if (!temp.isFull())
            {
                return temp;
            }
            queue.addAll(temp.getChildren());
        }
        throw new RuntimeException("No node available to accept children");
    }
}
