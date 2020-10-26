package library.gpLibrary.helpers;

import library.gpLibrary.models.primitives.IFitnessFunction;
import library.gpLibrary.models.primitives.enums.PrintLevel;
import library.gpLibrary.specialisations.classification.ClassifierFitnessFunction;
import library.gpLibrary.specialisations.classification.ProblemSet;
import library.helpers.FileManager;
import u17112631.covid.helpers.CovidEntry;
import u17112631.covid.helpers.IFileEntry;
import u17112631.covid.infrastructure.Covid19FitnessFunction;
import u17112631.covid.nodes.CovidTerminal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class SetupManager {

    private final FileManager fileManager;
    private final BufferedReader reader;

    private final RunInfo info;

    public SetupManager(){
        this.fileManager = new FileManager();
        this.reader = new BufferedReader(new InputStreamReader(System.in));
        info = new RunInfo();
    }

    public void readDataFile(double trainingSplit,boolean skipheader){

        fileManager.setupDirectories(info.getDirectory().equals("Default"));

        info.setTrainData(fileManager.getData(skipheader,info.getTrainCountry()));
        info.setTestData(fileManager.getData(skipheader,info.getTestCountry()));
    }

    public void setupGPParameters(){

        String useDefaults = getStringInput("Use defaults? {Y/N}");
        if(useDefaults.toUpperCase().contains("Y")){
            int populationSize = 500;
            int numberOfGenerations = 1000;
            int numberOfRuns = 10;

            double crossoverRate = 0.4;
            double mutationRate = 0.4;
            double reproductionRate = 0.2;

            info.setPopulationSize(populationSize);
            info.setNumberOfGenerations(numberOfGenerations);
            info.setNumberOfRuns(numberOfRuns);

            info.setCrossoverRate(crossoverRate);
            info.setMutationRate(mutationRate);
            info.setReproductionRate(reproductionRate);
            info.setDisplayType(PrintLevel.NONE);
            info.setRunType("COVID");

            info.setTrainCountry("South Africa");
            info.setTestCountry("South Korea");
            info.setDirectory("Default");

        }else{
            int populationSize = (int) getNumericInput("Population size:");
            int numberOfGenerations = (int) getNumericInput("Number of generations:");
            int numberOfRuns = (int) getNumericInput("Number of runs:");
            Printer.printLine();
            double crossoverRate = getNumericInput("Crossover rate: {0-1}");
            double mutationRate = getNumericInput("Mutation rate: {0-1}");
            double reproductionRate = getNumericInput("Reproduction rate: {0-1}");
            Printer.printLine();

            StringBuilder options = new StringBuilder();
            for (PrintLevel value : PrintLevel.values()) {
                options.append(value).append(",");
            }
            options = new StringBuilder(options.substring(0, options.length() - 1));
            String displayType = getStringInput("Display option: {" + options + "}" );
            displayType = displayType.toUpperCase();

            String method = getStringInput("Covid19 dataset or patient");
            if(method.toUpperCase().contains("COVID"))
                method = "COVID";
            else
                method = "PATIENT";

            info.setPopulationSize(populationSize);
            info.setNumberOfGenerations(numberOfGenerations);
            info.setNumberOfRuns(numberOfRuns);

            info.setCrossoverRate(crossoverRate);
            info.setMutationRate(mutationRate);
            info.setReproductionRate(reproductionRate);
            info.setDisplayType(PrintLevel.valueOf(displayType));
            info.setRunType(method);

            if(info.getRuntType() == "COVID"){
                String trainCountry = getStringInput("Please select a country to train on:");
                String testCountry = getStringInput("Please select a country to test on:");

                info.setTrainCountry(trainCountry);
                info.setTestCountry(testCountry);
            }

            info.setDirectory("Custom");
        }

    }

    private String getStringInput(String message) {
        try {
            System.out.println(message);

            return reader.readLine();

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Unable to read input");
        }
    }

    public RunInfo getInfo() {
        return info;
    }

    public IFitnessFunction<String> createClassifierFitnessFunction(String answerField,String fieldNames){
        String[] fields = fieldNames.split(",");

        ProblemSet<String> trainingSet = new ProblemSet<>(this.info.getTrainData(), Arrays.asList(fields),answerField);
        ProblemSet<String> testSet = new ProblemSet<>(this.info.getTestData(), Arrays.asList(fields),answerField);

        return new ClassifierFitnessFunction<>(trainingSet,testSet);
    }

    public IFitnessFunction<Double> createCovidFitnessFunction(IFileEntry template,int lookAhead){
        List<String> training = info.getTrainData();
        List<String> testing = info.getTestData();

        List<CovidTerminal> trainingSet = new ArrayList<>();
        List<CovidTerminal> testingSet = new ArrayList<>();

        createCovidTerminals(template, training, trainingSet);

        createCovidTerminals(template, testing, testingSet);

        System.out.println("Training on " + trainingSet.size() + " samples");
        System.out.println("Testing on " + testingSet.size() + " samples");

        trainingSet.sort(Comparator.comparing(a -> a.entry.observationDate));
        testingSet.sort(Comparator.comparing(a -> a.entry.observationDate));

        var fitnessFunction = new  Covid19FitnessFunction(lookAhead,trainingSet,testingSet);

        //Constant
//        var entry = new CovidEntry();
//        entry.observationDate = new Date();
//        entry.confirmedCases = 2;
//        entry.deaths = 2;
//        entry.recoveries = 2;
//        fitnessFunction.setConstant( new CovidTerminal(entry));

        return fitnessFunction;
    }

    private void createCovidTerminals(IFileEntry template, List<String> dataSet, List<CovidTerminal> testingSet) {
        for (String row : dataSet) {
            String[] data = row.split(",");

            try {
                CovidTerminal terminal = new CovidTerminal((CovidEntry) template.MakeCopy(data));
                testingSet.add(terminal);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private double getNumericInput(String message){

        try {
            System.out.println(message);

            return Double.parseDouble(reader.readLine());

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Unable to read input");
        }
    }

    private static List<List<String>> splitData(List<String> data,double trainingPercentage) {

        int numTrain = (int) (((double)data.size()*trainingPercentage));

        Random numgen = new Random(1L);
        List<String> training = new ArrayList<>();

        while (training.size() != numTrain){
            int next = numgen.nextInt(data.size());

            String dataItem = data.get(next);
            data.remove(next);
            training.add(dataItem);
        }

        List<String> testing = new ArrayList<>(data);

        return Arrays.asList(training,testing);
    }

}
