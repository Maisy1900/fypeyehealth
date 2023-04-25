package com.example.myeyehealth.ui;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.myeyehealth.R;
import com.example.myeyehealth.controller.AmslerGridMethods;
import com.example.myeyehealth.model.Database;
import com.example.myeyehealth.utils.SessionActivity;
import com.example.myeyehealth.utils.SessionManager;
import com.example.myeyehealth.controller.UserMethods;
import com.example.myeyehealth.model.User;
import com.example.myeyehealth.ui.profile.ProfileActivity;
import com.example.myeyehealth.ui.amslergrid.AmslerGridTutorial1Activity;
import com.example.myeyehealth.ui.exercise.saccades.SaccadesTutorial1Activity;
import com.example.myeyehealth.ui.reminders.RemindersActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainMenuActivity extends SessionActivity implements View.OnClickListener {

    private ImageButton ScrollUpButton;
    private ImageButton ScrollDownButton;
    private Button[] mMenuButtons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        createNotificationChannel();
        Database database = Database.getInstance(this);

        ScrollUpButton = findViewById(R.id.scroll_up_button);
        ScrollDownButton = findViewById(R.id.scroll_down_button);

        mMenuButtons = new Button[]{
                findViewById(R.id.button1),
                findViewById(R.id.button2),
                findViewById(R.id.button3),
                findViewById(R.id.button4),
                findViewById(R.id.button5)
        };

        String[] buttonTexts = getResources().getStringArray(R.array.menu_button_texts);

        for (int i = 0; i < mMenuButtons.length; i++) {
            mMenuButtons[i].setTag(i);
            mMenuButtons[i].setText(buttonTexts[i]);
            mMenuButtons[i].setTextSize(32);
        }

        for (Button button : mMenuButtons) {
            button.setOnClickListener(this);
        }

        ScrollDownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollMenu(-1);
            }
        });

        ScrollUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollMenu(1);
            }
        });

        SessionManager sessionManager = SessionManager.getInstance(this);
        if (sessionManager.isLoggedIn()) {
            User user = sessionManager.getUser();

        }
    }

    @Override
    protected void onLoggedIn(User user) {

    }


    @Override
    protected boolean shouldCheckLoginStatus() {
        return false;
    }

    private void scrollMenu(int direction) {
        String[] buttonTexts = getResources().getStringArray(R.array.menu_button_texts);

        for (int i = 0; i < mMenuButtons.length; i++) {
            int currentIndex = (Integer) mMenuButtons[i].getTag();
            int newIndex = (currentIndex + direction + buttonTexts.length) % buttonTexts.length;
            mMenuButtons[i].setTag(newIndex);
            mMenuButtons[i].setText(buttonTexts[newIndex]);


            switch (newIndex) {
                case 0:
                    mMenuButtons[i].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MainMenuActivity.this, AmslerGridTutorial1Activity.class);
                            startActivity(intent);
                        }
                    });
                    break;
                case 1:
                    mMenuButtons[i].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MainMenuActivity.this, SaccadesTutorial1Activity.class);
                            startActivity(intent);
                        }
                    });
                    break;
                case 2:
                    mMenuButtons[i].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MainMenuActivity.this, RemindersActivity.class);
                            startActivity(intent);
                        }
                    });
                    break;
                case 3:
                    mMenuButtons[i].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MainMenuActivity.this, SettingsActivity.class);
                            startActivity(intent);
                        }
                    });
                    break;
                case 4:
                    mMenuButtons[i].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MainMenuActivity.this, ProfileActivity.class);
                            startActivity(intent);
                        }
                    });
                    break;
                default:
                    break;
            }
        }
    }


    @Override
    public void onClick(View view) {
        if (view instanceof Button) {
            int position = -1;
            for (int i = 0; i < mMenuButtons.length; i++) {
                if (view == mMenuButtons[i]) {
                    position = i;
                    break;
                }
            }

            if (position == -1) {
                return;
            }

            Intent intent;

            switch (position) {
                case 0:
                    intent = new Intent(this, AmslerGridTutorial1Activity.class);
                    break;
                case 1:
                    intent = new Intent(this, SaccadesTutorial1Activity.class);
                    break;
                case 2:
                    intent = new Intent(this, RemindersActivity.class);
                    break;
                case 3:
                    intent = new Intent(this, SettingsActivity.class);
                    break;
                case 4:
                    intent = new Intent(this, ProfileActivity.class);
                    break;
                default:
                    return;
            }
            startActivity(intent);
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Reminder Channel";
            String description = "Channel for reminder notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("reminder_channel", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}