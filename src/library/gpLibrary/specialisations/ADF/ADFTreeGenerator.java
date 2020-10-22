package library.gpLibrary.specialisations.ADF;

import library.gpLibrary.infrastructure.abstractClasses.TreeGenerator;
import library.gpLibrary.models.highOrder.implementation.FunctionalSet;
import library.gpLibrary.models.highOrder.implementation.NodeTree;
import library.gpLibrary.models.highOrder.implementation.PopulationMember;
import library.gpLibrary.models.highOrder.implementation.TerminalSet;
import library.gpLibrary.models.primitives.nodes.abstractClasses.Node;
import library.gpLibrary.specialisations.ADF.infrastructure.ADFFuncDefinition;
import library.gpLibrary.specialisations.ADF.infrastructure.ADFMain;
import library.gpLibrary.specialisations.ADF.infrastructure.ADFunction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class ADFTreeGenerator<T> extends TreeGenerator<T> {

    private int maxFuncDepth;
    private int maxFuncBreadth;
    private int maxMainDepth;
    private int maxMainBreadth;

    public ADFTreeGenerator(FunctionalSet<T> functionalSet, TerminalSet<T> terminals){
        super(functionalSet,terminals);

    }

    public void setDepths(int maxFuncDepth,int maxFuncBreadth,int maxMainDepth,int maxMainBreadth){
        this.maxFuncDepth = maxFuncDepth;
        this.maxFuncBreadth = maxFuncBreadth;

        this.maxMainDepth = maxMainDepth;
        this.maxMainBreadth = maxMainBreadth;
    }

    @Override
    public ADFTree<T> createRandom() {
        ADFTree<T> newTree = new ADFTree<>(maxFuncDepth,maxFuncBreadth,maxMainDepth,maxMainBreadth);

        ADFFuncDefinition<T> functionDefinition = newTree.getFunctionDefinition();
        ADFMain<T> main = newTree.getMain();

        try {
            functionDefinition.addNode(pickFunction());
        }catch (Exception e){
            throw new RuntimeException("Unable to create tree root");
        }

        fillTree(functionDefinition);

        ADFunction<T> func = functionDefinition.getFunction().getCopy();
        func.removeLeaves();
        functionDefinition.setFunctionComposition();
        functionalSet.addFunction(func);
        try {
            main.addNode(pickFunction());
        }catch (Exception e){
            throw new RuntimeException("Unable to create tree root");
        }

        fillTree(main);

        newTree.useMain();

        functionalSet.removeFunction(func.name);
        return newTree;
    }

    @Override
    public NodeTree<T> create(String chromosome) {
        String[] chromosomes = chromosome.split("\\|");
        String[] joinedChromosomes = Stream.concat(Arrays.stream(chromosomes[0].split("\\.")),
                Arrays.stream(chromosomes[1].split("\\.")))
                .toArray(String[]::new);

        ADFTree<T> newTree = new ADFTree<>(maxFuncDepth,maxFuncBreadth,maxMainDepth,maxMainBreadth);

        insertChromosomeMembersIntoTree(newTree,joinedChromosomes);

        return newTree;
    }

    @Override
    public ADFTree<T> replaceSubTree(PopulationMember<T> chromosome) {
        ADFTree<T> tree = (ADFTree<T>) chromosome.getTree();

        int choice = randomGenerator.nextInt(2);

        Node<T> replacingNode = pickNode();
        if(choice == 0){
            tree.replaceNodeInFunction(randomGenerator,replacingNode);
            fillTree(tree.getFunctionDefinition());
            tree.updateMainWithNewFunctionDefinition();
        }else{
            tree.replaceNodeInMain(randomGenerator,replacingNode);
        }

        if(!tree.isValid())
            throw new RuntimeException("Created invalid tree");

        return tree;
    }

    @Override
    public List<NodeTree<T>> swapSubTrees(PopulationMember<T> first, PopulationMember<T> second) {
        //TODO: make it work
        ADFTree<T> firstMember = (ADFTree<T>) first.getTree();
        NodeTree<T> firstTree;

        ADFTree<T> secondMember = (ADFTree<T>) second.getTree();
        NodeTree<T> secondTree;

        int choice = randomGenerator.nextInt(2);

        if(choice == 0){
            firstTree = firstMember.getFunctionDefinition();
            secondTree = secondMember.getFunctionDefinition();

        }else{
            firstTree = firstMember.getMain();
            secondTree = secondMember.getMain();
        }

        int pointToReplaceInFirst = randomGenerator.nextInt(firstTree.getSize() - 1) + 1;
        int pointToReplaceInSecond = randomGenerator.nextInt(secondTree.getSize() - 1) + 1;

        Node<T> firstSubtree = firstTree.getNode(pointToReplaceInFirst).getCopy(true);
        Node<T> secondSubTree = secondTree.getNode(pointToReplaceInSecond).getCopy(true);


        firstTree.replaceNode(pointToReplaceInFirst,secondSubTree);
        secondTree.replaceNode(pointToReplaceInSecond,firstSubtree);

        if(!firstTree.isValid()){
            var node = firstTree.getNode(pointToReplaceInFirst);
            throw new RuntimeException("Invalid");
        }

        if(!secondTree.isValid()){
            throw new RuntimeException("Invalid");
        }
        List<NodeTree<T>> newTrees = new ArrayList<>();
        newTrees.add(firstTree);
        newTrees.add(secondTree);

        return newTrees;
    }

}
