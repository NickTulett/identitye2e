package com.example.cartest.utils.tests;

import com.example.cartest.utils.AutoTrader;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class TestAutotrader {
  // Shared between all tests in this class.
  static Playwright playwright;
  static Browser browser;
  private AutoTrader autoTrader;
  static String regNum = "GJ20XDL";
  static String expectedValue = "£13,270";

  BrowserContext context;
  Page page;

@BeforeAll  static void launchBrowser() {
    playwright = Playwright.create();
    // use an existing logged in browser to skip auth steps
    browser = playwright.chromium().connectOverCDP("http://127.0.0.1:9222");
  }

@AfterAll  static void closeBrowser() {
    playwright.close();
  }

@BeforeEach  void useExistingContextAndPage() {
  context = browser.contexts().getLast();
  page = context.pages().getLast();
  autoTrader = new AutoTrader(page);
}

@AfterEach  void closeContext() {
    context.close();
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

}