package GML;

import Util.Util;

import java.io.FileNotFoundException;

public class GMLEdge {

    private String label;
    private String gml_open;
    private String gml_close;
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
            this.gml_open = Util.readFileToStringBuilder("./src/main/resources/edge_open.txt").toString();
            if (!label.equals("")) {
                this.label = Util.readFileToStringBuilder("./src/main/resources/edgeLabel_gml.txt").toString();
            }
            this.gml_close = Util.readFileToStringBuilder("./src/main/resources/edge_close.txt").toString();

        } catch (FileNotFoundException e) {
            throw new GMLException("GMLEdge::Failed to get GMLEdge boilerplate gml_open");
        }
    }

    private void initGML(int edgeId, String sourceId, String targetId) {
        this.id = "e" + Integer.toString(edgeId);
        this.gml_open = this.gml_open.replaceAll("<edge id=\"e0\" source=\"n0\" target=\"n1\">",
                "<edge id=\"" + this.getId() + "\" source=\"" + sourceId + "\" target=\"" + targetId + "\">");
    }

    public String getId() {
        return this.id;
    }

    public String render() {
        if (label.equals("")) {
            return (new StringBuffer()).append(gml_open).append(gml_close).toString();
        } else {
            return (new StringBuffer()).append(gml_open).append(label).append(gml_close).toString();
        }
    }
}
