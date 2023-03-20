package com.example.a5dayappchat2.Activity.Utills;

public class Comment {
    private String username,userProfileImageUrl,comment;

    public Comment() {
    }

    public Comment(String username, String userProfileImageUrl, String comment) {
        this.username = username;
        this.userProfileImageUrl = userProfileImageUrl;
        this.comment = comment;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserProfileImageUrl() {
        return userProfileImageUrl;
    }

    public void setUserProfileImageUrl(String userProfileImageUrl) {
        this.userProfileImageUrl = userProfileImageUrl;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
