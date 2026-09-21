package com.valuecomposite.revibr.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.google.android.gms.analytics.Tracker;
import com.valuecomposite.revibr.utils.ApplicationController;
import com.valuecomposite.revibr.utils.Initializer;
import com.valuecomposite.revibr.utils.TTSManager;
import com.valuecomposite.revibr.utils.Vibrator;

/**
 * Created by ayh07 on 12/28/2017.
 */

public class BaseActivity extends Activity implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {

   //region Implement_Methods
    @Override
    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
        return false;
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
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }
    //endregion

    static TTSManager ttsManager;
    static Vibrator vibrator;
    final int ZERO = 0;
    final int GESTURE_LIMIT = 250;
    Tracker mTracker;
    GestureDetectorCompat gDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Initializer.Instantiate(getApplicationContext());
        ApplicationController application = (ApplicationController) getApplication();
        mTracker = application.getDefaultTracker();
        vibrator = Vibrator.getInstace(getApplicationContext());
        ttsManager = TTSManager.getInstance(getApplicationContext());
    }
}
