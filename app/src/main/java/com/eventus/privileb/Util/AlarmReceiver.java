package com.eventus.privileb.Util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.eventus.privileb.SignupActivity;

/**
 * Created by BLOOMAY on 6/21/2017.
 */

public class AlarmReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("Inside Alarm", "Receiver");
            Intent i = new Intent(context, SignupActivity.class);
            context.startActivity(i);
        }

}
