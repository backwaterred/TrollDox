package GML;

import Graph.GraphInfo;
import Util.Util;

import java.io.FileNotFoundException;

public class TextBox extends AbstractGraphNode {

    private static final double WIDTH_SLOPE =  7.52398;
    private static final double WIDTH_PADDING = 13.46257;

    public TextBox(String msg) throws GraphElementException {
        super();
        this.msg = msg;
        try {
            this.GML_open = Util.readFileToStringBuilder("./src/main/resources/SimpleTextNode_open.txt").toString();
            this.GML_close = Util.readFileToStringBuilder("./src/main/resources/SimpleTextNode_close.txt").toString();
        } catch (FileNotFoundException e) {
            throw new GraphElementException();
        }

        // Set node info in GML
        this.GML_open = this.GML_open.replaceAll("<node id=\"n\\d\">",
                "<node id=\"" + this.getId() + "\">");

        // Set position in GML
        this.GML_open = this.GML_open.replace("<y:Geometry height=\"40.0\" width=\"108.376953125\" x=\"477.8115234375\" y=\"148.0\"/>",
                generateGeometryString(msg));
    }

    private double calcWidth(String str) {
        // todo: msg starts with e.g. 'LogEvent: + /n' -> remove/account for this in width calculation
        return WIDTH_SLOPE * str.length() + WIDTH_PADDING;
    }

    private double calcHeight(String str) {
        // todo: account for line wraps (also mod width)
        return 40.0;
    }

    private String generateGeometryString(String msg) {
        StringBuilder geoStr = new StringBuilder();

        return geoStr.append("<y:Geometry height=\"")
                .append(calcHeight(msg))
                .append("\" width=\"")
                .append(calcWidth(msg))
                .append("\" x=\"")
                .append(GraphInfo.instance().getX())
                .append("\" y=\"")
                .append(GraphInfo.instance().getAndIncrementY())
                .append("\"/>")
                .toString();
    }

    @Override
    public String getId() {
        return "n" + this.id;
    }
}
