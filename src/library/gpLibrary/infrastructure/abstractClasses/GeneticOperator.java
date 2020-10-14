package library.gpLibrary.infrastructure.abstractClasses;

import library.gpLibrary.infrastructure.interfaces.IGeneticOperator;

public abstract class GeneticOperator<T> implements IGeneticOperator<T> {

    protected int outputCount;
    protected int inputCount;
    protected int populationSize;
    protected String name;
    protected double rate;

    protected GeneticOperator(String name,int inputCount, int outputCount, int populationSize,double rate){
        this.name = name;
        this.inputCount = inputCount;
        this.outputCount = outputCount;
        this.populationSize = populationSize;
        this.rate = rate;
    }

    @Override
    public int getOutputCount() {
        return outputCount;
    }

    @Override
    public void setOutputCount(int count) {
        this.outputCount = count;
    }

    @Override
    public int getInputCount() {
        return inputCount;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setPopulationSize(int populationSize) {
        this.populationSize = populationSize;
    }

    public double getRate(){
        return this.rate;
    }

}
