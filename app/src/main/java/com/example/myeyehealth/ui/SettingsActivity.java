package com.example.myeyehealth.ui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myeyehealth.R;
import com.example.myeyehealth.data.SessionManager;

public class SettingsActivity extends AppCompatActivity {
    private ScrollView settingsScrollView;
    private LinearLayout scroll_container;
    private Button fontSmall, fontMedium, fontLarge;
    private Button thicknessSmall, thicknessMedium, thicknessLarge;
    private Button colorRed, colorBlue, colorGreen, colorYellow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Set up the buttons
        fontSmall = findViewById(R.id.font_small);
        fontMedium = findViewById(R.id.font_medium);
        fontLarge = findViewById(R.id.font_large);

        thicknessSmall = findViewById(R.id.thickness_small);
        thicknessMedium = findViewById(R.id.thickness_medium);
        thicknessLarge = findViewById(R.id.thickness_large);

        colorRed = findViewById(R.id.color_red);
        colorBlue = findViewById(R.id.color_blue);
        colorGreen = findViewById(R.id.color_green);
        colorYellow = findViewById(R.id.color_yellow);

        // Retrieve the saved settings
        String fontSize = SessionManager.getInstance(this).getFontSize();
        String saccadesColor = SessionManager.getInstance(this).getSaccadesColor();
        String gridThickness = SessionManager.getInstance(this).getGridThickness();
        // Set the default settings if there are no saved settings
        if (fontSize.equals("S")) {
            setSelected(fontSmall);
        } else if (fontSize.equals("M")) {
            setSelected(fontMedium);
        } else if (fontSize.equals("L")) {
            setSelected(fontLarge);
        }

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
        fontSmall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearSelected(new Button[]{fontMedium, fontLarge});
                setSelected(fontSmall);
                SessionManager.getInstance(getApplicationContext()).setFontSize("S");
            }
        });

        fontMedium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearSelected(new Button[]{fontSmall, fontLarge});
                setSelected(fontMedium);
                SessionManager.getInstance(getApplicationContext()).setFontSize("M");
            }
        });

        fontLarge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearSelected(new Button[]{fontSmall, fontMedium});
                setSelected(fontLarge);
                SessionManager.getInstance(getApplicationContext()).setFontSize("L");
            }
        });

        thicknessSmall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearSelected(new Button[]{thicknessMedium, thicknessLarge});
                setSelected(thicknessSmall);
                SessionManager.getInstance(getApplicationContext()).setGridThickness("S");
            }
        });

        thicknessMedium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearSelected(new Button[]{thicknessSmall, thicknessLarge});
                setSelected(thicknessMedium);
                SessionManager.getInstance(getApplicationContext()).setGridThickness("M");
            }
        });

        thicknessLarge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearSelected(new Button[]{thicknessSmall, thicknessMedium});
                setSelected(thicknessLarge);
                SessionManager.getInstance(getApplicationContext()).setGridThickness("L");
            }
        });

        colorRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearSelected(new Button[]{colorBlue, colorGreen, colorYellow});
                setSelected(colorRed);
                SessionManager.getInstance(getApplicationContext()).setSaccadesColor("red");
            }
        });

        colorBlue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearSelected(new Button[]{colorRed, colorGreen, colorYellow});
                setSelected(colorBlue);
                SessionManager.getInstance(getApplicationContext()).setSaccadesColor("blue");
            }
        });

        colorGreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearSelected(new Button[]{colorRed, colorBlue, colorYellow});
                setSelected(colorGreen);
                SessionManager.getInstance(getApplicationContext()).setSaccadesColor("green");
            }
        });

        colorYellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearSelected(new Button[]{colorRed, colorBlue, colorGreen});
                setSelected(colorYellow);
                SessionManager.getInstance(getApplicationContext()).setSaccadesColor("yellow");
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
    private void setSelected(Button button) {
        int strokeWidth = 5; // Border width in pixels
        int strokeColor = Color.parseColor("#80D8FF"); // Border color

        GradientDrawable border = new GradientDrawable();
        border.setColor(Color.TRANSPARENT); // Border background color
        border.setCornerRadius(20); // Button corner radius
        border.setStroke(strokeWidth, strokeColor); // Apply the border

        button.setForeground(border);
    }

    private void clearSelected(Button[] buttons) {
        for (Button button : buttons) {
            button.setForeground(null);
        }
    }


}
