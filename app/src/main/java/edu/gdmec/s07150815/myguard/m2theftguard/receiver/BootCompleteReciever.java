package edu.gdmec.s07150815.myguard.m2theftguard.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import edu.gdmec.s07150815.myguard.App;

/**
 * Created by D on 2016/12/20.
 */
public class BootCompleteReciever extends BroadcastReceiver{
    private static final String TAG  = BootCompleteReciever.class.getSimpleName();
    @Override
    public void onReceive(Context context, Intent intent) {
        ((App) context.getApplicationContext()).correctSIM();
    }
}
