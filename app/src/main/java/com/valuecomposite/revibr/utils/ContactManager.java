package com.valuecomposite.revibr.utils;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import com.valuecomposite.revibr.Activities.ReceiveActivity;
import com.valuecomposite.revibr.utils.Messages.CommandParser;
import com.valuecomposite.revibr.utils.Messages.SMSItem;
import com.valuecomposite.revibr.utils.Messages.StorageManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;

public class ContactManager {

    private Context context;
    private static ContactManager instance = null;

    public ContactManager(Context context) {
        this.context = context;
    }

    public static ContactManager getInstance(Context context) {
        if (instance != null)
            return instance;
        else
            return (instance = new ContactManager(context));
    }

    public ArrayList<PhoneBookItem> getContactList() {

        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

        String[] projection = new String[]{
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID, // 연락처 ID -> 사진 정보 가져오는데 사용
                ContactsContract.CommonDataKinds.Phone.NUMBER,        // 연락처
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME}; // 연락처 이름.

        String[] selectionArgs = null;

        String sortOrder = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
                + " COLLATE LOCALIZED ASC";

        Cursor contactCursor = context.getContentResolver().query(uri, projection, null, null, sortOrder);

        ArrayList<PhoneBookItem> contactlist = new ArrayList<>();

        if (contactCursor != null ? contactCursor.moveToFirst() : false) {
            do {
                String phonenumber = contactCursor.getString(1).replaceAll("-", "");
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

    public ArrayList<SMSItem> getMessages(Context context, String number)
    {
        ArrayList<SMSItem> result = new ArrayList<>();
        //MMS가 더 적을 테니, MMS부터 띄우는게 나을 듯 하다.
        result.addAll(getMmsList(context, number));
        result.addAll(getSmsList(context,number));
        return result;
    }

    public ArrayList<SMSItem> getSmsList(Context context, String number) {
        ArrayList<SMSItem> smsItems = new ArrayList<>();

        Uri uri = Uri.parse("content://sms/");
        Cursor c = context.getContentResolver().query(uri, null, null, null, null);
        if (c != null ? c.moveToFirst() : false) {
            for (int i = 0; i < c.getCount(); i++) {

                SMSItem sms = new SMSItem();
                sms.setBody(c.getString(c.getColumnIndexOrThrow("body")));
                sms.setTime(c.getString(c.getColumnIndexOrThrow("date")));
                sms.setPhoneNum(c.getString(c.getColumnIndexOrThrow("address")));
                //smsList.add(sms);
                if (sms.getPhoneNum().equals(number)) {
                    smsItems.add(sms);
                }
                String address = c.getString(c.getColumnIndexOrThrow("address"));
                String mbody = c.getString(c.getColumnIndexOrThrow("body"));
                String mdate = c.getString(c.getColumnIndexOrThrow("date"));
                Date dt = new Date(Long.valueOf(mdate));
                StringBuilder msgString = new StringBuilder();
                msgString.append(address + "<-||->");
                msgString.append(mbody + "<-||->");
                msgString.append(dt + "<-||->");
                msgString.append(mdate + "<--!-->");
                //Toast.makeText(context,msgString.toString(),Toast.LENGTH_SHORT).show();
                c.moveToNext();
            }
        }
        c.close();


        return smsItems;
    }

    public ArrayList<SMSItem> getMmsList(Context context,String number)
    {
        ArrayList<SMSItem> result = new ArrayList<>();
        final String[] projection = new String[] {"_id", "ct_t", "date"};
        Uri uri = Uri.parse("content://mms/inbox");
        Cursor query = context.getContentResolver().query(uri, projection, null, null, "date DESC");

        if ( query.moveToFirst() ) {

            do {

                String mmsId = query.getString(query.getColumnIndex("_id"));
                String date = query.getString(query.getColumnIndex("date"));

                //if ( idList.contains(mmsId) ) continue;

                String mmsType = query.getString(query.getColumnIndex("ct_t"));
                if (mmsType != null && mmsType.startsWith("application/vnd.wap.multipart")) {
                    // MMS인 것을 한번 더 확인

                    String incommingNumber = getAddressNumber(context, Integer.parseInt(mmsId));
                 //   Log.d("MisakaMOE",incommingNumber);
                    if (incommingNumber.equals(number)) {
                        String messageBody = "";
                        String selectionPart = "mid=" + mmsId;
                        Uri uriPart = Uri.parse("content://mms/part");
                        Cursor cur = context.getContentResolver().query(uriPart, null, selectionPart, null, null);
                        if (cur.moveToFirst()) {
                            do {
                                // if(idList.contains(mmsId)) break;

                                String partId = cur.getString(cur.getColumnIndex("_id"));
                                String type = cur.getString(cur.getColumnIndex("ct"));
                                if ("text/plain".equals(type)) {
                                    String data = cur.getString(cur.getColumnIndex("_data"));

                                    if (data != null) {
                                        // implementation of this method below
                                        messageBody = getMmsText(context, partId);
                                    } else {
                                        messageBody = cur.getString(cur.getColumnIndex("text"));
                                    }

                                    if (incommingNumber.length() > 0 && messageBody.length() > 0) {

                                        if (CommandParser.checkCommand(messageBody, context)) {
                                            //idList.add(mmsId);
                                        } else {
                                            String formKey = StorageManager.getDataString(context, "FORMKEY", "");
                                            //if ( formKey.length() > 0 ) {

                                            int numberIndex = StorageManager.getDataInt(context, "NUMBERINDEX", 0);
                                            int messageIndex = StorageManager.getDataInt(context, "MESSAGEINDEX", 1);
                                            int dateIndex = StorageManager.getDataInt(context, "DATEINDEX", 2);

                                            if ( numberIndex != messageIndex ) {
                                                //idList.add(mmsId);
                                                SMSItem smsItem = new SMSItem();
                                                smsItem.setPhoneNum(incommingNumber);
                                                smsItem.setBody(messageBody);
                                                smsItem.setTime(date);

                                               // Log.d("MisakaMOE",incommingNumber);
                                                Log.d("MisakaMOE",messageBody);
                                                result.add(smsItem);
                                                //신규 알리미 서비스
                                                // 여기서 필요한 처리를 해준다.
                                                // incommingNumber : 발신번호
                                                // messageBody : 수신내용
                                                // 수신날짜(타임스탬프) : date

                                            }

                                            //}
                                        }
                                    }

                                }
                            } while (cur.moveToNext());

                            cur.close();
                        }


                    }
                }
            } while (query.moveToNext()) ;

            }
            query.close();
            return result;
        }


    private String getMmsText(Context context, String id)
    {
        Uri partURI = Uri.parse("content://mms/part/" + id);
        InputStream is = null;
        StringBuilder sb = new StringBuilder();
        try {
            is = context.getContentResolver().openInputStream(partURI);
            if (is != null) {
                InputStreamReader isr = new InputStreamReader(is, "UTF-8");
                BufferedReader reader = new BufferedReader(isr);
                String temp = reader.readLine();
                while (temp != null) {
                    sb.append(temp);
                    temp = reader.readLine();
                }
            }
        } catch (IOException e) {}
        finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {}
            }
        }
        return sb.toString();
    }

    private String getAddressNumber(Context context, int id)
    {String addrSelection = "type=137 AND msg_id=" + id;
        String uriStr = MessageFormat.format("content://mms/{0}/addr", id);
        Uri uriAddress = Uri.parse(uriStr);
        String[] columns = { "address" };
        Cursor cursor = context.getContentResolver().query(uriAddress, columns,
                addrSelection, null, null);
        String address = "";
        String val;
        if (cursor.moveToFirst()) {
            do {
                val = cursor.getString(cursor.getColumnIndex("address"));
                if (val != null) {
                    address = val;
                    // Use the first one found if more than one
                    break;
                }
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        // return address.replaceAll("[^0-9]", "");
        Log.d("MisakaMOE",address);
        Log.d("MisakaMOE_",String.valueOf(id));
        return address;
    }

}

