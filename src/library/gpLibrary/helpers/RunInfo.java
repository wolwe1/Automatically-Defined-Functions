package library.gpLibrary.helpers;

import java.util.List;

public class RunInfo {

    private List<String> testData;
    private List<String> trainData;

    private int populationSize = 1;
    private int numberOfGenerations = 1;
    private int numberOfRuns = 1;

    public List<String> getTrainData() {
        return trainData;
    }

    public void setTrainData(List<String> trainData) {
        this.trainData = trainData;
    }

    public List<String> getTestData() {
        return testData;
    }

    public void setTestData(List<String> testData) {
        this.testData = testData;
    }

    public int getPopulationSize() {
        return populationSize;
    }

    public void setPopulationSize(int populationSize) {
        this.populationSize = populationSize;
    }

    public int getNumberOfGenerations() {
        return numberOfGenerations;
    }

    public void setNumberOfGenerations(int numberOfGenerations) {
        this.numberOfGenerations = numberOfGenerations;
    }

    public int getNumberOfRuns() {
        return numberOfRuns;
    }

    public void setNumberOfRuns(int numberOfRuns) {
        this.numberOfRuns = numberOfRuns;
    }

}

