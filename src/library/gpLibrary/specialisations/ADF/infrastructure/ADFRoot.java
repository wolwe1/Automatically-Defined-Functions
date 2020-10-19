package library.gpLibrary.specialisations.ADF.infrastructure;

import library.gpLibrary.models.highOrder.implementation.NodeTree;
import library.gpLibrary.models.primitives.nodes.abstractClasses.Node;

import java.util.List;

public class ADFRoot<T> extends NodeTree<T> {

    private final ADFFuncDefinition<T> definition;
    private final ADFMain<T> main;
    private boolean isDoingFuncInsert = true;

    public ADFRoot(int maxDepth, int maxBreadth, int maxMainDepth, int maxMainBreadth) {
        super();

        definition = new ADFFuncDefinition<>(maxDepth,maxBreadth);
        main = new ADFMain<>(maxMainDepth,maxMainBreadth);
        //Need to handle breadth and width calculations manually
        _maxNodes = definition.getMaxNodes() + main.getMaxNodes();
    }

    public ADFRoot(ADFRoot<T> other) {
        super(other.maxDepth,other.maxBreadth);

        definition = (ADFFuncDefinition<T>) other.definition.getCopy();
        main = (ADFMain<T>) other.main.getCopy();
    }


    @Override
    public int getSize() {
        return definition.getSize() + main.getSize();
    }

    @Override
    public void addNode(Node<T> node) throws Exception {

        if(isDoingFuncInsert){
            definition.addNode(node);
            if(definition.isFull())
                isDoingFuncInsert = false;
        }else{
            main.addNode(node);
        }

        numberOfNodes++;
    }

    @Override
    protected void setRoot(Node<T> node) {
        throw new RuntimeException("Set root not defined on ADF root");
    }


    @Override
    public void clearLeaves() {
        definition.clearLeaves();
        main.clearLeaves();
    }

    @Override
    public NodeTree<T> getCopy() {
        return new ADFRoot<>(this);
    }

    @Override
    public boolean isFull() {
        return definition.isFull() && main.isFull();
    }

    @Override
    public boolean requiresTerminals() {
        if(isDoingFuncInsert)
            return definition.requiresTerminals();
        else
            return main. requiresTerminals();
    }

    @Override
    public boolean isValid() {
        return definition.isValid() && main.isValid();
    }

    @Override
    public Node<T> getRoot() {
        return main.getRoot();
    }

    @Override
    public boolean acceptsNode(Node<T> nodeToAdd) {

        if(isDoingFuncInsert)
            return definition.acceptsNode(nodeToAdd);
        else
            return main.acceptsNode(nodeToAdd);
    }

    @Override
    public void addNodes(List<? extends Node<T>> nodesToLoad) {

        if(isDoingFuncInsert)
            definition.addNodes(nodesToLoad);
        else
            main.addNodes(nodesToLoad);
    }

    public ADFFuncDefinition<T> getFunction() {
        return definition;
    }

    public  ADFMain<T> getMain() {
        return main;
    }

    @Override
    public int getNumberOfPossibleLeafNodes() {
        return main.getNumberOfPossibleLeafNodes();
    }

    public void removeLeaves() {
        main.clearLeaves();
        numberOfNodes = definition.getSize() + main.getSize();
    }
}
