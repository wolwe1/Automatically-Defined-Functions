package library.gpLibrary.helpers;

import library.gpLibrary.models.primitives.IFitnessFunction;
import library.gpLibrary.models.primitives.enums.PrintLevel;
import library.gpLibrary.specialisations.classification.ClassifierFitnessFunction;
import library.gpLibrary.specialisations.classification.ProblemSet;
import library.helpers.FileManager;
import u17112631.functions.covid.Covid19FitnessFunction;
import u17112631.functions.covid.CovidTerminal;
import u17112631.helpers.covid.CovidEntry;
import u17112631.helpers.covid.IFileEntry;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

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
        fileManager.setupDirectories();

        var data = fileManager.getData(skipheader);
        List<List<String>> dataSets = splitData(data,trainingSplit);
        info.setTrainData(dataSets.get(0));
        info.setTestData(dataSets.get(1));

    }

    public void setupGPParameters(){

        String useDefaults = getStringInput("Use defaults? {Y/N}");
        if(useDefaults.toUpperCase().contains("Y")){
            int populationSize = 1;
            int numberOfGenerations = 1;
            int numberOfRuns = 1;

            double crossoverRate = 0.2;
            double mutationRate = 0.2;
            double reproductionRate = 0.2;

            info.setPopulationSize(populationSize);
            info.setNumberOfGenerations(numberOfGenerations);
            info.setNumberOfRuns(numberOfRuns);

            info.setCrossoverRate(crossoverRate);
            info.setMutationRate(mutationRate);
            info.setReproductionRate(reproductionRate);
            info.setDisplayType(PrintLevel.ALL);
            info.setRunType("COVID");
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
        }

        if(info.getRuntType() == "COVID"){
            String trainCountry = getStringInput("Please select a country to train on:");
            String testCountry = getStringInput("Please select a country to test on:");

            info.setTrainCountry(trainCountry);
            info.setTestCountry(testCountry);
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

        createCovidTerminals(template, training, trainingSet,info.getTrainCountry());

        createCovidTerminals(template, testing, testingSet,info.getTestCountry());

        System.out.println("Training on " + trainingSet.size() + " samples");
        System.out.println("Testing on " + testingSet.size() + " samples");

        return new Covid19FitnessFunction(lookAhead,trainingSet,testingSet);

    }

    private void createCovidTerminals(IFileEntry template, List<String> testing, List<CovidTerminal> testingSet,String country) {
        for (String row : testing) {
            String[] data = row.split(",");

            if(data[3].toUpperCase().equals(country.toUpperCase())){
                try {
                    CovidTerminal terminal = new CovidTerminal((CovidEntry) template.MakeCopy(data));
                    testingSet.add(terminal);
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
