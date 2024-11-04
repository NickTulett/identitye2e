package com.example.cartest.testdata;

public class VehicleValuation {
    private String type;
    private String variantReg;
    private String make;
    private String model;
    private String year;
    private int minValue;
    private int maxValue;
    private String givenValue;


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

    public int getMinValue() {
        return minValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }
}
