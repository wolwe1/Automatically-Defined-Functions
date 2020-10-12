package library.gpLibrary.models.primitives.functions.interfaces;


import library.gpLibrary.models.primitives.nodes.abstractClasses.ValueNode;

public abstract class IOperateUpFunction<T> extends ValueNode<T> {

    protected IOperateUpFunction(String name) {
        super(name);
    }

    /**
     * Specifies the operation performed by the function
     * @return The calculated value by performing the operation
     */
    abstract void Operation();

    @Override
    public boolean IsFull() {
        return _children.size() == _maxChildren;
    }
}
