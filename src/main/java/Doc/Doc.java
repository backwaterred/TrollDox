package Doc;

import Graph.iGraphNode;
import TrollLang.AngryTrollException;
import TrollLang.TrollParser.ParserInput;
import TrollLang.TrollParser.TrollParser;

import java.io.IOException;

public class Doc {

    public Doc(ParserInput input) throws AngryTrollException, IOException {

        TrollParser parser = new TrollParser(input, this);

    }

    public boolean addNode(iGraphNode node) {
        return false;
    }

}
