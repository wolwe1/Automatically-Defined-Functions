package library.gpLibrary.specialisations.ADF;

import library.gpLibrary.functionality.implementation.TreeCombinationVisitor;
import library.gpLibrary.models.highOrder.implementation.NodeTree;
import library.gpLibrary.models.primitives.nodes.abstractClasses.Node;
import library.gpLibrary.specialisations.ADF.infrastructure.ADFFuncDefinition;
import library.gpLibrary.specialisations.ADF.infrastructure.ADFMain;
import library.gpLibrary.specialisations.ADF.infrastructure.ADFRoot;
import library.gpLibrary.specialisations.ADF.infrastructure.ADFunction;

import java.util.List;

public class ADFTree<T> extends NodeTree<T> {

    private ADFRoot<T> root;

    public ADFTree(int maxFuncDepth, int maxFuncBreadth,int maxMainDepth, int maxMainBreadth) {
        super();

        root = new ADFRoot<>(maxFuncDepth,maxFuncBreadth,maxMainDepth,maxMainBreadth);
        //Must handle breadth and depth calculations manually
        _maxNodes = root.getMaxNodes();
    }

    public ADFTree(NodeTree<T> other) {
        super(other);
    }

    @Override
    public void addNode(Node<T> newNode) throws Exception {
        if (isFull())
            throw new Exception("Tree full");

        root.addNode(newNode);
        numberOfNodes++;
    }

    @Override
    protected void setRoot(Node<T> node) {
        throw new RuntimeException("No reason to use set root on ADF Tree");
    }

    @Override
    public NodeTree<T> getCopy() {
        return new ADFTree<>(this);
    }

    @Override
    public boolean isFull() {
        return root.isFull();
    }

    @Override
    public boolean requiresTerminals() {
        return root.requiresTerminals();
    }

    @Override
    public Node<T> getRoot() {
        return root.getRoot();
    }

    @Override
    public boolean acceptsNode(Node<T> nodeToAdd) {
        return true;
    }

    @Override
    public void addNodes(List<? extends Node<T>> nodesToLoad) {
        root.addNodes(nodesToLoad);
    }

    @Override
    public boolean isValid(){
        return root.isValid();
    }

    @Override
    public String getCombination() {

        //Get the nodes in breadth first order
        TreeCombinationVisitor<T> visitor = new TreeCombinationVisitor<>();

        String combo = "[Func].%1$s[Main].%2$s";
        String functionStr;
        String main;

        //Get function combination
        ADFFuncDefinition<T> functionDefinition = root.getFunction().getCopy();
        functionDefinition.clearLeaves();
        functionDefinition.visitTree(visitor);
        functionStr = visitor.getCombination();
        //functionStr.replace("Empty.","T");
        visitor.clear();

        //Get MAIN combination
        var function = functionDefinition.getFunction();
        String funcHeader = function.name + "." + functionStr;
        root.getMain().visitTree(visitor);
        main = visitor.getCombination();
        main = main.replace(funcHeader,function.name + ".");
        //main = main.replace("Empty.","T");

        return String.format(combo,functionStr,main);
    }

    @Override
    public int getMaximumNumberOfPossibleLeafNodes() {
        return root.getNumberOfLeafNodes();
    }

    @Override
    public void clearLeaves() {
        root.removeLeaves();
    }

    @Override
    public int getSize(){
        return root.getSize();
    }

    public ADFFuncDefinition<T> getFunctionDefinition() {
        return root.getFunction();
    }

    public ADFMain<T> getMain(){
        return root.getMain();
    }

    public ADFunction<T> getFunction() {
        return getFunctionDefinition().getFunction();
    }

    public void useMain() {
        root.useMain();
    }
}
