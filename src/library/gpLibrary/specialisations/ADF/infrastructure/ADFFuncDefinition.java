package library.gpLibrary.specialisations.ADF.infrastructure;

import library.gpLibrary.models.primitives.nodes.abstractClasses.Node;
import library.gpLibrary.models.primitives.nodes.abstractClasses.ValueNode;

import java.util.ArrayList;
import java.util.List;

public class ADFFuncDefinition<T> extends ValueNode<T>{

    List<T> arguments;
    ADFunction<T> function;

    protected ADFFuncDefinition() {
        super("Defun");
        arguments = new ArrayList<>();
    }

    public ADFFuncDefinition(ADFFuncDefinition<T> other) {
        super(other.name);
        arguments = other.arguments;
        function = (ADFunction<T>) other.function.getCopy();
    }

    @Override
    public boolean isFull() {
        return function.isFull();
    }

    @Override
    protected Node<T> getCopy() {
        return new ADFFuncDefinition<>(this);
    }

    @Override
    public boolean canTakeMoreChildren() {
        return function.canTakeMoreChildren();
    }

    @Override
    public boolean requiresTerminals(int maxDepth) {
        return function.requiresTerminals(maxDepth);
    }

    @Override
    public boolean hasAncestor(Node<T> nodeToAdd) {
        return false;
    }

    @Override
    public boolean isValid() {
        return function.isValid();
    }

    @Override
    public T getValue() {
        return function.getValue();
    }

    @Override
    public T getBaseValue() {
        return function.getBaseValue();
    }

    public int getSize() {
        return function.getSize();
    }
}
