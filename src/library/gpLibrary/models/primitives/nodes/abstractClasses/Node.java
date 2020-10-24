package library.gpLibrary.models.primitives.nodes.abstractClasses;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a node that can have multiple children and a parent
 * @param <T> The type of the value stored by the node
 */
public abstract class Node<T>
{
    protected final List<Node<T>> children;

    public Node<T> parent;
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
        children = new ArrayList<>();
    }

    protected Node(String name)
    {
        this.name = name;
        children = new ArrayList<>();
    }

    protected Node(Node<T> other){
        _maxChildren  = other._maxChildren;
        _depth = other._depth;
        _drawPos = other._drawPos;
        _level = other._level;

        children = new ArrayList<>();
        name = other.name;
    }

    public Node<T> addChild(Node<T> newNode) throws Exception {

        if (isFull()) throw new Exception("Node cannot have any more children");

        newNode.parent = this;
        newNode.setLevel(_level + 1);
        newNode.index = children.size();
        children.add(newNode);

        return newNode;
    }

    public List<Node<T>> getChildren(){ return children; }

    public Node<T> getChild(int index){
        if (index >= children.size())
            return null;

        return children.get(index);
    }

    public abstract boolean isFull();

    protected abstract Node<T> getCopy();

    public void setChild(int index,Node<T> newChild){
        if(index < 0 || index >= _maxChildren)
            throw new RuntimeException("Attempted to set a child out of range");

        newChild.parent = this;
        newChild.index = index;
        newChild.setLevel(this._level + 1);
        children.set(index,newChild);
    }

    public void setLevel(int level) {
        this._level = level;

        for (Node<T> child : children) {
            child.setLevel(level + 1);
        }
    }

    public int getIndexOfChild(Node<T> child){

        for (int i = 0, childrenSize = children.size(); i < childrenSize; i++) {
            Node<T> tNode = children.get(i);
            if (tNode == child)
                return i;
        }
        return -1;
    }

    public void removeChildren() {
        //Check children
        for (int i = children.size() - 1; i >= 0; i--) {
            removeChild(i);
        }
    }

    public Node<T> removeChild(int index){
        if(index < 0 || index >= children.size()) throw new RuntimeException("Cannot remove child at this index");

        return children.remove(index);
    }


    public Node<T> getCopy(boolean includeChildren){
        var copy = getCopy();

        try{
            if(includeChildren){
                for (Node<T> child : children) {
                    copy.addChild(child.getCopy(true));
                }
            }
        }catch(Exception e){
            throw new RuntimeException("Unable to create copy");
        }

        return copy;
    }

    public void removeLeaves(){

        for (int i = children.size() - 1; i >= 0; i--) {
            Node<T> child = children.get(i);

            if (child._maxChildren == 0)
                removeChild(i);
            else
                child.removeLeaves();
        }
    }

    public abstract boolean canTakeMoreChildren();

    public abstract boolean requiresTerminals(int maxDepth);

    public boolean hasAncestor(Node<T> nodeToAdd) {

        if(nodeToAdd.name.equals(name)) return true;

        if(this.parent == null) return false;

        return this.parent.hasAncestor(nodeToAdd);
    }

    public abstract boolean isValid();

    public int getSize() {

        int numberOfNodes = 1;

        for (Node<T> child : children) {
            numberOfNodes += child.getSize();
        }
        return numberOfNodes;
    }

    public abstract int countLeaves();

    public abstract void setChildAtFirstTerminal(Node<T> nodeInMain);

    public abstract void cutNodes(int i);

}
