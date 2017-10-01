package com.valuecomposite.revibr;

import android.app.PendingIntent;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.valuecomposite.revibr.databinding.ActivitySendBinding;

import static com.valuecomposite.revibr.DataManager.mContext;
import static com.valuecomposite.revibr.PhoneBook.GESTURE_LIMIT;
import static com.valuecomposite.revibr.PhoneBook.ZERO;
import static com.valuecomposite.revibr.PhoneBook.binding;

/**
 * Created by ayh07 on 8/12/2017.
 */

public class SendActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {
    BroadcastReceiver receiver = new BroadcastReceiver();
    static ActivitySendBinding binding;
    private GestureDetectorCompat gDetector;
    private static String smsNum = "";
    public static int count = 0;
    private Tracker mTracker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_send);
        mContext = getApplicationContext();
        gDetector = new GestureDetectorCompat(this,this);
        Intent getIntent = getIntent();
        smsNum = getIntent.getExtras().getString("number");
        ApplicationController application = (ApplicationController) getApplication();
        mTracker = application.getDefaultTracker();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        //using tracker variable to set Screen Name
        mTracker.setScreenName("PhoneBookActivity");
        //sending the screen to analytics using ScreenViewBuilder() method
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public boolean onTouchEvent(MotionEvent e)
    {
        gDetector.onTouchEvent(e);
        return super.onTouchEvent(e);
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float v, float v1) {
        if (Math.abs(e1.getX() - e2.getX()) < GESTURE_LIMIT && (e1.getY() - e2.getY() > ZERO)) {
            //위로 드래그
            Toast.makeText(mContext, "UP", Toast.LENGTH_SHORT).show();
            colorIdentify(count,true);
            BrailleInput.Input(true);
        } else if (Math.abs(e1.getX() - e2.getX()) < GESTURE_LIMIT && (e2.getY() - e1.getY() > ZERO)) {
            //아래로 드래그
            Toast.makeText(mContext, "DOWN", Toast.LENGTH_SHORT).show();
            colorIdentify(count,false);
            BrailleInput.Input(false);
        } else if (Math.abs(e1.getY() - e2.getY()) < GESTURE_LIMIT && (e1.getX() - e2.getX() > ZERO)) {
            //왼쪽 드래그
            Toast.makeText(mContext, "LEFT", Toast.LENGTH_SHORT).show();
            // 지우기 코드
            try {
                binding.txtSend.setText(binding.txtSend.getText().toString().substring(0, binding.txtSend.getText().toString().length() - 1));
                BrailleInput.flush();
            }
            catch (Exception e)
            {  }
        } else if (Math.abs(e1.getY() - e2.getY()) < GESTURE_LIMIT && (e2.getX() - e1.getX() > ZERO)) {
            //오른쪽 드래그
            Toast.makeText(mContext, "RIGHT", Toast.LENGTH_SHORT).show();
            // 문자 보내기 코드
            sendSMS(binding.txtSend.getText().toString());
        } else if ((e2.getX() - e1.getX() > ZERO) && (e2.getY() - e1.getY() > ZERO)) {
            //오른쪽 아래 대각선 드래그
            //Toast.makeText(getApplicationContext(), "message sending activity", Toast.LENGTH_SHORT).show();
        } else if ((e1.getX() - e2.getX() > 0) && (e1.getY() - e2.getY() > 0)) {
            //왼쪽 위 대각선 드래그
            Toast.makeText(getApplicationContext(), "message sending activity", Toast.LENGTH_SHORT).show();
            BrailleInput.flush();
            finish();
            //뒤로 돌아가기 코드
        } else {
            Toast.makeText(mContext, "ignore", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    public static void AddText(String s)
    {
        String t = binding.txtSend.getText().toString();
        binding.txtSend.setText(t.substring(0,t.length()-1) + s);
    }

    public static void AddChosung(String s)
    {
        binding.txtSend.setText(binding.txtSend.getText() + s);
    }

    private static void sendSMS(String s)
    {
        String smsText = s;
        if(smsNum.length() > 0 && smsText.length() > 0)
        {
            sendSMS(smsNum, smsText);
            binding.txtSend.setText("");
            Toast.makeText(mContext,"전송됨",Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(mContext,"비었습니다.",Toast.LENGTH_SHORT).show();
        }
    }

    private static void sendSMS(String smsNum, String smsText) {
        PendingIntent sentIntent = PendingIntent.getBroadcast(mContext,0,new Intent("SMS_SENT_ACTION"),0);
        PendingIntent deliverIntent = PendingIntent.getBroadcast(mContext,0,new Intent("SMS_DELIVERED_ACTION"),0);
        SmsManager mSmsManager = SmsManager.getDefault();
        mSmsManager.sendTextMessage(smsNum,null,smsText,sentIntent,deliverIntent);
    }

    public static void clearColor()
    {
        binding.idxOne.setBackground(ContextCompat.getDrawable(mContext,R.drawable.roundtext));
        binding.idxTwo.setBackground(ContextCompat.getDrawable(mContext,R.drawable.roundtext));
        binding.idxThree.setBackground(ContextCompat.getDrawable(mContext,R.drawable.roundtext));
        binding.idxFour.setBackground(ContextCompat.getDrawable(mContext,R.drawable.roundtext));
        binding.idxFive.setBackground(ContextCompat.getDrawable(mContext,R.drawable.roundtext));
        binding.idxSix.setBackground(ContextCompat.getDrawable(mContext,R.drawable.roundtext));

    }

    private void colorIdentify(int idx, boolean gesture)
    {
        if(gesture) {
            switch (idx) {
                case 0:
                    binding.idxOne.setBackgroundColor(Color.parseColor("#000000"));
                    count++;
                    break;
                case 1:
                    binding.idxTwo.setBackgroundColor(Color.parseColor("#000000"));
                    count++;
                    break;
                case 2:
                    binding.idxThree.setBackgroundColor(Color.parseColor("#000000"));
                    count++;
                    break;
                case 3:
                    binding.idxFour.setBackgroundColor(Color.parseColor("#000000"));
                    count++;
                    break;
                case 4:
                    binding.idxFive.setBackgroundColor(Color.parseColor("#000000"));
                    count++;
                    break;
                case 5:
                    binding.idxSix.setBackgroundColor(Color.parseColor("#000000"));
                    count++;
                    break;
            }
        }
        else
        {
            switch (idx) {
                case 0:
                    binding.idxOne.setBackgroundColor(Color.parseColor("#eeeeee"));
                    count++;
                    break;
                case 1:
                    binding.idxTwo.setBackgroundColor(Color.parseColor("#eeeeee"));
                    count++;
                    break;
                case 2:
                    binding.idxThree.setBackgroundColor(Color.parseColor("#eeeeee"));
                    count++;
                    break;
                case 3:
                    binding.idxFour.setBackgroundColor(Color.parseColor("#eeeeee"));
                    count++;
                    break;
                case 4:
                    binding.idxFive.setBackgroundColor(Color.parseColor("#eeeeee"));
                    count++;
                    break;
                case 5:
                    binding.idxSix.setBackgroundColor(Color.parseColor("#eeeeee"));
                    count++;
                    break;
            }
        }
    }
}
