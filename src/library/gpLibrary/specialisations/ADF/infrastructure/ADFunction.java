package library.gpLibrary.specialisations.ADF.infrastructure;

import library.gpLibrary.models.primitives.nodes.abstractClasses.Node;
import library.gpLibrary.models.primitives.nodes.abstractClasses.ValueNode;

public class ADFunction<T> extends ValueNode<T> {

    ValueNode<T> root;

    protected ADFunction() {
        super("Unimplemented");
    }

    @Override
    public boolean isFull() {

        if(root == null)
            return false;

        return root.isFull();
    }

    @Override
    protected Node<T> getCopy() {
        if(root == null) throw new RuntimeException("Function does not contain definition");

        return root.getCopy(true);
    }

    @Override
    public boolean canTakeMoreChildren() {
        return root.canTakeMoreChildren();
    }

    @Override
    public boolean requiresTerminals(int maxDepth) {
        return root.requiresTerminals(maxDepth);
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
    public T getValue() {
        return root.getValue();
    }

    @Override
    public T getBaseValue() {
        return root.getBaseValue();
    }

    public int getSize() {
        return root.getSize();
    }
}
