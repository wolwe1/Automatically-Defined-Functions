package library.gpLibrary.infrastructure.implementation;

import library.gpLibrary.functionality.implementation.StatisticsManager;
import library.gpLibrary.helpers.Printer;
import library.gpLibrary.infrastructure.interfaces.IGeneticOperator;
import library.gpLibrary.infrastructure.interfaces.IPopulationManager;
import library.gpLibrary.infrastructure.interfaces.ITreeGenerator;
import library.gpLibrary.models.highOrder.implementation.NodeTree;
import library.gpLibrary.models.highOrder.implementation.PopulationMember;
import library.gpLibrary.models.highOrder.implementation.PopulationStatistics;
import library.gpLibrary.models.highOrder.interfaces.IMemberStatistics;
import library.gpLibrary.models.primitives.IFitnessFunction;

import java.util.*;

public class TreePopulationManager<T> implements IPopulationManager<T> {

    protected Random randomNumberGenerator;
    protected ITreeGenerator<T> generator;
    protected IFitnessFunction<T> _fitnessFunction;
    protected List<PopulationMember<T>> _currentPopulation;
    protected List<PopulationMember<T>> _nextPopulation;

    //Statistics
    protected List<IMemberStatistics<Double>> _populationStatistics;
    protected List<PopulationStatistics<Double>> _populationHistory;
    private final List<PopulationStatistics<Double>> compositionHistory;


    public TreePopulationManager(ITreeGenerator<T> treeGenerator, IFitnessFunction<T> fitnessFunction, long seed){
        generator = treeGenerator;
        _fitnessFunction = fitnessFunction;
        randomNumberGenerator = new Random(seed);

        generator.setRandomFunction(randomNumberGenerator);

        _currentPopulation = new ArrayList<>();
        _nextPopulation = new ArrayList<>();
        _populationStatistics = new ArrayList<>();
        _populationHistory = new ArrayList<>();
        compositionHistory = new ArrayList<>();
    }

    @Override
    public void createPopulation(int populationSize) {

        for (int i = 0; i < populationSize; i++) {
            NodeTree<T> newTree = generator.createRandom();
            PopulationMember<T> newMember = new PopulationMember<>(newTree);
            _nextPopulation.add(newMember);
        }
        setNewPopulation();
    }

    @Override
    public boolean firstFitterThanSecond(PopulationMember<T> first, PopulationMember<T> second) {
        if(first == null && second == null) throw new RuntimeException("Attempted to calculate fitness on two nulls");

        if(first == null)
            return false;

        if(second == null)
            return true;

        return _fitnessFunction.firstFitterThanSecond(first.getFitness(),second.getFitness());
    }

    @Override
    public PopulationMember<T> getBest() {
        var fittest = _fitnessFunction.getFittest(_currentPopulation);
        fittest.getTree().clearLeaves();
        generator.fillTree(fittest.getTree());

        return fittest;
    }

    @Override
    public void printPopulationStatistics() {

        var popstats = _populationHistory.get(_populationHistory.size() - 1);

        popstats.print();
        Printer.underline();
    }

    @Override
    public void printBasicHistory() {
        PopulationStatistics<String> changeOverRun = StatisticsManager.calculateChange(_populationHistory.get(0),_populationHistory.get(_populationHistory.size() - 1));
        changeOverRun.print();
    }

    @Override
    public void printPopulationComposition() {

        var latestTreeComp = compositionHistory.get(compositionHistory.size() - 1);
        Printer.print("Tree composition");
        latestTreeComp.print();
        Printer.underline();
    }

    @Override
    public void printFullHistory() {
        printBasicHistory();

        Printer.print("\nComposition change");
        Printer.underline();
        PopulationStatistics<String> changeInComposition =
        StatisticsManager.calculateChange(compositionHistory.get(0),compositionHistory.get(compositionHistory.size() - 1));

        changeInComposition.print();
        Printer.underline();
    }

    @Override
    public void setSeed(long seed) {
        this.generator.setSeed(seed);
        this.randomNumberGenerator.setSeed(seed);
    }

    private HashMap<String, Double> getTreeComposition() {
        HashMap<String,Integer> chromosomeFrequency = new HashMap<>();
        HashMap<String,Double> percentageComposition = new HashMap<>();
        int numItems = 0;

        for (PopulationMember<T> member : _currentPopulation) {
            String comp = member.getId();
            String[] items = comp.split("\\.");
            items = stripAnnotations(items);
            numItems += items.length;

            for (String item : items) {
                chromosomeFrequency.merge(item, 1, Integer::sum);
            }
        }

        for (Map.Entry<String, Integer> item : chromosomeFrequency.entrySet()) {
            double percentageValue = Math.round( (((double)item.getValue() / numItems) * 100d) * 100d)/100d;
            percentageComposition.put(item.getKey(),percentageValue);
        }

        return percentageComposition;
    }

    private String[] stripAnnotations(String[] items) {
        List<String> nonAnnotatedItems = new ArrayList<>();

        for (String item : items) {
            if(!item.contains("["))
                nonAnnotatedItems.add(item);
        }

        String[] arr = new String[nonAnnotatedItems.size()];
        return nonAnnotatedItems.toArray(arr);
    }

    private double getAverageTreeSize(){

        double averageTreeSize = 0;
        for (PopulationMember<T> populationMember : _currentPopulation) {
            averageTreeSize += populationMember.getTree().getSize();
        }
        averageTreeSize = averageTreeSize / _currentPopulation.size();

        return averageTreeSize;
    }

    @Override
    public void reset() {
        _currentPopulation = new ArrayList<>();
        _nextPopulation = new ArrayList<>();
        _populationStatistics = new ArrayList<>();
        _populationHistory = new ArrayList<>();
    }

    @Override
    public void operateOnPopulation(IGeneticOperator<T> operator) {
        List<PopulationMember<T>> unvisitedMembers = getUnvisitedMembers();
        List<PopulationMember<T>> chosenMembers = new ArrayList<>();

        //Get the number of members required
        int numberOfMembersRequired = operator.getInputCount();

        for (int i = 0; i < numberOfMembersRequired; i++) {
            int chosenIndex = randomNumberGenerator.nextInt(unvisitedMembers.size());
            PopulationMember<T> chosenMember = unvisitedMembers.get(chosenIndex);
            unvisitedMembers.remove(chosenIndex);

            chosenMembers.add(chosenMember.getCopy());
        }

        List<NodeTree<T>> newChromosomes = operator.operate(chosenMembers);

        for (NodeTree<T> newChromosome : newChromosomes) {
            if(!newChromosome.isValid()){
                throw new RuntimeException("Operation created invalid tree");
            }

        }
        //Create new trees from the new members
        for (NodeTree<T> newChromosome : newChromosomes) {
            PopulationMember<T> newMember = new PopulationMember<>(newChromosome);
            _nextPopulation.add(newMember);
        }
    }

    @Override
    public void setNewPopulation() {
        _currentPopulation = _nextPopulation;
        _nextPopulation = new ArrayList<>();

        for (PopulationMember<T> member : _currentPopulation) {
            IMemberStatistics<Double> memberStatistics = _fitnessFunction.calculateFitness(member.getTree());
            member.setFitness(memberStatistics.getFitness());
            _populationStatistics.add(memberStatistics);

            //Return member to steady state
            member.getTree().clearLeaves();
            generator.fillWithTerminals(member.getTree());
        }

        PopulationStatistics<Double> populationStatistics = StatisticsManager.calculateAverages(_populationStatistics);
        _populationHistory.add(populationStatistics);

        double averageTreeSize = getAverageTreeSize();
        var treeComposition = getTreeComposition();

        var stats = new PopulationStatistics<Double>();
        stats.setMeasure("Tree size",averageTreeSize);

        for (Map.Entry<String, Double> item : treeComposition.entrySet()) {
            stats.setMeasure(item.getKey(),item.getValue());
        }
        compositionHistory.add(stats);
    }

    @Override
    public ITreeGenerator<T> getTreeGenerator() {
        return generator;
    }

    private List<PopulationMember<T>> getUnvisitedMembers() {
        List<PopulationMember<T>> members = new ArrayList<>();

        for (PopulationMember<T> member : _currentPopulation) {
            if(!member.isVisited())
                members.add(member);
        }

        return members;
    }
}
