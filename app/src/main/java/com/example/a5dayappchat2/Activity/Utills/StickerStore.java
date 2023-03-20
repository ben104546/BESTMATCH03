package com.example.a5dayappchat2.Activity.Utills;

public class StickerStore {
    private String ImageIcon,NameSticker;

    public StickerStore() {
    }

    public StickerStore(String imageIcon, String nameSticker) {
        ImageIcon = imageIcon;
        NameSticker = nameSticker;
    }


    public String getImageIcon() {
        return ImageIcon;
    }

    public void setImageIcon(String imageIcon) {
        ImageIcon = imageIcon;
    }

    public String getNameSticker() {
        return NameSticker;
    }

    public void setNameSticker(String nameSticker) {
        NameSticker = nameSticker;
    }
}
