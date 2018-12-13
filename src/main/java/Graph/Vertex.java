package Graph;

import GML.GraphNode;

import java.util.LinkedList;

public class Vertex {

    private int id;
    private LinkedList<Vertex> connectedTo;
    private GraphNode gmlView;

    public Vertex(int id) {
        this.id = id;
        this.connectedTo = new LinkedList<>();
    }

    public Vertex connectsTo(Vertex node) {
        this.connectedTo.add(node);
        return this;
    }

    public int getId() {
        return id;
    }
}
