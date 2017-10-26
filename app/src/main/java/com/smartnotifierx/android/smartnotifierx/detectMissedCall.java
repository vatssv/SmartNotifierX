package com.smartnotifierx.android.smartnotifierx;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.widget.Toast;

/**
 * Created by sv300_000 on 07-Jul-17.
 */

public class detectMissedCall extends BroadcastReceiver {
    static boolean isRinging = false;
    static boolean isReceived = false;
    static String callerPhoneNumber;
    @Override
    public void onReceive(Context context, Intent intent) {
        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        if(state == null)
        {
            return;
        }

        if(state.equals(TelephonyManager.EXTRA_STATE_RINGING))
        {
            isRinging = true;
            Bundle bundle = intent.getExtras();
            callerPhoneNumber = bundle.getString("incoming_number");
        }

        if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK))
        {
            isReceived = true;
        }

        if (state.equals(TelephonyManager.EXTRA_STATE_IDLE))
        {
            if (isRinging == true && isReceived == false)
            {
                Toast.makeText(context, "Got a missed call " + callerPhoneNumber, Toast.LENGTH_SHORT).show();
                //SmsManager smsManager = SmsManager.getDefault();
                //smsManager.sendTextMessage(callerPhoneNumber, null, "hi ", null, null);
            }
        }

    }
}
