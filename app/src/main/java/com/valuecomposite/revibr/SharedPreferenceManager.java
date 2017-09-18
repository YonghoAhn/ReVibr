package com.valuecomposite.revibr;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by ayh07 on 9/18/2017.
 */

public class SharedPreferenceManager extends Activity {

    public String getPreferences(String key, String subkey){
        SharedPreferences pref = getSharedPreferences(key, MODE_PRIVATE);
        return pref.getString(subkey, "");
    }

    // 값 저장하기
    public void savePreferences(String key, String subkey, String content){
        SharedPreferences pref = getSharedPreferences(key, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(subkey, content);
        editor.commit();
    }

    // 값(Key Data) 삭제하기
    public void removePreferences(String key, String subkey){
        SharedPreferences pref = getSharedPreferences(key, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove(subkey);
        editor.commit();
    }

    // 값(ALL Data) 삭제하기
    public void removeAllPreferences(String key){
        SharedPreferences pref = getSharedPreferences(key, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }

}
