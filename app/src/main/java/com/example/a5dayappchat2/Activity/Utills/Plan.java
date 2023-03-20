package com.example.a5dayappchat2.Activity.Utills;

public class Plan {
    private String PlanDate,PlanDescription,PlanName,PlanTime,PlanStatus;

    public Plan() {
    }


    public Plan(String planStatus) {
        PlanStatus = planStatus;
    }

    public Plan(String planDate, String planDescription, String planName, String planTime) {
        PlanDate = planDate;
        PlanDescription = planDescription;
        PlanName = planName;
        PlanTime = planTime;

    }

    public String getPlanStatus() {
        return PlanStatus;
    }

    public void setPlanStatus(String planStatus) {
        PlanStatus = planStatus;
    }

    public String getPlanDate() {
        return PlanDate;
    }

    public void setPlanDate(String planDate) {
        PlanDate = planDate;
    }

    public String getPlanDescription() {
        return PlanDescription;
    }

    public void setPlanDescription(String planDescription) {
        PlanDescription = planDescription;
    }

    public String getPlanName() {
        return PlanName;
    }

    public void setPlanName(String planName) {
        PlanName = planName;
    }

    public String getPlanTime() {
        return PlanTime;
    }

    public void setPlanTime(String planTime) {
        PlanTime = planTime;
    }
}
