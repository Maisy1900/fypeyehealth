package com.example.myeyehealth.ui;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ScrollView;

import com.example.myeyehealth.R;
import com.example.myeyehealth.model.User;
public class MainMenuActivity extends com.example.myeyehealth.ui.LoggedIn {

    private ScrollView mScrollView;
    private ImageButton mScrollUpButton;
    private ImageButton mScrollDownButton;

    @Override
    protected void onLoggedIn(User user) {
        setContentView(R.layout.activity_main_menu);

        // Get references to the ScrollView and ImageButtons
        mScrollView = findViewById(R.id.scroll_view);
        mScrollUpButton = findViewById(R.id.scroll_up_button);
        mScrollDownButton = findViewById(R.id.scroll_down_button);

        // Set click listeners for the up and down buttons
        mScrollUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Scroll up by one screen height
                mScrollView.smoothScrollBy(0, -mScrollView.getHeight());
            }
        });

        mScrollDownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Scroll down by one screen height
                mScrollView.smoothScrollBy(0, mScrollView.getHeight());
            }
        });
    }
}
