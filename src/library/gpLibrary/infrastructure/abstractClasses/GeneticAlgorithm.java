package library.gpLibrary.infrastructure.abstractClasses;

import library.gpLibrary.helpers.Printer;
import library.gpLibrary.infrastructure.implementation.operators.Create;
import library.gpLibrary.infrastructure.interfaces.IGeneticAlgorithm;
import library.gpLibrary.infrastructure.interfaces.IGeneticOperator;
import library.gpLibrary.infrastructure.interfaces.IPopulationManager;
import library.gpLibrary.models.highOrder.implementation.PopulationMember;
import library.gpLibrary.models.primitives.IFitnessFunction;
import library.gpLibrary.models.primitives.enums.PrintLevel;

import java.util.HashMap;

public class GeneticAlgorithm<T> implements IGeneticAlgorithm<T> {

    protected HashMap<String, IGeneticOperator<T>> _operators;
    protected IPopulationManager<T> populationManager;

    protected int _populationSize;
    protected int _numGenerations;
    protected boolean _fixedPopulation;
    protected PrintLevel _printLevel;

    /**
     * Sets the values necessary for the genetic algorithm to operate
     * @param populationSize The size of the population to manage
     * @param numberOfGenerations The number of generations the algorithm runs for
     */
    public GeneticAlgorithm(int populationSize,int numberOfGenerations,IPopulationManager<T> populationManager){
        _populationSize = populationSize;
        _numGenerations = numberOfGenerations;
        this.populationManager = populationManager;

        _fixedPopulation = true;
        _printLevel = PrintLevel.ALL;

        _operators = new HashMap<>();
        _operators.put("Create", Create.create(populationSize,0d, this.populationManager.getTreeGenerator()));
    }

    /**
     * Performs a single generation, finding the best performer, displaying statistics and evolving the population
     * @param i The generation number
     * @return The best performer of the generation
     */
    public PopulationMember<T> performGeneration(int i) {

        //Prerequisites
        PopulationMember<T> bestTreeInGeneration = populationManager.getBest();
        display(i,bestTreeInGeneration);
        evolve();

        return bestTreeInGeneration;
    }

    private void evolve() {

        evaluatePopulationIntegrity();

        for (IGeneticOperator<T> operator : _operators.values()) {
            populationManager.operateOnPopulation(operator);
        }

        populationManager.setNewPopulation();
    }

    /**
     * Performs the genetic algorithm, displaying statistics and returning the best performing member
     * @return The best performing member of the run
     */
    public PopulationMember<T> run() {

        //Check conditions
        if(populationManager == null) throw new RuntimeException("Population manager was not initialised");

        PopulationMember<T> bestPerformer = null;
        int bestPerformingGeneration = 0;

        populationManager.reset();
        populationManager.createPopulation(_populationSize);


        for (int i = 0; i < _numGenerations; i++) {
            PopulationMember<T> best = performGeneration(i);

            if(populationManager.firstFitterThanSecond(best,bestPerformer)){
                bestPerformer = best;
                bestPerformingGeneration = i;
            }
        }
        summarise(bestPerformer,bestPerformingGeneration);
        return bestPerformer;
    }

    public void addOperator(IGeneticOperator<T> newOperator){
        newOperator.setPopulationSize(_populationSize);
        _operators.put(newOperator.getName(),newOperator);
    }

    /**
     * Prints out the population histories statistics
     * @param bestPerformer The best performing member of the run
     * @param bestPerformingGeneration The generation the best performer was found in
     */
    protected void summarise(PopulationMember<T> bestPerformer, int bestPerformingGeneration){
        //if(_printLevel == PrintLevel.NONE) return;

        Printer.printLine();
        Printer.print("Summary of algorithm performance");
        Printer.underline();

//        if(_printLevel == PrintLevel.MINOR)
//            _populationManager.printBasicHistory();
//        else
            populationManager.printFullHistory();

        Printer.printLine();
        Printer.print("The best tree was found in generation" + bestPerformingGeneration + " :" + bestPerformer.getId());
        Printer.print("Fitness:" + bestPerformer.getFitness());
    }

    private void evaluatePopulationIntegrity() {
        int totalNewPopulation = 0;

        for (IGeneticOperator<T> operator : _operators.values()) {
            totalNewPopulation += operator.getOutputCount();
        }

        if(totalNewPopulation > _populationSize) throw new RuntimeException("Population attempted to increase");

        //Fill up the pool if required
        if(_fixedPopulation && (totalNewPopulation < _populationSize)){
            int difference = _populationSize - totalNewPopulation;
            _operators.get("Create").setOutputCount(difference);
        }
    }

    private void display(int generation,PopulationMember<T> member) {

        if(_printLevel == PrintLevel.NONE) return;

        Printer.printLine();
        Printer.print("Generation " + generation);
        Printer.printLine();

        if(_printLevel == PrintLevel.MINOR) return;

        Printer.print("Best performing member:" + member.getId());
        Printer.underline();


        Printer.print("Statistics:");
        populationManager.printPopulationStatistics();

        if(_printLevel == PrintLevel.MAJOR) return;
        populationManager.printPopulationComposition();
        Printer.printLine();
    }

    //Setters

    /**
     * Sets whether the population must maintain a fixed size or can reduce
     * @param val Fixed status
     */
    public void setFixedPopulation(boolean val){_fixedPopulation = val;}

    /**
     * Sets the population manager to use
     * @param populationManager The new population manager
     */
    public void setPopulationManager(IPopulationManager<T> populationManager){
        this.populationManager = populationManager;
    }

    /**
     * Sets the level of output from the algorithm
     * @param level The output level
     */
    public void setPrintLevel(PrintLevel level){
        _printLevel = level;
    }

    /**
     * Sets the seed for all internal parts
     * @param seed The seed value
     */
    @Override
    public void setSeed(long seed) {
        this.populationManager.setSeed(seed);
    }

    public IFitnessFunction<T> getFitnessFunction() {
        return populationManager.getFitnessFunction();
    }
}
