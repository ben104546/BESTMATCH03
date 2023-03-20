package com.example.a5dayappchat2.Activity.Utills;

public class Order {
    private String OderStatus,ProuctPrice;


    public Order() {
    }

    public Order(String oderStatus, String prouctPrice) {
        OderStatus = oderStatus;
        ProuctPrice = prouctPrice;
    }


    public String getOderStatus() {
        return OderStatus;
    }

    public void setOderStatus(String oderStatus) {
        OderStatus = oderStatus;
    }

    public String getProuctPrice() {
        return ProuctPrice;
    }

    public void setProuctPrice(String prouctPrice) {
        ProuctPrice = prouctPrice;
    }
}
