package library.gpLibrary.functionality.interfaces;


import library.gpLibrary.models.primitives.nodes.abstractClasses.Node;

public interface ITreeVisitor<T> {
    void visit(Node<T> temp);
}
