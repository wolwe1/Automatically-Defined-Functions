package library.gpLibrary.functionality.implementation;

import library.gpLibrary.functionality.interfaces.ITreeVisitor;
import library.gpLibrary.models.primitives.nodes.abstractClasses.Node;

import java.util.ArrayList;
import java.util.List;

public class BreadthFirstVisitor<T> implements ITreeVisitor<T> {

    private final List<Node<T>> _visitedNodes;

    public BreadthFirstVisitor(){
        _visitedNodes = new ArrayList<>();
    }

    @Override
    public void visit(Node<T> node) {
        _visitedNodes.add(node);
    }

    public List<Node<T>> getNodes() {
        return _visitedNodes;
    }

    /**
     * Returns the node at a given index, used after visiting a tree
     * @param index The index of the node to retrieve
     * @return The node at the breadth first index of the tree
     */
    public Node<T> getNode(int index){

        if(index >= _visitedNodes.size())
            throw new RuntimeException("Attempted to access node that doesnt exist");

        return _visitedNodes.get(index);
    }
}
