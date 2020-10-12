package library.gpLibrary.models.primitives.nodes.interfaces;

import library.gpLibrary.models.classification.Problem;

public interface IChoiceNode<T> {

    T feed(Problem<T> problem);
}
