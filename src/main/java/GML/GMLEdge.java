package GML;

import Graph.GraphInfo;
import Util.Util;

import java.io.FileNotFoundException;

public class GMLEdge {

    private String label;
    private String gml;
    private String id;



    public GMLEdge(int id, GMLNode from, GMLNode to) throws GMLException {
        label = "";
        this.id = "e" + id;
        this.loadBoilerplateText();
        this.initGML(id, from.getId(), to.getId());
    }

    public GMLEdge(int id, GMLNode from, GMLNode to, String label) throws GMLException {
        this.label = label;
        this.id = "e" + id;
        this.loadBoilerplateText();
        this.initGML(id, from.getId(), to.getId());
    }

    private void loadBoilerplateText() throws GMLException {
        try {
            this.gml = Util.readFileToStringBuilder("./src/main/resources/edge_gml.txt").toString();

        } catch (FileNotFoundException e) {
            throw new GMLException("GMLEdge::Failed to get GMLEdge boilerplate gml");
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
