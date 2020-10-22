package library.gpLibrary.infrastructure.abstractClasses;

import library.gpLibrary.infrastructure.interfaces.ITreeGenerator;
import library.gpLibrary.models.highOrder.implementation.FunctionalSet;
import library.gpLibrary.models.highOrder.implementation.NodeTree;
import library.gpLibrary.models.highOrder.implementation.TerminalSet;
import library.gpLibrary.models.primitives.nodes.abstractClasses.Node;

import java.util.Random;

public abstract class TreeGenerator<T> implements ITreeGenerator<T> {

    protected final FunctionalSet<T> functionalSet;
    protected final TerminalSet<T> terminals;
    protected Random randomGenerator;
    protected int terminalRatio;

    public TreeGenerator(FunctionalSet<T> functionalSet, TerminalSet<T> terminals) {
        randomGenerator = new Random(0);
        this.functionalSet = functionalSet;
        this.terminals = terminals;
        terminalRatio = 20;
    }

    @Override
    public void setRandomFunction(Random randomNumberGenerator) {
        randomGenerator = randomNumberGenerator;
    }

    /**
     * Randomly chooses to pick a terminal or function node based on the terminal ratio
     * @return A terminal or function node
     */
    protected Node<T> pickNode() {
        int choice = randomGenerator.nextInt(100);

        if(choice < terminalRatio)
            return pickTerminal();
        else
            return pickFunction();
    }

    /**
     * Selects a random function from the function set
     * @return A random function
     */
    protected Node<T> pickFunction() {
        int functionIndex = randomGenerator.nextInt(functionalSet.size());
        return functionalSet.get(functionIndex);
    }

    /**
     * Selects a random terminal from the terminal set
     * @return A random terminal node
     */
    protected Node<T> pickTerminal() {
        int terminalIndex = randomGenerator.nextInt(terminals.size());
        return terminals.get(terminalIndex);
    }

    /**
     * A whole number between 0-100 representing the percentage chance of a node being selected as a terminal
     * @param percentage The percentage chance of a terminal being picked
     */
    public void setTerminalToFunctionRatio(int percentage){
        this.terminalRatio = percentage;
    }

    @Override
    public NodeTree<T> fillTree(NodeTree<T> tree) {

        while (!tree.isFull()) {
            try {
                if(tree.requiresTerminals())
                    tree.addNode(pickTerminal());
                else{
                    Node<T> nodeToAdd = pickNode();

                    while(!tree.acceptsNode(nodeToAdd)){
                        nodeToAdd = pickNode();
                    }
                    tree.addNode(nodeToAdd);
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Unable to generate tree");
            }
        }

        if(!tree.isValid())
            throw new RuntimeException("Invalid tree created");

        return tree;
    }

    protected void insertChromosomeMembersIntoTree(NodeTree<T> newTree, String[] chromosomeItems) {
        for (String name : chromosomeItems) {
            try {
                var function = functionalSet.get(name);
                newTree.addNode(function);

            } catch (Exception e) { //Chromosome item represents a terminal

                try {
                    var terminal = terminals.get(name);
                    newTree.addNode(terminal);

                } catch (Exception exception) {
                    exception.printStackTrace();
                    throw new RuntimeException("Unable to generate tree from chromosome");
                }
            }
        }
    }

    @Override
    public void setSeed(long seed) {
        this.randomGenerator.setSeed(seed);
    }
}
