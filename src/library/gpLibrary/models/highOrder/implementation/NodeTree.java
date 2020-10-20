package library.gpLibrary.models.highOrder.implementation;

import library.gpLibrary.functionality.implementation.BreadthFirstVisitor;
import library.gpLibrary.functionality.implementation.TreeCombinationVisitor;
import library.gpLibrary.functionality.interfaces.ITreeVisitor;
import library.gpLibrary.models.primitives.nodes.abstractClasses.Node;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

public abstract class NodeTree<T>
{
    //Structure
    public int depth;
    public int maxDepth;
    public int maxBreadth;

    protected int _maxNodes;
    protected int numberOfNodes;

    public NodeTree(int maxDepth,int maxBreadth)
    {
        this.maxDepth = maxDepth;
        this.maxBreadth = maxBreadth;
        depth = 0;
        numberOfNodes = 0;
        //root = null;
        _maxNodes = calculateMaximumSize(maxBreadth,maxDepth);
    }

    /**
     * Delegate depth and breadth calculations to sub classes
     */
    protected NodeTree(){
        depth = 0;
        numberOfNodes = 0;
    }

    /**
     * Creates a tree with the same specification as the other, does not copy nodes
     * @param other The tree to copy
     */
    public NodeTree(NodeTree<T> other){
        this.maxDepth = other.maxDepth;
        this.maxBreadth = other.maxBreadth;
        this.depth = other.depth;
        this.numberOfNodes = other.numberOfNodes;
        //root = null;
        _maxNodes = other._maxNodes;
    }

    public int getSize()
    {
        return getRoot().getSize();
    }

    public void addNode(Node<T> node) throws Exception {
        if (isFull())
            throw new Exception("Tree full");

        //Empty tree
        if (getRoot() == null)
        {
            node._level = 0;
            setRoot(node);
        }
        else
        {
            breadthFirstInsert(node);
        }
        numberOfNodes++;
    }

    protected abstract void setRoot(Node<T> node);

    public void visitTree(ITreeVisitor<T> visitor)
    {
        Queue<Node<T>> queue = new ArrayDeque<>();
        Node<T> temp;

        if(getRoot() == null) return;

        queue.add(getRoot());

        while (queue.size() != 0)
        {
            temp = queue.remove();

            visitor.visit(temp);

            queue.addAll(temp.getChildren());
        }
    }

    /**
     * Returns the total amount of possible leaf nodes the tree can contain
     * @return The maximum possible leaf nodes a tree can support
     */
    public int getMaximumNumberOfPossibleLeafNodes() {
        return (int) Math.pow(maxBreadth,maxDepth - 1);
    }

    public int getNumberOfLeafNodes(){
        return getRoot().countLeaves();
    }

    /**
     * Calculates the hypothetical maximum size of the tree
     * @param maxBreadth The breadth of the tree, ie. The number of children each node may have
     * @param maxDepth The maximum depth of the tree, ie. The maximum number of nodes from root to terminal
     * @return The maximum number of nodes the tree can hold
     */
    protected int calculateMaximumSize(int maxBreadth, int maxDepth)
    {
        int total = 0;

        for (int i = 0; i < maxDepth; i++)
        {
            total += (int)Math.pow(maxBreadth, i);
        }

        return total;
    }


    protected void breadthFirstInsert(Node<T> node) throws Exception {
        Queue<Node<T>> queue = new ArrayDeque<>();
        Node<T> temp;

        queue.add(getRoot());

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

        throw new RuntimeException("The node was not able to be added");
    }

    /**
     * Returns a string representing the tree's members, in breadth first order
     * @return The trees makeup
     */
    public String getCombination() {

        //Get the nodes in breadth first order
        TreeCombinationVisitor<T> visitor = new TreeCombinationVisitor<>();
        visitTree(visitor);

        return visitor.getCombination();
    }

    public Node<T> getNode(int nodeIndex){

        BreadthFirstVisitor<T> visitor = new BreadthFirstVisitor<>();
        visitTree(visitor);

        return visitor.getNode(nodeIndex);
    }

    public void clearLeaves() {
        getRoot().removeLeaves();
        numberOfNodes = getSize();
    }

    public abstract NodeTree<T> getCopy();

    public int getMaxNodes() {
        return _maxNodes;
    }

    public void replaceNode(int nodeToReplace, Node<T> newNode) {

        Node<T> nodeInTree = getNode(nodeToReplace);
        nodeInTree.parent.setChild(nodeInTree.index,newNode);
    }

    public abstract boolean isFull();

    public abstract boolean requiresTerminals();

    public boolean isValid() {
        return getRoot().isValid();
    }

    public abstract Node<T> getRoot();

    public abstract boolean acceptsNode(Node<T> nodeToAdd);

    public abstract void addNodes(List<? extends Node<T>> nodesToLoad);
}