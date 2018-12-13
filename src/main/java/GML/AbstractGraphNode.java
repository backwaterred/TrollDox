package GML;

import Graph.GraphInfo;

import java.util.LinkedList;

public abstract class AbstractGraphNode implements GraphNode {

    protected String GML_open;
    protected int id;
    protected String msg;
    protected String GML_close;

    protected LinkedList<GraphNode> connectedNodes;

    public GraphNode connectsTo(GraphNode ele) {
        this.connectedNodes.add(ele);
        return this;
    }

    protected AbstractGraphNode() {
        this.id = GraphInfo.instance().getAndIncrementNodeNum();
        connectedNodes = new LinkedList<>();
    }

    @Override
    public String getText() {
        return msg;
    }

    @Override
    public String render() {
        return GML_open.concat(msg).concat(GML_close);
    }

    @Override
    public LinkedList<GraphNode> getConnectedNodes() {
        return new LinkedList<GraphNode>(connectedNodes);
    }
}
