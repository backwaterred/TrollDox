package FlowGraph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

public class FlowGraph {

    private HashMap<Integer, iFlowGraphElement> nodes;
    private HashSet<FlowGraphEdge> edges;
    private LinkedList<String> atts;
    private static final String DEFAULT_FG_WIDTH = "8.5";
    private static final double HEIGHT_PER_NODE = 0.75;

    public FlowGraph() {
        nodes = new HashMap<>();
        edges = new HashSet<>();
        atts = new LinkedList<>();
        // size attribute is set by render
        this.addAttribute("margin",   "0.5");
        this.addAttribute("nodesep",  "0.25");
        this.addAttribute("ranksep",  "0.25");
        this.addAttribute("packmode", "array_u1");
    }

    /**
     * Gets an existing node with the given id or creates a new one if it does not yet exist.
     * @param id The id (line number) of the element
     * @return the element with the given id or null if it dne
     **/
    public iFlowGraphElement getNode(int id) {
        return nodes.get(id);
    }

    public void addAttribute(String name, String value) {
        atts.add(name + "=\"" + value + "\"");
    }

    public String render() {
        this.setGraphSizeAtt();

        StringBuilder rtn = new StringBuilder();
        rtn.append("digraph {\n");

        rtn.append("\n// Graph Attributes\n");
        rtn.append("graph[");
        atts.forEach(s -> rtn.append(s).append(" "));
        rtn.append("]\n");

        rtn.append("\n// Nodes\n");
        nodes.values().forEach(nd -> rtn.append(nd.render()).append("\n"));

        rtn.append("\n// Edges\n");
        edges.forEach(e -> rtn.append(e.render()).append("\n"));

        rtn.append("}\n");

        return rtn.toString();
    }

    private void setGraphSizeAtt() {
        this.addAttribute("size", DEFAULT_FG_WIDTH + "," + this.getNodeCount()*HEIGHT_PER_NODE);
    }

    /**
     * Adds an node to the graph
     **/
    public void addNode(iFlowGraphElement nd) {
        nodes.put(nd.getId(), nd);
    }

    /**
     * Adds an edge to the graph
    **/
    public void addEdge(FlowGraphEdge e) {
        edges.add(e);
    }

    /**
     * Returns number of edges currently present in the graph.
     */
    public int getNodeCount() {
        return nodes.size();
    }

    /**
     * Returns true iff the connection from->to exists
     * @param from
     * @param to
     * @return
     */
    public boolean hasConnection(int from, int to) {

        for (FlowGraphEdge e : edges) {
            if (e.getFromId() == from && e.getToId() == to)
                    return true;
        }
        return false;
    }
}