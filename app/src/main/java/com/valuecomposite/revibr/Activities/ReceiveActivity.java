package com.valuecomposite.revibr.Activities;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.view.MotionEvent;

import com.google.android.gms.analytics.HitBuilders;
import com.valuecomposite.revibr.R;
import com.valuecomposite.revibr.databinding.ActivityReceiveBinding;
import com.valuecomposite.revibr.utils.Braille.BrailleConverter;
import com.valuecomposite.revibr.utils.Braille.BrailleOutput;
import com.valuecomposite.revibr.utils.ContactManager;
import com.valuecomposite.revibr.utils.DataManager;
import com.valuecomposite.revibr.utils.PhoneBookItem;
import com.valuecomposite.revibr.utils.Messages.SMSItem;

import java.util.ArrayList;

public class ReceiveActivity extends BaseActivity{

    static ActivityReceiveBinding binding;
    static boolean IsTouched = false;
    static ArrayList<String> BrailleContent = new ArrayList<>();
    private static int count = 0;
    private static int sub_count = 0;
    static String CurrentBraille = "";
    static BrailleConverter BC = new BrailleConverter();
    private ArrayList<SMSItem> smsItems = new ArrayList<>();
    int smsIndex = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_receive);
        gDetector = new GestureDetectorCompat(this, this);

        ContactManager c = ContactManager.getInstance(getApplicationContext());
        smsItems = c.getMessages(getApplicationContext(),DataManager.CurrentSMS.getPhoneNum());

        Intent intent = getIntent();
        boolean extra = intent.getBooleanExtra("IsReceiver",false);
        if(!extra) {
            if (smsItems.size() > 0)
                DataManager.CurrentSMS = smsItems.get(0);
            else {
                DataManager.CurrentSMS = new SMSItem("", DataManager.CurrentSMS.getPhoneNum(), DataManager.CurrentSMS.getDisplayName(), "주고받은 문자가 없습니다.");
                ttsManager.speak("문자 내역이 없습니다.");
            }
        }
        else
        {

        }
        parseSMS(DataManager.CurrentSMS);

    }

    @Override
    protected void onResume()
    {
        super.onResume();
        //using tracker variable to set Screen Name
        mTracker.setScreenName("ReceiveActivity");
        //sending the screen to analytics using ScreenViewBuilder() method
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

    }

    @Override
    protected void onPause()
    {
        super.onPause();
        //finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        gDetector.onTouchEvent(motionEvent);
        return true;
    }



    public void parseSMS(SMSItem smsItem)
    {
        ContactManager contactManager = new ContactManager(getApplicationContext());
        DataManager.PBItems = contactManager.getContactList();
        for(PhoneBookItem phoneBookItem : DataManager.PBItems)
        {
            if(phoneBookItem.getPhoneNumber().replace("-","").equals(smsItem.getPhoneNum()))
                smsItem.setDisplayName(phoneBookItem.getDisplayName().toString());
        }
        display(smsItem.getTime(),smsItem.getDisplayName(),smsItem.getPhoneNum(),smsItem.getBody());

        //SMS를 진동으로 바꾸기만 하면 됨.
        //3개씩 끊어서 돌려주기?
        //이거 ""인거 생각 안함 ㅋㅋㅋㅋㅋ
        if(!(smsItem.getBody() == null||smsItem.getBody().equals(""))){
            BrailleContent = BrailleOutput.parseSMS(smsItem.getBody());
            count = 0;
        }
    }

    public void display(String time, String name, String number, String content)
    {
        binding.time.setText(time);
        binding.name.setText(name);
        binding.number.setText(number);
        binding.body.setText(content);
    }

    public static void onVibrate(boolean Gesture)
    {
        // 일반 출력방식
        //      제스처가 어떤가
        //      터치일때/슬라이드일때
        // 3점식 출력방식
        //      제스처가 어떤가
        //      터치일때만
        // 6점식 출력방식
        //      제스처가 어떤가
        //      터치일때만


        try {
            if (DataManager.VibrateMode == 1) { //기존입력
                if (Gesture) {
                    if (!IsTouched) {
                        binding.BrailleChar.setText(Integer.toString(count));
                        CurrentBraille = BrailleContent.get(count);
                        binding.brailleText.setText(CurrentBraille);
                        IsTouched = true;
                        parseBraille(false);
                    }
                } else {
                    if (IsTouched) {
                        parseBraille(true);
                        binding.BrailleChar.setText(Integer.toString(++count));
                        IsTouched = false;
                    }
                }
            } else {

                if (DataManager.VibrateMode == 2) //3점식
                {
                    CurrentBraille = BrailleContent.get(count++);
                    binding.brailleText.setText(CurrentBraille);
                    char[] chars = CurrentBraille.toCharArray();
                    //binding.BrailleChar.setText(Character.toString(binding.body.getText().charAt(count/2)));
                    vibrator.vibrate((chars[0] == '0' ? 100 : 300), (chars[1] == '0' ? 100 : 300), (chars[2] == '0' ? 100 : 300));
                } else if (DataManager.VibrateMode == 3) //6점식
                {
                    CurrentBraille = BrailleContent.get(count++) + BrailleContent.get(count++);
                    binding.brailleText.setText(CurrentBraille);
                    char[] chars = CurrentBraille.toCharArray();
                   // binding.BrailleChar.setText(Character.toString(binding.brailleText.getText().charAt(count)));
                    vibrator.vibrate((chars[0] == '0' ? 100 : 300), (chars[1] == '0' ? 100 : 300), (chars[2] == '0' ? 100 : 300), (chars[3] == '0' ? 100 : 300), (chars[4] == '0' ? 100 : 300), (chars[5] == '0' ? 100 : 300));
                } else if (DataManager.VibrateMode == 4) //1점식
                {
                    //3점식 베이스에 따로 카운트 올려서 계산한다.
                    //보조카운트++ 해서 3되면 카운트 1 올리기

                    if (sub_count == 3 && count < BrailleContent.size()) {
                        sub_count = 0; //0,1,2 되면 0으로 바꿈
                        count++; //카운트 올림
                        CurrentBraille = BrailleContent.get(count);
                        binding.brailleText.setText(CurrentBraille);//현재 점자 교체
                        binding.BrailleChar.setText(Integer.toString(count));
                    }
                    if (count == 0) {
                        CurrentBraille = BrailleContent.get(count);
                        binding.brailleText.setText(CurrentBraille);//현재 점자 교체
                        binding.BrailleChar.setText(Integer.toString(count));
                    }
                    char[] chars = CurrentBraille.toCharArray(); //char 배열로 만듬
                    vibrator.vibrate((chars[sub_count++] == '0' ? 100 : 300)); //한글자씩 올림 0->1->2->3
                }

            }
        }
        catch(Exception e)
        {
            vibrator.vibrate(1000);
        }
    }

    public static void onTouch() //터치
    {
        onVibrate(true);
    }

    public static void onFling() //쓸어내리기
    {
        onVibrate(false);
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
        if (Math.abs(e1.getX() - e2.getX()) < 250 && (e1.getY() - e2.getY() > 0) || (Math.abs(e1.getX() - e2.getX()) < 250 && (e2.getY() - e1.getY() > 0))) {
            //위/아래로 드래그
            ReceiveActivity.onFling();
        }
        else if (Math.abs(e1.getX() - e2.getX()) > 250 && (e1.getY() - e2.getY() > 100) && (e1.getX() - e2.getX()) < 0 )
        {
            //오른쪽 위로 슬라이드
            ttsManager.speak(binding.name.getText().toString() + " " + binding.body.getText().toString()); //이름 번호 말한다
        }
        else if (Math.abs(e1.getY() - e2.getY()) < GESTURE_LIMIT && (e2.getX() - e1.getX() > ZERO))
        {
            //오른쪽 드래그
            //prev
            nextDisplay(0);
        }
        else if (Math.abs(e1.getY() - e2.getY()) < GESTURE_LIMIT && (e1.getX() - e2.getX() > ZERO))
        {
            //왼쪽 드래그
            //next
            nextDisplay(1);
        } else if((e1.getX() - e2.getX() > 0)&&(e1.getY()-e2.getY() < 0)){
            //왼쪽 아래 드래그
            finish();
        }
        return true;
    }

    public void nextDisplay(int mode)
    {
        if(smsItems.size() > 0 && smsIndex >= 0) { //한 건도 없지 않아야 함 //0 이상이어야 뭘 움직일수 있음

            if (mode == 0) {
                //Backside : Before
                if(smsIndex > 0) { //현재위치가 1 이상이어야 뒤로감
                    smsIndex--;
                    DataManager.CurrentSMS = smsItems.get(smsIndex);
                    parseSMS(DataManager.CurrentSMS);
                    ttsManager.stop();
                }

            } else {
                //Fore-side : After
                if(smsIndex < smsItems.size()-1) { //현재위치가 최대사이즈보다 작아야 다음으로감
                    smsIndex++;
                    DataManager.CurrentSMS = smsItems.get(smsIndex);
                    parseSMS(DataManager.CurrentSMS);
                    ttsManager.stop();
                }
            }
        }
    }
}