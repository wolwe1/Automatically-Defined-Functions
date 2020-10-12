package library.gpLibrary.infrastructure.implementation.operators;

import library.gpLibrary.infrastructure.abstractClasses.GeneticOperator;
import library.gpLibrary.infrastructure.interfaces.ITreeGenerator;
import library.gpLibrary.models.highOrder.implementation.NodeTree;
import library.gpLibrary.models.highOrder.implementation.PopulationMember;

import java.util.ArrayList;
import java.util.List;

public class Crossover<T> extends GeneticOperator<T> {

    private final ITreeGenerator<T> generator;

    protected Crossover(int inputCount, int populationSize, ITreeGenerator<T> generator) {
        super("Crossover", inputCount, inputCount, populationSize);
        this.generator = generator;
    }

    public static <T> Crossover<T> create(int populationSize, double percentageOfPopulation, ITreeGenerator<T> generator){

        int inputCount = (int)(populationSize*percentageOfPopulation);

        //Ensure boundary
        while(inputCount % 2 != 0){
            inputCount--;
        }

        return new Crossover<>(inputCount,populationSize,generator);
    }

    @Override
    public List<NodeTree<T>> operate(List<PopulationMember<T>> chromosomes) {

        List<NodeTree<T>> newTrees = new ArrayList<>();

        for (int i = 0; i < chromosomes.size(); i++) {
            PopulationMember<T> first = chromosomes.get(i++);
            PopulationMember<T> second = chromosomes.get(i);
            
            List<NodeTree<T>> crossoverTrees = generator.replaceSubTrees(first,second);
            newTrees.addAll(crossoverTrees);
        }
        return newTrees;
    }
}
