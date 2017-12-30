package com.valuecomposite.revibr.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.*;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.valuecomposite.revibr.utils.ApplicationController;
import com.valuecomposite.revibr.utils.BrailleOutput;
import com.valuecomposite.revibr.utils.ContactManager;
import com.valuecomposite.revibr.utils.DataManager;
import com.valuecomposite.revibr.utils.Initializer;
import com.valuecomposite.revibr.utils.PhoneBookItem;
import com.valuecomposite.revibr.R;
import com.valuecomposite.revibr.utils.SMSItem;
import com.valuecomposite.revibr.utils.TTSManager;
import com.valuecomposite.revibr.databinding.ActivityPhonebookBinding;
import com.valuecomposite.revibr.utils.Vibrator;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static com.valuecomposite.revibr.utils.DataManager.mContext;

/**
 * Created by ayh07 on 8/10/2017.
 */

public class PhoneBook extends BaseActivity{
    //region variables
    static ActivityPhonebookBinding binding;
    static int PhoneBookPosition = 0;
    int count = 0;
    ArrayList<String> braille = new ArrayList<>();
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_phonebook);
        mContext = getApplicationContext();
        gDetector = new GestureDetectorCompat(this,this);
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

    public void Initialize()
    {
        Initializer.Instantiate(getApplicationContext());

        if(DataManager.MODE == 0)
        {
            ttsManager.speak("주소록");
        }
        else
        {
            ttsManager.speak("검색 결과");
        }
        if(DataManager.PBItems.size()==0)
            ttsManager.speak("결과 없음");
        PBDisplay(0);
    }

    public void PBDisplay(int pos)
    {
        PhoneBookItem p = DataManager.PBItems.get(pos);
        binding.number.setText(p.getPhoneNumber());
        binding.name.setText(p.getDisplayName());
        braille = BrailleOutput.parseSMS(binding.name.getText().toString());
        count = 0;
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
    public boolean onSingleTapUp(MotionEvent motionEvent)  {
        //다른 터치메소드가 아니고 그냥 누른거면
        //읽어주고 진동도 울려줘야 하는데

        //연락처가 바뀌면 자동으로 이름 점자로 변환함
        //카운트 돌려서 끊어서 6점씩 불러오면 된다.
        //parseSMS 공용으로 사용하므로, 3개씩 리턴될것임.
        //두번가져와야한다.
        //근데 6점식이라 해도 여러번 나오는데 그동안 TTS는 계속읽나?
        if(count == 0) //카운트가 0일때만 읽어주면 된다.
        {
            ttsManager.speak(binding.name.getText().toString());
        }
        //0부터 가져온다.
        if(count <= braille.size()-1) {
            char[] chars = (braille.get(count++) + braille.get(count++)).toCharArray();
            vibrator.vibrate((chars[0] == '0' ? 100 : 300), (chars[1] == '0' ? 100 : 300), (chars[2] == '0' ? 100 : 300), (chars[3] == '0' ? 100 : 300), (chars[4] == '0' ? 100 : 300), (chars[5] == '0' ? 100 : 300));
        }

        return true;
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
            //보내기모션
            DataManager.MODE = 0; //이건 뭐가 됐든 보내거나 받거나 할때만 뜨므로, 보내기 모션이라면 무조건 모드를 0으로 바꿔줘야한다. 뒤로가기 누르면 1로 바꾸던가
            Toast.makeText(getApplicationContext(), "message sending activity", Toast.LENGTH_SHORT).show();
            Intent smsActivityIntent = new Intent(getApplicationContext(), SendActivity.class);
            smsActivityIntent.putExtra("number", DataManager.PBItems.get(PhoneBookPosition).getPhoneNumber());
            smsActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplicationContext().startActivity(smsActivityIntent);
        } else if ((e1.getX() - e2.getX() > 0) && (e1.getY() - e2.getY() > 0)) {
            //왼쪽 위 대각선 드래그
            Toast.makeText(getApplicationContext(), "message receiving activity", Toast.LENGTH_SHORT).show();
            DataManager.CurrentSMS = new SMSItem("",DataManager.PBItems.get(PhoneBookPosition).getPhoneNumber(),binding.name.getText().toString(),"");
            Intent scActivityIntent = new Intent(getApplicationContext(), ReceiveActivity.class);
            scActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplicationContext().startActivity(scActivityIntent);
        }
        else if (Math.abs(e1.getX() - e2.getX()) > 250 && (e1.getY() - e2.getY() > 0) && (e1.getX() - e2.getX()) < 0 )
        {
            //오른쪽 위로 슬라이드
            ttsManager.speak(binding.name.getText().toString() + " " + binding.number.getText().toString()); //이름 번호 말한다
        } else if((e1.getX() - e2.getX() > 0)&&(e1.getY()-e2.getY() < 0)){
            //왼쪽 아래 드래그
            finish();
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
