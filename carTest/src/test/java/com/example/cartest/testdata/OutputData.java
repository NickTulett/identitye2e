package com.example.cartest.testdata;

import com.opencsv.bean.CsvBindByName;

public class OutputData {
    @CsvBindByName(column = "VARIANT_REG")
    String variantReg;

    @CsvBindByName(column = "MAKE")
    String make;

    @CsvBindByName(column = "MODEL")
    String model;

    @CsvBindByName(column = "YEAR")
    String year;
}