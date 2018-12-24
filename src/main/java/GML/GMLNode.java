package GML;

import java.util.LinkedList;

public interface GMLNode {
    String render();
    String getId();
    boolean isConnectedTo(GMLNode node);
    GMLNode addConnection(GMLNode node);
    LinkedList<GMLNode> getConnectedNodes();
}
