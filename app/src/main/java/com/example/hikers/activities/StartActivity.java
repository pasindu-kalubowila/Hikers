package com.example.hikers.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hikers.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.victor.loading.newton.NewtonCradleLoading;
import com.wang.avi.AVLoadingIndicatorView;

public class StartActivity extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    NewtonCradleLoading newtonCradleLoading;
    AVLoadingIndicatorView avLoadingIndicatorView;

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }


    public void FullScreencall() {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
    }

    @Override
    protected void onStart() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        super.onStart();
        FullScreencall();

        int SPLASH_TIME_OUT = 3000;
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                startAnim();

                if (isNetworkConnected()) {
                    if (firebaseUser != null) {
                        boolean emailVerified = firebaseUser.isEmailVerified();
                        if (emailVerified) {

                            startActivity(new Intent(StartActivity.this, MainActivity.class));

                            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                            // Intent to start the Broadcast Receiver MyAlarm Kiyanne mage class eke name eka
                            Intent intent1 = new Intent(getApplicationContext(), MyAlarm.class);
                            // The Pending Intent to pass in AlarmManager
                            PendingIntent pendingIntent1 = PendingIntent.getBroadcast(getApplicationContext(), 0, intent1, 0);
                            alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000, pendingIntent1);
                            finish();
                        } else {
                            startActivity(new Intent(StartActivity.this, LoginActivity.class));
                            Toast.makeText(StartActivity.this, "Verify Your Email ", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Intent i = new Intent(StartActivity.this, LoginActivity.class);
                        startActivity(i);
                        finish();
                    }

                } else {
                    Toast.makeText(StartActivity.this, "No Network Connection Check Connection", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }, SPLASH_TIME_OUT);


    }

    public void startAnim() {
        avLoadingIndicatorView.smoothToShow();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        avLoadingIndicatorView = findViewById(R.id.avi);


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
