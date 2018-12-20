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
