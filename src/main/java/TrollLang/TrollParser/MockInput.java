package TrollLang.TrollParser;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

public class MockInput implements ParserInput {

    private HashMap<Integer, String> lines;
    int currLine;

    public MockInput() {
        currLine = 1;
        lines = new HashMap<>();
    }

    @Override
    public String getLine(int lineNum) throws IOException {
        String rtn = lines.get(lineNum);
        if (rtn !=null) {
            return rtn;
        } else {
            throw new IOException("MockInput::getLine - Invalid Line Number: " + lineNum);
        }
    }

    public MockInput addLine(String line) {
        lines.put(currLine++, line);
        return this;
    }
}

