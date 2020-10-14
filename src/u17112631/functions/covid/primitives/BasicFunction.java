package u17112631.functions.covid.primitives;

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
    public boolean hasAncestor(Node<T> nodeToAdd) {

        if(nodeToAdd.name.equals(this.name))
            return true;

        if(this.Parent == null)
            return false;

        return this.Parent.hasAncestor(nodeToAdd);
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
}
