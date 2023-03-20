package com.example.a5dayappchat2.Activity.Utills;

public class Users {
    private String username,city,country,description,profileImage,status;

    public Users() {
    }

    public Users(String username, String city, String country, String description, String profileImage, String status) {
        this.username = username;
        this.city = city;
        this.country = country;
        this.description = description;
        this.profileImage = profileImage;
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
