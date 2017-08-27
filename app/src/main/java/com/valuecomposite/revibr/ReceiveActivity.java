package com.valuecomposite.revibr;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.GestureDetector.*;
import android.view.MotionEvent;
import android.view.View;

import com.valuecomposite.revibr.databinding.ActivityReceiveBinding;

import java.util.ArrayList;

/**
 * Created by ayh07 on 8/12/2017.
 */
public class ReceiveActivity extends AppCompatActivity implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener{

    GestureDetector gestureDetector;
    static ActivityReceiveBinding binding;
    static boolean IsTouched = false;
    static ArrayList<String> BrailleContent = new ArrayList<>();
    private static int count = 0;
    static String CurrentBraille = "";
    static Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_receive);
        gestureDetector = new GestureDetector(this, this);
        vibrator = new Vibrator(getApplicationContext());
        parseSMS(DataManager.CurrentSMS);
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        gestureDetector.onTouchEvent(motionEvent);
        return true;
    }

    public void parseSMS(SMSItem smsItem)
    {
        display(smsItem.getTime(),smsItem.getDisplayName(),smsItem.getPhoneNum(),smsItem.getBody());
        //SMS를 진동으로 바꾸기만 하면 됨.
        //3개씩 끊어서 돌려주기?
        char[] chars = smsItem.getBody().toCharArray();
        BrailleConverter BC = new BrailleConverter();
        for(char c : chars)
        {
            if((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) //Is it English
            {
                String s = BC.getEnglishBraille(c);
                BrailleContent.add(s.substring(0,3));
                BrailleContent.add(s.substring(4,6));
            }
            else if(Character.isDigit(c)) //Is It Numeric
            {
                String s = BC.getNumericBraille(c);
                BrailleContent.add(s.substring(0,3));
                BrailleContent.add(s.substring(4,6));
            }
            else //Is It Korean Or Special Text Or Space
            {
                if(containsSpecialCharacter(new String(""+c))) //특수문자면
                {
                    //Ignore It
                }
                else if(c == ' ')
                {
                    //Space, 000 000 Add

                }
                else //Hangul
                {
                    //한글자만 있는것들
                    //초성 아니면 중성 단독이면 그냥 처리
                    //결합된 조합자면 분리 후 처리
                    if(new String(""+c).matches(".*[ㄱ-ㅎ]")) //초성류
                    {
                        BC.getKoreanBraille(c,0);
                    }
                    else if(new String(""+c).matches(".*[ㅏ-ㅣ]"))
                    {
                        BC.getKoreanBraille(c,1);
                    }
                    else
                    {
                        char[] hangul = HangulSupport.HangulAlphabet(c);
                        for(char c1 : hangul)
                        {
                            BC.getKoreanBraille(c1,1);
                        }
                    }
                }
            }
        }
    }

    public boolean containsSpecialCharacter(String s) {
        return (s == null) ? false : s.matches("[^A-Za-z0-9 ]");
    }

    public void display(String time, String name, String number, String content)
    {
        binding.time.setText(time);
        binding.name.setText(name);
        binding.number.setText(number);
        binding.body.setText(content);
    }

    public static void onTouch() //터치
    {
        IsTouched = true;
        CurrentBraille = BrailleContent.get(count);
        parseBraille(false);
    }

    public static void onFling() //쓸어내리기
    {
        if(IsTouched)
        {
            parseBraille(true);
            count++;
            if(count >= BrailleContent.size())
                count = 0;
        }
    }

    public static void parseBraille(boolean IsFling)
    {
        if(!IsFling) { //터치됐다면
            switch (CurrentBraille) {
                case "100":
                    vibrator.vibrate(100);
                    break;
                case "110":
                    vibrator.vibrate(100,100);
                    break;
                case "111":
                    vibrator.vibrate(100,100,100);
                    break;
                case "101":
                    vibrator.vibrate(100);
                    break;
            }
        }
        else
        {
            switch (CurrentBraille) {
                case "010":
                    vibrator.vibrate(100);
                    break;
                case "001":
                    vibrator.vibrate(500);
                    break;
                case "011":
                    vibrator.vibrate(100,100);
                    break;
                case "101":
                    vibrator.vibrate(500);
                    break;
            }
        }
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
        ReceiveActivity.onTouch();
        return true;
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
        if (Math.abs(e1.getX() - e2.getX()) < 250 && (e1.getY() - e2.getY() > 0)) {
            //위로 드래그
            ReceiveActivity.onFling();
        }
        return true;
    }
}