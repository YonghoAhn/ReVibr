package com.valuecomposite.revibr.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.valuecomposite.revibr.R;
import com.valuecomposite.revibr.Services.MMSReceiverService;
import com.valuecomposite.revibr.utils.ApplicationController;
import com.valuecomposite.revibr.utils.DataManager;
import com.valuecomposite.revibr.utils.Initializer;
import com.valuecomposite.revibr.utils.TTSManager;

public class SplashActivity extends Activity implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener{
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
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)   != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)           != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)      != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)           != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)       != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)         != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.MODIFY_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {

                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

// AlertDialog 셋팅
                    alertDialogBuilder
                            .setMessage("권한을 허용하지 않아 앱을 사용하실 수 없습니다.")
                            .setCancelable(false)
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    finish();
                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();

// 다이얼로그 보여주기
                    alertDialog.show();
                } else {
                    requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_CONTACTS, Manifest.permission.SEND_SMS,
                            Manifest.permission.INTERNET, Manifest.permission.RECORD_AUDIO, Manifest.permission.MODIFY_PHONE_STATE}, 200);
                }
            else
                //all of dangerous permissions are confirmed.
                Initializer.Instantiate(getApplicationContext());
        }
        else { //pre-MarshMellow : Already accepted
            Initializer.Instantiate(getApplicationContext());
        }

        Intent service = new Intent(this,MMSReceiverService.class);
        startService(service);
        ttsManager = TTSManager.getInstance(getApplicationContext());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Initializer.Instantiate(getApplicationContext());
        }
        else
        {
            //WTF why rejected
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

// AlertDialog 셋팅
            alertDialogBuilder
                    .setMessage("권한을 허용하지 않으면 앱을 사용하실 수 없습니다.")
                    .setCancelable(false)
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    });

            AlertDialog alertDialog = alertDialogBuilder.create();

// 다이얼로그 보여주기
            alertDialog.show();


        }
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
            //ttsManager.speak("검색");
            DataManager.MODE = 1; //Set Mode to 1 (Search mode)
            Intent searchActivityIntent = new Intent(getApplicationContext(), SendActivity.class);
            searchActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplicationContext().startActivity(searchActivityIntent);

        } else if ((e1.getX() - e2.getX() > 0) && (e1.getY() - e2.getY() > 0)) {
            //왼쪽 위 대각선 드래그 받기모션
            //수신함으로 보내버리기
            //PhoneBookActivity
            //최신순으로 결과->문자리스트 or 문자리스트
            ttsManager.speak("수신함");
            //모드를 0으로 설정(Send모드)
            DataManager.MODE = 0;
            //먼저 ContactsList를 초기화해서 다시 돌려둔다.
            Initializer.Instantiate(getApplicationContext());
            Intent phoneBookActivityIntent = new Intent(getApplicationContext(), PhoneBook.class);
            phoneBookActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplicationContext().startActivity(phoneBookActivityIntent);
        } else if((e1.getX() - e2.getX() > 0)&&(e1.getY()-e2.getY() < 0)){
            //왼쪽 아래 드래그
            //옵션 띄워줘야함
            ttsManager.speak("옵션");
            Intent optActivityIntent = new Intent(getApplicationContext(), OptionActivity.class);
            optActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplicationContext().startActivity(optActivityIntent);

        }
        return true;
    }


}
