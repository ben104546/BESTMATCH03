package com.example.a5dayappchat2.Activity.Utills;

public class InviteFriend {
    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    String profileImage,username,profileImageUrl;

    public InviteFriend(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public InviteFriend() {
    }

    public InviteFriend(String profileImage, String username) {
        this.profileImage = profileImage;
        this.username = username;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
