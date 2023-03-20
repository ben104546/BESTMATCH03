package com.example.a5dayappchat2.Activity.Utills;

public class MemberOfActivity {

    String User_Id,profileImage,username;

    public MemberOfActivity() {
    }

    public MemberOfActivity(String user_Id, String profileImage, String username) {
        User_Id = user_Id;
        this.profileImage = profileImage;
        this.username = username;
    }

    public String getUser_Id() {
        return User_Id;
    }

    public void setUser_Id(String user_Id) {
        User_Id = user_Id;
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
