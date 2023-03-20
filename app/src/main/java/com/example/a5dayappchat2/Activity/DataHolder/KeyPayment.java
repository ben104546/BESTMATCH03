package com.example.a5dayappchat2.Activity.DataHolder;

public class KeyPayment {
    private static KeyPayment instance;
    private String PPkey;
    private String BNkey;

    private KeyPayment() {}

    public static KeyPayment getInstance() {
        if (instance == null) {
            instance = new KeyPayment();
        }
        return instance;
    }

    public String getPPkey() {
        return PPkey;
    }

    public void setPPkey(String PPkey) {
        this.PPkey = PPkey;
    }

    public String getBNkey() {
        return BNkey;
    }

    public void setBNkey(String BNkey) {
        this.BNkey = BNkey;
    }
}
