package TrollLang.TrollParser;

import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.HashMap;

public class FileInput implements ParserInput {

    private HashMap<Integer, String> lineMap;

    public FileInput(String filePath) throws IOException {
        lineMap = new HashMap<>();
        LineNumberReader lineReader = new LineNumberReader(new FileReader(filePath));
        for (String l=lineReader.readLine(); l !=null; l=lineReader.readLine()) {
            lineMap.put(lineReader.getLineNumber(), l);
        }
    }

    /**
     * A helper function to get next valid line. Returns current line if valid, else makes a recursive call with the next (sequentially) line number.
     * @param lineNumber the lowest valid line number to return
     */
    private int getNextValidLineNumber_helper(int lineNumber) throws IOException{
        // If past the EOF, then we're done.
        if (lineNumber > lineMap.size()) throw new IOException("FileInput::getNextValidLineNumber reached EOF");
        // Else return current line if it's valid; o.w. recurse on next line
        if (TrollParser.validInputLine(lineMap.get(lineNumber))) {
            return lineNumber;
        } else
            return getNextValidLineNumber_helper(++lineNumber);
    }

    @Override
    public int getNextValidLineNumber(int startNumber) throws IOException {

        return getNextValidLineNumber_helper(startNumber+1);
    }

    @Override
    public int getLineNumberStartingWith(String label) throws IOException {
        for (int i=1; i<=lineMap.size(); i++) {
            if (lineMap.get(i).startsWith(label)) {
                return i;
            }
        }
        throw new IOException("FileInput::getLineNumberStartingWith - File does not contain " + label);
    }

    @Override
    public String getLine(int lineNum) throws IOException {
        if (lineNum > lineMap.size()) {
            throw new IOException(
                    "FileInput::GetLine - Given line number past EOF - " + lineNum + " max: " + lineMap.size());
        }
        String rtn = lineMap.get(lineNum);
        if (rtn != null) {
            return rtn;
        } else {
            return "";
        }
    }

    @Override
    public boolean hasLine(int lineNum) {
        return lineNum < lineMap.size();
    }
}
