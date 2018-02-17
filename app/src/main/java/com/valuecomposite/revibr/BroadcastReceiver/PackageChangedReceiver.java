package com.valuecomposite.revibr.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.valuecomposite.revibr.Services.MMSReceiverService;

public class PackageChangedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("MisakaMOE","Called_SERVICE");
        Intent i = new Intent(context, MMSReceiverService.class);
        context.startService(i);
    }
}
