package u17112631.functions.covid.primitives;

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
    public Double Operation(){
        Double baseValue = ((ValueNode<Double>)children.get(0)).getBaseValue();

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
    public Double getValue() {
        return null;
    }

    @Override
    public Double getBaseValue() {
        return null;
    }
}
