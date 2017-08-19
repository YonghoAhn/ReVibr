package com.valuecomposite.revibr;

/**
 * Created by ayh07 on 8/10/2017.
 */

public class PhoneBookItem {
    private String phoneNum, displayName;
    public PhoneBookItem() { }
    public PhoneBookItem(String Num, String Name)
    {
        phoneNum = Num; displayName = Name;
    }
    public String getPhoneNum() { return phoneNum; }
    public String getDisplayName() { return  displayName; }
    public void setPhoneNum(String num) { phoneNum = num; }
    public void setDisplayName(String name) {displayName=name;}
}
