package com.example.a5dayappchat2.Activity;

public class CalendarList {

private String DoingsName,DoingsTime,DoingsImage,DoingsDate;

    public CalendarList() {
    }

    public CalendarList(String doingsName, String doingsTime, String doingsImage, String doingsDate) {
        DoingsName = doingsName;
        DoingsTime = doingsTime;
        DoingsImage = doingsImage;
        DoingsDate = doingsDate;
    }

    public String getDoingsDate() {
        return DoingsDate;
    }

    public void setDoingsDate(String doingsDate) {
        DoingsDate = doingsDate;
    }

    public String getDoingsImage() {
        return DoingsImage;
    }

    public void setDoingsImage(String doingsImage) {
        DoingsImage = doingsImage;
    }

    public String getDoingsName() {
        return DoingsName;
    }

    public void setDoingsName(String doingsName) {
        DoingsName = doingsName;
    }

    public String getDoingsTime() {
        return DoingsTime;
    }

    public void setDoingsTime(String doingsTime) {
        DoingsTime = doingsTime;
    }
}
