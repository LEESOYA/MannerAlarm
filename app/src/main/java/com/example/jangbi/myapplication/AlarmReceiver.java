package com.example.jangbi.myapplication;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.*;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;



public class AlarmReceiver extends BroadcastReceiver {

    private AudioManager aManager;
    private NotificationManager nManager;
    private String ticker;
    private String Mode;
    private String startTime;
    private String finishTime;
    private Boolean notiCancel;
    private String tickerInfo;
    Bundle bundle;

    @Override
    public void onReceive(Context context, Intent intent) {
        bundle(intent);
        //TimeNotify(context);
        RingerMode(context, Mode);
        Log.d("Mode", Mode);
    }

    //값받아오기
    private void bundle(Intent intent) {
        bundle = intent.getExtras();
        Mode = bundle.getString("Mode");
        startTime = bundle.getString("startTime");
        finishTime = bundle.getString("finishTime");
        notiCancel = bundle.getBoolean("notiCancel");
        ticker = bundle.getString("ticker");

    }

/*

    @SuppressWarnings("deprecation")
    //정해진시간에 띄우는 Notification
    private void TimeNotify(Context context) {
        nManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        Notification noti = new Notification(R.drawable.clock_add, ticker + " - " + Mode + "(으)로 전환합니다", System.currentTimeMillis());
        noti.flags = Notification.FLAG_ONGOING_EVENT; //진행중 알림
        noti.setLatestEventInfo(, ticker, "" + startTime + "~" + finishTime + " / " + Mode, null);

        nManager.notify(1234, noti);
        if (notiCancel)
            nManager.cancel(1234);
        Log.d("Time", "시작시간 : " + startTime + " 끝나는시간 : " + finishTime);

    }

*/


    //RingerMode 선택
    private void RingerMode(Context context, String mode) {
        aManager = (AudioManager) context.getSystemService(context.AUDIO_SERVICE);
        if (mode.equals("소리")) {
            aManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        } else if (mode.equals("진동")) {
            aManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
        } else if (mode.equals("무음")) {
            aManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        }
    }

}
