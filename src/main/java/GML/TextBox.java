package GML;

import Graph.GraphInfo;

public class TextBox extends AbstractGMLNode {

    private static final double WIDTH_SLOPE =  7.52398;
    private static final double WIDTH_PADDING = 13.46257;

    public TextBox(int id, String msg) throws GMLException {
        super(id, msg,
                "./src/main/resources/SimpleTextNode_open.txt",
                "./src/main/resources/SimpleTextNode_close.txt"
        );
    }

    protected TextBox(int id, String msg, String open, String close) throws GMLException {
        super(id, msg, open, close);
    }

    @Override
    protected double calcWidth() {
        // todo: msg starts with e.g. 'LogEvent: + /n' -> remove/account for this in width calculation
        return WIDTH_SLOPE * this.msg.length() + WIDTH_PADDING;
    }

    @Override
    protected double calcHeight() {
        // todo: account for line wraps (also mod width)
        return 40.0;
    }

//    @Override
//    protected String generateGeometryString() {
//        StringBuilder geoStr = new StringBuilder();
//
//        return geoStr.append("<y:Geometry height=\"")
//                .append(calcHeight(this.msg))
//                .append("\" width=\"")
//                .append(calcWidth(this.msg))
//                .append("\" x=\"")
//                .append(GraphInfo.instance().getX())
//                .append("\" y=\"")
//                .append(GraphInfo.instance().getAndIncrementY())
//                .append("\"/>")
//                .toString();
//    }
}
