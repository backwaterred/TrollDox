package Graph;

import GML.GMLNode;

import java.util.LinkedList;

public class GraphNode {

    private LinkedList<GraphNode> outgoingConnections;
    private GMLNode gmlView;
    private int id;

    public GraphNode(int id, GMLNode gmlView) {
        this.id = id;
        this.gmlView = gmlView;
        this.outgoingConnections = new LinkedList<>();
    }

    public int getId() {
        return id;
    }

    public GraphNode addConnectedNode(GraphNode node) {
        this.outgoingConnections.add(node);
        return this;
    }

    public GMLNode getView() {
        return this.gmlView;
    }

    public LinkedList<GraphNode> getOutgoingConnections() {
        return outgoingConnections;
    }

}
