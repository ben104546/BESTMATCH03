package com.example.a5dayappchat2.Activity.Utills;

public class ChatGroup {
    private String Sms,Time,User_Id,picture,video,audio,doc,sticker;

    public ChatGroup() {
    }

    public ChatGroup(String sticker) {
        this.sticker = sticker;
    }

    public ChatGroup(String sms, String time, String user_Id, String picture, String video, String audio) {
        Sms = sms;
        Time = time;
        User_Id = user_Id;
        this.picture = picture;
        this.video = video;
        this.audio = audio;
        this.doc = doc;

    }

    public String getSticker() {
        return sticker;
    }

    public void setSticker(String sticker) {
        this.sticker = sticker;
    }

    public String getSms() {
        return Sms;
    }

    public void setSms(String sms) {
        Sms = sms;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getUser_Id() {
        return User_Id;
    }

    public void setUser_Id(String user_Id) {
        User_Id = user_Id;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getDoc() {
        return doc;
    }

    public void setDoc(String doc) {
        this.doc = doc;
    }
}
