package library.gpLibrary.infrastructure.interfaces;

import library.gpLibrary.models.highOrder.implementation.PopulationMember;

public interface IPopulationManager<T> {

    /**
     * Generates a population of the set size using the internal creator and stores it
     * @param populationSize The number of elements to create
     */
    void createPopulation(int populationSize);

    /**
     * Returns whether the first member is fitter than the second
     * @param first The first member
     * @param second The member to compare to
     * @return The fittest individual
     */
    boolean firstFitterThanSecond(PopulationMember<T> first, PopulationMember<T> second);

    /**
     * Returns the best population member stored
     * @return The best population member in the population
     */
    PopulationMember<T> getBest();

    /**
     * Uses the internal statistics manager to print statistics about the population
     */
    void printPopulationStatistics();

    /**
     * Prints the change in statistics metrics over the course of the populations history
     */
    void printBasicHistory();

    /**
     * Resets the state of the population manager, clearing the population,history and statistics
     */
    void reset();

    /**
     * Using the operator, create members of the next population, does not modify current population
     * @param operator The operator that will perform changes to the population
     */
    void operateOnPopulation(IGeneticOperator<T> operator);

    /**
     * Replaces the currently stored population with the new population
     */
    void setNewPopulation();

    ITreeGenerator<T> getTreeGenerator();

    void printPopulationComposition();

    void printFullHistory();
}
