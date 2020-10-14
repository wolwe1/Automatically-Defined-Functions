package u17112631.functions.covid.primitives;

import library.gpLibrary.models.primitives.nodes.abstractClasses.Node;
import library.gpLibrary.models.primitives.nodes.abstractClasses.ValueNode;

public class MultiplicationFunction extends BasicFunction<Double> {
    public MultiplicationFunction() {
        super("M");
    }

    @Override
    public Double Operation(){
        Double baseValue = ((ValueNode<Double>)children.get(0)).getValue();

        for (int i = 1; i < children.size(); i++) {
            baseValue *= ((ValueNode<Double>)children.get(i)).getValue();
        }
        return baseValue;
    }

    @Override
    public Node<Double> getCopy() {
       return new MultiplicationFunction();
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
