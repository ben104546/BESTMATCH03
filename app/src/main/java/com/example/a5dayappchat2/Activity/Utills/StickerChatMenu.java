package com.example.a5dayappchat2.Activity.Utills;

public class StickerChatMenu {
    String ImageIcon,ImageURI,NameSticker,StickerId;

    public StickerChatMenu() {
    }

    public StickerChatMenu(String imageIcon, String imageURI, String nameSticker) {
        ImageIcon = imageIcon;
        ImageURI = imageURI;
        NameSticker = nameSticker;
    }

    public StickerChatMenu(String stickerId) {
        StickerId = stickerId;
    }

    public String getImageIcon() {
        return ImageIcon;
    }

    public void setImageIcon(String imageIcon) {
        ImageIcon = imageIcon;
    }

    public String getImageURI() {
        return ImageURI;
    }

    public void setImageURI(String imageURI) {
        ImageURI = imageURI;
    }

    public String getNameSticker() {
        return NameSticker;
    }

    public void setNameSticker(String nameSticker) {
        NameSticker = nameSticker;
    }

    public String getStickerId() {
        return StickerId;
    }

    public void setStickerId(String stickerId) {
        StickerId = stickerId;
    }
}
