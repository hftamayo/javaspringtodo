package com.hftamayo.java.todo.utilities.samples;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;

public class FileUtils {
    public static String readFileAsString(String filePath) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filePath)));
    }
}