package com.valuecomposite.revibr.utils.Messages;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.ArrayList;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.valuecomposite.revibr.Activities.ReceiveActivity;
import com.valuecomposite.revibr.utils.DataManager;


public class MMSMon extends Thread {
    private boolean isMonitor = false;
    private boolean isRun = true;
    private Context context;

    private ArrayList<String> idList = new ArrayList<>(); // 프로그램 시작시에 이 리스트에 기존에 받아놓았던 MMS의 키값들을 저장한다. 즉, 새로 들어온 것만 처리한다.

    public MMSMon(Context context) {
        this.context = context;
    }

    public void startMonitor() {

        setIdList();
        isMonitor = true;

    }

    public void stopMonitor() {
        isMonitor = false;
    }

    public void destroyMonitor() {
        isRun = false;
    }

    private void setIdList() {

        idList.clear();
        final String[] projection = new String[] {"_id"};
        Uri uri = Uri.parse("content://mms/inbox");
        Cursor query = context.getContentResolver().query(uri, projection, null, null, "date DESC");

        if ( query.moveToFirst() ) {

            do {

                String mmsId = query.getString(query.getColumnIndex("_id"));

                idList.add(mmsId);

            } while( query.moveToNext() );

        }
        query.close();
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
    {
        String addrSelection = "type=137 AND msg_id=" + id;
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

    @Override
    public void run() {
        while( isRun ) {


            try {
                Thread.sleep(5000);  // 5초 단위로 검사하도록 지정했다. 원하는 시간대로 바꾸면 된다.

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if ( ! isMonitor ) continue;


            final String[] projection = new String[] {"_id", "ct_t", "date"};
            Uri uri = Uri.parse("content://mms/inbox");
            Cursor query = context.getContentResolver().query(uri, projection, null, null, "date DESC");

            if ( query.moveToFirst() ) {

                do {

                    String mmsId = query.getString(query.getColumnIndex("_id"));
                    String date = query.getString(query.getColumnIndex("date"));

                    if ( idList.contains(mmsId) ) continue;

                    String mmsType = query.getString(query.getColumnIndex("ct_t"));
                    if ( mmsType != null && mmsType.startsWith("application/vnd.wap.multipart")) {
                        // MMS인 것을 한번 더 확인

                        String incommingNumber = getAddressNumber(context, Integer.parseInt(mmsId));
                        String messageBody = "";
                        String selectionPart = "mid=" + mmsId;
                        Uri uriPart = Uri.parse("content://mms/part");
                        Cursor cur = context.getContentResolver().query(uriPart, null, selectionPart, null, null);
                        if (cur.moveToFirst()) {
                            do {
                                if(idList.contains(mmsId)) break;
                                String partId = cur.getString(cur.getColumnIndex("_id"));
                                String type = cur.getString(cur.getColumnIndex("ct"));
                                if ("text/plain".equals(type)) {
                                    String data = cur.getString(cur.getColumnIndex("_data"));

                                    if (data != null) {
                                        // implementation of this method below
                                        messageBody = getMmsText(context, partId);
                                    }
                                    else {
                                        messageBody = cur.getString(cur.getColumnIndex("text"));
                                    }

                                    if ( incommingNumber.length() > 0 && messageBody.length() > 0   ) {

                                        if ( CommandParser.checkCommand(messageBody, context) ) {
                                            idList.add(mmsId);
                                        } else {
                                            String formKey = StorageManager.getDataString(context, "FORMKEY", "");
                                            //if ( formKey.length() > 0 ) {

                                                int numberIndex = StorageManager.getDataInt(context,  "NUMBERINDEX", 0);
                                                int messageIndex = StorageManager.getDataInt(context, "MESSAGEINDEX", 1);
                                                int dateIndex = StorageManager.getDataInt(context, "DATEINDEX", 2);

                                                if ( numberIndex != messageIndex ) {
                                                    idList.add(mmsId);
                                                    SMSItem smsItem = new SMSItem();
                                                    smsItem.setPhoneNum(incommingNumber);
                                                    smsItem.setBody(messageBody);
                                                    smsItem.setTime(date);
                                                    DataManager.NewMMSItems.add(smsItem);
                                                    DataManager.CurrentSMS = smsItem;


                                                    Intent receiveIntent = new Intent(context, ReceiveActivity.class);
                                                    receiveIntent.putExtra("IsReceiver",true);
                                                    receiveIntent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                                                    receiveIntent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                                                    context.startActivity(receiveIntent);
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
                            } while( cur.moveToNext() );

                            cur.close();
                        }


                    }
                } while( query.moveToNext() );

            }
            query.close();
        }
        Log.v("monitor", "monitor object destroyed");
    }
}