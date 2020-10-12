package library.gpLibrary.models.highOrder.implementation;

import library.gpLibrary.models.primitives.nodes.abstractClasses.Node;

import java.util.ArrayList;
import java.util.List;

public class TerminalSet<T> {

    private final List<Node<T>> terminals;

    public TerminalSet(){
        terminals = new ArrayList<>();
    }

    public List<Node<T>> getTerminals() {
        return terminals;
    }

    public void addTerminal(Node<T> newFunc){
        terminals.add(newFunc);
    }

    public int size(){
        return terminals.size();
    }

    public Node<T> get(int index){

        if(index < 0 || index >= size())
            throw new RuntimeException("Attempted to access function out of range");

        return terminals.get(index).getCopy(false);
    }

    public Node<T> get(String terminalName) throws Exception {

        for (Node<T> terminal : terminals) {
            if(terminal.name.equals(terminalName))
                return terminal.getCopy(false);
        }

        throw new Exception("Attempted to access terminal '"+terminalName+"' which does not exist");
    }
}
