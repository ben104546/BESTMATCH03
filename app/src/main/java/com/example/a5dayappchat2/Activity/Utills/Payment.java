package com.example.a5dayappchat2.Activity.Utills;

public class Payment {
    private String Bank_Holder,Bank_Name,Bank_Number,Bank_Number_Image,Prompt_Pay_Name_Holder,Prompt_Pay_Phone_Number;

    public Payment() {
    }

    public Payment(String bank_Holder, String bank_Name, String bank_Number, String bank_Number_Image, String prompt_Pay_Name_Holder, String prompt_Pay_Phone_Number) {
        Bank_Holder = bank_Holder;
        Bank_Name = bank_Name;
        Bank_Number = bank_Number;
        Bank_Number_Image = bank_Number_Image;
        Prompt_Pay_Name_Holder = prompt_Pay_Name_Holder;
        Prompt_Pay_Phone_Number = prompt_Pay_Phone_Number;
    }


    public String getBank_Holder() {
        return Bank_Holder;
    }

    public void setBank_Holder(String bank_Holder) {
        Bank_Holder = bank_Holder;
    }

    public String getBank_Name() {
        return Bank_Name;
    }

    public void setBank_Name(String bank_Name) {
        Bank_Name = bank_Name;
    }

    public String getBank_Number() {
        return Bank_Number;
    }

    public void setBank_Number(String bank_Number) {
        Bank_Number = bank_Number;
    }

    public String getBank_Number_Image() {
        return Bank_Number_Image;
    }

    public void setBank_Number_Image(String bank_Number_Image) {
        Bank_Number_Image = bank_Number_Image;
    }

    public String getPrompt_Pay_Name_Holder() {
        return Prompt_Pay_Name_Holder;
    }

    public void setPrompt_Pay_Name_Holder(String prompt_Pay_Name_Holder) {
        Prompt_Pay_Name_Holder = prompt_Pay_Name_Holder;
    }

    public String getPrompt_Pay_Phone_Number() {
        return Prompt_Pay_Phone_Number;
    }

    public void setPrompt_Pay_Phone_Number(String prompt_Pay_Phone_Number) {
        Prompt_Pay_Phone_Number = prompt_Pay_Phone_Number;
    }
}
