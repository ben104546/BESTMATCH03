package com.example.a5dayappchat2.Activity.Utills;

public class Sub_Plan {
    private String Sub_PlanDate,Sub_PlanName,Sub_PlanDescription;

    public Sub_Plan() {
    }

    public Sub_Plan(String sub_PlanDate, String sub_PlanName, String sub_PlanDescription) {
        Sub_PlanDate = sub_PlanDate;
        Sub_PlanName = sub_PlanName;
        Sub_PlanDescription = sub_PlanDescription;
    }

    public String getSub_PlanDate() {
        return Sub_PlanDate;
    }

    public void setSub_PlanDate(String sub_PlanDate) {
        Sub_PlanDate = sub_PlanDate;
    }

    public String getSub_PlanName() {
        return Sub_PlanName;
    }

    public void setSub_PlanName(String sub_PlanName) {
        Sub_PlanName = sub_PlanName;
    }

    public String getSub_PlanDescription() {
        return Sub_PlanDescription;
    }

    public void setSub_PlanDescription(String sub_PlanDescription) {
        Sub_PlanDescription = sub_PlanDescription;
    }
}
