package u17112631.functions.covid.primitives;

import library.gpLibrary.models.primitives.nodes.abstractClasses.Node;
import library.gpLibrary.models.primitives.nodes.abstractClasses.ValueNode;

public class DivisionFunction extends BasicFunction<Double> {

    public DivisionFunction() {
        super("D");
    }

    public DivisionFunction(DivisionFunction divisionFunction) {
        super(divisionFunction.name);
    }

    @Override
    public Double Operation(){
        Double baseValue = ((ValueNode<Double>)children.get(0)).getValue();

        for (int i = 1; i < children.size(); i++) {
            Double val = ((ValueNode<Double>)children.get(i)).getValue();

            if(val == 0d)
                baseValue = 0d;
            else
                baseValue /= val;
        }
        return baseValue;
    }

    @Override
    public Node<Double> getCopy() {
        return new DivisionFunction(this);
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
