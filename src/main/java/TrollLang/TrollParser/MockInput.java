package TrollLang.TrollParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

public class MockInput implements ParserInput {

    private String[] lines;
    private StringBuilder builder;
    private boolean inputAllowed;
    private int currentLineNum;
    private int totalLines;

    public MockInput() {
        builder = new StringBuilder();
        inputAllowed = true;
        totalLines = 0;
    }

    @Override
    public String getLine(int lineNum) throws IOException {
        if (inputAllowed) {
            throw new IOException("Must call finalizeInput on MockReader before getting content.");
        }
        return lines[lineNum];
    }

    public MockInput addLine(String line) {
        if (inputAllowed) {
            builder.append(line + "\n");
            totalLines++;
        }
        return this;
    }

    public MockInput finalizeInput() {
        // Accept no more input
        inputAllowed = false;

        lines = builder.toString().split("\n");

        return this;
    }

//    @Override
//    public String getNextLine() throws IOException {
//        if (!inputAllowed) {
//            currentLineNum++;
//            return buffReader.readLine();
//        }
//        else {
//            throw new IOException();
//        }
//    }
//
//    @Override
//    public boolean hasNextLine() {
//        return !inputAllowed && currentLineNum <= totalLines;
//    }

    @Override
    public int getLineNumber() {
        return currentLineNum;
    }

    @Override
    public void close() throws IOException {
        lines = null;
    }
}

