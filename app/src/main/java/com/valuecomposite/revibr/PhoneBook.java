package com.valuecomposite.revibr;

import android.Manifest;
import android.app.VoiceInteractor;
import android.content.*;
import android.content.BroadcastReceiver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.valuecomposite.revibr.databinding.ActivityPhonebookBinding;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.valuecomposite.revibr.DataManager.mContext;

/**
 * Created by ayh07 on 8/10/2017.
 */

public class PhoneBook extends AppCompatActivity implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener{
    static ActivityPhonebookBinding binding;
    private GestureDetectorCompat gDetector;
    TTSManager ttsManager;
    static final int ZERO = 0;
    static final int GESTURE_LIMIT = 250;
    static int PhoneBookPosition = 0;
    private Tracker mTracker;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_phonebook);
        mContext = getApplicationContext();
        gDetector = new GestureDetectorCompat(this,this);
        ApplicationController application = (ApplicationController) getApplication();
        mTracker = application.getDefaultTracker();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.MODIFY_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)

                requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_CONTACTS, Manifest.permission.SEND_SMS,
                        Manifest.permission.INTERNET, Manifest.permission.RECORD_AUDIO, Manifest.permission.MODIFY_PHONE_STATE}, 200);
        }
        Initialize();
        //getSMSTest();
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



    public String getPreferences(String key, String subkey){
        SharedPreferences pref = getSharedPreferences(key, MODE_PRIVATE);
        return pref.getString(subkey, "");
    }

    public void savePreferences(String key, String subkey, String content){
        SharedPreferences pref = getSharedPreferences(key, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(subkey, content);
        editor.commit();
    }

    public void Initialize()
    {
        ContactManager contactManager = new ContactManager(getApplicationContext());
        DataManager.PBItems = contactManager.getContactList();
        ttsManager = new TTSManager(getApplicationContext());
        if(getPreferences("setting","mode").equals(""))
            savePreferences("setting","mode","1");
        PBDisplay(0);
    }

    public void PBDisplay(int pos)
    {
        PhoneBookItem p = DataManager.PBItems.get(pos);
        binding.number1.setText(p.getPhoneNumber());
        binding.name1.setText(p.getDisplayName());
        PhoneBookItem p2 = DataManager.PBItems.get(pos+1);
        binding.number2.setText(p2.getPhoneNumber());
        binding.name2.setText(p2.getDisplayName());

    }

    @Override
    public boolean onTouchEvent(MotionEvent e)
    {
        gDetector.onTouchEvent(e);
        return super.onTouchEvent(e);
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return true;
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
        } else if (Math.abs(e1.getX() - e2.getX()) < GESTURE_LIMIT && (e2.getY() - e1.getY() > ZERO)) {
            //아래로 드래그
            Toast.makeText(mContext, "DOWN", Toast.LENGTH_SHORT).show();
        } else if (Math.abs(e1.getY() - e2.getY()) < GESTURE_LIMIT && (e1.getX() - e2.getX() > ZERO)) {
            //오른쪽 드래그
            Toast.makeText(mContext, "RIGHT", Toast.LENGTH_SHORT).show();
            nextDisplay('n');
        } else if (Math.abs(e1.getY() - e2.getY()) < GESTURE_LIMIT && (e2.getX() - e1.getX() > ZERO)) {
            //왼쪽 드래그
            Toast.makeText(mContext, "LEFT", Toast.LENGTH_SHORT).show();
            nextDisplay('b');
        } else if ((e2.getX() - e1.getX() > ZERO) && (e2.getY() - e1.getY() > ZERO)) {
            //오른쪽 아래 대각선 드래그
            Toast.makeText(getApplicationContext(), "message sending activity", Toast.LENGTH_SHORT).show();
            Intent smsActivityIntent = new Intent(getApplicationContext(), SendActivity.class);
            smsActivityIntent.putExtra("number", DataManager.PBItems.get(PhoneBookPosition).getPhoneNumber());
            smsActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplicationContext().startActivity(smsActivityIntent);
        } else if ((e1.getX() - e2.getX() > 0) && (e1.getY() - e2.getY() > 0)) {
            //왼쪽 위 대각선 드래그
            Toast.makeText(getApplicationContext(), "message receiving activity", Toast.LENGTH_SHORT).show();
            DataManager.CurrentSMS = new SMSItem("11/24","01043406162","밸류컴포짓","안녕하세요! 밸류컴포짓 입니다. 이제 진동은 또 하나의 언어입니다. 진동점자를 느껴보세요.");
            Intent scActivityIntent = new Intent(getApplicationContext(), ReceiveActivity.class);
            scActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplicationContext().startActivity(scActivityIntent);
        } else if((e1.getX() - e2.getX() > 0)&&(e1.getY()-e2.getY() < 0)){
            Intent optActivityIntent = new Intent(getApplicationContext(), OptionActivity.class);
            optActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplicationContext().startActivity(optActivityIntent);
        }
        else if (Math.abs(e1.getX() - e2.getX()) > 250 && (e1.getY() - e2.getY() > 0) && (e1.getX() - e2.getX()) < 0 ) //오른쪽 위로 슬라이드
        {
            ttsManager.speak(binding.name1.getText().toString() + " " + binding.number1.getText().toString()); //이름 번호 말한다
        }
        else
        {
            Toast.makeText(mContext, "nothing on gesture", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    private void nextDisplay(char c) {
        if(c == 'n')
        {
            PhoneBookPosition+=2;
            if(PhoneBookPosition>=DataManager.PBItems.size())
                PhoneBookPosition-=2;
            PBDisplay(PhoneBookPosition);
        }
        else
        {
            PhoneBookPosition-=2;
            if(PhoneBookPosition<0)
                PhoneBookPosition = 0;
            PBDisplay(PhoneBookPosition);
        }

    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent motionEvent) {
        return false;
    }
}
