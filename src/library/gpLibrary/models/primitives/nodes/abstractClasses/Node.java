package library.gpLibrary.models.primitives.nodes.abstractClasses;

import library.gpLibrary.models.primitives.nodes.implementation.TerminalNode;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a node that can have multiple children and a parent
 * @param <T> The type of the value stored by the node
 */
public abstract class Node<T>
{
    protected final List<Node<T>> _children;

    public Node<T> Parent;
    public final String name;
    public int _maxChildren;
    public int _level = 0;
    public int _drawPos = 0;
    public int _depth = 0;
    public int index = 0;

    protected Node(String name,int maxChildren)
    {
        _maxChildren = maxChildren;
        this.name = name;
        _children = new ArrayList<>();
    }

    protected Node(String name)
    {
        this.name = name;
        _children = new ArrayList<>();
    }

    protected Node(Node<T> other){
        _maxChildren  = other._maxChildren;
        _depth = other._depth;
        _drawPos = other._drawPos;
        _level = other._level;

        _children = new ArrayList<>();
        name = other.name;
    }

    public Node<T> addChild(Node<T> newNode) throws Exception {

        if (isFull()) throw new Exception("Node cannot have any more children");

        newNode.Parent = this;
        newNode._level = _level + 1;
        newNode.index = _children.size();
        _children.add(newNode);

        return newNode;
    }

    public List<Node<T>> getChildren(){ return _children; }

    public Node<T> getChild(int index){
        if (index >= _children.size())
            return null;

        return _children.get(index);
    }

    public abstract boolean isFull();

    protected abstract Node<T> getCopy();

    public void setChild(int index,Node<T> newChild){
        if(index < 0 || index >= _maxChildren)
            throw new RuntimeException("Attempted to set a child out of range");

        newChild.Parent = this;
        newChild.index = index;
        newChild.setLevel(this._level + 1);
        _children.set(index,newChild);
    }

    private void setLevel(int level) {
        this._level = level;

        for (Node<T> child : _children) {
            child.setLevel(level + 1);
        }
    }

    public int getIndexOfChild(Node<T> child){

        for (int i = 0, childrenSize = _children.size(); i < childrenSize; i++) {
            Node<T> tNode = _children.get(i);
            if (tNode == child)
                return i;
        }
        return -1;
    }

    public void removeChildren() {
        //Check children
        for (int i = _children.size() - 1; i >= 0; i--) {
            removeChild(i);
        }
    }

    public Node<T> removeChild(int index){
        return (index >= 0 && index < _children.size()) ? _children.remove(index) : null;
    }


    public Node<T> getCopy(boolean includeChildren){
        var copy = getCopy();

        try{
            if(includeChildren){
                for (Node<T> child : _children) {
                    copy.addChild(child.getCopy(true));
                }
            }
        }catch(Exception e){
            throw new RuntimeException("Unable to create copy");
        }

        return copy;
    }

    public void removeLeaves(){

        for (int i = _children.size() - 1; i >= 0; i--) {
            Node<T> child = _children.get(i);

            if (child instanceof TerminalNode)
                removeChild(i);
            else
                child.removeLeaves();
        }
    }

    public abstract boolean canTakeMoreChildren();

    public abstract boolean requiresTerminals(int maxDepth);

    public abstract boolean hasAncestor(Node<T> nodeToAdd);

    public abstract boolean isValid();

    public int getSize() {

        int numberOfNodes = 1;

        for (Node<T> child : _children) {
            numberOfNodes += child.getSize();
        }
        return numberOfNodes;
    }
}
