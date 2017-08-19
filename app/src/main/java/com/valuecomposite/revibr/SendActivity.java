package com.valuecomposite.revibr;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.valuecomposite.revibr.databinding.ActivitySendBinding;

import static com.valuecomposite.revibr.DataManager.mContext;

/**
 * Created by ayh07 on 8/12/2017.
 */

public class SendActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {
    BroadcastReceiver receiver = new BroadcastReceiver();
    static ActivitySendBinding binding;
    private GestureDetectorCompat gDetector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_send);
        mContext = getApplicationContext();
        gDetector = new GestureDetectorCompat(this,this);

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
