package Doc;

import GML.GMLException;
import GML.GMLNode;
import GML.TextBox;
import Graph.GraphNode;
import TrollLang.AngryTrollException;
import TrollLang.TrollParser.ParserInput;
import TrollLang.TrollParser.TrollParser;
import Util.Util;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;

public class Doc implements DocumentElement {

    private LinkedList<Page> pages;
    private Page currentPage;
    private StringBuilder documentBoilerPlateGML_open;
    private StringBuilder documentBoilerPlateGML_close;

    public Doc(ParserInput input) throws AngryTrollException, GMLException, IOException {
        GraphNode startNode = new GraphNode(0, new TextBox("START"));
        TrollParser parser = new TrollParser(input, startNode);
        pages = new LinkedList<>();
        currentPage = new Page();
        pages.add(currentPage);

        getBoilerPlateCode();
        addConnectedNodes(startNode);
    }

    private boolean addNewElement(GMLNode e) {
        // Todo: check page has (printable) space!
        return currentPage.addElement(e);
    }

    private void addConnectedNodes(GraphNode root) {
        LinkedList<GraphNode> queue = new LinkedList<>(); queue.push(root);
        GraphNode currNode;
        while (!queue.isEmpty()) {
            currNode = queue.pop();
            this.addNewElement(currNode.getView());

            for (GraphNode n : currNode.getOutgoingConnections()) {
                queue.push(n);
            }
        }
    }

    private void getBoilerPlateCode() throws GMLException {

        try {
            documentBoilerPlateGML_open = Util.readFileToStringBuilder("./src/main/resources/empty_graph_open.txt");
            documentBoilerPlateGML_close = Util.readFileToStringBuilder("./src/main/resources/empty_graph_close.txt");
        } catch (Exception e) {
            throw new GMLException("Doc::getBoilerPlateCode - Failed to read boilerplate code from file");
        }
    }

    @Override
    public String render() {
        StringBuilder rtn = documentBoilerPlateGML_open;
        for (Page p : pages) {
            rtn.append(p.render());
        }
        rtn.append(documentBoilerPlateGML_close);

        return Arrays.stream(rtn.toString().split(">\\s*<"))
                .reduce((acc, ele)-> acc + ">\n<" + ele)
                .get();
    }
}
