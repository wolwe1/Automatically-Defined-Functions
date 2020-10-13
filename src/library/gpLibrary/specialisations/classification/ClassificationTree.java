package library.gpLibrary.specialisations.classification;

import library.gpLibrary.functionality.interfaces.ITreeVisitor;
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


    @Override
    public int getTreeSize()
    {
        return sumNodes(root);
    }

    @Override
    public void addNode(Node<T> node) throws Exception {
        if (IsFull())
            throw new Exception("Tree full");

        //Empty tree
        if (root == null)
        {
            node._level = 0;
            root = (ChoiceNode<T>) node;
        }
        else
        {
            breadthFirstInsert(node);
        }
        numberOfNodes++;
    }

    @Override
    public void visitTree(ITreeVisitor<T> visitor)
    {
        Queue<Node<T>> queue = new ArrayDeque<>();
        Node<T> temp;

        queue.add(root);

        while (queue.size() != 0)
        {
            temp = queue.remove();

            visitor.visit(temp);

            queue.addAll(temp.getChildren());
        }
    }

    protected void breadthFirstInsert(Node<T> node) throws Exception {
        Queue<Node<T>> queue = new ArrayDeque<>();
        Node<T> temp;

        queue.add(root);

        while (queue.size() != 0)
        {
            temp = queue.remove();

            if (!temp.isFull())
            {
                temp.addChild(node);
                return;
            }

            queue.addAll(temp.getChildren());
        }
    }

    @Override
    public void clearLeaves() {
        root.removeLeaves();
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
    public void replaceNode(int nodeToReplace, Node<T> newNode) {

        Node<T> nodeInTree = getNode(nodeToReplace);
        nodeInTree.Parent.setChild(nodeInTree.index,newNode);
    }

    @Override
    public boolean IsFull() {
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
    public boolean isValid() {
        return root.isValid();
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
