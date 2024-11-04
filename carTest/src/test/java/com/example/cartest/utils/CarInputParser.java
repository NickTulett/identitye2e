package com.example.cartest.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class CarInputParser {
    public List<String> allLines;
    public List<String> regLines = new ArrayList<>();

    public CarInputParser(Path filepath) throws IOException {
        this.allLines = Files.readAllLines(filepath);
    }

    public void findRegLines() {
        for (String line: this.allLines) {
            if (line.matches(".*([A-Z]{2}[0-9]{2} *[A-Z]{3}).*")) {
                this.regLines.add(line);
            }
        }
    }
}

