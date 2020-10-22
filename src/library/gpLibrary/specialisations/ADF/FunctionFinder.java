package library.gpLibrary.specialisations.ADF;

import library.gpLibrary.functionality.interfaces.ITreeVisitor;
import library.gpLibrary.models.primitives.nodes.abstractClasses.Node;
import library.gpLibrary.specialisations.ADF.infrastructure.ADFunction;

import java.util.ArrayList;
import java.util.List;

public class FunctionFinder<T> implements ITreeVisitor<T> {

    List<ADFunction<T>> functions;

    public FunctionFinder(){
        functions = new ArrayList<>();
    }

    @Override
    public void visit(Node<T> node) {
        if(node instanceof ADFunction)
            functions.add((ADFunction<T>) node);
    }

    @Override
    public void clear() {
        functions.clear();
    }

    @Override
    public List<ADFunction<T>> getNodes() {
        return functions;
    }
}
