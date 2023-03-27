package com.example.myeyehealth.ui;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.myeyehealth.R;
import com.example.myeyehealth.data.Database;
import com.example.myeyehealth.data.SessionActivity;
import com.example.myeyehealth.model.User;
import com.example.myeyehealth.ui.amslergrid.AmslerGridTutorial1Activity;
import com.example.myeyehealth.ui.exercise.saccades.SaccadesTutorial1Activity;
import com.example.myeyehealth.ui.reminders.RemindersActivity;

public class MainMenuActivity extends SessionActivity implements View.OnClickListener {

    private ImageButton mScrollUpButton;
    private ImageButton mScrollDownButton;
    private Button[] mMenuButtons;

    @Override
    protected void onLoggedIn(User user) {
        Database database = Database.getInstance(this);
        setContentView(R.layout.activity_main_menu);

        // Get references to the ImageButtons and menu buttons
        mScrollUpButton = findViewById(R.id.scroll_up_button);
        mScrollDownButton = findViewById(R.id.scroll_down_button);

        mMenuButtons = new Button[]{
                findViewById(R.id.button1),
                findViewById(R.id.button2),
                findViewById(R.id.button3),
                findViewById(R.id.button4),
                findViewById(R.id.button5)
        };

        // Initialize the menu buttons' tags and texts
        String[] buttonTexts = getResources().getStringArray(R.array.menu_button_texts);
        for (int i = 0; i < mMenuButtons.length; i++) {
            mMenuButtons[i].setTag(i);
            mMenuButtons[i].setText(buttonTexts[i]);
        }

        // Set click listeners for the menu buttons
        for (Button button : mMenuButtons) {
            button.setOnClickListener(this);
        }

        // Set click listeners for the up and down buttons
        mScrollDownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollMenu(-1);
            }
        });

        mScrollUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollMenu(1);
            }
        });
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
        }
    }

    private String getMenuButtonText(int position) {
        String[] buttonTexts = getResources().getStringArray(R.array.menu_button_texts);
        return buttonTexts[position];
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
                return; // No action for other cases
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
                    return; // No action for other cases
            }
            startActivity(intent);
        }
    }
}
