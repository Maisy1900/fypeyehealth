package com.example.myeyehealth.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.example.myeyehealth.R;
import com.example.myeyehealth.utils.BaseActivity;
import com.example.myeyehealth.utils.SessionManager;

public class SettingsActivity extends BaseActivity {
    private ScrollView settingsScrollView;
    private LinearLayout scroll_container;

    private Button thicknessSmall, thicknessMedium, thicknessLarge;
    private Button colorRed, colorBlue, colorGreen, colorYellow;

    private Button themeLight, themeDark, themeHighContrast;
    private static final String PREFS_NAME = "MyEyeHealthPrefs";
    private static final String KEY_THEME = "theme";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the theme based on the saved preference
        SharedPreferences sharedPreferences = getSharedPreferences("theme_prefs", MODE_PRIVATE);
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

        setContentView(R.layout.activity_settings);


        thicknessSmall = findViewById(R.id.thickness_small);
        thicknessMedium = findViewById(R.id.thickness_medium);
        thicknessLarge = findViewById(R.id.thickness_large);

        colorRed = findViewById(R.id.color_red);
        colorBlue = findViewById(R.id.color_blue);
        colorGreen = findViewById(R.id.color_green);
        colorYellow = findViewById(R.id.color_yellow);

        themeLight = findViewById(R.id.theme_light);

        themeHighContrast = findViewById(R.id.theme_high_contrast);


        // Retrieve the saved settings
        String saccadesColor = SessionManager.getInstance(this).getSaccadesColor();
        String gridThickness = SessionManager.getInstance(this).getGridThickness();



        if (saccadesColor.equals("red")) {
            setSelected(colorRed);
        } else if (saccadesColor.equals("blue")) {
            setSelected(colorBlue);
        } else if (saccadesColor.equals("green")) {
            setSelected(colorGreen);
        } else if (saccadesColor.equals("yellow")) {
            setSelected(colorYellow);
        }


        if (gridThickness.equals("S")) {
            setSelected(thicknessSmall);
        } else if (gridThickness.equals("M")) {
            setSelected(thicknessMedium);
        } else if (gridThickness.equals("L")) {
            setSelected(thicknessLarge);
        }

        // Apply the selected state to the theme buttons
        if (theme.equals("Theme.MyEyeHealth.Light")) {
            setSelected(themeLight);
        } else if (theme.equals("Theme.MyEyeHealth.Dark")) {
            setSelected(themeDark);
        } else if (theme.equals("Theme.MyEyeHealth.HighContrast")) {
            setSelected(themeHighContrast);
        }

        scroll_container = findViewById(R.id.scroll_container);
        scroll_container.post(new Runnable() {
            @Override
            public void run() {
                scroll_container.scrollTo(0, 0); // Set the initial scroll position to 0
            }
        });

        // Remove the padding line
       scroll_container.setPadding(0, 500, 0, 1500);

        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, MainMenuActivity.class);
                startActivity(intent);
                finish();
            }
        });

        settingsScrollView = findViewById(R.id.tutorial_scroll_view);

        ImageButton scrollUpButton = findViewById(R.id.scroll_up_button);
        scrollUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollSettings(-50);
            }
        });

        ImageButton scrollDownButton = findViewById(R.id.scroll_down_button);
        scrollDownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollSettings(50);
            }
        });
        settingsScrollView.post(new Runnable() {
            @Override
            public void run() {
                settingsScrollView.scrollTo(0, 500); // Set the initial scroll position to 500
            }
        });
        thicknessSmall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearSelected(new Button[]{thicknessMedium, thicknessLarge});
                setSelected(thicknessSmall);
                SessionManager.getInstance(getApplicationContext()).setGridThickness("S");
                recreate();
            }
        });

        thicknessMedium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearSelected(new Button[]{thicknessSmall, thicknessLarge});
                setSelected(thicknessMedium);
                SessionManager.getInstance(getApplicationContext()).setGridThickness("M");
                recreate();
            }
        });

        thicknessLarge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearSelected(new Button[]{thicknessSmall, thicknessMedium});
                setSelected(thicknessLarge);
                SessionManager.getInstance(getApplicationContext()).setGridThickness("L");
                recreate();
            }
        });

        colorRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearSelected(new Button[]{colorBlue, colorGreen, colorYellow});
                setSelected(colorRed);
                SessionManager.getInstance(getApplicationContext()).setSaccadesColor("red");
                recreate();
            }
        });

        colorBlue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearSelected(new Button[]{colorRed, colorGreen, colorYellow});
                setSelected(colorBlue);
                SessionManager.getInstance(getApplicationContext()).setSaccadesColor("blue");
                recreate();
            }
        });

        colorGreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearSelected(new Button[]{colorRed, colorBlue, colorYellow});
                setSelected(colorGreen);
                SessionManager.getInstance(getApplicationContext()).setSaccadesColor("green");
                recreate();
            }
        });

        colorYellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearSelected(new Button[]{colorRed, colorBlue, colorGreen});
                setSelected(colorYellow);
                SessionManager.getInstance(getApplicationContext()).setSaccadesColor("yellow");
                recreate();
            }
        });
        // Set the onClick listeners for the theme buttons
        themeLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SessionManager.getInstance(getApplicationContext()).updateTheme("Theme.MyEyeHealth.Light");
                recreate();
            }
        });

        themeHighContrast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SessionManager.getInstance(getApplicationContext()).updateTheme("Theme.MyEyeHealth.HighContrast");
                recreate();
            }
        });

    }

    private void scrollSettings(int dy) {
        int maxScrollY = settingsScrollView.getChildAt(0).getHeight() - settingsScrollView.getHeight();
        int targetY = settingsScrollView.getScrollY() + dy;
        if (targetY < 0) {
            targetY = 0;
        } else if (targetY > maxScrollY) {
            targetY = maxScrollY;
        }
        settingsScrollView.smoothScrollTo(0, targetY);
    }
    private void saveTheme(String theme) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_THEME, theme);
        editor.apply();
    }

    private void setSelected(Button button) {
        int strokeWidth = 4; // Border width in pixels
        int strokeColor = Color.parseColor("#000000"); // Border color
        int fillColor = Color.parseColor("#99FFFFFF"); // Fill color (60% transparent white)

        GradientDrawable border = new GradientDrawable();
        border.setColor(fillColor); // Fill background color
        border.setCornerRadius(20); // Button corner radius
        border.setStroke(strokeWidth, strokeColor); // Apply the border

        button.setBackground(border);
        button.setTextColor(strokeColor);
    }



    private void clearSelected(Button[] buttons) {
        for (Button button : buttons) {
            button.setForeground(null);
        }
    }
    private void updateTheme(String theme) {
        SharedPreferences sharedPreferences = getSharedPreferences("appTheme", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("theme", theme);
        editor.apply();
        recreate();
    }
    private void setThemePreference(String theme) {
        SharedPreferences sharedPreferences = getSharedPreferences("theme_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("theme", theme);
        editor.apply();
    }
}

