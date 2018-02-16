package com.valuecomposite.revibr.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.valuecomposite.revibr.Services.MMSReceiverService;



public class ServiceRestartBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //TaskKiller Killed Service or Phone Rebooted
        if((intent.getAction().equals("ACTION.RESTART.PersistentService"))||(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)))
        {
            Intent i = new Intent(context, MMSReceiverService.class);
            context.startService(i);
        }
        //App was updated
        if(intent.getAction().equals(Intent.ACTION_PACKAGE_REPLACED)){

            // 앱이 업데이트 되었을 때
            Intent i = new Intent(context, MMSReceiverService.class);

            context.startService(i);
        }
    }
}
