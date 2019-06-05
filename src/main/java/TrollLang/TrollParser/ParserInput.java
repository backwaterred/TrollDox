package TrollLang.TrollParser;

import java.io.IOException;

public interface ParserInput {
    String getLine(int lineNum) throws IOException;
    int getNextValidLineNumber(int lineNum) throws IOException;
    int getLineNumberStartingWith(String label) throws IOException;
}
