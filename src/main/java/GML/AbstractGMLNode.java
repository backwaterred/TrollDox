package GML;

import Graph.GraphInfo;
import Util.Util;

import java.io.FileNotFoundException;
import java.util.LinkedList;

public abstract class AbstractGMLNode implements GMLNode {

    protected String GML_open;
    protected int id;
    protected String msg;
    protected String GML_close;

    protected LinkedList<GMLNode> connectedNodes;

    protected AbstractGMLNode(String filepath_open, String filepath_close) throws GMLException {
        this.id = GraphInfo.instance().getAndIncrementNodeNum();
        connectedNodes = new LinkedList<>();

//        try {
//            this.GML_open = Util.readFileToStringBuilder(filepath_open).toString();
//            this.GML_close = Util.readFileToStringBuilder(filepath_close).toString();
//        } catch (FileNotFoundException e) {
//            throw new GMLException();
//        }
//
//        // Set node info in GML
//        this.GML_open = this.GML_open.replaceAll("<node id=\"n\\d\">",
//                "<node id=\"" + this.getId() + "\">");
//
//        // Set position in GML
//        this.GML_open = this.GML_open.replace("<y:Geometry height=\"40.0\" width=\"108.376953125\" x=\"477.8115234375\" y=\"148.0\"/>",
//                generateGeometryString(msg));


    }

    protected abstract String generateGeometryString(String msg);

    @Override
    public String getText() {
        return msg;
    }

    @Override
    public boolean isConnectedTo(GMLNode node) {
        return connectedNodes.contains(node);
    }

    @Override
    public String render() {
        return GML_open.concat(msg).concat(GML_close);
    }

    @Override
    public LinkedList<GMLNode> getConnectedNodes() {
        return new LinkedList<GMLNode>(connectedNodes);
    }
}
