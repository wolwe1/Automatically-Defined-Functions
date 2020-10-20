package u17112631.covid.infrastructure;

import library.gpLibrary.models.highOrder.implementation.NodeTree;
import library.gpLibrary.models.highOrder.implementation.PopulationMember;
import library.gpLibrary.models.highOrder.implementation.PopulationStatistics;
import library.gpLibrary.models.highOrder.interfaces.IMemberStatistics;
import library.gpLibrary.models.primitives.IFitnessFunction;
import library.gpLibrary.models.primitives.nodes.abstractClasses.ValueNode;
import u17112631.covid.nodes.CovidTerminal;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Covid19FitnessFunction implements IFitnessFunction<Double> {

    private final List<CovidTerminal> trainingSet;
    private final List<CovidTerminal> testingSet;
    private List<CovidTerminal> currentSet;
    private final int _lookAhead;
    private CovidTerminal constant;

    public Covid19FitnessFunction(int lookAhead,List<CovidTerminal> trainingSet,List<CovidTerminal> testingSet){
        _lookAhead = lookAhead;
        this.trainingSet = trainingSet;
        this.testingSet = testingSet;
        this.currentSet = this.trainingSet;
    }
    @Override
    public double getWorstPossibleValue() {
        return Double.POSITIVE_INFINITY;
    }

    @Override
    public IMemberStatistics<Double> calculateFitness(NodeTree<Double> populationMember){

        int numberOfDataPointsTreeCanContain = populationMember.getMaximumNumberOfPossibleLeafNodes();
        int startIndexOfComparisons =  numberOfDataPointsTreeCanContain + _lookAhead;

        List<Double> answerSet = currentSet.stream().map(CovidTerminal::getValue)
                .collect(Collectors.toList()).subList(startIndexOfComparisons,currentSet.size());
        List<Double> treeAnswers = new ArrayList<>();

        //Perform a sliding window comparison
        int endPointForTest = currentSet.size() - (numberOfDataPointsTreeCanContain + _lookAhead);
        for (int i = 0; i < endPointForTest; i++) {

            populationMember.clearLeaves();
            //Load tree with data points
            int lastIndexOfDayToUse = i + numberOfDataPointsTreeCanContain;
            if(constant != null)
                lastIndexOfDayToUse--;

            double treeAnswer = getTreeAnswerOnNextSetOfData(populationMember,currentSet.subList(i,lastIndexOfDayToUse));

            treeAnswers.add(treeAnswer);

        }

        return calculateTreePerformance(treeAnswers,answerSet);

    }

    private IMemberStatistics<Double> calculateTreePerformance(List<Double> treeAnswers, List<Double> answerSet) {
        IMemberStatistics<Double> treePerformance = new PopulationStatistics<>();
        double mae = 0;
        double mse = 0;

        for (int i = 0, treeAnswersSize = treeAnswers.size(); i < treeAnswersSize; i++) {
            Double treeAnswer = treeAnswers.get(i);
            Double answer = answerSet.get(i);
            mae += Math.abs(treeAnswer - answer);
            mse += Math.pow((treeAnswer - answer),2);
        }

        treePerformance.setMeasure("MAE",mae);
        treePerformance.setMeasure("MSE",mse);
        treePerformance.setFitness("MAE");
        return treePerformance;
    }

    private double getTreeAnswerOnNextSetOfData(NodeTree<Double> populationMember, List<CovidTerminal> nodesToLoad) {


        try {
            populationMember.addNodes(nodesToLoad);
            if(constant != null)
                populationMember.addNode(constant);
        } catch (Exception e) {
            throw new RuntimeException("Error loading nodes into tree");
        }

        return ( (ValueNode<Double>)populationMember.getRoot()).getValue();
    }

    @Override
    public boolean firstFitterThanSecond(double firstFitness, double secondFitness) {
        return firstFitness < secondFitness;
    }

    @Override
    public PopulationMember<Double> getFittest(List<PopulationMember<Double>> populationMembers) {

        double bestFitness = getWorstPossibleValue();
        int bestIndex = 0;

        for (int i = 0; i < populationMembers.size(); i++) {
            PopulationMember<Double> member = populationMembers.get(i);

            if(member.getFitness() < bestFitness){
                bestFitness = (int) member.getFitness();
                bestIndex = i;
            }
        }
        return populationMembers.get(bestIndex);
    }

    @Override
    public void useTrainingSet() {
        this.currentSet = this.trainingSet;
    }

    @Override
    public void useTestingSet() {
        this.currentSet = this.testingSet;
    }

    public int getLookAhead(){
        return _lookAhead;
    }

    public void setConstant(CovidTerminal constant) {
        this.constant = constant;
    }
}
