package u17112631;

import library.gpLibrary.helpers.SetupManager;
import library.gpLibrary.infrastructure.abstractClasses.GeneticAlgorithm;
import library.gpLibrary.infrastructure.implementation.TreePopulationManager;
import library.gpLibrary.infrastructure.interfaces.IPopulationManager;
import library.gpLibrary.infrastructure.interfaces.ITreeGenerator;
import library.gpLibrary.models.primitives.IFitnessFunction;
import library.gpLibrary.specialisations.ADF.ADFTreeGenerator;

public class Main {

    public static void main(String[] args) {

        SetupManager setup = new SetupManager();
        setup.readDataFile(0.7);
        setup.setupParametersgetGPParameters();

        IFitnessFunction<Double> fitnessFunction = new ADFFitnessFunction<Double>();
        ITreeGenerator<Double> treeGenerator = new ADFTreeGenerator();
        IPopulationManager<Double> populationManager = new TreePopulationManager<>(treeGenerator,fitnessFunction,SEED);

        GeneticAlgorithm<Double> geneticAlgorithm = new GeneticAlgorithm<>(POPULATION_SIZE,NUMBER_OF_GENERATIONS,populationManager);
	// write your code here
    }
}
