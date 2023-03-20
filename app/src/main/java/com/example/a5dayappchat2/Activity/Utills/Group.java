package com.example.a5dayappchat2.Activity.Utills;

public class Group {
    private String GroupName,GroupProfileImage,groupVision;

    public Group() {
    }

    public Group(String groupName, String groupProfileImage, String groupVision) {
        GroupName = groupName;
        GroupProfileImage = groupProfileImage;
        this.groupVision = groupVision;
    }


    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String groupName) {
        GroupName = groupName;
    }

    public String getGroupProfileImage() {
        return GroupProfileImage;
    }

    public void setGroupProfileImage(String groupProfileImage) {
        GroupProfileImage = groupProfileImage;
    }

    public String getGroupVision() {
        return groupVision;
    }

    public void setGroupVision(String groupVision) {
        this.groupVision = groupVision;
    }
}
