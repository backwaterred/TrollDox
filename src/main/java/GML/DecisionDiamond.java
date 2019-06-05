package GML;

public class DecisionDiamond extends AbstractGMLNode {

    private static final double WIDTH_SLOPE =  7.52398;
    private static final double WIDTH_PADDING = 13.46257;

    public DecisionDiamond(int id, String questionText) throws GMLException {
        super(id,
                questionText,
                "./src/main/resources/DecisionNode_open.txt",
                "./src/main/resources/DecisionNode_close.txt");
    }

    @Override
    protected double calcWidth() {
        return WIDTH_SLOPE * this.msg.length() + WIDTH_PADDING;
    }

    @Override
    protected double calcHeight() {
        return 0.5* calcWidth();
    }
}
