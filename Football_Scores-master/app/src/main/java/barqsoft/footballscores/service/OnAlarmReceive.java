package barqsoft.footballscores.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import barqsoft.footballscores.Utilities;

/**
 * Created by benjaminshockley on 10/6/15.
 */
public class OnAlarmReceive extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("InBR","onReceive");

        Utilities.updateScores(context);

        Log.d("onReceive", "Scores Update Ran");
    }
}
