package library.gpLibrary.models.primitives.nodes.abstractClasses;

import library.gpLibrary.models.primitives.nodes.interfaces.IValueNode;

public abstract class ValueNode<T> extends Node<T> implements IValueNode<T> {

    protected ValueNode(String name) {
        super(name);
    }

    public ValueNode(Node<T> other) {
        super(other);
    }
}
