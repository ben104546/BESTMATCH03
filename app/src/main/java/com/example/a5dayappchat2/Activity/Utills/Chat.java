package com.example.a5dayappchat2.Activity.Utills;

public class Chat {
    private String sms,status,userID,picture,video,audio,doc,sticker,order_id;


    public Chat() {
    }

    public Chat(String sms, String status, String userID, String picture, String video, String audio, String doc, String sticker, String order_id) {
        this.sms = sms;
        this.status = status;
        this.userID = userID;
        this.picture = picture;
        this.video = video;
        this.audio = audio;
        this.doc = doc;
        this.sticker = sticker;
        this.order_id = order_id;
    }


    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getSticker() {
        return sticker;
    }

    public void setSticker(String sticker) {
        this.sticker = sticker;
    }

    public String getSms() {
        return sms;
    }

    public void setSms(String sms) {
        this.sms = sms;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
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
