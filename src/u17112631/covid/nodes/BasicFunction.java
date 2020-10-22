package u17112631.covid.nodes;

import library.gpLibrary.models.primitives.functions.interfaces.IOperateUpFunction;
import library.gpLibrary.models.primitives.nodes.abstractClasses.Node;

public abstract class BasicFunction<T> extends IOperateUpFunction<T> {

    protected BasicFunction(String name) {
        super(name);
        _maxChildren = 2;
    }

    @Override
    public boolean canTakeMoreChildren() {

        if(children.size() < _maxChildren)
            return true;

        for (Node<T> child : children) {
            if(child.canTakeMoreChildren())
                return true;
        }

        return false;
    }

    @Override
    public boolean requiresTerminals(int maxDepth) {
        if(_level == maxDepth - 1)
            return true;

        for (Node<T> child : children) {
            if(child.requiresTerminals(maxDepth))
                return true;
        }

        return false;
    }

    @Override
    public boolean isValid() {
        if(children.size() != _maxChildren)
            return false;

        for (Node<T> child : children) {
            if(!child.isValid())
                return false;
        }

        return true;
    }

    @Override
    public T getValue() {
        return operation();
    }

    @Override
    public int countLeaves(){
        int numLeaves = 0;
        for (Node<T> child : children) {
            numLeaves += child.countLeaves();
        }

        return numLeaves;
    }

    @Override
    public void setChildAtFirstTerminal(Node<T> node) {

        if(children.size() == 0) {
            try {
                addChild(node);
            } catch (Exception e) {
                throw new RuntimeException("Unable to set node as child");
            }
        }

        for (int i = 0, childrenSize = children.size(); i < childrenSize; i++) {
            Node<T> child = children.get(i);
            if (child.getChildren().size() == 0) {//Terminal node
                setChild(i,node);
                return;
            }
        }

        throw new RuntimeException("Unable to set child");
    }
}
