package library.gpLibrary.specialisations.classification;

import library.gpLibrary.models.highOrder.implementation.NodeTree;
import library.gpLibrary.models.highOrder.implementation.PopulationMember;
import library.gpLibrary.models.highOrder.interfaces.IMemberStatistics;
import library.gpLibrary.models.primitives.IFitnessFunction;

import java.util.List;

/**
 * Measures the fitness of an individual by calculating how "far" it is from the correct classification, lower is better
 */
public class ClassifierFitnessFunction<T> implements IFitnessFunction<T> {

    private final ProblemSet<T> trainingSet;
    private final ProblemSet<T> testingSet;
    ProblemSet<T> problemSet;

    /**
     * Class assumes hit rate as fitness, therefore higher is better
     * @param trainingSet The problem set used for comparison
     */
    public ClassifierFitnessFunction(ProblemSet<T> trainingSet,ProblemSet<T> testingSet){
        this.trainingSet = trainingSet;
        this.testingSet = testingSet;
        problemSet = this.trainingSet;
    }

    @Override
    public double getWorstPossibleValue() {
        return Integer.MIN_VALUE;
    }

    @Override
    public IMemberStatistics<Double> calculateFitness(NodeTree<T> populationMember) {
        ClassificationTree<T> tree = (ClassificationTree<T>) populationMember;
        IMemberStatistics<Double> treeStats = new ClassificationStatistic();

        double accuracy = 0;
        double hits = 0;

        List<Problem<T>> problems = problemSet.getProblems();
        int totalProblems = problems.size();

        for (Problem<T> problem : problems) {
            T answer = problem.getAnswer();
            T guess = tree.feedProblem(problem);

            if(answer.equals(guess)){
                hits++;
            }

        }
        accuracy = (hits/(double) totalProblems) * 100;
        accuracy = Math.round(accuracy * 100.0) / 100.0;
        treeStats.setMeasure("Accuracy",accuracy);
        treeStats.setMeasure("Hits",hits);

        return treeStats;
    }


    @Override
    public boolean firstFitterThanSecond(double firstFitness, double secondFitness) {
        return firstFitness > secondFitness;
    }

    @Override
    public PopulationMember<T> getFittest(List<PopulationMember<T>> list) {
        int bestFitness = Integer.MIN_VALUE;
        int bestIndex = 0;

        for (int i = 0; i < list.size(); i++) {
            PopulationMember<T> member = list.get(i);

            if(member.getFitness() > bestFitness){
                bestFitness = (int) member.getFitness();
                bestIndex = i;
            }
        }
        return list.get(bestIndex);
    }

    @Override
    public void useTrainingSet() {
        problemSet = trainingSet;
    }

    @Override
    public void useTestingSet() {
        problemSet = testingSet;
    }

}
