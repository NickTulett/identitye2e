package com.example.cartest.utils.tests;

import com.example.cartest.testdata.OutputData;
import com.example.cartest.utils.CarFileParser;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class TestCarFileParser {
    static CarFileParser carFileParser;
    static Path testFile = Path.of("src/test/java/com/example/cartest/testdata/car_input.txt");

@BeforeAll static void parseInput() throws IOException {
    carFileParser = new CarFileParser(testFile);
}

    @Test
    void shouldHaveTwentyOneLines() {
        assertEquals(21, carFileParser.allLines.size());
    }
    @Test
    void shouldFindThreeRegLines() {
        carFileParser.findRegLines();
        System.out.println(carFileParser.regLines.toString());
        assertEquals(3, carFileParser.regLines.size());
    }
    @Test
    void shouldFindFourRegNums() {
        carFileParser.getRegList();
        assertEquals(4, carFileParser.regList.size());
    }
    @Test
    void shouldCreateFourValuations() {
        carFileParser.createValuationsFromCarInputFile();
        assertEquals(4, carFileParser.valuations.size());
        assertEquals("3000", carFileParser.valuations.get(0).getGivenValue());
        assertEquals(3300, Math.floor(carFileParser.valuations.get(0).getMaxValue()));
        assertEquals("500", carFileParser.valuations.get(1).getGivenValue());
        assertEquals(10000, Math.floor(carFileParser.valuations.get(2).getMinValue()));
        assertEquals(10000, Math.floor(carFileParser.valuations.get(3).getMinValue()));
    }

    @Test
    void shouldFindFourCarRecords() throws IOException {
        List<OutputData> carRecords = carFileParser.getOutputData("src/test/java/com/example/cartest/testdata/car_output.txt");
        assertEquals(4, carRecords.size());
    }

}
