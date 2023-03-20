package com.example.a5dayappchat2.Activity.Utills;

public class Post {
    private String datePost,postDesc,postImage,userProfileImageUrl,username;

    public Post() {
    }

    public Post(String datePost, String postDesc, String postImage, String userProfileImageUrl, String username) {
        this.datePost = datePost;
        this.postDesc = postDesc;
        this.postImage = postImage;
        this.userProfileImageUrl = userProfileImageUrl;
        this.username = username;
    }

    public String getDatePost() {
        return datePost;
    }

    public void setDatePost(String datePost) {
        this.datePost = datePost;
    }

    public String getPostDesc() {
        return postDesc;
    }

    public void setPostDesc(String postDesc) {
        this.postDesc = postDesc;
    }

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    public String getUserProfileImageUrl() {
        return userProfileImageUrl;
    }

    public void setUserProfileImageUrl(String userProfileImageUrl) {
        this.userProfileImageUrl = userProfileImageUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
