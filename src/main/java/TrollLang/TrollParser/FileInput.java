package TrollLang.TrollParser;

import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;

public class FileInput implements ParserInput {

    private LineNumberReader lineReader = null;
    private String nextLine;

    public FileInput(String filePath) throws IOException {
        lineReader = new LineNumberReader(new FileReader(filePath));
        do {
            // Advance member nextLine until lineReader reaches EOF (returns null) or a valid line is reached
            nextLine = lineReader.readLine();
        } while(nextLine != null && !TrollParser.validInputLine(nextLine));
    }

    @Override
    public boolean hasNextLine() {
        return nextLine != null;
    }

    @Override
    public String getNextLine() throws IOException {
        if (!this.hasNextLine())
            throw new IOException("FileInput::getNextLine - No more lines to read. Line number: " + this.getLineNumber());

        String rtnLine = nextLine;
        do {
            // Advance member nextLine until lineReader reaches EOF (returns null) or a valid line is reached
            nextLine = lineReader.readLine();
        } while(nextLine != null && !TrollParser.validInputLine(nextLine));

        // Return original value of nextLine
        return rtnLine;
    }

    @Override
    public int getLineNumber() {
        return lineReader.getLineNumber();
    }

    @Override
    public void close() throws IOException {
        if (lineReader != null) {
            lineReader.close();
            lineReader = null;
        }
    }
}
