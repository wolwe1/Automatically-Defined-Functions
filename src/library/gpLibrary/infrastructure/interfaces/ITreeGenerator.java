package library.gpLibrary.infrastructure.interfaces;


import library.gpLibrary.models.highOrder.implementation.NodeTree;
import library.gpLibrary.models.highOrder.implementation.PopulationMember;

import java.util.List;
import java.util.Random;

public interface ITreeGenerator<T> {

    NodeTree<T> createRandom();

    NodeTree<T> create(String chromosome);

    void setRandomFunction(Random randomNumberGenerator);

    NodeTree<T> replaceSubTree(PopulationMember<T> chromosome);

    List<NodeTree<T>> replaceSubTrees(PopulationMember<T> first, PopulationMember<T> second);

    NodeTree<T> fillTree(NodeTree<T> mutatedChromosome);
}
