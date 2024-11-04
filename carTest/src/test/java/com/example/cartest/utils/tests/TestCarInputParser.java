package com.example.cartest.utils.tests;

import com.example.cartest.utils.CarInputParser;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestCarInputParser {
    static CarInputParser carInputParser;
    static Path testFile = Path.of("src/test/java/com/example/cartest/testdata/car_input.txt");

@BeforeAll static void parseInput() throws IOException {
    carInputParser = new CarInputParser(testFile);
}

    @Test
    void shouldHaveTwentyOneLines() {
        assertEquals(21, carInputParser.allLines.size());
    }
    @Test
    void shouldFindThreeRegLines() {
        carInputParser.findRegLines();
        System.out.println(carInputParser.regLines.toString());
        assertEquals(3, carInputParser.regLines.size());
    }

}
