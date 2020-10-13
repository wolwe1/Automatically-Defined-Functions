package u17112631.functions;

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

        if(_children.size() < _maxChildren)
            return true;

        for (Node<String> child : _children) {
            if(child.canTakeMoreChildren())
                return true;
        }

        return false;
    }

    @Override
    public boolean requiresTerminals(int maxDepth) {
        if(_level == maxDepth - 1)
            return true;

        for (Node<String> child : _children) {
            if(child.requiresTerminals(maxDepth))
                return true;
        }

        return false;
    }

    @Override
    public boolean hasAncestor(Node<String> nodeToAdd) {

        if(nodeToAdd.name.equals(this.name))
            return true;

        if(this.Parent == null)
            return false;

        return this.Parent.hasAncestor(nodeToAdd);
    }

    @Override
    public boolean isValid() {

        if(_children.size() != _maxChildren)
            return false;

        for (Node<String> child : _children) {
            if(!child.isValid())
                return false;
        }

        return true;
    }
}
