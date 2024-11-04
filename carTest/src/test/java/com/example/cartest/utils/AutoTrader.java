package com.example.cartest.utils;

import com.example.cartest.testdata.VehicleValuation;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class AutoTrader {
    private Page page;
    String valuationPortal = "https://www.autotrader.co.uk/cars/valuation";
    Locator regNumInput;
    public String carDetails;
    public String carValue;
    public VehicleValuation vehicleValuation;

    private final String dummyDetails = "Kia Soul 64kWh First Edition SUV 5dr Electric Auto (201 bhp)\n" +
            "Registration number:\n" +
            "GJ20XDL\n" +
            "Mileage:\n" +
            "25,000\n" +
            "Fuel type:\n" +
            "Electric\n" +
            "Engine size:\n" +
            "N/A\n" +
            "Body type:\n" +
            "SUV\n" +
            "Colour:\n" +
            "Black\n" +
            "Transmission:\n" +
            "Automatic\n" +
            "Date of first registration:\n" +
            "March 2020\n" +
            "\n" +
            "Not your car? change details";
    private final String dummyValue = "£13,270";

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
//        this.carDetails = dummyDetails;
        String[] carLines = this.carDetails.split("\n");
        this.vehicleValuation = new VehicleValuation("car");
        this.vehicleValuation.setVariantReg(carLines[2]);
        String carMake = carLines[0].split(" ")[0];
        this.vehicleValuation.setMake(carMake.toUpperCase());
        this.vehicleValuation.setModel(carLines[0].replace(carMake + " ", ""));
        this.vehicleValuation.setYear(carLines[16]);


//        Kia Soul 64kWh First Edition SUV 5dr Electric Auto (201 bhp)
//        Registration number:
//        GJ20XDL
//        Mileage:
//        25,000
//        Fuel type:
//        Electric
//        Engine size:
//        N/A
//        Body type:
//        SUV
//        Colour:
//        Black
//        Transmission:
//        Automatic
//        Date of first registration:
//        March 2020
//
//        Not your car? change details
    }
    public void getValuation() {
//        Locator valuationButton = page.getByText("Get valuation");
//        valuationButton.waitFor();
//        valuationButton.click();
//        this.carValue = page.locator("[data-testid='valuation-details-container'] h2").innerText();
        this.carValue = dummyValue;
        this.vehicleValuation.setGivenValue(this.carValue);
    }
}
