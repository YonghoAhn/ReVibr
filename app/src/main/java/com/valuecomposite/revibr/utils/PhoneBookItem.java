package com.valuecomposite.revibr.utils;

/**
 * Created by ayh07 on 8/10/2017.
 */

public class PhoneBookItem {
    private String phoneNum, displayName,chosung;
    private long id;
    public PhoneBookItem() { }
    public PhoneBookItem(String Num, String Name,String chosung)
    {
        phoneNum = Num; displayName = Name;this.chosung = chosung;
    }
    public String getPhoneNumber() { return phoneNum; }
    public String getDisplayName() { return  displayName; }
    public void setPhoneNumber(String num) { phoneNum = num; }
    public void setDisplayName(String name) {displayName=name;}

    public void setId(long id) {
        this.id = id;
    }

    public String getChosung() {
        return chosung;
    }

    public void setChosung(String chosung) {
        this.chosung = chosung;
    }
}
