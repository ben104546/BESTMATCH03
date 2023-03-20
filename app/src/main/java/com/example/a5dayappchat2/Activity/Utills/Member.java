package com.example.a5dayappchat2.Activity.Utills;

public class Member {
    String username,profileImage,position;

    public Member() {
    }

    public Member(String username, String profileImage, String position) {
        this.username = username;
        this.profileImage = profileImage;
        this.position = position;
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

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
