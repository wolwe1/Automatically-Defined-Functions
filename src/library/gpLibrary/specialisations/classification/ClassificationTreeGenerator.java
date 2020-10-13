package library.gpLibrary.specialisations.classification;

import library.gpLibrary.infrastructure.abstractClasses.TreeGenerator;
import library.gpLibrary.models.highOrder.implementation.FunctionalSet;
import library.gpLibrary.models.highOrder.implementation.NodeTree;
import library.gpLibrary.models.highOrder.implementation.PopulationMember;
import library.gpLibrary.models.highOrder.implementation.TerminalSet;
import library.gpLibrary.models.primitives.nodes.abstractClasses.Node;

import java.util.ArrayList;
import java.util.List;

public class ClassificationTreeGenerator<T> extends TreeGenerator<T> {

    private final int maxTreeDepth;
    private final int maxTreeBreadth;

    public ClassificationTreeGenerator(FunctionalSet<T> functionalSet,TerminalSet<T> terminals,int maxTreeDepth, int maxTreeBreadth){
        super(functionalSet, terminals);

        this.maxTreeDepth = maxTreeDepth;
        this.maxTreeBreadth = maxTreeBreadth;
    }

    @Override
    public NodeTree<T> createRandom() {
        ClassificationTree<T> newTree = new ClassificationTree<>(maxTreeDepth,maxTreeBreadth);
        try {
            newTree.addNode(pickFunction());
        } catch (Exception e) {
            throw new RuntimeException("Unable to create tree root");
        }
        //TODO: Ensure branch uniqueness
        return fillTree(newTree);
    }

    @Override
    public NodeTree<T> create(String chromosome) {
        NodeTree<T> newTree = new ClassificationTree<>(maxTreeDepth,maxTreeBreadth);
        String[] chromosomeItems = chromosome.split("\\.");

        insertChromosomeMembersIntoTree(newTree, chromosomeItems);

        return newTree;
    }

    @Override
    public NodeTree<T> replaceSubTree(PopulationMember<T> chromosome) {
        ClassificationTree<T> tree = (ClassificationTree<T>) chromosome.getTree();
        int pointToReplace = randomGenerator.nextInt(tree.getSize() - 1) + 1;

        Node<T> replacingNode = pickNode();

        tree.replaceNode(pointToReplace,replacingNode);
        tree = (ClassificationTree<T>) fillTree(tree);

        if(!tree.isValid())
            throw new RuntimeException("Created invalid tree");

        return tree;
    }

    @Override
    public List<NodeTree<T>> replaceSubTrees(PopulationMember<T> first, PopulationMember<T> second) {
        NodeTree<T> firstTree = first.getTree();
        NodeTree<T> secondTree = second.getTree();

        int pointToReplaceInFirst = randomGenerator.nextInt(firstTree.getSize() - 1) + 1;
        int pointToReplaceInSecond = randomGenerator.nextInt(secondTree.getSize() - 1) + 1;

        Node<T> firstSubtree = firstTree.getNode(pointToReplaceInFirst).getCopy(true);
        Node<T> secondSubTree = secondTree.getNode(pointToReplaceInSecond).getCopy(true);


        firstTree.replaceNode(pointToReplaceInFirst,secondSubTree);
        secondTree.replaceNode(pointToReplaceInSecond,firstSubtree);
        //TODO: Trees arent updated correctly
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
