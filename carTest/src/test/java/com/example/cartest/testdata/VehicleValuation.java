package com.example.cartest.testdata;

public class VehicleValuation {
    private String type;
    private String variantReg;
    private String make;
    private String model;
    private String year;
    private double minValue = 0;
    private double maxValue = 0;
    private String givenValue = "0";


    public String getGivenValue() {
        return givenValue;
    }

    public void setGivenValue(String givenValue) {
        this.givenValue = givenValue;
    }

    public VehicleValuation(String type) {
        setType(type);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVariantReg() {
        return variantReg;
    }

    public void setVariantReg(String variantReg) {
        this.variantReg = variantReg;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getYear() {
        return year.split(" ")[1];
    }

    public void setYear(String year) {
        this.year = year;
    }

    public double getMinValue() {
        return minValue;
    }

    public void setMinValue(double minValue) {
        this.minValue = minValue;
    }

    public double getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
    }
}
