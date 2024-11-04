package com.example.cartest.utils;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class CarWow {
    private Page page;
    String valuationPortal = "https://www.carwow.co.uk/sell-my-car?vrm=";
    Locator regNumInput;
    public String carDetails;
    public String carValue;

    public CarWow(Page page) {
        this.page = page;
    }
    public void startValuation(String carReg) {
        this.page.navigate(this.valuationPortal);
        this.regNumInput = page.locator("#registration_number");
        this.regNumInput.fill(carReg);
        this.regNumInput.press("Enter");
    }
    public void getCarDetails() {
        this.carDetails = page.locator(".vehicle-information__details").innerText();
    }
    public void confirmCarDetails() {
        page.getByText("Continue").click();
    }
    public String getMileage() {
        return page.locator(".car-valuation input").first().inputValue();
    }
    public void getValuation() {
        Locator valuationButton = page.getByText("Show me my valuation");
        valuationButton.waitFor();
        valuationButton.click();
        this.carValue = page.locator("h1.estimated-price__card-price-range").innerText();
    }
}
