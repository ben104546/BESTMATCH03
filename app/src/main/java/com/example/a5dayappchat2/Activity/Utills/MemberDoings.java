package com.example.a5dayappchat2.Activity.Utills;

public class MemberDoings {
    private String profileImage,username;

    public MemberDoings() {
    }

    public MemberDoings(String profileImage, String username) {
        this.profileImage = profileImage;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
