package Util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class Util {

    public static StringBuilder readFileToStringBuilder(String path) throws FileNotFoundException {
        StringBuilder sb = new StringBuilder();

        String allLinesStream = (new BufferedReader(new FileReader(path)))
                .lines()
                .reduce((acc, ele) -> acc + ele)
                .get();
        return sb.append(allLinesStream);
    }
}
