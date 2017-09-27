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
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.valuecomposite.revibr.databinding.ActivityPhonebookBinding;
import static com.valuecomposite.revibr.DataManager.mContext;

/**
 * Created by ayh07 on 8/10/2017.
 */

public class PhoneBook extends AppCompatActivity implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener{
    BroadcastReceiver receiver = new com.valuecomposite.revibr.BroadcastReceiver();
    static ActivityPhonebookBinding binding;
    private GestureDetectorCompat gDetector;
    LinearLayout gestureOverlay;
    TTSManager ttsManager;
    static final int ZERO = 0;
    static final int GESTURE_LIMIT = 250;
    static int PhoneBookPosition = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_phonebook);
        mContext = getApplicationContext();
        gDetector = new GestureDetectorCompat(this,this);
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
    }

    public void Initialize()
    {
        ContactManager contactManager = new ContactManager(getApplicationContext());
        DataManager.PBItems = contactManager.getContactList();
        ttsManager = new TTSManager(getApplicationContext());
        PBDisplay(0);
    }

    public void PBDisplay(int pos)
    {
        PhoneBookItem p = DataManager.PBItems.get(pos);
        binding.number.setText(p.getPhoneNumber());
        binding.name.setText(p.getDisplayName());
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
            DataManager.CurrentSMS = new SMSItem(" "," "," "," ");
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
            ttsManager.speak(binding.name.getText().toString() + " " + binding.number.getText().toString()); //이름 번호 말한다
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
            PhoneBookPosition++;
            if(PhoneBookPosition>=DataManager.PBItems.size())
                PhoneBookPosition--;
            PBDisplay(PhoneBookPosition);
        }
        else
        {
            PhoneBookPosition--;
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
