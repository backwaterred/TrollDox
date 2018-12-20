package GML;

import java.util.LinkedList;

public interface GMLNode {

    String render();
    String getText();
    String getId();
    boolean isConnectedTo(GMLNode node);
    LinkedList<GMLNode> getConnectedNodes();
}
