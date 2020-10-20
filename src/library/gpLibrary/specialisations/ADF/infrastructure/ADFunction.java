package library.gpLibrary.specialisations.ADF.infrastructure;

import library.gpLibrary.models.primitives.nodes.abstractClasses.Node;
import library.gpLibrary.models.primitives.nodes.abstractClasses.ValueNode;

import java.util.ArrayDeque;
import java.util.Queue;

public class ADFunction<T> extends ValueNode<T> {

    ValueNode<T> root;

    protected ADFunction(String name) {
        super(name);
        _maxChildren = 1;
    }

    public ADFunction(ADFunction<T> other) {
        super(other);
        root = (ValueNode<T>) other.root.getCopy(true);

        this.children.add(root);
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
    public boolean isValid() {
        return root.isValid();
    }

    @Override
    public int countLeaves() {
        return root.countLeaves();
    }

    @Override
    public boolean isFull() {
        if(root == null)
            return false;

        return !root.canTakeMoreChildren();
    }


    @Override
    public T getValue() {
        return root.getValue();
    }

    @Override
    public T getBaseValue() {
        return root.getBaseValue();
    }

    public void setRoot(Node<T> node) {
        this.root = (ValueNode<T>) node;
        this.children.clear();
        this.children.add(root);
    }

    @Override
    public Node<T> addChild(Node<T> newNode){
        return breadthFirstInsert(newNode);
    }

    private Node<T> breadthFirstInsert(Node<T> newNode){
        Queue<Node<T>> queue = new ArrayDeque<>();
        Node<T> temp;

        queue.add(root);

        while (queue.size() != 0)
        {
            temp = queue.remove();

            if (!temp.isFull())
            {
                try {
                    temp.addChild(newNode);
                } catch (Exception e) {
                    throw new RuntimeException("Unable to place node in function");
                }
                return newNode;
            }

            queue.addAll(temp.getChildren());
        }

        throw new RuntimeException("The node was not able to be added");
    }
}
