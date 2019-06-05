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

    @Override
    public int getNextValidLineNumber(int lineNum) throws IOException {
        if (TrollParser.validInputLine(lineMap.get(lineNum))) {
            return lineNum;
        } else if (lineNum < lineMap.size()) {
            return getNextValidLineNumber(++lineNum);
        } else {
            throw new IOException("FileInput::getNextValidLineNumber - No more valid lines");
        }
    }

    @Override
    public int getLineNumberStartingWith(String label) throws IOException {
        for (int i=1; i<lineMap.size(); i++) {
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
}
