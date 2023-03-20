package com.example.a5dayappchat2.Activity.DataHolder;

public class DataHolder {
     private static DataHolder instance;
    private String SumPrice;

    private DataHolder() {}

    public static DataHolder getInstance() {
        if (instance == null) {
            instance = new DataHolder();
        }
        return instance;
    }

    public String getSumPrice() {
        return SumPrice;
    }

    public void setSumPrice(String SumPrice) {
        this.SumPrice = SumPrice;
    }
}
