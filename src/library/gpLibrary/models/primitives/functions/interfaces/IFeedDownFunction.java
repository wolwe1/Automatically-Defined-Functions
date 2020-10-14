package library.gpLibrary.models.primitives.functions.interfaces;

import library.gpLibrary.specialisations.classification.Problem;
import library.gpLibrary.models.primitives.nodes.abstractClasses.ChoiceNode;

import java.util.List;

public abstract class IFeedDownFunction<T> extends ChoiceNode<T> {

    protected String matchField;

    protected List<T> choices;

    protected IFeedDownFunction(String matchField,String name, List<T> choices) {
        super(name,choices.size());
        this.matchField = matchField;
        this.choices = choices;
    }
    
    @Override
    public T feed(Problem<T> problem){

        String valueToSwitchOn = (String) problem.getValue(matchField);

        for (int i = 0, choicesSize = choices.size(); i < choicesSize; i++) {
            T choice = choices.get(i);
            ChoiceNode<T> child = (ChoiceNode<T>) children.get(i);

            if(choice.equals(valueToSwitchOn))
                return child.feed(problem);
        }

        throw new RuntimeException("Function did not have appropriate child to resolve problem");
    }


    @Override
    public boolean isFull(){
        return children.size() == _maxChildren;
    }
}
