package com.valuecomposite.revibr.utils;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.valuecomposite.revibr.R;

import java.util.HashMap;

/**
 * Created by anyongho on 2017. 9. 28..
 */

public class ApplicationController extends Application {
    private static final String PROPERTY_ID = "UA-107184842-1";
    private Tracker mTracker;
    public enum TrackerName {
        APP_TRACKER,           // 앱 별로 트래킹
        GLOBAL_TRACKER,        // 모든 앱을 통틀어 트래킹
        ECOMMERCE_TRACKER,     // 아마 유료 결재 트래킹 개념 같음
    }

    HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();

    synchronized public Tracker getTracker(TrackerName trackerId){
        if(!mTrackers.containsKey(trackerId)){
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            Tracker t = (trackerId == TrackerName.APP_TRACKER) ? analytics.newTracker(PROPERTY_ID) :
                    (trackerId == TrackerName.GLOBAL_TRACKER) ? analytics.newTracker(R.xml.global_tracker) :
                            analytics.newTracker(R.xml.ecommerse_tracker);
            mTrackers.put(trackerId, t);
        }
        return mTrackers.get(trackerId);
    }

    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // Setting mTracker to Analytics Tracker declared in our xml Folder
            mTracker = analytics.newTracker(R.xml.analytics_tracker);
        }
        return mTracker;
    }

}
