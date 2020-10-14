package library.gpLibrary.helpers;

import library.gpLibrary.models.primitives.enums.PrintLevel;

import java.util.List;

public class RunInfo {

    private List<String> testData;
    private List<String> trainData;

    private int populationSize = 1;
    private int numberOfGenerations = 1;
    private int numberOfRuns = 1;

    private double crossoverRate;
    private double mutationRate;
    private double reproductionRate;
    private PrintLevel displayType;
    private String runtType;
    private String trainCountry;
    private String testCountry;

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

    public double getCrossoverRate() {
        return this.crossoverRate;
    }

    public void setCrossoverRate(double crossoverRate) {
        this.crossoverRate = crossoverRate;
    }

    public double getMutationRate() {
        return mutationRate;
    }

    public void setMutationRate(double mutationRate) {
        this.mutationRate = mutationRate;
    }

    public double getReproductionRate() {
        return reproductionRate;
    }

    public void setReproductionRate(double reproductionRate) {
        this.reproductionRate = reproductionRate;
    }

    public PrintLevel getDisplayType() {
        return displayType;
    }

    public void setDisplayType(PrintLevel displayType) {
        this.displayType = displayType;
    }

    public void setRunType(String method) {
        this.runtType = method;
    }

    public String getRuntType() {
        return runtType;
    }

    public void setTrainCountry(String train) {
        this.trainCountry = train;
    }

    public String getTrainCountry() {
        return trainCountry;
    }

    public void setTestCountry(String testCountry) {
        this.testCountry = testCountry;
    }

    public String getTestCountry() {
        return testCountry;
    }
}

