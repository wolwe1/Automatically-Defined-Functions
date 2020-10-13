package library.gpLibrary.models.primitives.functions;

import library.gpLibrary.models.primitives.nodes.abstractClasses.Node;

/**
 * This class is used by all implementers of a genetic function
 * @param <T> The type returned by the function
 */
public abstract class GeneticFunction<T> extends Node<T> {

    protected GeneticFunction(String name) {
        super(name);
    }

    @Override
    public boolean isFull() {
        return _children.size() == _maxChildren;
    }
}
