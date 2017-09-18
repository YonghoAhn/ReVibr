package com.valuecomposite.revibr;

import android.content.Context;

/**
 * Created by ayh07 on 8/12/2017.
 */

public class Vibrator {

    //메서드를 호출, 진동을 울리는 모듈이다.

    private android.os.Vibrator m_Vibrator;
    public Vibrator(Context context)
    {
        m_Vibrator = (android.os.Vibrator)context.getSystemService(context.VIBRATOR_SERVICE);
    }

    public void vibrate(int millisecond)
    {
        m_Vibrator.vibrate(millisecond);
        m_Vibrator.cancel();
    }

    public void cancel()
    {
        m_Vibrator.cancel();
    }

    //진동을 울리는데, 경우의 수가 여러 가지가 있다.

    public void vibrate(int millisecond, int millisecond2)
    {
        long[] pattern = new long[]{0,millisecond,20, millisecond2};
        m_Vibrator.vibrate(pattern,-1);
    }

    public void vibrate(int millisecond, int millisecond2, int millisecond3)
    {
        long[] pattern = new long[]{0,millisecond,20, millisecond2,20,millisecond3};
        m_Vibrator.vibrate(pattern,-1);
    }
}
