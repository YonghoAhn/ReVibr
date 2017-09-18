package com.valuecomposite.revibr;

import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.valuecomposite.revibr.databinding.ActivityOptionBinding;

/**
 * Created by ayh07 on 9/17/2017.
 */

public class OptionActivity extends AppCompatActivity {
    static ActivityOptionBinding binding;
    static SharedPreferenceManager sharedPreferenceManager = new SharedPreferenceManager();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_option);
        switch (sharedPreferenceManager.getPreferences("setting","mode").toCharArray()[0]) //설정된 데이터를 바인딩
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
        }
        binding.Optspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { //아이템 변경 리스너
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch(i)
                {
                    case 0:
                        DataManager.VibrateMode = 1;
                        sharedPreferenceManager.savePreferences("setting","mode","1");
                        break;
                    case 1:
                        DataManager.VibrateMode = 2;
                        sharedPreferenceManager.savePreferences("setting","mode","2");
                        break;
                    case 2:
                        DataManager.VibrateMode = 3;
                        sharedPreferenceManager.savePreferences("setting","mode","3");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

}
