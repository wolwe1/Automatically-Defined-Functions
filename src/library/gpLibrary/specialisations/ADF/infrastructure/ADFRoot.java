package library.gpLibrary.specialisations.ADF.infrastructure;

import library.gpLibrary.functionality.implementation.BreadthFirstVisitor;
import library.gpLibrary.functionality.implementation.DepthFirstVisitor;
import library.gpLibrary.functionality.interfaces.ITreeVisitor;
import library.gpLibrary.models.highOrder.implementation.NodeTree;
import library.gpLibrary.models.primitives.nodes.abstractClasses.Node;
import library.gpLibrary.specialisations.ADF.FunctionFinder;

import java.util.ArrayList;
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
        maxNodes = definition.getMaxNodes() + main.getMaxNodes();
    }

    public ADFRoot(ADFRoot<T> other) {
        super(other.maxDepth,other.maxBreadth);

        definition = other.definition.getCopy();
        main = (ADFMain<T>) other.main.getCopy();
        isDoingFuncInsert = other.isDoingFuncInsert;
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

        return definition.requiresTerminals() || main.requiresTerminals();
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

    @Override
    public void cutTree(int maxDepthIncrease) {
        definition.cutTree(maxDepthIncrease);
        main.cutTree(maxDepthIncrease);
    }

    public ADFFuncDefinition<T> getFunctionDefinition() {
        return definition;
    }

    public  ADFMain<T> getMain() {
        return main;
    }

    @Override
    public int getMaximumNumberOfPossibleLeafNodes() {
        return main.getMaximumNumberOfPossibleLeafNodes();
    }

    @Override
    public int getNumberOfLeafNodes(){
        return main.getNumberOfLeafNodes();
    }

    public void removeLeaves() {
        main.clearLeaves();
        numberOfNodes = definition.getSize() + main.getSize();
    }

    public void useMain(){
        isDoingFuncInsert = false;
    }

    /**
     * Replaces all function usages in main with that of the currently set function
     */
    public void updateMainWithNewFunctionDefinition() {
        ADFunction<T> newFunction = definition.getFunction();
        List<ADFunction<T>> usagesOfFunctionInMain = getFunctionsInMain();

        for (int i = usagesOfFunctionInMain.size() - 1; i >= 0; i--) {
            ADFunction<T> funcInMain = usagesOfFunctionInMain.get(i);
            updateFunction(newFunction,funcInMain);
        }
    }

    private void updateFunction(ADFunction<T> newFunction, ADFunction<T> funcInMain) {

        List<? extends Node<T>> newFunctionComposition = getDepthFirstComposition(newFunction);
        List<? extends Node<T>> mainFunctionComposition = getDepthFirstComposition(funcInMain);

        int indexInMain = 0;
        for (int i = 0; i < newFunctionComposition.size(); i++) {

            if(indexInMain >= mainFunctionComposition.size()) return;

            Node<T> newNode = newFunctionComposition.get(i);
            Node<T>  nodeInMain = mainFunctionComposition.get(indexInMain);

            //If nodes are not equal then we must make a change to main function
            if(!newNode.name.equals(nodeInMain.name)){

                //New function changes this branch root, set new root and add old root as first child

                if(!newNode.name.equals("Empty")){
                    nodeInMain.parent.setChild(nodeInMain.index,newNode.getCopy(true));

                    i += newNode.getSize() - 1;
                    //New function changes branch root but is non terminal,
                    // need to propagate the add to first terminal
                    newNode.setChildAtFirstTerminal(nodeInMain.getCopy(true));

                }
                indexInMain += nodeInMain.getSize() - 1;

            }else{
                indexInMain++;
            }
        }

    }

    private List<? extends Node<T>> getDepthFirstComposition(ADFunction<T> tree) {

        DepthFirstVisitor<T> depthFirstVisitor = new DepthFirstVisitor<>();
        depthFirstVisitor.visit(tree.root);

        List<Node<T>> nodesExcludingFunctions = new ArrayList<>();
        for (Node<T> node : depthFirstVisitor.getNodes()) {
            if(!(node instanceof ADFunction))
                nodesExcludingFunctions.add(node);
        }
        //return depthFirstVisitor.getNodes();
        return nodesExcludingFunctions;
    }

    /**
     * Finds and returns all functions in main
     * @return Functions in main
     */
    private List<ADFunction<T>> getFunctionsInMain() {

        FunctionFinder<T> functionFinder = new FunctionFinder<>();
        main.visitTree(functionFinder);

        return functionFinder.getNodes();
    }

    /**
     * Determines whether the node index lies within a function
     * @param pointToReplace The node index in main
     * @return Whether the point is within a function definition
     */
    public boolean pointLiesWithinFunction(int pointToReplace) {
        List<? extends Node<T>> nodesInMain = getMainComposition();
        int sizeOfFunction = getFunctionDefinition().getFunction().getSize();

        for (int i = 0; i < nodesInMain.size(); i++) {

            Node<T> nodeInMain = nodesInMain.get(i);
            if(nodeInMain instanceof ADFunction)
                i += sizeOfFunction - 1;
            else{
                if(i == pointToReplace)
                    return false;
            }

            if(i > pointToReplace)
                return true;

        }
        return true;
    }

    /**
     * Returns the BFS order of nodes in main
     * @return BFS order of mains nodes
     */
    private List<? extends Node<T>> getMainComposition() {
        ITreeVisitor<T> visitor = new BreadthFirstVisitor<>();

        main.visitTree(visitor);
        return visitor.getNodes();

    }

    /**
     * Returns the BFS order of nodes in the function
     * @param function The function to get the composition of
     * @return The BFS order of nodes in the function
     */
    private List<? extends Node<T>> getFunctionComposition(ADFunction<T> function){
        ITreeVisitor<T> visitor = new BreadthFirstVisitor<>();

        //Create a wrapper around the functions to allow BFS
        ADFFuncDefinition<T> functionWrapper = new ADFFuncDefinition<>(10,2);

        //Get the BFS order of nodes in the new function
        functionWrapper.function = function;
        functionWrapper.visitTree(visitor);

        return visitor.getNodes();
    }
}
