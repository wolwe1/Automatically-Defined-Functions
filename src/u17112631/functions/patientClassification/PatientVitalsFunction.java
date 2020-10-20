package u17112631.functions.patientClassification;

import library.gpLibrary.models.primitives.functions.interfaces.IFeedDownFunction;
import library.gpLibrary.models.primitives.nodes.abstractClasses.Node;

import java.util.List;

public class PatientVitalsFunction extends IFeedDownFunction<String> {

    public PatientVitalsFunction(String matchField, String name, List<String> choices) {
        super(matchField, name,choices);
    }

    @Override
    protected Node<String> getCopy() {
        return new PatientVitalsFunction(this.matchField, this.name, this.choices);
    }

    @Override
    public boolean canTakeMoreChildren() {

        if(children.size() < _maxChildren)
            return true;

        for (Node<String> child : children) {
            if(child.canTakeMoreChildren())
                return true;
        }

        return false;
    }

    @Override
    public boolean requiresTerminals(int maxDepth) {
        if(_level == maxDepth - 1)
            return true;

        for (Node<String> child : children) {
            if(child.requiresTerminals(maxDepth))
                return true;
        }

        return false;
    }


    @Override
    public boolean isValid() {

        if(children.size() != _maxChildren)
            return false;

        for (Node<String> child : children) {
            if(!child.isValid())
                return false;
        }

        return true;
    }

}
