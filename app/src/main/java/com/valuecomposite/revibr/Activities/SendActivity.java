package com.valuecomposite.revibr.Activities;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.valuecomposite.revibr.utils.Braille.BrailleInput;
import com.valuecomposite.revibr.R;
import com.valuecomposite.revibr.utils.DataManager;
import com.valuecomposite.revibr.utils.HangulSupport;
import com.valuecomposite.revibr.utils.Initializer;
import com.valuecomposite.revibr.utils.PhoneBookItem;
import com.valuecomposite.revibr.databinding.ActivitySendBinding;

import java.util.ArrayList;

import static com.valuecomposite.revibr.utils.DataManager.MODE;
import static com.valuecomposite.revibr.utils.DataManager.PBItems;


public class SendActivity extends BaseActivity {
    //region variables
    public static int count = 0;
    static Context mContext;
    static ActivitySendBinding binding;
    private static String smsNum = "";
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
        //초성이 아닌 것은 무시하도록 전송모드일때만 중성/종성 추가되게함
        if (MODE == 0) {
            String t = binding.txtSend.getText().toString();
            binding.txtSend.setText(t.substring(0, t.length() - 1) + s);
            ttsManager.speak(s);
        }
    }

    public static void AddChosung(String s) {
        binding.txtSend.setText(binding.txtSend.getText() + s);
        ttsManager.speak(s);
    }

    private static boolean sendSMS(String s) {
        String smsText = s;
        if (smsNum.length() > 0 && smsText.length() > 0) {
            if(sendSMS(smsNum, smsText))
            {
                //문자 전송 성공 시
                binding.txtSend.setText("");
                Toast.makeText(mContext, "전송됨", Toast.LENGTH_SHORT).show();

                return true;
            }
            else
            {
                Toast.makeText(mContext,"실패",Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(mContext, "비었습니다.", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private static boolean sendSMS(String smsNum, String smsText) {
        try {
            PendingIntent sentIntent = PendingIntent.getBroadcast(mContext, 0, new Intent("SMS_SENT_ACTION"), 0);
            PendingIntent deliverIntent = PendingIntent.getBroadcast(mContext, 0, new Intent("SMS_DELIVERED_ACTION"), 0);
            SmsManager mSmsManager = SmsManager.getDefault();
            mSmsManager.sendTextMessage(smsNum, null, smsText, sentIntent, deliverIntent);
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
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
            //but wait
            //then divide char to cho-joong-jong, and remove some char and re-assemble it
            try {
                char lastChar = binding.txtSend.getText().charAt(binding.txtSend.getText().length()-1);
                if(HangulSupport.IsHangul(lastChar)) //try divide when it was hangul
                {
                    char[] chars = HangulSupport.DivideHangulChar(lastChar);
                    //if char was just chosung, then return {'r',' ',' '}
                    //if char was cho+jung then return {'r','r',' '}
                    //else, return {'r','r','r'}
                    //so we need just remove last char.
                    // composition with char array[length-2]
                    if(chars[0]!=' ' && chars[1] != ' ' && chars[2] != ' ') //cho, joong, jong is not empty
                    {
                        AddText(Character.toString(HangulSupport.CombineHangul(new char[] {chars[0],chars[1],' '})));
                        BrailleInput.SetChosung(chars[0]);
                        BrailleInput.SetJoongsung(chars[1]);
                    }
                    else if(chars[0] !=' ' && chars[1] != ' ' && chars[2] == ' ') //cho, joong is not empty, jong is empty
                    {
                        AddText(Character.toString(chars[0])); //Add Chosung using addtext
                        BrailleInput.SetChosung(chars[0]);
                    }
                    else if(chars[0]!=' ' && chars[1] == ' ' && chars[2] == ' ') //cho is not empty, joong+jong was empty.
                    {
                        binding.txtSend.setText(binding.txtSend.getText().toString().substring(0, binding.txtSend.getText().length() - 1)); //Remove one char : 초성지우기
                        BrailleInput.SetChosung(' ');
                    }
                }
                else //character was not hangul.
                {
                    binding.txtSend.setText(binding.txtSend.getText().toString().substring(0, binding.txtSend.getText().length() - 1));
                    //just remove it
                }
                ttsManager.speak("지움");
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

    public static void Delete(boolean deleteChar) {
        //Motion Delete, and its failed, we should remove character{
        try {
            binding.txtSend.setText(binding.txtSend.getText().toString().substring(0, binding.txtSend.getText().length() - 1));
            ttsManager.speak("지움");
        } catch (Exception e) {
            Log.d("MisakaMOE", e.getMessage());
        }
    }


    public void ClearAll()
    {
        BrailleInput.Flush(true);
        count=0;
        binding.txtSend.setText("");
        clearColor();
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
        //검색모드면 번호를 가져올 이유가 없음
        if(MODE==0) {
            ttsManager.speak("문자 전송");
            Intent getIntent = getIntent();
            smsNum = getIntent.getExtras().getString("number");
        }
        else
        {
            ttsManager.speak("대화상대의 초성을 입력하세요");
        }

        intent =  new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"ko-KR");

        mRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mRecognizer.setRecognitionListener(listener);
        BrailleInput.Initialize();

        mContext = getApplicationContext();

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
    protected void onDestroy()
    {
        ClearAll();
        super.onDestroy();
    }

    //뒤로가기로 끄면 꺼버림
    @Override
    public void onBackPressed()
    {
        finish();
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float v, float v1) {
        if (Math.abs(e1.getX() - e2.getX()) < GESTURE_LIMIT && (e1.getY() - e2.getY() > GESTURE_LIMIT)) {
            //위로 드래그
            Toast.makeText(mContext, "UP", Toast.LENGTH_SHORT).show();
            colorIdentify(count,true);
            vibrator.vibrate(300);
            BrailleInput.Input(true);
        } else if (Math.abs(e1.getX() - e2.getX()) < GESTURE_LIMIT && (e2.getY() - e1.getY() > GESTURE_LIMIT)) {
            //아래로 드래그
            Toast.makeText(mContext, "DOWN", Toast.LENGTH_SHORT).show();
            colorIdentify(count,false);
            vibrator.vibrate(100);
            BrailleInput.Input(false);
        } else if (Math.abs(e1.getY() - e2.getY()) < GESTURE_LIMIT && (e1.getX() - e2.getX() > ZERO)) {
            //왼쪽 드래그
            Toast.makeText(mContext, "LEFT", Toast.LENGTH_SHORT).show();
            // 지우기 코드
            Delete();
            //BrailleInput.Delete();

        } else if (Math.abs(e1.getY() - e2.getY()) < GESTURE_LIMIT && (e2.getX() - e1.getX() > ZERO)) {

            if(MODE==0)
            {
                //오른쪽 드래그
                Toast.makeText(mContext, "RIGHT", Toast.LENGTH_SHORT).show();
                // 문자 보내기 코드
                if(sendSMS(binding.txtSend.getText().toString()))
                {
                    //문자 전송 성공
                    Intent i = new Intent(mContext,SplashActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    finish();
                }

            }
            else if(MODE==1) //검색모드면 검색해서 띄워준다.
            {
                //DataManager.PBItems를 돌면서, chosung.equals(input text)인것만 추출하는 새로운 ArrayList<PBItem> 만들고 그걸 PBITems로 재할당
                ArrayList<PhoneBookItem> SearchResult = new ArrayList<>();
                Initializer.InitializeContactsList(getApplicationContext()); //검색하려면 먼저 불러와야지

                for(PhoneBookItem phoneBookItem : DataManager.PBItems)
                {
                    //if(phoneBookItem.getChosung().contains(binding.txtSend.getText().toString())) //포함으로 해야할듯
                    if(phoneBookItem.getChosung().startsWith(binding.txtSend.getText().toString())) //초성으로 시작하는 것들
                    {
                        SearchResult.add(phoneBookItem);
                    }
                }
                if(SearchResult.size() == 0) //아무것도 없다면 검색결과 없음 띄우기
                    SearchResult.add(new PhoneBookItem("","검색결과가 없습니다.",""));
                //PBItems를 검색결과로 대체한다.
                PBItems = SearchResult;
                //그리고 결과를 초기화한다.
                ClearAll();
                //그다음 검색 Result를 띄워준다.
                Intent resultIntent = new Intent(getApplicationContext(),PhoneBook.class);
                resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(resultIntent);

            }

        } else if ((e2.getX() - e1.getX() > ZERO) && (e2.getY() - e1.getY() > ZERO)) {
            //오른쪽 아래 대각선 드래그
            //듣는건 문자보내는 모드에서만 ㅇㅇ
            if(MODE==0) {
                Toast.makeText(getApplicationContext(), "message sending activity", Toast.LENGTH_SHORT).show();
                mRecognizer.startListening(intent);
            }
        } else if((e1.getX() - e2.getX() > 0)&&(e1.getY()-e2.getY() < 0)){
            //왼쪽 아래 드래그
            finish();
        }
        else if (Math.abs(e1.getX() - e2.getX()) > 250 && (e1.getY() - e2.getY() > 0) && (e1.getX() - e2.getX()) < 0 )
        {
            //오른쪽 위로 슬라이드
            ttsManager.speak(binding.txtSend.getText().toString()); //이름 번호 말한다
        }

        else {
            Toast.makeText(mContext, "ignore", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

}
