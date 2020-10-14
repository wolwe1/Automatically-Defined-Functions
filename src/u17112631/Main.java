package u17112631;

import library.gpLibrary.helpers.RunInfo;
import library.gpLibrary.helpers.SetupManager;
import library.gpLibrary.infrastructure.abstractClasses.GeneticAlgorithm;
import library.gpLibrary.infrastructure.implementation.TreePopulationManager;
import library.gpLibrary.infrastructure.implementation.operators.Crossover;
import library.gpLibrary.infrastructure.implementation.operators.LazyReproduction;
import library.gpLibrary.infrastructure.implementation.operators.Mutation;
import library.gpLibrary.infrastructure.interfaces.ITreeGenerator;
import library.gpLibrary.models.highOrder.implementation.FunctionalSet;
import library.gpLibrary.models.highOrder.implementation.TerminalSet;
import library.gpLibrary.models.primitives.IFitnessFunction;
import library.gpLibrary.specialisations.ADF.ADFTreeGenerator;
import u17112631.functions.covid.primitives.AddFunction;
import u17112631.functions.covid.primitives.DivisionFunction;
import u17112631.functions.covid.primitives.MultiplicationFunction;
import u17112631.functions.covid.primitives.SubtractFunction;
import u17112631.functions.patientClassification.PatientResult;
import u17112631.functions.patientClassification.PatientVitalsFunction;
import u17112631.functions.patientClassification.PatientVitalsFunctionSingleValue;
import u17112631.helpers.covid.CovidEntry;

import java.util.Arrays;

public class Main {
    static String dataNames = "L-CORE,L-SURF,L-O2,L-BP,SURF-STBL,CORE-STBL,BP-STBL,COMFORT,ADM-DECS";

    public static void main(String[] args) {
        long SEED = 1;
        SetupManager setupInfo = new SetupManager();
        GeneticAlgorithm<Double> geneticAlgorithm = setupGeneticAlgorithm(SEED,setupInfo);

        for (int i = 0; i < setupInfo.getInfo().getNumberOfRuns(); i++) {
            geneticAlgorithm.setSeed(i);
            geneticAlgorithm.run();
        }
	// write your code here
    }

    private static <T> GeneticAlgorithm<T> setupGeneticAlgorithm(long SEED,SetupManager setup) {
        SetupManager setupInfo = doSetup(setup);
        RunInfo info = setupInfo.getInfo();

        if(setupInfo.getInfo().getRuntType().equals("COVID")){
            IFitnessFunction<Double> fitnessFunction = setup.createCovidFitnessFunction(new CovidEntry(),2);
            ITreeGenerator<Double> treeGenerator = getCovidTreeGenerator();
            TreePopulationManager<Double> populationManager = new TreePopulationManager<>(treeGenerator,fitnessFunction, SEED);
            GeneticAlgorithm<Double> geneticAlgorithm = new GeneticAlgorithm<>(info.getPopulationSize(),info.getNumberOfGenerations(),populationManager);

            geneticAlgorithm.addOperator(Crossover.create(info.getPopulationSize(),info.getCrossoverRate(),treeGenerator));
            geneticAlgorithm.addOperator(Mutation.create(info.getPopulationSize(),info.getMutationRate(),treeGenerator));
            geneticAlgorithm.addOperator( LazyReproduction.create(2,info.getPopulationSize(),info.getReproductionRate(),fitnessFunction));
            geneticAlgorithm.setPrintLevel(info.getDisplayType());

            return (GeneticAlgorithm<T>) geneticAlgorithm;

        }else{
            IFitnessFunction<String> fitnessFunction = setup.createClassifierFitnessFunction("ADM-DECS",dataNames);
            ITreeGenerator<String> treeGenerator = getPatientTreeGenerator();
            TreePopulationManager<String> populationManager = new TreePopulationManager<>(treeGenerator,fitnessFunction, SEED);
            GeneticAlgorithm<String> geneticAlgorithm = new GeneticAlgorithm<>(info.getPopulationSize(),info.getNumberOfGenerations(),populationManager);

            geneticAlgorithm.addOperator(Crossover.create(info.getPopulationSize(),info.getCrossoverRate(),treeGenerator));
            geneticAlgorithm.addOperator(Mutation.create(info.getPopulationSize(),info.getMutationRate(),treeGenerator));
            geneticAlgorithm.addOperator( LazyReproduction.create(2,info.getPopulationSize(),info.getReproductionRate(),fitnessFunction));
            geneticAlgorithm.setPrintLevel(info.getDisplayType());

            return (GeneticAlgorithm<T>) geneticAlgorithm;
        }
    }

    private static ITreeGenerator<Double> getCovidTreeGenerator() {
        FunctionalSet<Double> functionalSet = new FunctionalSet<>();
        functionalSet.addFunction(new AddFunction());
        functionalSet.addFunction(new SubtractFunction());
        functionalSet.addFunction(new MultiplicationFunction());
        functionalSet.addFunction(new DivisionFunction());

        TerminalSet<Double> terminalSet = new TerminalSet<>();

        return new ADFTreeGenerator<>(functionalSet,terminalSet);
    }


    private static SetupManager doSetup(SetupManager setup) {
        setup.readDataFile(0.7,true);
        setup.setupGPParameters();
        return setup;
    }


    private static ITreeGenerator<String> getPatientTreeGenerator() {
        FunctionalSet<String> functionalSet = new FunctionalSet<>();
        functionalSet.addFunction(new PatientVitalsFunction("L-CORE","L-CORE",Arrays.asList("high","mid","low")));
        functionalSet.addFunction(new PatientVitalsFunction("L-SURF","L-SURF",Arrays.asList("high","mid","low")));
        functionalSet.addFunction(new PatientVitalsFunction("L-O2","L-O2",Arrays.asList("excellent","good","fair","poor")));
        functionalSet.addFunction(new PatientVitalsFunction("L-BP","L-BP",Arrays.asList("high","mid","low")));
        functionalSet.addFunction(new PatientVitalsFunction("SURF-STBL","SURF-STBL",Arrays.asList("stable","mod-stable","unstable")));
        functionalSet.addFunction(new PatientVitalsFunction("CORE-STBL","CORE-STBL",Arrays.asList("stable","mod-stable","unstable")));
        functionalSet.addFunction(new PatientVitalsFunction("BP-STBL","BP-STBL",Arrays.asList("stable","mod-stable","unstable")));
        functionalSet.addFunction(new PatientVitalsFunctionSingleValue("COMFORT","COMFORT",Arrays.asList("7","15","20"))); //TODO: This is a special case where a choice must be created from a single number

        TerminalSet<String> terminalSet = new TerminalSet<>();
        terminalSet.addTerminal(new PatientResult("I"));
        terminalSet.addTerminal(new PatientResult("S"));
        terminalSet.addTerminal(new PatientResult("A"));

        return new ADFTreeGenerator<>(functionalSet,terminalSet);
    }
}
