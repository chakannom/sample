package kr.co.gtson.example.introactivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

public class IntroActivity extends Activity {

    Handler handlerIntro;
    Runnable runnableIntro = new Runnable() {
        @Override
        public void run() {
            Intent intentIntro = new Intent(IntroActivity.this, MainActivity.class); //인텐트 생성(현 액티비티, 새로 실행할 액티비티)
            startActivity(intentIntro);
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            //overridePendingTransition 이란 함수를 이용하여 fade in,out 효과를줌. 순서가 중요
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_intro);

        handlerIntro = new Handler(); //딜래이를 주기 위해 핸들러 생성
        handlerIntro.postDelayed(runnableIntro, 5000); // 딜레이 (런어블 객체는 runnableIntro, 시간 5초)
    }

    //인트로 중에 뒤로가기를 누를 경우 핸들러를 끊어버려 아무일 없게 만드는 부분
    //미 설정시 인트로 중 뒤로가기를 누르면 인트로 후에 홈화면이 나옴.
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        handlerIntro.removeCallbacks(runnableIntro);
    }
}
