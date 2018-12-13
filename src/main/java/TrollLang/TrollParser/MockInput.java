package TrollLang.TrollParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

public class MockInput implements ParserInput {

    private BufferedReader buffReader;

    private StringBuilder builder;
    private boolean inputAllowed;
    private int currentLineNum;
    private int totalLines;

    public MockInput() {
        buffReader = null;
        builder = new StringBuilder();
        inputAllowed = true;
        totalLines = 0;
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

        // Create buffer
        buffReader = new BufferedReader(
                new StringReader(builder.toString()));
        currentLineNum = 1;

        return this;
    }

    @Override
    public String getNextLine() throws IOException {
        if (!inputAllowed) {
            currentLineNum++;
            return buffReader.readLine();
        }
        else {
            throw new IOException();
        }
    }

    @Override
    public boolean hasNextLine() {
        return !inputAllowed && currentLineNum <= totalLines;
    }

    @Override
    public int getLineNumber() {
        return currentLineNum;
    }

    @Override
    public void close() throws IOException {
        if (buffReader != null) {
            buffReader.close();
            buffReader = null;
        }
    }
}

