package u17112631.functions.covid;

import library.gpLibrary.models.primitives.nodes.abstractClasses.Node;
import library.gpLibrary.models.primitives.nodes.abstractClasses.ValueNode;
import u17112631.helpers.covid.CovidEntry;
import u17112631.helpers.covid.CovidPredictionMode;

public class CovidTerminal extends ValueNode<Double> {

    private final CovidEntry entry;
    public CovidPredictionMode mode;
    private Double value;

    public CovidTerminal(CovidEntry entry) {
        super(entry.observationDate.toString());
        this.entry = entry;
        setMode(CovidPredictionMode.Cases);

    }

    @Override
    public Double getValue() {
        return value;
    }

    @Override
    public Double getBaseValue() {
        return 0d;
    }

    @Override
    public boolean isFull() {
        return false;
    }

    @Override
    public CovidTerminal getCopy() {
        CovidTerminal newTerminal = new CovidTerminal(entry.makeCopy());
        newTerminal.mode = mode;
        newTerminal.value = value;

        return newTerminal;
    }

    @Override
    public boolean canTakeMoreChildren() {
        return false;
    }

    @Override
    public boolean requiresTerminals(int maxDepth) {
        throw new RuntimeException("Requires terminal called on terminal");
    }

    @Override
    public boolean hasAncestor(Node<Double> nodeToAdd) {
        return false;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    public void setMode(CovidPredictionMode mode){
        this.mode = mode;

        switch (this.mode){

            case Cases:
                value = entry.confirmedCases;
                break;
            case Deaths:
                value =  entry.deaths;
                break;
            case Recoveries:
                value =  entry.recoveries;
                break;
            default:
                value = 0d;
        }
    }
}
