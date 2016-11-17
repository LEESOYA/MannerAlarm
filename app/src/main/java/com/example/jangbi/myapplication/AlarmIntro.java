package com.example.jangbi.myapplication;



import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.ImageView;


public class AlarmIntro extends Activity {

    Handler h;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀 바 삭제
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.intro);

        h = new Handler();
        //3초동안 유지
        h.postDelayed(irun, 3000);
    }

    Runnable irun = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(AlarmIntro.this, Alarm.class);
            startActivity(intent);
            finish();

            //애니메이션 효과
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    };

    //인트로화면 중간에 뒤로가기 버튼 누르면 3초 뒤에 메인페이지가 뜨지 않게 하기
    public void onBackPressed() {
        super.onBackPressed();
        h.removeCallbacks(irun);
    }
}