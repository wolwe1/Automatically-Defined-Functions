package library.gpLibrary.infrastructure.implementation.operators;

import library.gpLibrary.infrastructure.abstractClasses.GeneticOperator;
import library.gpLibrary.infrastructure.interfaces.ITreeGenerator;
import library.gpLibrary.models.highOrder.implementation.NodeTree;
import library.gpLibrary.models.highOrder.implementation.PopulationMember;

import java.util.ArrayList;
import java.util.List;

public class Create<T> extends GeneticOperator<T> {

    private final ITreeGenerator<T> generator;

    protected Create(int populationSize,int outputCount,ITreeGenerator<T> generator,double percentageOfPopulation) {
        super("Create",0,outputCount,populationSize,percentageOfPopulation);
        this.populationSize = populationSize;
        this.outputCount = outputCount;
        this.generator = generator;
    }

    public static <T> Create<T> create(int populationSize, double percentageOfPopulation, ITreeGenerator<T> generator){
        int outputCount = (int) (percentageOfPopulation * populationSize);
        return new Create<>(populationSize,outputCount,generator,percentageOfPopulation);
    }

    @Override
    public List<NodeTree<T>> operate(List<PopulationMember<T>> chromosomes) {

        List<NodeTree<T>> newChromosomes = new ArrayList<>();

        for (int i = 0; i < outputCount; i++) {
            NodeTree<T> tree = generator.createRandom();
            newChromosomes.add(tree);
        }

        return newChromosomes;
    }
}
