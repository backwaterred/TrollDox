package GML;

import Graph.GraphInfo;
import Util.Util;

import java.io.FileNotFoundException;

public class GraphEdge {

    private String label;
    private String gml;
    private String id;



    public GraphEdge(GraphNode from, GraphNode to) throws GraphElementException {
        label = "";
        this.id = "e" + GraphInfo.instance().getAndIncrementEdgeNum();
        this.loadBoilerplateText();
        this.initGML(GraphInfo.instance().getAndIncrementEdgeNum(), from.getId(), to.getId());
    }

    public GraphEdge(GraphNode from, GraphNode to, String label) throws GraphElementException {
        this.label = label;
        this.id = "e" + GraphInfo.instance().getAndIncrementEdgeNum();
        this.loadBoilerplateText();
        this.initGML(GraphInfo.instance().getAndIncrementEdgeNum(), from.getId(), to.getId());
    }

    private void loadBoilerplateText() throws GraphElementException {
        try {
            this.gml = Util.readFileToStringBuilder("./src/main/resources/edge_gml.txt").toString();

        } catch (FileNotFoundException e) {
            throw new GraphElementException("GraphEdge::Failed to get GraphEdge boilerplate gml");
        }
    }

    private void initGML(int edgeId, String sourceId, String targetId) {
        this.id = "e" + Integer.toString(edgeId);
        this.gml = this.gml.replaceAll("<edge id=\"e0\" source=\"n0\" target=\"n1\">",
                "<edge id=\"" + this.getId() + "\" source=\"" + sourceId + "\" target=\"" + targetId + "\">");
    }

    public String getId() {
        return this.id;
    }

    public String render() {
        return this.gml;
    }

    public String getText() {
        return this.label;
    }
}
