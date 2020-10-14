package library.gpLibrary.infrastructure.implementation.operators;

import library.gpLibrary.infrastructure.abstractClasses.GeneticOperator;
import library.gpLibrary.models.highOrder.implementation.NodeTree;
import library.gpLibrary.models.highOrder.implementation.PopulationMember;
import library.gpLibrary.models.primitives.IFitnessFunction;

import java.util.ArrayList;
import java.util.List;

public class LazyReproduction<T> extends GeneticOperator<T> {

    private int rate;
    private int tournamentSize;
    private IFitnessFunction<T> fitnessFunction;

    protected LazyReproduction(int tournamentSize, int input, int output, int populationSize, IFitnessFunction<T> fitnessFunction, double percentageOfPopulation){
        super("Lazy reproduction",input,output,populationSize,percentageOfPopulation);
        this.tournamentSize = tournamentSize;
        this.fitnessFunction = fitnessFunction;
    }

    public static <T> LazyReproduction<T> create(int tournamentSize, int populationSize, double percentageOfPopulation, IFitnessFunction<T> fitnessFunction){
        int input = (int)Math.round(((double) populationSize*percentageOfPopulation));

        while (input % tournamentSize != 0)
            input--;

        int output = input / tournamentSize;
        return new LazyReproduction<>(tournamentSize,input,output,populationSize,fitnessFunction,percentageOfPopulation);
    }

    @Override
    public List<NodeTree<T>> operate(List<PopulationMember<T>> chromosomes) {
        List<NodeTree<T>> winners = new ArrayList<>();

        for (int i = 0; i < chromosomes.size(); i++) {
            PopulationMember<T> chosenOne = chromosomes.get(i);
            for (int j = 1; j < tournamentSize; j++) {
                PopulationMember<T> contender = chromosomes.get(i + j);
                if(fitnessFunction.firstFitterThanSecond(contender.getFitness(),chosenOne.getFitness()))
                    chosenOne = contender;

                i++;
            }
            winners.add(chosenOne.getTree());
        }
        return winners;
    }
}
