package Doc;

import GML.GraphEdge;
import GML.GraphElementException;
import GML.GraphNode;

import java.util.HashSet;
import java.util.LinkedList;

public class Page implements DocumentElement {

    private LinkedList<GraphNode> nodes;
    private LinkedList<GraphEdge> edges;

    public Page() {
        nodes = new LinkedList<>();
        edges = new LinkedList<>();
    }

    public boolean addElement(GraphNode element) {
        return nodes.add(element);
    }

    private void createEdges() throws GraphElementException {
        HashSet<String> visited = new HashSet<>();

        for (GraphNode source: nodes) {
            visited.add(source.getId());
            for (GraphNode target: source.getConnectedNodes()) {
                edges.add(new GraphEdge(source, target));
            }
        }
    }

    @Override
    public String render() {
        StringBuilder rtn = new StringBuilder();

        // todo: don't fail silently!
        try {
            createEdges();
        } catch (GraphElementException e) {
            e.printStackTrace();
        }

        for (GraphNode node : nodes) {
            rtn.append(node.render());
        }
        for (GraphEdge edge: edges) {
            rtn.append(edge.render());
        }

        return rtn.toString();
    }
}
