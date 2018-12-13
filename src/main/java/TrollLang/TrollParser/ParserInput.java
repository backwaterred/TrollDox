package TrollLang.TrollParser;

import java.io.Closeable;
import java.io.IOException;

public interface ParserInput extends AutoCloseable, Closeable {
    boolean hasNextLine();
    String getNextLine() throws IOException;
    int getLineNumber();
    void close() throws IOException;
}
