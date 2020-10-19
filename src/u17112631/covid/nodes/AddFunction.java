package u17112631.covid.nodes;

import library.gpLibrary.models.primitives.nodes.abstractClasses.Node;
import library.gpLibrary.models.primitives.nodes.abstractClasses.ValueNode;

public class AddFunction extends BasicFunction<Double> {

    public AddFunction() {
        super("A");
    }

    public AddFunction(AddFunction addFunction) {
        super(addFunction.name);

    }

    @Override
    public Double operation(){
        Double baseValue = getBaseValue();

        for (Node<Double> child : children) {

            baseValue += ((ValueNode<Double>)child).getValue();
        }
        return baseValue;
    }

    @Override
    public Node<Double> getCopy() {
        return new AddFunction(this);
    }

    @Override
    public Double getBaseValue() {
        return ((ValueNode<Double>)children.get(0)).getBaseValue();
    }
}
