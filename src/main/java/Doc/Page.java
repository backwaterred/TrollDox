package Doc;

import GML.GMLEdge;
import GML.GMLNode;

import java.util.LinkedList;

public class Page implements DocumentElement {

    private double topY;
    private double leftX;

    private int currCol;
    private double[] colXpos;
    private LinkedList<GMLNode>[] nodes;
    private LinkedList<GMLEdge> edges;

    private static final double DEFAULT_COL_WIDTH = 360;
    private static final int MAX_COLS = 3;

    public Page(double topY, double leftX) {
        this.topY = topY;
        this.leftX = leftX;
        this.currCol = 0;

        colXpos = new double[MAX_COLS];
        nodes = new LinkedList[MAX_COLS];
        for (int i=0; i<MAX_COLS; i++) {
            colXpos[i] = leftX + i*DEFAULT_COL_WIDTH;
            nodes[i] = new LinkedList<>();
        }
        edges = new LinkedList<>();
    }

    public double getCurrentX() {
        return colXpos[currCol];
    }

    public double getSuperX() {
        if (currCol < MAX_COLS) {
            return colXpos[currCol + 1 % MAX_COLS];
        } else {
            return -1;
        }
    }

    public boolean addNodeToCurrentCol(GMLNode node) {
        return this.addNodeHelper(node, currCol);
    }

    public boolean addNodeToSuperCol(GMLNode node) {
        return this.addNodeHelper(node, currCol+1);
    }

    private boolean addNodeHelper(GMLNode node, int col) {
        node.setX(colXpos[col]);
        return nodes[col].add(node);
    }

    public boolean addEdge(GMLEdge edge) {
        return edges.add(edge);
    }

    public LinkedList<GMLNode> getNodes() {
        LinkedList<GMLNode> rtn = new LinkedList<>();

        for (int i=0; i<MAX_COLS; i++) {
            for (GMLNode n : nodes[i]) {
                rtn.add(n);
            }
        }

        return rtn;
    }

    @Override
    public String render() {
        StringBuilder rtn = new StringBuilder();

        for (GMLNode node : getNodes()) {
            rtn.append(node.render());
        }
        for (GMLEdge edge: edges) {
            rtn.append(edge.render());
        }

        return rtn.toString();
    }
}
