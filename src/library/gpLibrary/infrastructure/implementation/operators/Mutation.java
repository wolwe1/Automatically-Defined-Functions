package library.gpLibrary.infrastructure.implementation.operators;

import library.gpLibrary.infrastructure.abstractClasses.GeneticOperator;
import library.gpLibrary.infrastructure.interfaces.ITreeGenerator;
import library.gpLibrary.models.highOrder.implementation.NodeTree;
import library.gpLibrary.models.highOrder.implementation.PopulationMember;

import java.util.ArrayList;
import java.util.List;

public class Mutation<T> extends GeneticOperator<T> {

    private final ITreeGenerator<T> generator;

    protected Mutation(int inputCount, int populationSize, ITreeGenerator<T> generator,double percentageOfPopulation) {
        super("Mutation", inputCount, inputCount, populationSize,percentageOfPopulation);
        this.generator = generator;
    }

    public static <T> Mutation<T> create(int populationSize, double percentageOfThePopulation, ITreeGenerator<T> generator){
        int inputCount = (int)(populationSize * percentageOfThePopulation);
        return new Mutation<>(inputCount,populationSize,generator,percentageOfThePopulation);
    }

    @Override
    public List<NodeTree<T>> operate(List<PopulationMember<T>> chromosomes) {

        List<NodeTree<T>> mutatedChromosomes = new ArrayList<>();

        for (PopulationMember<T> chromosome : chromosomes) {
            NodeTree<T> newTree = generator.replaceSubTree(chromosome);

            mutatedChromosomes.add(newTree);
        }

        for (NodeTree<T> mutatedChromosome : mutatedChromosomes) {
            if(!mutatedChromosome.isValid()){
                throw new RuntimeException("Invalid tree");
            }
        }

        return mutatedChromosomes;
    }
}
