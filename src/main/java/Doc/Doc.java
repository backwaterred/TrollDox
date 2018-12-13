package Doc;

import GML.GraphNode;
import GML.GraphElementException;
import TrollLang.AngryTrollException;
import TrollLang.TrollParser.TrollParser;
import Util.Util;

import java.util.Arrays;
import java.util.LinkedList;

public class Doc implements DocumentElement {

    private TrollParser parser;
    private LinkedList<Page> pages;
    private Page currentPage;
    private StringBuilder documentBoilerPlateGML_open;
    private StringBuilder documentBoilerPlateGML_close;

    public Doc(TrollParser p) throws AngryTrollException, GraphElementException {
        parser = p;
        pages = new LinkedList<>();
        currentPage = new Page();
        pages.add(currentPage);

        getBoilerPlateCode();
        parseAll();
    }

    private boolean addNewElement(GraphNode e) {
        // Todo: check page has (printable) space!
        return currentPage.addElement(e);
    }

    private void parseAll() throws AngryTrollException, GraphElementException {
        while (parser.hasNextElement()) {
            this.addNewElement(parser.getNextElement());
        }
    }

    private void getBoilerPlateCode() throws GraphElementException {

        try {
            documentBoilerPlateGML_open = Util.readFileToStringBuilder("./src/main/resources/empty_graph_open.txt");
            documentBoilerPlateGML_close = Util.readFileToStringBuilder("./src/main/resources/empty_graph_close.txt");
        } catch (Exception e) {
            throw new GraphElementException("Doc::getBoilerPlateCode - Failed to read boilerplate code from file");
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
