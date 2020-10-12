package library.gpLibrary.models.primitives.nodes.interfaces;

public interface IValueNode<T> {

    /**
     * Returns the value stored or calculated by the node
     * @return The nodes value
     */
    T getValue();

    /**
     * Returns the base value supported by the tree
     * @return A value with minimal representative value
     */
    T getBaseValue();

}
