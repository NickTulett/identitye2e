package com.example.cartest.utils.tests;

import com.example.cartest.utils.CarWow;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class TestCarWow {
  // Shared between all tests in this class.
  static Playwright playwright;
  static Browser browser;
  private CarWow carWow;
  static String regNum = "GJ20XDL";
  static String expectedValue = "£13,348";

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
  carWow = new CarWow(page);
}

@AfterEach  void closeContext() {
    context.close();
  }

  @Test
  void shouldStartValuation() {
    carWow.startValuation(regNum);
    assertTrue(page.url().contains("quotes.carwow.co.uk/selling"));
  }

  @Test
  void shouldMatchCarDetails() {
    carWow.startValuation(regNum);
    carWow.getCarDetails();
    assertTrue(carWow.carDetails.contains("Kia Soul EV"));
  }

  @Test
  void shouldMatchMileage() {
    carWow.startValuation(regNum);
    carWow.getCarDetails();
    carWow.confirmCarDetails();
    assertEquals("25000", carWow.getMileage());
  }

  @Test
  void shouldMatchValue() {
    carWow.startValuation(regNum);
    carWow.getCarDetails();
    carWow.confirmCarDetails(); //make, model, etc
    carWow.confirmCarDetails(); // mileage
    carWow.getValuation();
    assertTrue(carWow.carValue.contains(expectedValue));
  }

//  @Test
//  void shouldCheckTheBox() {
//    page.setContent("<input id='checkbox' type='checkbox'></input>");
//    page.locator("input").check();
//    assertTrue((Boolean) page.evaluate("() => window['checkbox'].checked"));
//  }
//
//  @Test
//  void shouldSearchWiki() {
//    page.navigate("https://www.wikipedia.org/");
//    page.locator("input[name=\"search\"]").click();
//    page.locator("input[name=\"search\"]").fill("playwright");
//    page.locator("input[name=\"search\"]").press("Enter");
//assertEquals("https://en.wikipedia.org/wiki/Playwright", page.url());  }
}