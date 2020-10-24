package library.gpLibrary.specialisations.ADF.infrastructure;

import library.gpLibrary.models.primitives.nodes.abstractClasses.Node;
import library.gpLibrary.models.primitives.nodes.abstractClasses.ValueNode;

import java.util.ArrayDeque;
import java.util.Queue;

public class ADFunction<T> extends ValueNode<T> {

    ValueNode<T> root;
    String composition;

    protected ADFunction(String name) {
        super(name);
        _maxChildren = 1;
        composition = "";
    }

    public ADFunction(ADFunction<T> other) {
        super(other);
        setRoot(other.root.getCopy(true));
        root.setLevel(this._level);
        composition = other.composition;
    }


    @Override
    public ADFunction<T> getCopy() {
        return new ADFunction<>(this);
    }

    @Override
    public Node<T> getCopy(boolean includeChildren){
        return getCopy();
    }

    @Override
    public boolean canTakeMoreChildren() {
        if(root == null)
            return true;

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
        return children.size() == _maxChildren && root.isValid() && root.parent == this;
    }

    @Override
    public int countLeaves() {
        return root.countLeaves();
    }

    @Override
    public void setChildAtFirstTerminal(Node<T> nodeInMain) {
        root.setChildAtFirstTerminal(nodeInMain);
    }

    @Override
    public void cutNodes(int maxDepth) {
        root.cutNodes(maxDepth);
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
        this.root.parent = this;
        this.root.setLevel(this._level);

        this.children.clear();
        this.children.add(root);
    }

    @Override
    public Node<T> addChild(Node<T> newNode){

        if(root == null){
            setRoot(newNode);
            return newNode;
        }else
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

    public String getComposition() {
        return composition;
    }

    public void setComposition(String composition) {
        this.composition = composition;
    }

    @Override
    public void setChild(int index,Node<T> newChild){
        if(index < 0 || index >= _maxChildren)
            throw new RuntimeException("Attempted to set a child out of range");

        newChild.index = index;
        //
        setRoot(newChild);
    }

    @Override
    public void removeLeaves(){

        for (int i = children.size() - 1; i >= 0; i--) {
            Node<T> child = children.get(i);

            if (child._maxChildren == 0){
                removeChild(i);
                root = null;
            }
            else
                child.removeLeaves();
        }
    }
}
