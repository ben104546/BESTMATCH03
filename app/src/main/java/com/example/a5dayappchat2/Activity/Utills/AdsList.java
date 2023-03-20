package com.example.a5dayappchat2.Activity.Utills;

public class AdsList {
    private String AdsImage;
    private boolean AdsStatus;

    public AdsList() {
    }

    public AdsList(String adsImage, boolean adsStatus) {
        AdsImage = adsImage;
        AdsStatus = adsStatus;
    }

    public boolean isAdsStatus() {
        return AdsStatus;
    }

    public void setAdsStatus(boolean adsStatus) {
        AdsStatus = adsStatus;
    }

    public String getAdsImage() {
        return AdsImage;
    }

    public void setAdsImage(String adsImage) {
        AdsImage = adsImage;
    }
}
