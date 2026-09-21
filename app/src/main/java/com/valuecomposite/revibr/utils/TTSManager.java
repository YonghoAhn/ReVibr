package com.valuecomposite.revibr.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.speech.tts.TextToSpeech;

import java.util.HashMap;
import java.util.Locale;

/**
 * Created by anyongho on 2017. 9. 17..
 */

public class TTSManager {
    static TextToSpeech tts;
    static TTSManager instance = null;

    public static TTSManager getInstance(Context context)
    {
        if(instance!=null)
            return instance;
        else
            return (instance = new TTSManager(context));
    }

    public TTSManager(Context context) {
        tts = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i != TextToSpeech.ERROR)
                {
                    tts.setLanguage(Locale.KOREA);
                }
                else
                {
                    tts.setLanguage(Locale.KOREA);
                }
            }
        });
    }

    public boolean IsSpeaking()
    {
        if(tts!=null) {
            if (tts.isSpeaking())
                return true;
        }
        return false;
    }

    public void stop()
    {
        if(IsSpeaking())
            tts.stop();

    }

    public void speak(String text)
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            ttsGreater21(text);
        }
        else
        {
            ttsUnder20(text);
        }
    }

    @SuppressWarnings("deprecation")
    private void ttsUnder20(String text) {
        HashMap<String, String> map = new HashMap<>();
        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "MessageId");
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, map);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void ttsGreater21(String text) {
        String utteranceId=this.hashCode() + "";
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
    }
}
