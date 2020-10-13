package library.gpLibrary.models.primitives;

import library.gpLibrary.models.highOrder.implementation.NodeTree;
import library.gpLibrary.models.highOrder.implementation.PopulationMember;
import library.gpLibrary.models.highOrder.interfaces.IMemberStatistics;

import java.util.List;

public interface IFitnessFunction<T> {

    /**
     * Returns the highest or lowest value depending on fitness function implementation
     * @return Double.NegativeInfinity or Double.Infinity
     */
    double getWorstPossibleValue();

    /**
     * Calculates the fitness of the underlying tree returning the value
     * @param populationMember The package containing the tree
     * @return The fitness of the underlying tree
     */
    IMemberStatistics<Double> calculateFitness(NodeTree<T> populationMember);

    /**
     * Evaluates if the fitness provided in the first parameter is better that that of the second
     * @param firstFitness The base value for the comparison
     * @param secondFitness The value the first parameter is compared to
     * @return True if the first fitness is better than the second else false
     */
    boolean firstFitterThanSecond(double firstFitness, double secondFitness);

    /**
     * Returns the fittest individual in the population
     * @param members The members to compare the fitness of
     * @return The fittest individual
     */
    PopulationMember<T> getFittest(List<PopulationMember<T>> members);

    /**
     * Specifies the members to be run against the training set
     */
    void useTrainingSet();

    /**
     * Specifies the members to run against the testing set
     */
    void useTestingSet();
}
