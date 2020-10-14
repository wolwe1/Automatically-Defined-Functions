package library.gpLibrary.infrastructure.interfaces;

import library.gpLibrary.models.highOrder.implementation.NodeTree;
import library.gpLibrary.models.highOrder.implementation.PopulationMember;

import java.util.List;

public interface IGeneticOperator<T> {

    List<NodeTree<T>> operate(List<PopulationMember<T>> chromosomes);

    int getOutputCount();

    void setOutputCount(int count);

    int getInputCount();

    String getName();

    void setPopulationSize(int populationSize);

    double getRate();
}
