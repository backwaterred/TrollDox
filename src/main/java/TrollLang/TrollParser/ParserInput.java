package TrollLang.TrollParser;

import java.io.IOException;

public interface ParserInput {
    String getLine(int lineNum) throws IOException;
}
