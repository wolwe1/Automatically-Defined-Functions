package library.gpLibrary.specialisations.ADF;

import library.gpLibrary.infrastructure.interfaces.ITreeGenerator;
import library.gpLibrary.models.highOrder.implementation.NodeTree;
import library.gpLibrary.models.highOrder.implementation.PopulationMember;

import java.util.List;
import java.util.Random;

public class ADFTreeGenerator implements ITreeGenerator<Double> {

    @Override
    public NodeTree<Double> createRandom() {
        return null;
    }

    @Override
    public NodeTree<Double> create(String chromosome) {
        return null;
    }

    @Override
    public void setRandomFunction(Random randomNumberGenerator) {

    }

    @Override
    public NodeTree<Double> replaceSubTree(PopulationMember<Double> chromosome) {
        return null;
    }

    @Override
    public List<NodeTree<Double>> replaceSubTrees(PopulationMember<Double> first, PopulationMember<Double> second) {
        return null;
    }

    @Override
    public NodeTree<Double> fillTree(NodeTree<Double> mutatedChromosome) {
        return null;
    }
}
