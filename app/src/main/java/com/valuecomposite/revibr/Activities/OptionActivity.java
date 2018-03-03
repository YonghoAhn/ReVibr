package com.valuecomposite.revibr.Activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.valuecomposite.revibr.utils.ApplicationController;
import com.valuecomposite.revibr.utils.DataManager;
import com.valuecomposite.revibr.R;
import com.valuecomposite.revibr.databinding.ActivityOptionBinding;

/**
 * Created by ayh07 on 9/17/2017.
 */

public class OptionActivity extends BaseActivity {
    static ActivityOptionBinding binding;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_option);
        gDetector = new GestureDetectorCompat(this, this);

        if(getPreferences("setting","mode").equals(""))
            savePreferences("setting","mode","1");
        switch (getPreferences("setting","mode").toCharArray()[0]) //설정된 데이터를 바인딩
        {
            case '1':
                binding.Optspinner.setSelection(0);
                break;
            case '2':
                binding.Optspinner.setSelection(1);
                break;
            case '3':
                binding.Optspinner.setSelection(2);
                break;
            case '4':
                binding.Optspinner.setSelection(3);
                break;
        }
        binding.Optspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { //아이템 변경 리스너
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch(i)
                {
                    case 0:
                        DataManager.VibrateMode = 1;
                        savePreferences("setting","mode","1");
                        break;
                    case 1:
                        DataManager.VibrateMode = 2;
                        savePreferences("setting","mode","2");
                        break;
                    case 2:
                        DataManager.VibrateMode = 3;
                        savePreferences("setting","mode","3");
                        break;
                    case 3:
                        DataManager.VibrateMode = 4;
                        savePreferences("setting","mode","4");
                        break;
                }
                ttsManager.speak(binding.Optspinner.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    @Override
    protected void onResume()
    {
        super.onResume();
        //using tracker variable to set Screen Name
        mTracker.setScreenName("OptionActivity");
        //sending the screen to analytics using ScreenViewBuilder() method
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        gDetector.onTouchEvent(motionEvent);
        return true;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float v, float v1) {
        if (Math.abs(e1.getX() - e2.getX()) < 250 && (e1.getY() - e2.getY() > 0) || (Math.abs(e1.getX() - e2.getX()) < 250 && (e2.getY() - e1.getY() > 0))) {
            //위/아래로 드래그

        }
        else if (Math.abs(e1.getX() - e2.getX()) > 250 && (e1.getY() - e2.getY() > 100) && (e1.getX() - e2.getX()) < 0 )
        {
            //오른쪽 위로 슬라이드
           // ttsManager.speak(binding.name.getText().toString() + " " + binding.body.getText().toString()); //이름 번호 말한다
        }
        else if (Math.abs(e1.getY() - e2.getY()) < GESTURE_LIMIT && (e2.getX() - e1.getX() > ZERO))
        {
            //오른쪽 드래그
            //prev
            //nextDisplay(0);
        }
        else if (Math.abs(e1.getY() - e2.getY()) < GESTURE_LIMIT && (e1.getX() - e2.getX() > ZERO))
        {
            //왼쪽 드래그
            //next
            //nextDisplay(1);
        } else if((e1.getX() - e2.getX() > 0)&&(e1.getY()-e2.getY() < 0)){
            ttsManager.speak("뒤로 가기");
            //왼쪽 아래 드래그
            finish();
        }
        return true;
    }

}
