package com.valuecomposite.revibr.utils;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by ayh07 on 12/17/2017.
 */

public class Initializer {
    static Initializer instance = null;
    static Context context;
    static TTSManager ttsManager;
    static Vibrator vibrator;

    public Initializer(Context context)
    {
        this.context = context;
        if(DataManager.MODE == 0)
            InitializeContactsList(context);
        ttsManager = TTSManager.getInstance(context);
        vibrator = Vibrator.getInstace(context);
        if(getPreferences("setting","mode").equals(""))
            savePreferences("setting","mode","1");
        switch (getPreferences("setting","mode").toCharArray()[0]) //설정된 데이터를 바인딩
        {
            case '1':
                DataManager.VibrateMode = 1;
                break;
            case '2':
                DataManager.VibrateMode = 2;
                break;
            case '3':
                DataManager.VibrateMode = 3;
                break;
            case '4':
                DataManager.VibrateMode = 4;
                break;
        }
    }

    public static void InitializeContactsList(Context context)
    {
        ContactManager contactManager = ContactManager.getInstance(context);
        DataManager.PBItems = contactManager.getContactList();
    }

    public String getPreferences(String key, String subkey){
        SharedPreferences pref = context.getSharedPreferences(key, MODE_PRIVATE);
        return pref.getString(subkey, "");
    }

    public void savePreferences(String key, String subkey, String content){
        SharedPreferences pref = context.getSharedPreferences(key, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(subkey, content);
        editor.commit();
    }

    public static void Instantiate(Context context)
    {
        instance = new Initializer(context);
    }
}
