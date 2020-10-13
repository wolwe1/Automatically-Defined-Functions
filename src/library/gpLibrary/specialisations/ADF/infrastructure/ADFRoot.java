package library.gpLibrary.specialisations.ADF.infrastructure;

import library.gpLibrary.models.primitives.nodes.abstractClasses.Node;
import library.gpLibrary.models.primitives.nodes.abstractClasses.ValueNode;

public class ADFRoot<T> extends ValueNode<T> {

    private final ADFFuncDefinition<T> definition;
    private final ADFMain<T> main;

    public ADFRoot() {
        super("progn");

        definition = new ADFFuncDefinition<>();
        main = new ADFMain<>();
    }

    public ADFRoot(ADFRoot<T> other) {
        super(other.name);
        definition = (ADFFuncDefinition<T>) other.definition.getCopy();
        main = (ADFMain<T>) other.main.getCopy();
    }

    @Override
    public boolean isFull() {
        return definition.isFull() && main.isFull();
    }

    @Override
    protected Node<T> getCopy() {
        return new ADFRoot<>(this);
    }

    @Override
    public boolean canTakeMoreChildren() {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public boolean requiresTerminals(int maxDepth) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public boolean hasAncestor(Node<T> nodeToAdd) {
        return false;
    }

    @Override
    public boolean isValid() {
        return definition.isValid() && main.isValid();
    }

    @Override
    public T getValue() {
        return main.getValue();
    }

    @Override
    public T getBaseValue() {
        throw new RuntimeException("Not implemented");
    }

    public int getSize() {
        return definition.getSize() + main.getSize();
    }
}
