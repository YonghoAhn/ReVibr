package com.valuecomposite.revibr;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Debug;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.*;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

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
    static boolean isEnglish = false;
    static boolean isNumeric = false;
    static BrailleConverter BC = new BrailleConverter();

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
        Log.d("MisakaMOE","Content : " + Character.toString(chars[0]));
        Log.d("MisakaMOE","Content : " + new String("" + chars[0]).matches(".*[a-z|A-Z]"));
        for(int i = 0; i < chars.length;i++)
        {
            Log.d("MisakaMOE","Enter For");

            //c = c;
            //Log.d("MisakaMOE","Content : " + chars.toString());
            Log.d("MisakaMOE","Content : " + chars[0]);
            String str = Character.toString(chars[i]);
            try {

                if (new String("" + chars[0]).matches(".*[a-z|A-Z]")) //Is it English
                {
                    Log.d("MisakaMOE","Alphabet");
                    isNumeric = false;
                    if (!isEnglish) {
                        BrailleContent.add("001");
                        BrailleContent.add("011");
                        isEnglish = true;
                        Log.d("MisakaMOE","First Alphabet");
                    }
                    String s = BC.getEnglishBraille(chars[i]);
                    Log.d("MisakaMOE","Content is " + s);
                    BrailleContent.add(s.substring(0, 3));
                    BrailleContent.add(s.substring(3, 6));
                } else if (Character.isDigit(chars[i])) //Is It Numeric
                {
                    Log.d("MisakaMOE","Numeric ");
                    isEnglish = false;
                    if (!isNumeric) {
                        BrailleContent.add("001");
                        BrailleContent.add("111");
                        Log.d("MisakaMOE","First Numeric");
                    }
                    String s = BC.getNumericBraille(chars[i]);
                    Log.d("MisakaMOE","Content is " + s);
                    BrailleContent.add(s.substring(0, 3));
                    BrailleContent.add(s.substring(3, 6));
                } else //Is It Korean Or Special Text Or Space
                {
                    isEnglish = false;
                    isNumeric = false;
                    if (!new String("" + chars[0]).matches("[0-9|a-z|A-Z|ㄱ-ㅎ|ㅏ-ㅣ|가-힝]*")) //특수문자면
                    {
//Ignore It
                        Log.d("MisakaMOE","Special Character");
                    } else if (chars[i] == ' ') {
//Space, 000 000 Add
                        Log.d("MisakaMOE","Space");
                        BrailleContent.add("000");
                        BrailleContent.add("000");
                    } else //Hangul
                    {
                        Log.d("MisakaMOE","Hangul");
//한글자만 있는것들
//초성 아니면 중성 단독이면 그냥 처리
//결합된 조합자면 분리 후 처리
                        if (str.matches(".*[ㄱ-ㅎ]")) //초성류
                        {
                            Log.d("MisakaMOE","Chosung");
                            if (chars[i] == 'ㅇ') {
                                Log.d("MisakaMOE","초성이 ㅇ");
                                BrailleContent.add("110");
                                BrailleContent.add("110");
                            } else {

                                if(chars[i] == 'ㄲ'||chars[i] == 'ㄸ'||chars[i] == 'ㅃ'||chars[i] == 'ㅆ'||chars[i] == 'ㅉ')
                                {
                                    BrailleContent.add("000");
                                    BrailleContent.add("001");
                                    switch(chars[i])
                                    {
                                        case 'ㄲ':
                                            chars[i] = 'ㄱ';
                                            break;
                                        case 'ㄸ':
                                            chars[i] = 'ㄷ';
                                            break;
                                        case 'ㅃ':
                                            chars[i] = 'ㅂ';
                                            break;
                                        case 'ㅆ':
                                            chars[i] = 'ㅅ';
                                            break;
                                        case 'ㅉ':
                                            chars[i] = 'ㅈ';
                                            break;
                                    }
                                }
                                String s = BC.getKoreanBraille(chars[i], 0);
                                Log.d("MisakaMOE","Content is " + s);
                                BrailleContent.add(s.substring(0, 3));
                                BrailleContent.add(s.substring(3, 6));
                            }
                        } else if (str.matches(".*[ㅏ-ㅣ]")) {
                            Log.d("MisakaMOE","Joongsung");
                            String s = BC.getKoreanBraille(chars[i], 1);
                            Log.d("MisakaMOE","Content is " + s);
                            BrailleContent.add(s.substring(0, 3));
                            BrailleContent.add(s.substring(3, 6));
                        } else {
                            char[] hangul = HangulSupport.HangulAlphabet(chars[i]);
                            int cnt = 0;
                            for (char c1 : hangul) {
                                if (c1 == 'ㅇ' && cnt == 0) {
                                    Log.d("MisakaMOE","Chosung is ㅇ");
                                    BrailleContent.add("110");
                                    BrailleContent.add("110");
                                } else {

                                    String s = BC.getKoreanBraille(c1, cnt);
                                    Log.d("MisakaMOE","Content is " + s);
                                    BrailleContent.add(s.substring(0, 3));
                                    BrailleContent.add(s.substring(3, 6));
                                }
                                cnt++;
                            }
                        }
                    }
                }
            }
            catch(Exception e)
            {
                Log.d("MisakaMOE","Error is " + e.getMessage().toString());
                Toast.makeText(getApplicationContext(),e.getMessage().toString(),Toast.LENGTH_SHORT).show();
            }
        }
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
        if(DataManager.VibrateMode == 1) {
            if (count == 0)
                CurrentBraille = BrailleContent.get(count);
            IsTouched = true;
            binding.brailleText.setText(CurrentBraille);
            parseBraille(false);
        }
        else if(DataManager.VibrateMode == 2)
        {
            CurrentBraille = BrailleContent.get(count);
            char[] a = CurrentBraille.toCharArray();
            for(char c : a)
            {
                int tmp = (int)c;
                if(tmp==0)
                {
                    vibrator.vibrate(300);
                }
                else
                {
                    vibrator.vibrate(100);
                }
            }
            count++;
        }
        else
        {
            CurrentBraille = BrailleContent.get(count++)+BrailleContent.get(count);
            char[] a = CurrentBraille.toCharArray();
            for(char c : a)
            {
                int tmp = (int)c;
                if(tmp==0)
                {
                    vibrator.vibrate(300);
                }
                else
                {
                    vibrator.vibrate(100);
                }
            }
            count++;
        }
    }

    public static void onFling() //쓸어내리기
    {
        if(IsTouched)
        {
            CurrentBraille = BrailleContent.get(count);
            parseBraille(true);
            binding.brailleText.setText(CurrentBraille);
            count++;
            binding.BrailleChar.setText(Integer.toString(count));
            if(count >= BrailleContent.size()) {
                vibrator.vibrate(1000);
                count = 0;
            }
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