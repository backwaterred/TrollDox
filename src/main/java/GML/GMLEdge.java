package GML;

import Util.Util;

import java.io.FileNotFoundException;

public class GMLEdge {

    private String label;
    private String gml_open;
    private String gml_label;
    private String gml_close;
    private String id;

    private static final String LABEL_PLACEHOLDER_TEXT = "LABEL_TEXT";


    public GMLEdge(GMLNode from, GMLNode to) throws GMLException {
        label = "";
        this.loadBoilerplateText();
        this.initGML(from.getId(), to.getId());
    }

    public GMLEdge(GMLNode from, GMLNode to, String label) throws GMLException {
        this.label = label;
        this.id = "e:" + from.getId() + "->" + to.getId();
        this.loadBoilerplateText();
        this.initGML(from.getId(), to.getId());
    }

    // PRE: this.label must be set
    // POST: all GML texts are assigned
    private void loadBoilerplateText() throws GMLException {
        try {
            this.gml_open = Util.readFileToStringBuilder("./src/main/resources/edge_open.txt").toString();
            if (!label.equals("")) {
                this.gml_label = Util.readFileToStringBuilder("./src/main/resources/edgeLabel_gml.txt")
                        .toString()
                        .replaceAll(LABEL_PLACEHOLDER_TEXT, label);
            }
            this.gml_close = Util.readFileToStringBuilder("./src/main/resources/edge_close.txt").toString();

        } catch (FileNotFoundException e) {
            throw new GMLException("GMLEdge::Failed to get GMLEdge boilerplate gml (open, label or close)");
        }
    }

    private void initGML(String sourceId, String targetId) {
        this.id = "e:" + sourceId + "->" + targetId;
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
            return (new StringBuffer()).append(gml_open).append(gml_label).append(gml_close).toString();
        }
    }
}
