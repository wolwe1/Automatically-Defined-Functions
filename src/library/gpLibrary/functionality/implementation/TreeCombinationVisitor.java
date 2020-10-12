package library.gpLibrary.functionality.implementation;

import library.gpLibrary.functionality.interfaces.ITreeVisitor;
import library.gpLibrary.models.primitives.nodes.abstractClasses.Node;

import java.util.ArrayList;
import java.util.List;

public class TreeCombinationVisitor<T> implements ITreeVisitor<T> {

    private final List<Node<T>> _visitedNodes;

    public TreeCombinationVisitor(){
        _visitedNodes = new ArrayList<>();
    }

    public String getCombination() {

        var ret = new StringBuilder();
        for (Node<T> visitedNode : _visitedNodes) {
            ret.append(visitedNode.name).append(".");
        }
        return ret.toString();
    }

    @Override
    public void visit(Node<T> temp) {
        _visitedNodes.add(temp);
    }
}
