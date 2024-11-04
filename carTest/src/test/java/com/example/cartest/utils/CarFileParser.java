package com.example.cartest.utils;

import com.example.cartest.testdata.OutputData;
import com.example.cartest.testdata.VehicleValuation;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;




public class CarFileParser {
    public List<String> allLines;

    private static final Pattern regMatch = Pattern.compile("([A-Z]{2}[0-9]{2} *[A-Z]{3})");
    private static final Pattern roughValueMatch = Pattern.compile("(roughly\\D+)(\\d+)");
    private static final Pattern notMuchMatch = Pattern.compile("not worth much");
    private static final Pattern higherThanMatch = Pattern.compile("(higher than\\D+)(\\d+)(k)");

    public List<String> regLines = new ArrayList<>();
    public List<String> regList = new ArrayList<>();
    public List<VehicleValuation> valuations = new ArrayList<>();

    public CarFileParser(Path filepath) throws IOException {
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
                this.regList.add(matchingReg.group(1).replaceAll("\\s", ""));
            }
        }
    }

    public void createValuationsFromRegNums() {
        this.getRegList();
        for (String regNum: this.regList) {
            VehicleValuation vehicleValuation = new VehicleValuation("car");
            vehicleValuation.setVariantReg(regNum);
            this.valuations.add(vehicleValuation);
        }
    }

    public void createValuationsFromCarInputFile() {
        findRegLines();
        for (String regLine: this.regLines) {
            Matcher matchingReg = regMatch.matcher(regLine);
            while (matchingReg.find()) {
                this.regList.add(matchingReg.group(1).replaceAll("\\s", ""));
                Matcher matchingRough = roughValueMatch.matcher(regLine);
                Matcher matchingNotMuch = notMuchMatch.matcher(regLine);
                Matcher matchingHigherThan = higherThanMatch.matcher(regLine);
                String regNum = matchingReg.group(1).replaceAll("\\s", "");
                VehicleValuation vehicleValuation = new VehicleValuation("car");
                vehicleValuation.setVariantReg(regNum);
                String givenValue;
                while (matchingRough.find()) {
                    givenValue = matchingRough.group(2);
                    vehicleValuation.setMinValue(Double.parseDouble(givenValue) * 0.9);
                    vehicleValuation.setMaxValue(Double.parseDouble(givenValue) * 1.1);
                }
                while (matchingNotMuch.find()) {
                    vehicleValuation.setGivenValue("0");
                }
                while (matchingHigherThan.find()) {
                    vehicleValuation.setMinValue(Double.parseDouble(matchingHigherThan.group(2)) * 1000);
                }
                this.valuations.add(vehicleValuation);
            }
        }
    }

    public List<OutputData> getOutputData(String outputFile) throws IOException {
        return new CsvToBeanBuilder(new FileReader(outputFile))
                .withType(OutputData.class)
                .build()
                .parse();
    }

}

