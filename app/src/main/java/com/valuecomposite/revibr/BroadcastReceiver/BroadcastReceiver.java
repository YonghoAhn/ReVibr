package com.valuecomposite.revibr.BroadcastReceiver;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.widget.Toast;

import com.valuecomposite.revibr.Activities.ReceiveActivity;
import com.valuecomposite.revibr.Services.MMSReceiverService;
import com.valuecomposite.revibr.utils.DataManager;
import com.valuecomposite.revibr.utils.PhoneBookItem;

import java.util.Date;

public class BroadcastReceiver extends android.content.BroadcastReceiver {

    PhoneBookItem phoneBookItem;
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        String action = intent.getAction();
        if(action.equals("android.provider.Telephony.SMS_RECEIVED") || action.equals("android.provider.Telephony.SMS_DELIVER"))
        {
            Object messages[] = (Object[])bundle.get("pdus");
            SmsMessage smsMessage[] = new SmsMessage[messages.length];
            for(int i = 0;i<messages.length;i++)
            {
                smsMessage[i] = SmsMessage.createFromPdu((byte[])messages[i]);
            }

            //SMS 수신 시간간
            Date curDate = new Date(smsMessage[0].getTimestampMillis());
            String time = curDate.toString();

            //SMS 수신 번호
            String number = smsMessage[0].getOriginatingAddress();

            //SMS 수신 번호가 등록된 사람인가?
            String person = "모르는 번호";


            //SMS 수신 메세지
            String message = smsMessage[0].getMessageBody().toString();

            Intent receiveIntent = new Intent(context, ReceiveActivity.class);
            DataManager.CurrentSMS.setBody(message);
            DataManager.CurrentSMS.setPhoneNum(number);
            DataManager.CurrentSMS.setTime(time);
            DataManager.CurrentSMS.setDisplayName(person);
            //receiveIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            receiveIntent.putExtra("IsReceiver",true);
            receiveIntent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            receiveIntent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);

            context.startActivity(receiveIntent);
        }

        else if (action.equals("SMS_SENT_ACTION")) {
            switch (getResultCode()) {
                case Activity.RESULT_OK:
                    // 전송 성공
                    Toast.makeText(context, "전송 완료", Toast.LENGTH_SHORT).show();
                    break;
                case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                    // 전송 실패
                    Toast.makeText(context, "전송 실패", Toast.LENGTH_SHORT).show();
                    break;
                case SmsManager.RESULT_ERROR_NO_SERVICE:
                    // 서비스 지역 아님
                    Toast.makeText(context, "서비스 지역이 아닙니다", Toast.LENGTH_SHORT).show();
                    break;
                case SmsManager.RESULT_ERROR_RADIO_OFF:
                    // 무선 꺼짐
                    Toast.makeText(context, "무선(Radio)가 꺼져있습니다", Toast.LENGTH_SHORT).show();
                    break;
                case SmsManager.RESULT_ERROR_NULL_PDU:
                    // PDU 실패
                    Toast.makeText(context, "PDU Null", Toast.LENGTH_SHORT).show();
                    break;
            }
        } else if (action.equals("SMS_DELIVERED_ACTION")) {
            switch (getResultCode()) {
                case Activity.RESULT_OK:
                    // 도착 완료
                    Toast.makeText(context, "SMS 도착 완료", Toast.LENGTH_SHORT).show();
                    Intent scActivity = new Intent(context, ReceiveActivity.class);
                    //scActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    scActivity.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                    scActivity.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);

                    context.startActivity(scActivity);
                    break;
                case Activity.RESULT_CANCELED:
                    // 도착 안됨
                    Toast.makeText(context, "SMS 도착 실패", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}
