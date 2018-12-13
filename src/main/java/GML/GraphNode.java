package GML;

import java.util.LinkedList;
import java.util.List;

public interface GraphNode {

    String render();
    String getText();
    String getId();
    GraphNode connectsTo(GraphNode element);
    LinkedList<GraphNode> getConnectedNodes();
}
