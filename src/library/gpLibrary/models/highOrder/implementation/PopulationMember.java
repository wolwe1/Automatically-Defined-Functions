package library.gpLibrary.models.highOrder.implementation;

import library.gpLibrary.models.primitives.nodes.abstractClasses.Node;
import library.gpLibrary.models.primitives.nodes.implementation.TerminalNode;

import java.util.List;

/**
 * A POJO that wraps around a node tree
 * @param <T> The type of the underlying tree
 */
public class PopulationMember<T> {

    protected NodeTree<T> tree;
    protected double fitness;
    protected String id;
    protected boolean visited;

    public NodeTree<T> getTree() {
        return tree;
    }

    public void setTree(NodeTree<T> tree) {
        this.tree = tree;
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }



    public PopulationMember(NodeTree<T> innerTree){
        tree = innerTree;
        visited = false;
        updateId();
    }

    public void updateId(){
        id = tree.getCombination();
    }

    public PopulationMember<T> getCopy(){

        PopulationMember<T> newMember = new PopulationMember<>(tree.getCopy());
        newMember.id = id;
        newMember.visited = visited;
        newMember.fitness = fitness;

        return newMember;
    }

    public void loadLeaves(List< ? extends TerminalNode<T>> leaves) {
        tree.clearLeaves();
        for (Node<T> terminal : leaves) {
            try {
                tree.addNode(terminal.getCopy(false));
            } catch (Exception e) {
                throw new RuntimeException("Unable to load leaves");
            }
        }
    }
}
