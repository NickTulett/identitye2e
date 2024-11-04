package com.example.cartest.utils.tests;

import com.example.cartest.testdata.OutputData;
import com.example.cartest.testdata.VehicleValuation;
import com.example.cartest.utils.AutoTrader;
import com.example.cartest.utils.CarFileParser;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestAutotrader {
    // Shared between all tests in this class.
    static Playwright playwright;
    static Browser browser;
    private AutoTrader autoTrader;
    static CarFileParser carFileParser;
    static Path testFile = Path.of("src/test/java/com/example/cartest/testdata/car_input.txt");
    static List<OutputData> carRecords;
    static List<VehicleValuation> autoTraderValuations = new ArrayList<>();
    BrowserContext context;
    Page page;

    @BeforeAll
    static void launchBrowser() throws IOException {
        playwright = Playwright.create();
        // use an existing logged in browser to skip auth steps
        browser = playwright.chromium().connectOverCDP("http://127.0.0.1:9222");
        carFileParser = new CarFileParser(testFile);
        carFileParser.createValuationsFromCarInputFile();
        carRecords = carFileParser.getOutputData("src/test/java/com/example/cartest/testdata/car_output.txt");
    }

    @AfterAll
    static void closeBrowser() {
        playwright.close();
    }

    @BeforeEach
    void useExistingContextAndPage() {
        context = browser.contexts().getLast();
        context.setDefaultTimeout(10e3);
        page = context.pages().getLast();
        autoTrader = new AutoTrader(page);
    }

    static Stream<String> regNumProvider() {
        return carFileParser.regList.stream();
    }

    static OutputData carFromOutputData(String regNum) {
        OutputData matchingCar = null;
        for (OutputData car : carRecords) {
            if (car.variantReg.equals(regNum)) {
                matchingCar = car;
            }
        }
        return matchingCar;
    }

    static VehicleValuation valuationFromAutoTrader(String regNum) {
        VehicleValuation vehicleValuation = null;
        for (VehicleValuation val : autoTraderValuations) {
            if (val.getVariantReg().equals(regNum)) {
                vehicleValuation = val;
            }
        }
        return vehicleValuation;
    }


    @ParameterizedTest
    @MethodSource("regNumProvider")
    @Order(1)
    void shouldFindCar(String regNum) {
        // find the car in the output file that matches this regnum
        OutputData matchingCar = carFromOutputData(regNum);
        assert matchingCar != null : regNum + " does not appear in the output file";

        //TODO use carwow for older cars
        assert Integer.parseInt(matchingCar.year) > 2009 : regNum + " is too old (" + matchingCar.year + ") for AutoTrader valuation ";

        // find the car on AutoTrader using the reg number
        autoTrader.startValuation(regNum, 25000);
        autoTrader.getCarDetails();
        autoTrader.getValuation();
        autoTraderValuations.add(autoTrader.vehicleValuation);
    }

    @ParameterizedTest
    @MethodSource("regNumProvider")
    @Order(2)
    void shouldMatchMake(String regNum) {
        OutputData matchingCar = carFromOutputData(regNum);
        VehicleValuation vehicleValuation = valuationFromAutoTrader(regNum);
        assertEquals(matchingCar.make.toUpperCase(), vehicleValuation.getMake(), "Wrong car make");
    }

    @ParameterizedTest
    @MethodSource("regNumProvider")
    @Order(3)
    void shouldMatchYear(String regNum) {
        OutputData matchingCar = carFromOutputData(regNum);
        VehicleValuation vehicleValuation = valuationFromAutoTrader(regNum);
        assertEquals(matchingCar.year, vehicleValuation.getYear(), "Wrong car year");
    }

    @ParameterizedTest
    @MethodSource("regNumProvider")
    @Order(4)
    void shouldMatchModel(String regNum) {
        OutputData matchingCar = carFromOutputData(regNum);
        VehicleValuation vehicleValuation = valuationFromAutoTrader(regNum);
        // the car model may not be an exact match
        // so check that all the words in the output file car model
        // are present in the AutoTrader car details
        String[] carModelParts = matchingCar.model.split(" ");
        String autoTraderModel = vehicleValuation.getModel().toUpperCase();
        for (String carModelPart : carModelParts) {
            if (!carModelPart.equals("-")) {
                assertTrue(autoTraderModel.contains(carModelPart.toUpperCase()), "Autotrader details missing part of the model name: " + carModelPart);
            }
        }
    }

    @ParameterizedTest
    @MethodSource("regNumProvider")
    @Order(5)
    void shouldMatchValuation(String regNum) {
        VehicleValuation matchingEstimate = null;
        for (VehicleValuation carEstimate : carFileParser.valuations) {
            if (carEstimate.getVariantReg().equals(regNum)) {
                matchingEstimate = carEstimate;
            }
        }
        assert matchingEstimate != null : "No matching estimate in input file for " + regNum;

        VehicleValuation vehicleValuation = valuationFromAutoTrader(regNum);
        assertTrue(valuationMatchesEstimate(matchingEstimate, Double.parseDouble(vehicleValuation.getGivenValue())), "AutoTrader valuation does not match the estimate in the input file");
    }


    static boolean valuationMatchesEstimate(VehicleValuation estimate, double marketValue) {
        if (!estimate.getGivenValue().equals("0")) {
            return Double.parseDouble(estimate.getGivenValue()) == marketValue;
        }
        if (estimate.getMaxValue() > 0) {
            return marketValue > estimate.getMinValue() && marketValue < estimate.getMaxValue();
        }
        if (estimate.getMinValue() > 0) {
            return marketValue > estimate.getMinValue();
        }
        return false;
    }

}