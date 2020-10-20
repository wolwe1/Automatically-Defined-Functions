package u17112631.functions.patientClassification;

import library.gpLibrary.models.primitives.nodes.abstractClasses.ChoiceNode;
import library.gpLibrary.models.primitives.nodes.abstractClasses.Node;
import library.gpLibrary.specialisations.classification.Problem;

public class PatientResult extends ChoiceNode<String> {

    public PatientResult(String name) {
        super(name,0);
    }

    @Override
    public boolean isFull() {
        return true;
    }

    @Override
    protected Node<String> getCopy() {
        return new PatientResult(this.name);
    }

    @Override
    public boolean canTakeMoreChildren() {
        return false;
    }

    @Override
    public boolean requiresTerminals(int maxDepth) {
        return false;
        //throw new RuntimeException("Terminal asked if required terminals");
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public int countLeaves() {
        return 1;
    }

    @Override
    public String feed(Problem<String> problem) {
        return name;
    }
}
