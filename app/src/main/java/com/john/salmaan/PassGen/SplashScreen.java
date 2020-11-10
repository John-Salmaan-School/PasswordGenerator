package com.john.salmaan.PassGen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(3000);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    Intent i = new Intent(getBaseContext(), MainActivity.class);
                    startActivity(i);
                }
            }
        };
        thread.start();
    }

    @Override
    public void onPause () {
        super.onPause();
        finish();
    }
}