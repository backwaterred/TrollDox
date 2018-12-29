package Doc;

import GML.GMLEdge;
import GML.GMLException;
import GML.GMLNode;

import java.util.HashSet;
import java.util.LinkedList;

public class Page implements DocumentElement {

    private double topY;
    private double leftX;
    private LinkedList<GMLNode> nodes;
    private LinkedList<GMLEdge> edges;

    public Page(double topY, double leftX) {
        this.topY = topY;
        this.leftX = leftX;

        nodes = new LinkedList<>();
        edges = new LinkedList<>();
    }

    public boolean addNode(GMLNode node) {
        // todo set x, y pos
        return nodes.add(node);
    }
    public boolean addEdge(GMLEdge edge) {
        return edges.add(edge);
    }

    public LinkedList<GMLNode> getNodes() {
        return nodes;
    }

    @Override
    public String render() {
        StringBuilder rtn = new StringBuilder();

        for (GMLNode node : nodes) {
            rtn.append(node.render());
        }
        for (GMLEdge edge: edges) {
            rtn.append(edge.render());
        }

        return rtn.toString();
    }
}
