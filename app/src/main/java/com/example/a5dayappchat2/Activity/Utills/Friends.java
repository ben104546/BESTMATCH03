package com.example.a5dayappchat2.Activity.Utills;

public class Friends {

    private String description,profileImageUrl,username,status;

    public Friends() {
    }

    public Friends(String description, String profileImageUrl, String username, String status) {
        this.description = description;
        this.profileImageUrl = profileImageUrl;
        this.username = username;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
