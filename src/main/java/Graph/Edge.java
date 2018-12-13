package Graph;

import GML.GraphNode;

public class Edge {
    private Vertex source;
    private Vertex target;
    private int id;
    private GraphNode gmlView;



    public Edge(int id, Vertex source, Vertex target, GraphNode view) {
        this.id = id;
        this.gmlView = view;
        this.source = source;
        this.target = target;
    }

    public Vertex getSource() {
        return source;
    }

    public Vertex getTarget() {
        return target;
    }

    public int getId() {
        return id;
    }

    public GraphNode getGmlView() {
        return gmlView;
    }
}
