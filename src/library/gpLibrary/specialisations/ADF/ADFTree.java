package library.gpLibrary.specialisations.ADF;

import library.gpLibrary.functionality.implementation.TreeCombinationVisitor;
import library.gpLibrary.models.highOrder.implementation.NodeTree;
import library.gpLibrary.models.primitives.nodes.abstractClasses.Node;
import library.gpLibrary.specialisations.ADF.infrastructure.ADFFuncDefinition;
import library.gpLibrary.specialisations.ADF.infrastructure.ADFMain;
import library.gpLibrary.specialisations.ADF.infrastructure.ADFRoot;
import library.gpLibrary.specialisations.ADF.infrastructure.ADFunction;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class ADFTree<T> extends NodeTree<T> {

    private final ADFRoot<T> root;

    public ADFTree(int maxFuncDepth, int maxFuncBreadth,int maxMainDepth, int maxMainBreadth) {
        super();

        root = new ADFRoot<>(maxFuncDepth,maxFuncBreadth,maxMainDepth,maxMainBreadth);
        //Must handle breadth and depth calculations manually
        maxNodes = root.getMaxNodes();
    }

    public ADFTree(ADFTree<T> other) {
        super(other);

        root = new ADFRoot<>(other.getADFRoot());
        maxNodes = root.getMaxNodes();
    }

    private ADFRoot<T> getADFRoot() {
        return root;
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
    public void cutTree(int maxDepthIncrease) {
        root.cutTree(maxDepthIncrease);
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
        ADFFuncDefinition<T> functionDefinition = root.getFunctionDefinition().getCopy();
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
        return root.getFunctionDefinition();
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

    /**
     * Selects a random node in the function definition and replaces it, updating mains references
     * @param randomGenerator The generator for selecting the point to replace
     */
    public void replaceNodeInFunction(Random randomGenerator, Node<T> replacingNode) {
        ADFFuncDefinition<T> functionDefinition = getFunctionDefinition();

        int pointToReplace = randomGenerator.nextInt(functionDefinition.getSize() - 1) + 1;

        functionDefinition.replaceNode(pointToReplace,replacingNode);

    }

    public void replaceNodeInMain(Random randomGenerator, Node<T> replacingNode) {
        ADFMain<T> main = getMain();
        int pointToReplace = getPointInMainNotFunction(randomGenerator);

        if(pointToReplace != -1)
            main.replaceNode(pointToReplace,replacingNode);
    }

    public int getPointInMainNotFunction(Random randomGenerator){

        ADFMain<T> main = getMain();
        int pointToReplace = randomGenerator.nextInt(main.getSize() - 1) + 1;

        Set<Integer> usedPoints = new HashSet<>();
        usedPoints.add(pointToReplace);

        while (usedPoints.contains(pointToReplace) || root.pointLiesWithinFunction(pointToReplace)){
            pointToReplace = randomGenerator.nextInt(main.getSize() - 1) + 1;
            usedPoints.add(pointToReplace);

            if(usedPoints.size() == main.getSize() - 1)
                return - 1;
        }

        return pointToReplace;
    }

    public void updateMainWithNewFunctionDefinition(){
        root.updateMainWithNewFunctionDefinition();
    }
}
