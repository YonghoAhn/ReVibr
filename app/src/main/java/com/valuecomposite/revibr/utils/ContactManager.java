package com.valuecomposite.revibr.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by anyongho on 2017. 9. 23..
 */

public class ContactManager {

    private Context context;
    static ContactManager instance=null;
    public ContactManager(Context context)
    {
        this.context = context;
    }

    public static ContactManager getInstance(Context context) {
        if(instance!=null)
            return instance;
        else
            return (instance=new ContactManager(context));
    }

    public ArrayList<PhoneBookItem> getContactList() {

        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

        String[] projection = new String[] {
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID, // 연락처 ID -> 사진 정보 가져오는데 사용
                ContactsContract.CommonDataKinds.Phone.NUMBER,        // 연락처
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME }; // 연락처 이름.

        String[] selectionArgs = null;

        String sortOrder = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
                + " COLLATE LOCALIZED ASC";

        Cursor contactCursor = context.getContentResolver().query(uri, projection, null,selectionArgs, sortOrder);

        ArrayList<PhoneBookItem> contactlist = new ArrayList<>();

        if (contactCursor.moveToFirst()) {
            do {
                String phonenumber = contactCursor.getString(1).replaceAll("-","");
                PhoneBookItem contact = new PhoneBookItem();
                contact.setId(contactCursor.getLong(0));
                contact.setPhoneNumber(phonenumber);
                contact.setDisplayName(contactCursor.getString(2));
                contact.setChosung(HangulSupport.CreateChosungString(contact.getDisplayName()));
                contactlist.add(contact);

            } while (contactCursor.moveToNext());
        }

        return contactlist;

    }

    public ArrayList<SMSItem> getSmsList(Context context,String number)
    {
        ArrayList<SMSItem> smsItems = new ArrayList<>();

        Uri uri = Uri.parse("content://sms/");
        Cursor c = context.getContentResolver().query(uri, null, null ,null,null);
        if(c.moveToFirst()) {
            for(int i=0; i < c.getCount(); i++) {

                SMSItem sms = new SMSItem();
                sms.setBody(c.getString(c.getColumnIndexOrThrow("body")).toString());
                sms.setTime(c.getString(c.getColumnIndexOrThrow("date")).toString());
                sms.setPhoneNum(c.getString(c.getColumnIndexOrThrow("address")).toString());
                //smsList.add(sms);
                if(sms.getPhoneNum().equals(number)){
                    smsItems.add(sms);
                }
                String address  = c.getString(c.getColumnIndexOrThrow("address")).toString();
                String mbody    = c.getString(c.getColumnIndexOrThrow("body")).toString();
                String mdate    = c.getString(c.getColumnIndexOrThrow("date")).toString();
                Date dt = new Date(Long.valueOf(mdate));
                StringBuilder msgString = new StringBuilder();
                msgString.append(address + "<-||->");
                msgString.append(mbody  + "<-||->");
                msgString.append(dt  + "<-||->");
                msgString.append(mdate + "<--!-->");
                //Toast.makeText(context,msgString.toString(),Toast.LENGTH_SHORT).show();
                c.moveToNext();
            }
        }
        c.close();


        return smsItems;
    }
}
