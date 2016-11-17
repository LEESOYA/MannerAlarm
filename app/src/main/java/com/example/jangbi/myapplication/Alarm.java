package com.example.jangbi.myapplication;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import android.widget.Toast;

public class Alarm extends Activity implements OnTimeChangedListener,
        OnClickListener, OnCheckedChangeListener {

    /*
     알람 관련 함수
     */
    //알람 매니저
    private AlarmManager mManager;
    //설정 일시
    private GregorianCalendar mstartCalendar;
    private GregorianCalendar mfinishCalendar;
    //시작시간
    private TimePicker mStartTime;
    //종료시간
    private TimePicker mFinishTime;

    /*
    받아 올 값들 멤버 변수
     */

    private EditText eMemo;
    private RadioGroup startOption;
    private String startRadio;
    private String finishRadio;
    private RadioGroup finishOption;
    private StringBuilder mStart;
    private int mstartHour;
    private int mstartMinute;
    private StringBuilder mFinish;
    private int mfinishHour;
    private int mfinishMinute;
    private String nowTime;
    private Boolean setAlarm = true;

    //통지 관련 멤버 변수
    private NotificationManager mNotification;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm);
        service();
        eMemo = (EditText) findViewById(R.id.Memo);
        startOption = (RadioGroup) findViewById(R.id.start_ringermode);
        finishOption = (RadioGroup) findViewById(R.id.finish_ringermode);

        //set 버튼 리스너 등록
        Button set_btn = (Button) findViewById(R.id.set);
        set_btn.setOnClickListener(this);

        //RingerMode설정할 값
        startOption.setOnCheckedChangeListener(this);
        finishOption.setOnCheckedChangeListener(this);

        //임시 시간 설정
        mStartTime = (TimePicker) findViewById(R.id.start_time_picker);
        mStartTime.setCurrentHour(mstartCalendar.get(Calendar.HOUR_OF_DAY));
        mstartHour = mstartCalendar.get(Calendar.HOUR_OF_DAY);
        mStartTime.setCurrentMinute(mstartCalendar.get(Calendar.MINUTE));
        mstartMinute = mstartCalendar.get(Calendar.MINUTE);
        //시간체인지
        mStartTime.setOnTimeChangedListener(this);

        mFinishTime = (TimePicker) findViewById(R.id.finish_time_picker);
        mFinishTime.setCurrentHour(mfinishCalendar.get(Calendar.HOUR_OF_DAY));
        mfinishHour = mfinishCalendar.get(Calendar.HOUR_OF_DAY);
        mFinishTime.setCurrentMinute(mfinishCalendar.get(Calendar.MINUTE));
        mfinishMinute = mfinishCalendar.get(Calendar.MINUTE);
        mFinishTime.setOnTimeChangedListener(this);
    }

    //시간 업데이트
    private void updateDisplay() {
        mStart = new StringBuilder().append(pad(mstartHour)).append(":")
                .append(pad(mstartMinute));
        mFinish = new StringBuilder().append(pad(mfinishHour)).append(":")
                .append(pad(mfinishMinute));
    }

    /*
    0~9시를 00시로 0~9분을 00~09분으로
     */
    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    // 서비스 취득
    private void service() {
        // 통지 매니저를 취득
       // mNotification = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // 알람 매니저를 취득
        mManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        // 현재 시각을 취득
        mstartCalendar = new GregorianCalendar();
        mfinishCalendar = new GregorianCalendar();
        nowTime = mstartCalendar.getTime().toString();
        // nowTime = mstartCalendar.getTimeInMillis();
    }


    //알람설정
    private void setAlarm() {
        // 예외처리 하기위해 Boolean값이 true
        if (setAlarm) {
            setAlarm = false;
            // 시작설정
            mManager.set(AlarmManager.RTC_WAKEUP,
                    mstartCalendar.getTimeInMillis(),
                    pendingIntent(startRadio, 0, false));
            // 종료설정
            mManager.set(AlarmManager.RTC_WAKEUP,
                    mfinishCalendar.getTimeInMillis(),
                    pendingIntent(finishRadio, 1, true));
            Toast.makeText(this, "설정되었습니다", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        // false 재설정
        else
            Toast.makeText(this, "다시 설정 해주세요", Toast.LENGTH_LONG).show();
    }

    //알람시간에 할 일들
    private PendingIntent pendingIntent(String mode, int requestCode, Boolean notiCancle) {
        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        intent.putExtra("ticker", eMemo.getText().toString());
        intent.putExtra("startTime", mStart.toString());
        intent.putExtra("finishTime", mFinish.toString());
        intent.putExtra("Mode", mode);
        intent.putExtra("notiCancle", notiCancle);

        PendingIntent sender = PendingIntent.getBroadcast(this, requestCode, intent, 0);
        return sender;
    }

    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        switch (view.getId()) {
            case R.id.start_time_picker:
                //시간설정
                mstartCalendar.set(mstartCalendar.get(Calendar.YEAR),
                        mstartCalendar.get(Calendar.MONTH),
                        mstartCalendar.get(Calendar.DAY_OF_MONTH), hourOfDay, minute, 00);
                //예외처리
                timeException(mstartCalendar, mStartTime);
                //업데이트
                mstartHour = hourOfDay;
                mstartMinute = minute;
                updateDisplay();
                break;
            case R.id.finish_time_picker:
                mfinishCalendar.set(mfinishCalendar.get(Calendar.YEAR),
                        mfinishCalendar.get(Calendar.MONTH),
                        mfinishCalendar.get(Calendar.DAY_OF_MONTH), hourOfDay, minute, 00);

                //예외처리
                timeException(mfinishCalendar, mFinishTime);
                mfinishHour = hourOfDay;
                mfinishMinute = minute;
                updateDisplay();
                break;
        }
    }
    // TimePick 예외처리
    private void timeException(GregorianCalendar calendarTime, TimePicker tmp) {
        // 현재시간
        long nowTime = System.currentTimeMillis();

        // 현재시간보다 작으면 예외발생
        if (nowTime >= calendarTime.getTimeInMillis()) {
            // 현재시간보다 작으면서 시작시간보다 작으면 예외발생
            if (mfinishCalendar.getTimeInMillis() >= mstartCalendar
                    .getTimeInMillis())
                Toast.makeText(this, "현재시간 이후로 설정하세요", Toast.LENGTH_SHORT)
                        .show();
            else
                Toast.makeText(this, "시작시간 이후로 설정하세요", Toast.LENGTH_LONG)
                        .show();
            return;
        } else
            // 아무런 예외없으면 알람설정 true
            setAlarm = true;
    }
    //설정 클릭시 발생 이벤트
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.set:
                setAlarm();
                break;
        }
    }

    //라디오 그룹에 의한 모드 설정
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (group == startOption) {
            switch (checkedId) {
                case R.id.s_sound:
                    startRadio = "소리";
                    break;
                case R.id.s_vibrate:
                    startRadio = "진동";
                    break;
                case R.id.s_silent:
                    startRadio = "무음";
                    break;
            }
        } else {
            switch (checkedId) {
                case R.id.f_sound:
                    finishRadio = "소리";
                    break;
                case R.id.f_vibrate:
                    finishRadio = "진동";
                    break;
                case R.id.f_silent:
                    finishRadio = "무음";
                    break;
            }
        }
    }
}
