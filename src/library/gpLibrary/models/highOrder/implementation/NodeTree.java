package library.gpLibrary.models.highOrder.implementation;

import library.gpLibrary.functionality.implementation.BreadthFirstVisitor;
import library.gpLibrary.functionality.implementation.TreeCombinationVisitor;
import library.gpLibrary.functionality.interfaces.ITreeVisitor;
import library.gpLibrary.models.primitives.nodes.abstractClasses.Node;

public abstract class NodeTree<T>
{
    //Structure
    //public  Node<T> root;
    public int depth;
    public final int maxDepth;
    public final int maxBreadth;

    protected final int _maxNodes;
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

    /**
     * Returns the amount of nodes in the tree
     * @return The amount of nodes in the tree
     */
    public abstract int getTreeSize();

    /**
     * Adds a node into the tree, breadth first manner
     * @param node The node to be added
     * @throws Exception If the tree is full
     */
    public abstract void addNode(Node<T> node) throws Exception;

    /**
     * This function allows for a visitor to perform an action on the trees members, breadth first
     * @param visitor The visitor object that will have its visit function called on every node
     */
    public abstract void visitTree(ITreeVisitor<T> visitor);

    /**
     * Returns the total amount of possible leaf nodes the tree can contain
     * @return The maximum possible leaf nodes a tree can support
     */
    public int getNumberOfPossibleLeafNodes() {
        return (int) Math.pow(maxBreadth,maxDepth - 1);
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


    /**
     * Counts the number of nodes in the tree
     * @param node The root of the tree
     * @return The amount of nodes in the tree
     */
    protected int sumNodes(Node<T> node)
    {
        int total = 0;

        if (node == null)
            return 0;

        for(var child : node.getChildren())
        {
            total += sumNodes(child);
        }
        return total + 1;
    }

    /**
     * Inserts a new node into the tree, breadth first manner
     * @param node The node to be added to the tree
     * @throws Exception If the tree is full
     */
    protected abstract void breadthFirstInsert(Node<T> node) throws Exception;

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

    public abstract void clearLeaves();

    public abstract NodeTree<T> getCopy();

    public int getMaxNodes() {
        return _maxNodes;
    }

    public abstract void replaceNode(int nodeToReplace, Node<T> newNode);

    public abstract boolean IsFull();

    public abstract boolean requiresTerminals();

    public abstract boolean isValid();
}