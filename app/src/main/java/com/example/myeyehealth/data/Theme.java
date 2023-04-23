package com.example.myeyehealth.data;

import android.content.Context;

import com.example.myeyehealth.R;
import com.example.myeyehealth.data.SessionManager;

public class Theme{

    public static void setAppTheme(Context context) {
        String theme = SessionManager.getInstance(context).getThemeName();
        switch (theme) {
            case "Theme.MyEyeHealth.Light":
                context.setTheme(R.style.Theme_MyEyeHealth_Light);
                break;
            case "Theme.MyEyeHealth.Dark":
                context.setTheme(R.style.Theme_MyEyeHealth_Dark);
                break;
            case "Theme.MyEyeHealth.HighContrast":
                context.setTheme(R.style.Theme_MyEyeHealth_HighContrast);
                break;
        }
    }
}
