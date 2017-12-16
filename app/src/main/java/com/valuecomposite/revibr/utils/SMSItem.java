package com.valuecomposite.revibr.utils;

/**
 * Created by ayh07 on 8/10/2017.
 */

public class SMSItem {
    private String time, phoneNum, displayName, body;

    public SMSItem() { }


    public SMSItem(String time, String phoneNum, String displayName, String body)
    {
        this.time = time;
        this.phoneNum = phoneNum;
        this.displayName = displayName;
        this.body = body;
    }

    public String getTime() { return time; }

    public void setTime(String time) { this.time = time; }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getBody(){
        return body;
    }

    public void setBody(String body){
        this.body = body;
    }
}
