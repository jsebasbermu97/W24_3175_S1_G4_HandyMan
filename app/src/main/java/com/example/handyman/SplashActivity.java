package com.example.handyman;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getSupportActionBar().hide();

        ((TextView) findViewById(R.id.textView)).animate().translationY(-300).setDuration(2700).setStartDelay(0);

        // ---------- splash screen animation --------
        ((LottieAnimationView) findViewById(R.id.lottie)).animate().translationX(2000).setDuration(2000).setStartDelay(2900);

        TimerTask timerTask2 = new TimerTask() {
            @Override
            public void run()
            {
                ((TextView) findViewById(R.id.textView)).animate().translationX(2000).setDuration(2000).setStartDelay(0);
            }
        };

        Timer timer2 = new Timer();
        timer2.schedule(timerTask2, 3300);

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            }
        };

        Timer timer = new Timer();
        timer.schedule(timerTask, 5000);
    }
}