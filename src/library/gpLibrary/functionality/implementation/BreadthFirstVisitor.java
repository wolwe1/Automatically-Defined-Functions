package library.gpLibrary.functionality.implementation;

import library.gpLibrary.functionality.interfaces.ITreeVisitor;
import library.gpLibrary.models.primitives.nodes.abstractClasses.Node;

import java.util.ArrayList;
import java.util.List;

public class BreadthFirstVisitor<T> implements ITreeVisitor<T> {

    private final List<Node<T>> visitedNodes;

    public BreadthFirstVisitor(){
        visitedNodes = new ArrayList<>();
    }

    @Override
    public void visit(Node<T> node) {
        visitedNodes.add(node);
    }

    @Override
    public void clear() {
        visitedNodes.clear();
    }

    public List<Node<T>> getNodes() {
        return visitedNodes;
    }

    /**
     * Returns the node at a given index, used after visiting a tree
     * @param index The index of the node to retrieve
     * @return The node at the breadth first index of the tree
     */
    public Node<T> getNode(int index){

        if(index >= visitedNodes.size())
            throw new RuntimeException("Attempted to access node that doesnt exist");

        return visitedNodes.get(index);
    }
}
