package u17112631;

import library.gpLibrary.infrastructure.abstractClasses.GeneticAlgorithm;
import library.gpLibrary.infrastructure.implementation.TreePopulationManager;
import library.gpLibrary.infrastructure.interfaces.IPopulationManager;
import library.gpLibrary.infrastructure.interfaces.ITreeGenerator;
import library.gpLibrary.models.primitives.IFitnessFunction;

public class Main {

    public static void main(String[] args) {
        int POPULATION_SIZE = 1;
        int NUMBER_OF_GENERATIONS = 1;
        long SEED = 1;

        IFitnessFunction<Double> fitnessFunction = new ADFFitnessFunction<Double>();
        ITreeGenerator<Double> treeGenerator = new ADFTreeGenerator<Double>();
        IPopulationManager<Double> populationManager = new TreePopulationManager<>(treeGenerator,fitnessFunction,SEED);

        GeneticAlgorithm<Double> geneticAlgorithm = new GeneticAlgorithm<>(POPULATION_SIZE,NUMBER_OF_GENERATIONS,populationManager);
	// write your code here
    }
}
