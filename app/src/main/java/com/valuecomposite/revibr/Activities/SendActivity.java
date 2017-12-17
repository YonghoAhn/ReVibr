package com.valuecomposite.revibr.Activities;

import android.app.PendingIntent;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.valuecomposite.revibr.utils.ApplicationController;
import com.valuecomposite.revibr.utils.BrailleInput;
import com.valuecomposite.revibr.R;
import com.valuecomposite.revibr.utils.TTSManager;
import com.valuecomposite.revibr.utils.Vibrator;
import com.valuecomposite.revibr.databinding.ActivitySendBinding;

import java.util.ArrayList;

import static com.valuecomposite.revibr.utils.DataManager.MODE;
import static com.valuecomposite.revibr.utils.DataManager.mContext;

/**
 * Created by ayh07 on 8/12/2017.
 */

public class SendActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {
    //region variables
    public static int count = 0;
    static ActivitySendBinding binding;
    private static String smsNum = "";
    private GestureDetectorCompat gDetector;
    private Tracker mTracker;
    static Vibrator vibrator;
    static TTSManager ttsManager;
    private Intent intent;
    private SpeechRecognizer mRecognizer;
    private RecognitionListener listener = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle bundle) {}

        @Override
        public void onBeginningOfSpeech() {}

        @Override
        public void onRmsChanged(float v) {}

        @Override
        public void onBufferReceived(byte[] bytes) {}

        @Override
        public void onEndOfSpeech() {}

        @Override
        public void onError(int i) {}

        @Override
        public void onPartialResults(Bundle bundle) {}

        @Override
        public void onEvent(int i, Bundle bundle) {}

        @Override
        public void onResults(Bundle results) {
            String key = "";
            key = SpeechRecognizer.RESULTS_RECOGNITION;
            ArrayList<String> mResult = results.getStringArrayList(key);
            String[] rs = new String[mResult.size()];
            mResult.toArray(rs);
            binding.txtSend.setText(binding.txtSend.getText() + " " +rs[0]);
            vibrator.vibrate(500); //Listen Completed
            ttsManager.speak(binding.txtSend.getText().toString()); //뭐가 입력됐는지 읽어줌
        }

    };
    //endregion

    //region Braille_Method
    public static void AddText(String s) {
        String t = binding.txtSend.getText().toString();
        binding.txtSend.setText(t.substring(0, t.length() - 1) + s);
    }

    public static void AddChosung(String s) {
        binding.txtSend.setText(binding.txtSend.getText() + s);
    }

    private static void sendSMS(String s) {
        String smsText = s;
        if (smsNum.length() > 0 && smsText.length() > 0) {
            sendSMS(smsNum, smsText);
            binding.txtSend.setText("");
            Toast.makeText(mContext, "전송됨", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mContext, "비었습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    private static void sendSMS(String smsNum, String smsText) {
        PendingIntent sentIntent = PendingIntent.getBroadcast(mContext, 0, new Intent("SMS_SENT_ACTION"), 0);
        PendingIntent deliverIntent = PendingIntent.getBroadcast(mContext, 0, new Intent("SMS_DELIVERED_ACTION"), 0);
        SmsManager mSmsManager = SmsManager.getDefault();
        mSmsManager.sendTextMessage(smsNum, null, smsText, sentIntent, deliverIntent);
    }

    public static void clearColor() {
        binding.idxOne.setBackground(ContextCompat.getDrawable(mContext, R.drawable.circle));
        binding.idxTwo.setBackground(ContextCompat.getDrawable(mContext, R.drawable.circle));
        binding.idxThree.setBackground(ContextCompat.getDrawable(mContext, R.drawable.circle));
        binding.idxFour.setBackground(ContextCompat.getDrawable(mContext, R.drawable.circle));
        binding.idxFive.setBackground(ContextCompat.getDrawable(mContext, R.drawable.circle));
        binding.idxSix.setBackground(ContextCompat.getDrawable(mContext, R.drawable.circle));
    }

    public static void setClearColor(int index)
    {
        switch(index)
        {
            case 1:
                binding.idxOne.setBackground(ContextCompat.getDrawable(mContext, R.drawable.circle));
                break;
            case 2:
                binding.idxTwo.setBackground(ContextCompat.getDrawable(mContext, R.drawable.circle));
                break;
            case 3:
                binding.idxThree.setBackground(ContextCompat.getDrawable(mContext, R.drawable.circle));
                break;
            case 4:
                binding.idxFour.setBackground(ContextCompat.getDrawable(mContext, R.drawable.circle));
                break;
            case 5:
                binding.idxFive.setBackground(ContextCompat.getDrawable(mContext, R.drawable.circle));
                break;
            case 6:
                binding.idxSix.setBackground(ContextCompat.getDrawable(mContext, R.drawable.circle));
                break;
        }
    }

    private void colorIdentify(int idx, boolean gesture)
    {
        if(gesture) {
            switch (idx) {
                case 0:
                    binding.idxOne.setBackground(ContextCompat.getDrawable(mContext, R.drawable.circle_black));
                    count++;
                    break;
                case 1:
                    binding.idxTwo.setBackground(ContextCompat.getDrawable(mContext, R.drawable.circle_black));
                    count++;
                    break;
                case 2:
                    binding.idxThree.setBackground(ContextCompat.getDrawable(mContext, R.drawable.circle_black));
                    count++;
                    break;
                case 3:
                    binding.idxFour.setBackground(ContextCompat.getDrawable(mContext, R.drawable.circle_black));
                    count++;
                    break;
                case 4:
                    binding.idxFive.setBackground(ContextCompat.getDrawable(mContext, R.drawable.circle_black));
                    count++;
                    break;
                case 5:
                    binding.idxSix.setBackground(ContextCompat.getDrawable(mContext, R.drawable.circle_black));
                    count++;
                    break;
            }
        }
        else
        {
            switch (idx) {
                case 0:
                    binding.idxOne.setBackground(ContextCompat.getDrawable(mContext, R.drawable.circle_white));
                    count++;
                    break;
                case 1:
                    binding.idxTwo.setBackground(ContextCompat.getDrawable(mContext, R.drawable.circle_white));
                    count++;
                    break;
                case 2:
                    binding.idxThree.setBackground(ContextCompat.getDrawable(mContext, R.drawable.circle_white));
                    count++;
                    break;
                case 3:
                    binding.idxFour.setBackground(ContextCompat.getDrawable(mContext, R.drawable.circle_white));
                    count++;
                    break;
                case 4:
                    binding.idxFive.setBackground(ContextCompat.getDrawable(mContext, R.drawable.circle_white));
                    count++;
                    break;
                case 5:
                    binding.idxSix.setBackground(ContextCompat.getDrawable(mContext, R.drawable.circle_white));
                    count++;
                    break;
            }
        }
    }

    public static void Delete() {
        if(BrailleInput.Delete() == -1)
        { //Motion Delete, and its failed, we should remove character{
            try {
                binding.txtSend.setText(binding.txtSend.getText().toString().substring(0, binding.txtSend.getText().length() - 1));
            }
            catch (Exception e)
            {
                Log.d("MisakaMOE",e.getMessage());
            }
        }
        else
        {

        }
    }
    //endregion

    //region unusedGestureMethods
    @Override
    public boolean onTouchEvent(MotionEvent e){
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
    //endregion

    //
    //Mode를 설정해서 SearchMode or SendMode로 나눌것
    //

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

        vibrator = Vibrator.getInstace(getApplicationContext());
        ttsManager = TTSManager.getInstance(getApplicationContext());

        intent =  new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"ko-KR");

        mRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mRecognizer.setRecognitionListener(listener);
        BrailleInput.Initialize();

    }

    @Override
    protected void onResume(){
        super.onResume();
        //using tracker variable to set Screen Name
        mTracker.setScreenName("SendActivity");
        //sending the screen to analytics using ScreenViewBuilder() method
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float v, float v1) {
        if (Math.abs(e1.getX() - e2.getX()) < PhoneBook.GESTURE_LIMIT && (e1.getY() - e2.getY() > PhoneBook.ZERO)) {
            //위로 드래그
            Toast.makeText(mContext, "UP", Toast.LENGTH_SHORT).show();
            colorIdentify(count,true);
            vibrator.vibrate(300);
            BrailleInput.Input(true);
        } else if (Math.abs(e1.getX() - e2.getX()) < PhoneBook.GESTURE_LIMIT && (e2.getY() - e1.getY() > PhoneBook.ZERO)) {
            //아래로 드래그
            Toast.makeText(mContext, "DOWN", Toast.LENGTH_SHORT).show();
            colorIdentify(count,false);
            vibrator.vibrate(100);
            BrailleInput.Input(false);
        } else if (Math.abs(e1.getY() - e2.getY()) < PhoneBook.GESTURE_LIMIT && (e1.getX() - e2.getX() > PhoneBook.ZERO)) {
            //왼쪽 드래그
            Toast.makeText(mContext, "LEFT", Toast.LENGTH_SHORT).show();
            // 지우기 코드
            Delete();
            //BrailleInput.Delete();

        } else if (Math.abs(e1.getY() - e2.getY()) < PhoneBook.GESTURE_LIMIT && (e2.getX() - e1.getX() > PhoneBook.ZERO)) {

            if(MODE==0)
            {
                //오른쪽 드래그
                Toast.makeText(mContext, "RIGHT", Toast.LENGTH_SHORT).show();
                // 문자 보내기 코드
                sendSMS(binding.txtSend.getText().toString());
            }
            else if(MODE==1) //검색모드면 검색해서 띄워준다.
            {

            }

        } else if ((e2.getX() - e1.getX() > PhoneBook.ZERO) && (e2.getY() - e1.getY() > PhoneBook.ZERO)) {
            //오른쪽 아래 대각선 드래그
            //듣는건 문자보내는 모드에서만 ㅇㅇ
            if(MODE==0) {
                Toast.makeText(getApplicationContext(), "message sending activity", Toast.LENGTH_SHORT).show();
                mRecognizer.startListening(intent);
            }
        } else if ((e1.getX() - e2.getX() > 0) && (e1.getY() - e2.getY() > 0)) {
            //왼쪽 위 대각선 드래그
            //이전으로 돌아가기
            Toast.makeText(getApplicationContext(), "message sending activity", Toast.LENGTH_SHORT).show();
            BrailleInput.Flush(true);
            finish();
            //뒤로 돌아가기 코드
        }
        else if (Math.abs(e1.getX() - e2.getX()) > 250 && (e1.getY() - e2.getY() > 0) && (e1.getX() - e2.getX()) < 0 ) //오른쪽 위로 슬라이드
        {
            ttsManager.speak(binding.txtSend.getText().toString()); //이름 번호 말한다
        }

        else {
            Toast.makeText(mContext, "ignore", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

}
