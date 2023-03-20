package com.example.a5dayappchat2.Activity.DataHolder;

public class DataKeyPaymentChoose {
    private static DataKeyPaymentChoose instance;
    private String KeyPayWayPN;
    private String KeyPayWayPP;

    private DataKeyPaymentChoose() {}

    public static DataKeyPaymentChoose getInstance() {
        if (instance == null) {
            instance = new DataKeyPaymentChoose();
        }
        return instance;
    }

    public String getKeyPayWayPN() {
        return KeyPayWayPN;
    }

    public void setKeyPayWayPN(String KeyPayWayPN) {
        this.KeyPayWayPN = KeyPayWayPN;
    }
    public String getKeyPayWayPP() {
        return KeyPayWayPP;
    }

    public void setKeyPayWayPP(String KeyPayWayPP) {
        this.KeyPayWayPP = KeyPayWayPP;
    }

}
