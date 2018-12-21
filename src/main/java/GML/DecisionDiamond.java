package GML;

public class DecisionDiamond extends AbstractGMLNode {

    private String questionText;

    public DecisionDiamond(String questionText) throws GMLException {
        super("./src/main/resources/DecisionNode_open.txt",
                "./src/main/resources/DecisionNode_close.txt");
        this.questionText = questionText;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    protected String generateGeometryString(String msg) {
        return null;
    }
}
