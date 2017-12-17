package com.valuecomposite.revibr.Activities;

import android.databinding.DataBindingUtil;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.valuecomposite.revibr.databinding.ActivitySendBinding;
import com.valuecomposite.revibr.utils.ApplicationController;
import com.valuecomposite.revibr.R;

// Layout은 Send와 공유
// 초성으로 검색하기
// PhoneBookItems -> 초성대로 다시 만든 List가 필요

public class SearchActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {
    private ActivitySendBinding binding;
    private GestureDetectorCompat gDetector;
    private Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

        gDetector = new GestureDetectorCompat(this,this);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_send);

        ApplicationController application = (ApplicationController) getApplication();
        mTracker = application.getDefaultTracker();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        mTracker.setScreenName("SearchActivity");
        //sending the screen to analytics using ScreenViewBuilder() method
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }


    @Override
    public boolean onTouchEvent(MotionEvent e)
    {
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

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }
}
