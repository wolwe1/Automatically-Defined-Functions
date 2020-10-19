package library.gpLibrary.specialisations.ADF;

import library.gpLibrary.infrastructure.abstractClasses.TreeGenerator;
import library.gpLibrary.models.highOrder.implementation.FunctionalSet;
import library.gpLibrary.models.highOrder.implementation.NodeTree;
import library.gpLibrary.models.highOrder.implementation.PopulationMember;
import library.gpLibrary.models.highOrder.implementation.TerminalSet;
import library.gpLibrary.specialisations.ADF.infrastructure.ADFFuncDefinition;
import library.gpLibrary.specialisations.ADF.infrastructure.ADFMain;
import library.gpLibrary.specialisations.ADF.infrastructure.ADFunction;

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
            functionDefinition.addNode(pickFunction()); //TODO: Ensure this sets function children
        }catch (Exception e){
            throw new RuntimeException("Unable to create tree root");
        }

        fillTree(functionDefinition);

        ADFunction<T> func = functionDefinition.getFunction().getCopy();
        func.removeLeaves();
        functionalSet.addFunction(func);
        try {
            main.addNode(pickFunction());
        }catch (Exception e){
            throw new RuntimeException("Unable to create tree root");
        }

        fillTree(main);

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
        return null;
    }

    @Override
    public List<NodeTree<T>> replaceSubTrees(PopulationMember<T> first, PopulationMember<T> second) {
        return null;
    }

}
