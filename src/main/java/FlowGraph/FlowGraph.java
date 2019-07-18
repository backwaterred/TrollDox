package FlowGraph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

public class FlowGraph {

    private FlowGraphType graphType;
    private HashMap<Integer, iFlowGraphElement> nodes;
    private HashSet<FlowGraphEdge> edges;
    private LinkedList<String> atts;

    public FlowGraph() {
        nodes = new HashMap<>();
        edges = new HashSet<>();
        atts = new LinkedList<>();
//        this.addAttribute("size",        "10.5,16.5");
        this.addAttribute("page",        "8.5,11");
        this.addAttribute("ratio",       "auto");
        this.addAttribute("pagedir",     "BR");
        this.addAttribute("orientation", "landscape");
//        this.addAttribute("fontsize",    "10");
        this.addAttribute("margin",      "0.25");
        this.addAttribute("nodesep",     "0.25");
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
        StringBuilder rtn = new StringBuilder();
        rtn.append(graphType).append(" {\n");

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