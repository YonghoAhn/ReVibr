package com.valuecomposite.revibr;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import java.util.ArrayList;

/**
 * Created by anyongho on 2017. 9. 23..
 */

public class ContactManager {

    private Context context;

    public ContactManager(Context context)
    {
        this.context = context;
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
                if (phonenumber.length() == 10) {
                    phonenumber = phonenumber.substring(0, 3) + "-"
                            + phonenumber.substring(3, 6) + "-"
                            + phonenumber.substring(6);
                } else if (phonenumber.length() > 8) {
                    phonenumber = phonenumber.substring(0, 3) + "-"
                            + phonenumber.substring(3, 7) + "-"
                            + phonenumber.substring(7);
                }

                PhoneBookItem contact = new PhoneBookItem();
                contact.setId(contactCursor.getLong(0));
                contact.setPhoneNumber(phonenumber);
                contact.setDisplayName(contactCursor.getString(2));
                contactlist.add(contact);

            } while (contactCursor.moveToNext());
        }

        return contactlist;

    }
}
