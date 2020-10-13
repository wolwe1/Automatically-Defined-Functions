package library.gpLibrary.infrastructure.interfaces;


import library.gpLibrary.models.highOrder.implementation.PopulationMember;

public interface IGeneticAlgorithm<T> {

    PopulationMember<T> performGeneration(int i);

    PopulationMember<T> run();

    void addOperator(IGeneticOperator<T> newOperator);

    void setSeed(long seed);
}
