package com.example.cartest.testdata;

import com.opencsv.bean.CsvBindByName;

public class OutputData {
    @CsvBindByName(column = "VARIANT_REG")
    public String variantReg;

    @CsvBindByName(column = "MAKE")
    public String make;

    @CsvBindByName(column = "MODEL")
    public String model;

    @CsvBindByName(column = "YEAR")
    public String year;
}