package com.example.cartest.utils.tests;

import com.example.cartest.testdata.OutputData;
import com.example.cartest.testdata.VehicleValuation;
import com.example.cartest.utils.AutoTrader;
import com.example.cartest.utils.CarFileParser;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class TestAutotrader {
  // Shared between all tests in this class.
  static Playwright playwright;
  static Browser browser;
  private AutoTrader autoTrader;
  static CarFileParser carFileParser;
  static Path testFile = Path.of("src/test/java/com/example/cartest/testdata/car_input.txt");
  static List<OutputData> carRecords;
  BrowserContext context;
  Page page;

@BeforeAll  static void launchBrowser() throws IOException {
    playwright = Playwright.create();
    // use an existing logged in browser to skip auth steps
    browser = playwright.chromium().connectOverCDP("http://127.0.0.1:9222");
    carFileParser = new CarFileParser(testFile);
    carFileParser.createValuationsFromCarInputFile();
    carRecords = carFileParser.getOutputData("src/test/java/com/example/cartest/testdata/car_output.txt");
  }

@AfterAll  static void closeBrowser() {
    playwright.close();
  }

@BeforeEach  void useExistingContextAndPage() {
  context = browser.contexts().getLast();
  context.setDefaultTimeout(10e3);
  page = context.pages().getLast();
  autoTrader = new AutoTrader(page);
}



  @ParameterizedTest
  @MethodSource("regNumProvider")
   void dataDrivenCarDetailsTest(String regNum) {
    // find the car in the output file that matches this regnum
    OutputData matchingCar = null;
    for (OutputData car: carRecords) {
      if (car.variantReg.equals(regNum)) {
        matchingCar = car;
      }
    }
    assert matchingCar != null : regNum + " does not appear in the output file";

    //TODO use carwow for older cars
    assert Integer.parseInt(matchingCar.year) > 2009 : regNum + " is too old (" + matchingCar.year + ") for AutoTrader valuation ";

    VehicleValuation matchingEstimate = null;
    for (VehicleValuation carEstimate : carFileParser.valuations) {
        if (carEstimate.getVariantReg().equals(regNum)) {
            matchingEstimate = carEstimate;
        }
    }
    assert matchingEstimate != null : "No matching estimate in input file for " + regNum;

    // find the car on AutoTrader using the reg number
    autoTrader.startValuation(regNum, 25000);
    autoTrader.getCarDetails();
    autoTrader.getValuation();

    //check that make and year match
    assertEquals(matchingCar.make.toUpperCase(), autoTrader.vehicleValuation.getMake(), "Wrong car make");
    assertEquals(matchingCar.year, autoTrader.vehicleValuation.getYear(), "Wrong car year");
    // the car model may not be an exact match
    // so check that all the words in the output file car model
    // are present in the AutoTrader car details
    String[] carModelParts = matchingCar.model.split(" ");
    String autoTraderModel = autoTrader.vehicleValuation.getModel().toUpperCase();
    for (String carModelPart: carModelParts) {
      if (!carModelPart.equals("-")) {
        assertTrue(autoTraderModel.contains(carModelPart.toUpperCase()), "Autotrader details missing part of the model name: " + carModelPart);
      }
    }
    // compare Autotrader valuation to input file estimates
    assertTrue(valuationMatchesEstimate(matchingEstimate, Double.parseDouble(autoTrader.vehicleValuation.getGivenValue())), "AutoTrader valuation does not match the estimate in the input file");
  }

  static Stream<String> regNumProvider() {
    return carFileParser.regList.stream();
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