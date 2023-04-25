package com.example.myeyehealth.utils;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myeyehealth.R;
import com.example.myeyehealth.model.User;
import com.example.myeyehealth.ui.LoginActivity;
import com.example.myeyehealth.utils.SessionManager;

public abstract class SessionActivity extends AppCompatActivity {

    private SessionManager sessionManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAppTheme();
        sessionManager = SessionManager.getInstance(this);

        if (shouldCheckLoginStatus()) {

            if (!sessionManager.isLoggedIn()) {

                Intent intent = new Intent(this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            } else {

                User user = sessionManager.getUser();

                onLoggedIn(user);
            }
        } else {

            onLoggedIn(null);
        }
    }


    protected abstract void onLoggedIn(User user);


    protected boolean shouldCheckLoginStatus() {
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        sessionManager = null;
    }

    protected void setAppTheme() {
        String theme = SessionManager.getInstance(this).getThemeName();
        switch (theme) {
            case "Theme.MyEyeHealth.Light":
                setTheme(R.style.Theme_MyEyeHealth_Light);
                break;
            case "Theme.MyEyeHealth.Dark":
                setTheme(R.style.Theme_MyEyeHealth_Dark);
                break;
            case "Theme.MyEyeHealth.HighContrast":
                setTheme(R.style.Theme_MyEyeHealth_HighContrast);
                break;
        }
    }
}
