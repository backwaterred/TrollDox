package GML;

import java.util.LinkedList;

public interface GMLNode {
    /**
     * Return the GraphML text for the current object
     * @return A string which can be interpreted by yEd's GraphML parser
     * */
    String render();
    /**
     * Returns the id of the node
     * @return The id: nodes prefixed with n, edges with e
     * */
    String getId();
    /**
     * Returns true if the current node is connected to the given node
     * @param node the node to test for connected-ness
     * @return true iff the current node is connected to the given node, else returns false
     * */
    boolean isConnectedTo(GMLNode node);
    /**
     * Adds a connection to the current object
     * @param node The node to connect
     * @return the current node after connection is performed (for chaining)
     * */
    GMLNode addConnection(GMLNode node);
    /**
     * Returns all conneccted nodes
     * @return A linked list of all connected nodes
     * */
    LinkedList<GMLNode> getConnectedNodes();

    double getX();
    double getY();
    void setX(double xPos);
    void setY(double yPos);
    void setWidth(double width);
    void setHeight(double height);
    double getWidth();
    double getHeight();
}
