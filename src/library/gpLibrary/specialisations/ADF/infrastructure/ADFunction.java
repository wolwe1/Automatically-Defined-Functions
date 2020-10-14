package library.gpLibrary.specialisations.ADF.infrastructure;

import library.gpLibrary.models.primitives.nodes.abstractClasses.Node;
import library.gpLibrary.models.primitives.nodes.abstractClasses.ValueNode;

public class ADFunction<T> extends ValueNode<T> {

    ValueNode<T> root;

    protected ADFunction(String name) {
        super(name);
    }

    public ADFunction(ADFunction<T> other) {
        super(other.name);
        root = (ValueNode<T>) other.root.getCopy(true);
    }


    @Override
    public ADFunction<T> getCopy() {
        return new ADFunction<>(this);
    }

    @Override
    public boolean canTakeMoreChildren() {
        if(root == null)
            return false;

        return root.canTakeMoreChildren();
    }

    @Override
    public boolean requiresTerminals(int maxDepth) {

        if(root == null)
            return false;

        return root.requiresTerminals(maxDepth - 1);
    }

    @Override
    public boolean hasAncestor(Node<T> nodeToAdd) {
        return false;
    }

    @Override
    public boolean isValid() {
        return root.isValid();
    }

    @Override
    public boolean isFull() {
        if(root == null)
            return false;

        return root.isFull();
    }


    @Override
    public T getValue() {
        return root.getValue();
    }

    @Override
    public T getBaseValue() {
        return root.getBaseValue();
    }
}
