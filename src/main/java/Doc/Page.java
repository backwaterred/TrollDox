package Doc;

import GML.GMLEdge;
import GML.GMLException;
import GML.GMLNode;

import java.util.HashSet;
import java.util.LinkedList;

public class Page implements DocumentElement {

    private LinkedList<GMLNode> nodes;
    private LinkedList<GMLEdge> edges;

    public Page() {
        nodes = new LinkedList<>();
        edges = new LinkedList<>();
    }

    public boolean addNode(GMLNode node) {
        return nodes.add(node);
    }
    public boolean addEdge(GMLEdge edge) {
        return edges.add(edge);
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
