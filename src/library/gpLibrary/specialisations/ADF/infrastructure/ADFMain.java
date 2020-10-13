package library.gpLibrary.specialisations.ADF.infrastructure;

import library.gpLibrary.models.primitives.nodes.abstractClasses.Node;
import library.gpLibrary.models.primitives.nodes.abstractClasses.ValueNode;

public class ADFMain<T> extends ValueNode<T> {

    ValueNode<T> root;

    protected ADFMain() {
        super("Main");
    }

    public ADFMain(ADFMain<T> other) {
        super(other.name);
        root = (ValueNode<T>) other.root.getCopy(true);
    }

    @Override
    public boolean isFull() {
        if(root == null)
            return false;

        return root.isFull();
    }

    @Override
    protected Node<T> getCopy() {
        return new ADFMain<>(this);
    }

    @Override
    public boolean canTakeMoreChildren() {
        if(root == null)
            return true;

        return root.canTakeMoreChildren();
    }

    @Override
    public boolean requiresTerminals(int maxDepth) {
        if(root == null) return false;

        return root.requiresTerminals(maxDepth);
    }

    @Override
    public boolean hasAncestor(Node<T> nodeToAdd) {
        return false;
    }

    @Override
    public boolean isValid() {
        if(root == null) return false;

        return root.isValid();
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
