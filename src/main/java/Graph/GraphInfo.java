package Graph;

public class GraphInfo {

    private static GraphInfo graph = null;
    private double x_pos;
    private double y_pos;
    private int nodeNum;
    private int edgeNum;

    private static final double Y_INCREMENT = 62.0;

    private GraphInfo() {
        x_pos = 0.0;
        y_pos = 0.0;
        nodeNum = 0;
        edgeNum = 0;
    }

    public static GraphInfo instance() {
        if (graph == null) {
            graph = new GraphInfo();
        }
        return graph;
    }

    public double getX() {
        return x_pos;
    }

    public int getNodeNum() {
        return nodeNum;
    }

    public int getAndIncrementNodeNum() {
        return nodeNum++;
    }

    public int getEdgeNum() {
        return edgeNum;
    }

    public int getAndIncrementEdgeNum() {
        return edgeNum++;
    }

    public double getY() {
        return y_pos;
    }

    public double getAndIncrementY() {
        double rtn_y = y_pos;
        y_pos += Y_INCREMENT;
        return rtn_y;
    }
}
