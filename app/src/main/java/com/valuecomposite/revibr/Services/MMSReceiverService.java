package com.valuecomposite.revibr.Services;


import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import com.valuecomposite.revibr.BroadcastReceiver.ServiceRestartBroadcastReceiver;
import com.valuecomposite.revibr.utils.Messages.MMSMon;

public class MMSReceiverService extends Service {
    private MMSMon mmsMon;

    private static final int MILLISINFUTURE = 1000*1000;
    private static final int COUNT_DOWN_INTERVAL = 1000;

    private CountDownTimer countDownTimer;



    @Override
    public int onStartCommand(Intent intent,int flags,int startId )
    {
        //뭐가 됐든간에 Service Start하면 호출됨
        mmsMon.startMonitor();
        startForeground(1,new Notification());

        /**
         * startForeground 를 사용하면 notification 을 보여주어야 하는데 없애기 위한 코드
         */
        NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        Notification notification;
        notification = new Notification.Builder(getApplicationContext())
                    .setContentTitle("")
                    .setContentText("")
                    .build();


        nm.notify(startId, notification);
        nm.cancel(startId);

        return START_REDELIVER_INTENT;
    }

    @Override
    public IBinder onBind(Intent intent){
// 다른 컴포넌트가 bindService()를 호출해서 서비스와 연결을 시도하면 이 메소드가 호출됩니다. 이 메소드에서 IBinder를 반환해서 서비스와 컴포넌트가 통신하는데 사용하는 인터페이스를 제공해야 합니다. 만약 시작 타입의 서비스를 구현한다면 null을 반환하면 됩니다.
        return null;
    }

    @Override
    public void onCreate(){
// 서비스가 처음으로 생성되면 호출됩니다. 이 메소드 안에서 초기의 설정 작업을 하면되고 서비스가 이미 실행중이면 이 메소드는 호출되지 않습니다.
        mmsMon = new MMSMon(getApplicationContext());
        mmsMon.setDaemon(true);
        mmsMon.start();
        mmsMon.startMonitor();
        unregisterRestartAlarm();
        initData();
    }

    @Override
    public void onDestroy(){
// 서비스가 소멸되는 도중에 이 메소드가 호출되며 주로 Thread, Listener, BroadcastReceiver와 같은 자원들을 정리하는데 사용하면 됩니다. TaskKiller에 의해 서비스가 강제종료될 경우에는 이 메소드가 호출되지 않는다는 점 !! ㅜㅜ
        mmsMon.stopMonitor();
        mmsMon.destroyMonitor();
        countDownTimer.cancel();

        /**
         * 서비스 종료 시 알람 등록을 통해 서비스 재 실행
         */
        registerRestartAlarm();

    }

    private void registerRestartAlarm(){

        Log.i("000 PersistentService" , "registerRestartAlarm" );
        Intent intent = new Intent(MMSReceiverService.this,ServiceRestartBroadcastReceiver.class);
        intent.setAction("ACTION.RESTART.PersistentService");
        PendingIntent sender = PendingIntent.getBroadcast(MMSReceiverService.this,0,intent,0);

        long firstTime = SystemClock.elapsedRealtime();
        firstTime += 1*1000;

        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);

        /**
         * 알람 등록
         */
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,firstTime,1*1000,sender);

    }

    private void unregisterRestartAlarm(){

        Log.i("000 PersistentService" , "unregisterRestartAlarm" );

        Intent intent = new Intent(MMSReceiverService.this,ServiceRestartBroadcastReceiver.class);
        intent.setAction("ACTION.RESTART.PersistentService");
        PendingIntent sender = PendingIntent.getBroadcast(MMSReceiverService.this,0,intent,0);

        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);

        /**
         * 알람 취소
         */
        alarmManager.cancel(sender);
    }

    private void initData(){


        countDownTimer();
        countDownTimer.start();
    }

    public void countDownTimer(){

        countDownTimer = new CountDownTimer(MILLISINFUTURE, COUNT_DOWN_INTERVAL) {
            public void onTick(long millisUntilFinished) {

                Log.i("PersistentService","onTick");
            }
            public void onFinish() {

                Log.i("PersistentService","onFinish");
            }
        };
    }

}
