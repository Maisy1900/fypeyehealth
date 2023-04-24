package com.example.myeyehealth.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

import com.example.myeyehealth.R;
import com.example.myeyehealth.utils.BaseActivity;
import com.example.myeyehealth.utils.SessionManager;

/**
 * This activity displays a splash screen with a logo and a loading bar. The loading bar updates
 * automatically to show the progress of a long-running operation. Once the operation is complete,
 * the activity finishes and launches the main activity.
 */
public class SplashActivity extends BaseActivity {
    private ProgressBar mProgressBar;
    private int mProgressStatus = 0;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mProgressBar = findViewById(R.id.loading_bar);

        new Thread(new Runnable() {
            public void run() {
                while (mProgressStatus < 100) {
                    mProgressStatus++;

                    mHandler.post(new Runnable() {
                        public void run() {
                            mProgressBar.setProgress(mProgressStatus);
                        }
                    });

                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                SessionManager sessionManager = SessionManager.getInstance(SplashActivity.this);
                if (sessionManager.isLoggedIn()) {
                    Intent intent = new Intent(SplashActivity.this, MainMenuActivity.class);
                    intent.putExtra("user", sessionManager.getUser());
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                }

                finish();
            }
        }).start();
    }
}

