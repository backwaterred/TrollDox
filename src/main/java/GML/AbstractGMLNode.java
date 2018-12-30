package GML;

import Graph.GraphInfo;
import Util.Util;

import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Objects;

public abstract class AbstractGMLNode implements GMLNode {

    protected String GML_open;
    protected String msg;
    protected int id;
    protected double xPos;
    protected double yPos;
    protected double width;
    protected double height;
    protected String GML_close;

    protected LinkedList<GMLNode> connectedNodes;
    protected abstract double calcWidth();
    protected abstract double calcHeight();


    protected AbstractGMLNode(int id, String msg, String filepath_open, String filepath_close) throws GMLException {
        this.id = id;
        this.msg = msg;
        this.width = calcWidth();
        this.height = calcHeight();
        connectedNodes = new LinkedList<>();
        setGMLText(filepath_open, filepath_close);
    }

    private void setGMLText(String open, String close) throws GMLException {
        try {
            this.GML_open = Util.readFileToStringBuilder(open).toString();
            this.GML_close = Util.readFileToStringBuilder(close).toString();
        } catch (FileNotFoundException e) {
            throw new GMLException();
        }


    }

    @Override
    public double getX() {
        return xPos;
    }

    @Override
    public double getY() {
        return yPos;
    }

    @Override
    public void setX(double xPos) {
        this.xPos = xPos;
    }

    @Override
    public void setY(double yPos) {
        this.yPos = yPos;
    }

    @Override
    public void setWidth(double width) {
        this.width = width;
    }

    @Override
    public void setHeight(double height) {
        this.height = height;
    }

    @Override
    public double getWidth() {
        return width;
    }

    @Override
    public double getHeight() {
        return height;
    }

    protected String generateGeometryString(double h, double w, double x, double y) {
        StringBuilder geoStr = new StringBuilder();

        return geoStr.append("<y:Geometry height=\"")
                .append(h)
                .append("\" width=\"")
                .append(w)
                .append("\" x=\"")
                .append(x)
                .append("\" y=\"")
                .append(y)
                .append("\"/>")
                .toString();
    }

    @Override
    public String getId() {
        return "n" + this.id;
    }

    @Override
    public GMLNode addConnection(GMLNode node) {
        this.connectedNodes.add(node);
        return this;
    }

    @Override
    public boolean isConnectedTo(GMLNode node) {
        return connectedNodes.contains(node);
    }

    @Override
    public String render() {
        // Set node info in GML
        this.GML_open = this.GML_open.replaceAll("<node id=\"n\\d\">",
                "<node id=\"" + this.getId() + "\">");

        // Set position in GML
        this.GML_open = this.GML_open.replaceAll(
                "<y:Geometry height=\"\\d*\\.\\d*\" width=\"\\d*\\.\\d*\" x=\"\\d*\\.\\d*\" y=\"\\d*\\.\\d*\"/>",
                generateGeometryString(calcHeight(), calcWidth(),
                        xPos, GraphInfo.instance().getAndIncrementY()));


        return (new StringBuilder())
                .append(GML_open)
                .append(this.msg)
                .append(GML_close)
                .toString();
    }

    @Override
    public LinkedList<GMLNode> getConnectedNodes() {
        return new LinkedList<GMLNode>(connectedNodes);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractGMLNode that = (AbstractGMLNode) o;
        return getId() == that.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
