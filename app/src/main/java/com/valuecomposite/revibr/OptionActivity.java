package com.valuecomposite.revibr;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_option);
        binding.Optspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch(i)
                {
                    case 0:
                        DataManager.VibrateMode = 0;
                        break;
                    case 1:
                        DataManager.VibrateMode = 1;
                        break;
                    case 2:
                        DataManager.VibrateMode = 2;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

}
