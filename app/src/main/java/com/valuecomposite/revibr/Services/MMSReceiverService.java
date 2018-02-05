package com.valuecomposite.revibr.Services;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.valuecomposite.revibr.utils.Messages.MMSMon;

public class MMSReceiverService extends Service {
    private MMSMon mmsMon;

    @Override
    public int onStartCommand(Intent intent,int flags,int startId )
    {
        //뭐가 됐든간에 Service Start하면 호출됨
        mmsMon.startMonitor();
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
    }

    @Override
    public void onDestroy(){
// 서비스가 소멸되는 도중에 이 메소드가 호출되며 주로 Thread, Listener, BroadcastReceiver와 같은 자원들을 정리하는데 사용하면 됩니다. TaskKiller에 의해 서비스가 강제종료될 경우에는 이 메소드가 호출되지 않는다는 점 !! ㅜㅜ
        mmsMon.stopMonitor();
        mmsMon.destroyMonitor();
    }

}
