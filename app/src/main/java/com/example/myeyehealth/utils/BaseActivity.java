package com.example.myeyehealth.utils;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.myeyehealth.R;
import com.example.myeyehealth.utils.SessionManager;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setAppTheme();
        super.onCreate(savedInstanceState);
    }

    protected void setAppTheme() {
        String theme = SessionManager.getInstance(this).getThemeName();
        switch (theme) {
            case "Theme.MyEyeHealth.Light":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                setTheme(R.style.Theme_MyEyeHealth_Light);
                break;
            case "Theme.MyEyeHealth.Dark":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                setTheme(R.style.Theme_MyEyeHealth_Dark);
                break;
            case "Theme.MyEyeHealth.HighContrast":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                setTheme(R.style.Theme_MyEyeHealth_HighContrast);
                break;
        }
    }
}
