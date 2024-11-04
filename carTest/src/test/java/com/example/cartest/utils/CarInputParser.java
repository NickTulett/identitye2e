package com.example.cartest.utils;

import com.example.cartest.testdata.VehicleValuation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CarInputParser {
    public List<String> allLines;
    private static final String regMatcher = "([A-Z]{2}[0-9]{2} *[A-Z]{3})";
    private static final Pattern regMatch = Pattern.compile(regMatcher);
    public List<String> regLines = new ArrayList<>();
    public List<String> regList = new ArrayList<>();
    public List<VehicleValuation> valuations = new ArrayList<>();

    public CarInputParser(Path filepath) throws IOException {
        this.allLines = Files.readAllLines(filepath);
    }

    public void findRegLines() {
        for (String line: this.allLines) {
            Matcher matchingReg = regMatch.matcher(line);
            if (matchingReg.find()) {
                this.regLines.add(line);
            }
        }
    }

    public void getRegList() {
        findRegLines();
        for (String regLine: this.regLines) {
            Matcher matchingReg = regMatch.matcher(regLine);
            while (matchingReg.find()) {
                this.regList.add(matchingReg.group(1));
            }
        }
    }

    public void createValuationsFromRegNums() {
        // TODO: change this to parse other vehicle details
        this.getRegList();
        for (String regNum: this.regList) {
            VehicleValuation vehicleValuation = new VehicleValuation("car");
            vehicleValuation.setVariantReg(regNum);
            this.valuations.add(vehicleValuation);
        }
    }
}

