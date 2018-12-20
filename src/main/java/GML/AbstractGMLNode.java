package GML;

import Graph.GraphInfo;

import java.util.LinkedList;

public abstract class AbstractGMLNode implements GMLNode {

    protected String GML_open;
    protected int id;
    protected String msg;
    protected String GML_close;

    protected LinkedList<GMLNode> connectedNodes;

    protected AbstractGMLNode() {
        this.id = GraphInfo.instance().getAndIncrementNodeNum();
        connectedNodes = new LinkedList<>();
    }

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
