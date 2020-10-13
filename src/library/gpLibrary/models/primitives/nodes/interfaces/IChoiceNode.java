package library.gpLibrary.models.primitives.nodes.interfaces;

import library.gpLibrary.specialisations.classification.Problem;

public interface IChoiceNode<T> {

    T feed(Problem<T> problem);
}
