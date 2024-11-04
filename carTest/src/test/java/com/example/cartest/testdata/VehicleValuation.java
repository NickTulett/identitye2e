package com.example.cartest.testdata;

public class VehicleValuation {
    private String type;
    private String variantReg;
    private String make;
    private String model;
    private int year;
    private int minValue;
    private int maxValue;

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

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
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
