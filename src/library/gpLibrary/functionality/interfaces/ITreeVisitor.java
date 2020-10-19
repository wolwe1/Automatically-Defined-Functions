package library.gpLibrary.functionality.interfaces;


import library.gpLibrary.models.primitives.nodes.abstractClasses.Node;

public interface ITreeVisitor<T> {

    //Add node to internal state
    void visit(Node<T> temp);

    //Clear any internal state
    void clear();
}
