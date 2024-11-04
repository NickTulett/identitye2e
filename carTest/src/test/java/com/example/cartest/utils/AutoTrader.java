package com.example.cartest.utils;

import com.example.cartest.testdata.VehicleValuation;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class AutoTrader {
    private final Page page;
    String valuationPortal = "https://www.autotrader.co.uk/cars/valuation";
    Locator regNumInput;
    public String carDetails;
    public String carValue;
    public VehicleValuation vehicleValuation;

    public AutoTrader(Page page) {
        this.page = page;
    }
    public void startValuation(String carReg, int mileage) {
        this.page.navigate(this.valuationPortal);
        this.regNumInput = page.locator("#valuations-reg");
        this.regNumInput.fill(carReg);
        page.locator("#valuations-mileage").fill(String.valueOf(mileage));
        this.regNumInput.press("Enter");
    }
    public void getCarDetails() {
        this.carDetails = page.locator("[data-testid='vehicle-details-container']", new Page.LocatorOptions()).innerText();
        String[] carLines = this.carDetails.split("\n");
        this.vehicleValuation = new VehicleValuation("car");
        this.vehicleValuation.setVariantReg(carLines[2]);
        String carMake = carLines[0].split(" ")[0];
        this.vehicleValuation.setMake(carMake.toUpperCase());
        this.vehicleValuation.setModel(carLines[0].replace(carMake + " ", ""));
        this.vehicleValuation.setYear(carLines[16]);
    }
    public void getValuation() {
        Locator valuationButton = page.getByText("Get valuation");
        valuationButton.waitFor();
        valuationButton.click();
        this.carValue = page.locator("[data-testid='valuation-details-container'] h2").innerText();
        this.vehicleValuation.setGivenValue(this.carValue);
    }
}
