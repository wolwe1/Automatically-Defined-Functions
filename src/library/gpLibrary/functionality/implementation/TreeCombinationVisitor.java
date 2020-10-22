package library.gpLibrary.functionality.implementation;

import library.gpLibrary.functionality.interfaces.ITreeVisitor;
import library.gpLibrary.models.primitives.nodes.abstractClasses.Node;

import java.util.ArrayList;
import java.util.List;

public class TreeCombinationVisitor<T> implements ITreeVisitor<T> {

    private final List<Node<T>> visitedNodes;

    public TreeCombinationVisitor(){
        visitedNodes = new ArrayList<>();
    }

    public String getCombination() {

        var ret = new StringBuilder();
        for (Node<T> visitedNode : visitedNodes) {
            ret.append(visitedNode.name).append(".");
        }
        return ret.toString();
    }

    @Override
    public void visit(Node<T> temp) {
        visitedNodes.add(temp);
    }

    @Override
    public void clear() {
        visitedNodes.clear();
    }

    @Override
    public List<? extends Node<T>> getNodes() {
        return visitedNodes;
    }
}
