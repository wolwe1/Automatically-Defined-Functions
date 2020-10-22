package library.gpLibrary.functionality.implementation;

import library.gpLibrary.functionality.interfaces.ITreeVisitor;
import library.gpLibrary.models.primitives.nodes.abstractClasses.Node;

import java.util.ArrayList;
import java.util.List;

public class DepthFirstVisitor<T> implements ITreeVisitor<T> {

    List<Node<T>> nodes;

    public DepthFirstVisitor(){
        nodes = new ArrayList<>();
    }

    @Override
    public void visit(Node<T> root) {
        nodes.add(root);

        for (Node<T> node : root.getChildren()) {
            visit(node);
        }
    }

    @Override
    public void clear() {
        nodes.clear();
    }

    @Override
    public List<? extends Node<T>> getNodes() {
        return nodes;
    }

    public String getCombination() {

        var ret = new StringBuilder();
        for (Node<T> visitedNode : nodes) {
            ret.append(visitedNode.name).append(".");
        }
        return ret.toString();
    }
}
