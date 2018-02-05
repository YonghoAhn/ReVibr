package com.valuecomposite.revibr.utils.Messages;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by ayh07 on 11/28/2017.
 */

public class SMSManager {
    public ArrayList<SMSItem> getSMSList(Context context, String phoneNum)
    {
        ArrayList<SMSItem> smsItems = new ArrayList<>();
        ContentResolver cr = context.getContentResolver();
        Cursor cursor = cr.query(Uri.parse("content://sms/inbox"),null,"address=?",new String[]{phoneNum}
                ,null);

        int nameidx = cursor.getColumnIndex("address");
        int dateidx = cursor.getColumnIndex("date");
        int bodyidx = cursor.getColumnIndex("body");

        StringBuilder result = new StringBuilder();
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd HH:mm");

        result.append("총 문자갯수 : " + cursor.getCount()+ "개\n");
        int count = 0;
        while (cursor.moveToNext()) {

            String name = cursor.getString(nameidx);
            long date = cursor.getLong(dateidx);
            String sdate = formatter.format(new Date(date));
            String body = cursor.getString(bodyidx);


            // 날짜
            result.append(sdate + ": \n");
            result.append("name" + "");
            // 내용
            result.append(body + "\n");

            SMSItem item = new SMSItem(sdate,phoneNum,"",body);
            smsItems.add(item);
            // 최대 100까지만
            if (count++ == 100) {
                break;
            }

        }
        cursor.close();
        Log.d("MisakaMOE",result.toString());
        return smsItems;
    }
}
