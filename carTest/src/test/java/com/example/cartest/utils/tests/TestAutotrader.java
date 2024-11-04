package com.example.cartest.utils.tests;

import com.example.cartest.utils.AutoTrader;
import com.example.cartest.utils.CarFileParser;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class TestAutotrader {
  // Shared between all tests in this class.
  static Playwright playwright;
  static Browser browser;
  private AutoTrader autoTrader;
  static String regNum = "GJ20XDL";
  static String expectedValue = "£13,270";
  static CarFileParser carInputParser;
  static Path testFile = Path.of("src/test/java/com/example/cartest/testdata/car_input.txt");

  BrowserContext context;
  Page page;

@BeforeAll  static void launchBrowser() throws IOException {
    playwright = Playwright.create();
    // use an existing logged in browser to skip auth steps
    browser = playwright.chromium().connectOverCDP("http://127.0.0.1:9222");
    carInputParser = new CarFileParser(testFile);
    carInputParser.getRegList();
  }

@AfterAll  static void closeBrowser() {
    playwright.close();
  }

@BeforeEach  void useExistingContextAndPage() {
  context = browser.contexts().getLast();
  page = context.pages().getLast();
  autoTrader = new AutoTrader(page);
}


  @Test
  void shouldMatchValue() {
    autoTrader.startValuation(regNum, 25000);
    autoTrader.getCarDetails();
    autoTrader.getValuation();
    assertTrue(autoTrader.carValue.contains(expectedValue));
  }

  @Test
  void testWithDummies() {
    autoTrader.getCarDetails();
    autoTrader.getValuation();
    assertEquals("March 2020", autoTrader.vehicleValuation.getYear());
  }

//  @ParameterizedTest
//  @FieldSource("carInputParser.regList")
////  @ValueSource(strings = {"SG18HTN", "AD58VNF", "BW57BOF", "KT17DLX"})
//  void dataDrivenDummyTest(String regNum) {
//    autoTrader.getCarDetails();
//    autoTrader.getValuation();
//    assertEquals("March 2020", autoTrader.vehicleValuation.getYear());
//  }

}