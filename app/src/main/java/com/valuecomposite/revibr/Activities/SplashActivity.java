package com.valuecomposite.revibr.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.valuecomposite.revibr.R;
import com.valuecomposite.revibr.utils.ApplicationController;
import com.valuecomposite.revibr.utils.ContactManager;
import com.valuecomposite.revibr.utils.DataManager;
import com.valuecomposite.revibr.utils.Initializer;
import com.valuecomposite.revibr.utils.SMSItem;
import com.valuecomposite.revibr.utils.TTSManager;

public class SplashActivity extends AppCompatActivity implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener{
    private GestureDetectorCompat gDetector;
    private Tracker mTracker;
    TTSManager ttsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ApplicationController application = (ApplicationController) getApplication();
        mTracker = application.getDefaultTracker();
        gDetector = new GestureDetectorCompat(this,this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (    ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)   != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)           != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)      != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)           != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)       != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)         != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.MODIFY_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)

                requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_CONTACTS, Manifest.permission.SEND_SMS,
                        Manifest.permission.INTERNET, Manifest.permission.RECORD_AUDIO, Manifest.permission.MODIFY_PHONE_STATE}, 200);
        }
        Initializer.Instantiate(getApplicationContext());
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        //using tracker variable to set Screen Name
        mTracker.setScreenName("SplashActivity");
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
        if ((e2.getX() - e1.getX() > 0) && (e2.getY() - e1.getY() > 0)) {
            //오른쪽 아래 대각선 드래그 보내기 모션
            //초성검색
            //초성검색->검색결과->문자보내기 or 문자리스트
            DataManager.MODE = 1; //Set Mode to 1 (Search mode)

        } else if ((e1.getX() - e2.getX() > 0) && (e1.getY() - e2.getY() > 0)) {
            //왼쪽 위 대각선 드래그 받기모션
            //수신함으로 보내버리기
            //PhoneBookActivity
            //최신순으로 결과->문자리스트 or 문자리스트

        }
        return true;
    }


}
