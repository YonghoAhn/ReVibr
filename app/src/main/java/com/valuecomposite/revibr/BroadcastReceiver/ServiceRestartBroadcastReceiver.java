package com.valuecomposite.revibr.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.valuecomposite.revibr.Services.MMSReceiverService;

/**
 * Created by misakamoe on 2018. 2. 16..
 */

public class ServiceRestartBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //TaskKiller Killed Service or Phone Rebooted
        if((intent.getAction().equals("ACTION.RESTART.PersistentService"))||(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)))
        {
            Intent i = new Intent(context, MMSReceiverService.class);
            context.startService(i);
        }
    }
}
