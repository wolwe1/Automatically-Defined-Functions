package library.gpLibrary.helpers;

import library.gpLibrary.models.primitives.IFitnessFunction;
import library.gpLibrary.models.primitives.enums.PrintLevel;
import library.gpLibrary.specialisations.classification.ClassifierFitnessFunction;
import library.gpLibrary.specialisations.classification.ProblemSet;
import library.helpers.FileManager;

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

    public void readDataFile(double trainingSplit){
        fileManager.setupDirectories();

        var data = fileManager.getData();
        List<List<String>> dataSets = splitData(data,trainingSplit);
        info.setTrainData(dataSets.get(0));
        info.setTestData(dataSets.get(1));

    }

    public void setupGPParameters(){

        int populationSize = (int) getNumericInput("Population size:");
        int numberOfGenerations = (int) getNumericInput("Number of generations:");
        int numberOfRuns = (int) getNumericInput("Number of runs:");
        Printer.printLine();
        int crossoverRate = (int) getNumericInput("Crossover rate: {0-1}");
        int mutationRate = (int) getNumericInput("Mutation rate: {0-1}");
        int reproductionRate = (int) getNumericInput("Reproduction rate: {0-1}");
        Printer.printLine();

        StringBuilder options = new StringBuilder();
        for (PrintLevel value : PrintLevel.values()) {
            options.append(value).append(",");
        }
        options = new StringBuilder(options.substring(0, options.length() - 1));
        String displayType = getStringInput("Display option: {" + options + "}" );

        info.setPopulationSize(populationSize);
        info.setNumberOfGenerations(numberOfGenerations);
        info.setNumberOfRuns(numberOfRuns);

        info.setCrossoverRate(crossoverRate);
        info.setMutationRate(mutationRate);
        info.setReproductionRate(reproductionRate);
        info.setDisplayType(PrintLevel.valueOf(displayType));


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
