package library.gpLibrary.helpers;

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

    public void setupParametersgetGPParameters(){
        int populationSize = 1;
        int numberOfGenerations = 1;
        int numberOfRuns = 1;

        populationSize = (int) getNumericInput("Population size:");
        numberOfGenerations = (int) getNumericInput("Number of generations:");
        numberOfRuns = (int) getNumericInput("Number of runs:");

        info.setPopulationSize(populationSize);
        info.setNumberOfGenerations(numberOfGenerations);
        info.setNumberOfRuns(numberOfRuns);

    }

    public RunInfo getInfo() {
        return info;
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
