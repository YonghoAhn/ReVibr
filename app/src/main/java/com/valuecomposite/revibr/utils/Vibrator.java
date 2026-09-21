package com.valuecomposite.revibr.utils;

import android.content.Context;

/**
 * Created by ayh07 on 8/12/2017.
 */

public class Vibrator {

    //메서드를 호출, 진동을 울리는 모듈이다.


    private android.os.Vibrator m_Vibrator;
    static Vibrator instance = null;


    public Vibrator(Context context)
    {
        m_Vibrator = (android.os.Vibrator)context.getSystemService(context.VIBRATOR_SERVICE);
    }

    public static Vibrator getInstace(Context context)
    {
        if(instance!=null)
            return instance;
        else
            return instance=new Vibrator(context);
    }

    public void vibrate(int millisecond)
    {
        m_Vibrator.vibrate(millisecond);
    }

    public void cancel()
    {
        m_Vibrator.cancel();
    }

    //진동을 울리는데, 경우의 수가 여러 가지가 있다.

    public void vibrate(int millisecond, int millisecond2)
    {
        long[] pattern = new long[]{0,millisecond,150, millisecond2};
        m_Vibrator.vibrate(pattern,-1);
    }

    public void vibrate(int millisecond, int millisecond2, int millisecond3)
    {
        long[] pattern = new long[]{0,millisecond,150, millisecond2,150,millisecond3};
        m_Vibrator.vibrate(pattern,-1);
    }

    public void vibrate(int i, int i1, int i2, int i3, int i4, int i5) {
        long[] pattern = new long[]{0,i,150, i1,150,i2,300,i3,150,i4,150,i5};
        m_Vibrator.vibrate(pattern,-1);
    }
}
