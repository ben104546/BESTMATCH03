package com.example.a5dayappchat2.Activity.Utills;

public class Activity {
    private String DoingsName,DoingsImage,DoingsDate;

    public Activity() {
    }



    public Activity(String doingsName, String doingsImage, String doingsDate) {
        DoingsName = doingsName;
        DoingsImage = doingsImage;
        DoingsDate = doingsDate;

    }


    public String getDoingsName() {
        return DoingsName;
    }

    public void setDoingsName(String doingsName) {
        DoingsName = doingsName;
    }

    public String getDoingsImage() {
        return DoingsImage;
    }

    public void setDoingsImage(String doingsImage) {
        DoingsImage = doingsImage;
    }

    public String getDoingsDate() {
        return DoingsDate;
    }

    public void setDoingsDate(String doingsDate) {
        DoingsDate = doingsDate;
    }
}
