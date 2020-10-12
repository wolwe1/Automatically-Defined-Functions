package library.gpLibrary.models.highOrder.implementation;

import library.gpLibrary.models.primitives.nodes.abstractClasses.Node;

import java.util.ArrayList;
import java.util.List;

public class FunctionalSet<T>
{
    private final List<Node<T>> _functions;

    public FunctionalSet(){
        _functions = new ArrayList<>();
    }

    public List<Node<T>> get_functions() {
        return _functions;
    }

    public void addFunction( Node<T> newFunc){
        _functions.add(newFunc);
    }

    public int size(){
        return _functions.size();
    }

    public Node<T> get(int index){

        if(index < 0 || index >= size())
            throw new RuntimeException("Attempted to access function out of range");

        return _functions.get(index).getCopy(false);
    }

    public Node<T> get(String functionName) throws Exception {

        for (Node<T> function : _functions) {
            if(function.name.equals(functionName))
                return function.getCopy(false);
        }

        throw new Exception("Attempted to access function '"+functionName+"' which does not exist");
    }
}
