package com.example.myeyehealth.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myeyehealth.R;
import com.example.myeyehealth.data.SessionManager;

/**
 * This activity displays a splash screen with a logo and a loading bar. The loading bar updates
 * automatically to show the progress of a long-running operation. Once the operation is complete,
 * the activity finishes and launches the main activity.
 */
public class SplashActivity extends AppCompatActivity {

    /**
     * The progress bar used to show the loading progress.
     */
    private ProgressBar mProgressBar;

    /**
     * The current progress status of the progress bar.
     */
    private int mProgressStatus = 0;

    /**
     * The handler used to update the progress bar using the UI thread.
     */
    private Handler mHandler = new Handler();

    /**
     * Sets up the activity by inflating the layout and initializing the views.
     * Starts a new thread to update the progress bar.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being
     *                           shut down then this Bundle contains the data it most recently
     *                           supplied in onSaveInstanceState. Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Find the progress bar in your layout
        mProgressBar = findViewById(R.id.loading_bar);

        // Create a new thread to update the progress bar
        new Thread(new Runnable() {
            public void run() {
                while (mProgressStatus < 100) {
                    mProgressStatus++;

                    // Update the progress bar using the UI thread
                    mHandler.post(new Runnable() {
                        public void run() {
                            mProgressBar.setProgress(mProgressStatus);
                        }
                    });

                    try {
                        // Sleep for a short duration to simulate a long-running operation
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                // Once the progress bar is finished, check if the user is already logged in
                SessionManager sessionManager = new SessionManager(SplashActivity.this);
                if (sessionManager.isLoggedIn()) {
                    // User is already logged in, start the MainMenuActivity
                    Intent intent = new Intent(SplashActivity.this, MainMenuActivity.class);
                    intent.putExtra("user", sessionManager.getUser());
                    startActivity(intent);
                } else {
                    // User is not logged in, start the LoginActivity
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                }

                // Finish this activity
                finish();
            }
        }).start();
    }
}
