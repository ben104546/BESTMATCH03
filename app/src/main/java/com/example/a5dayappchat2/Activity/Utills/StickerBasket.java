package com.example.a5dayappchat2.Activity.Utills;

public class StickerBasket {
    private String StickerId,StikcerStatusCheck;

    public StickerBasket() {
    }

    public StickerBasket(String stickerId, String stikcerStatusCheck) {
        StickerId = stickerId;
        StikcerStatusCheck = stikcerStatusCheck;
    }


    public String getStickerId() {
        return StickerId;
    }

    public void setStickerId(String stickerId) {
        StickerId = stickerId;
    }

    public String getStikcerStatusCheck() {
        return StikcerStatusCheck;
    }

    public void setStikcerStatusCheck(String stikcerStatusCheck) {
        StikcerStatusCheck = stikcerStatusCheck;
    }
}
