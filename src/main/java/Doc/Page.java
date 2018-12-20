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

    public boolean addElement(GMLNode element) {
        return nodes.add(element);
    }

    private void createEdges() throws GMLException {
        HashSet<String> visited = new HashSet<>();

        for (GMLNode source: nodes) {
            for (GMLNode target: source.getConnectedNodes()) {
                if (!visited.contains(source))
                    edges.add(new GMLEdge(source, target));
            }
            visited.add(source.getId());
        }
    }

    @Override
    public String render() {
        StringBuilder rtn = new StringBuilder();

        // todo: don't fail silently!
        try {
            createEdges();
        } catch (GMLException e) {
            e.printStackTrace();
        }

        for (GMLNode node : nodes) {
            rtn.append(node.render());
        }
        for (GMLEdge edge: edges) {
            rtn.append(edge.render());
        }

        return rtn.toString();
    }
}
