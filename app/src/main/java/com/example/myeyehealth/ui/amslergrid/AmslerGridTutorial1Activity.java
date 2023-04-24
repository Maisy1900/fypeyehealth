package com.example.myeyehealth.ui.amslergrid;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.myeyehealth.R;
import com.example.myeyehealth.utils.BaseActivity;
import com.example.myeyehealth.utils.SessionManager;
import com.example.myeyehealth.model.User;

public class AmslerGridTutorial1Activity extends BaseActivity {

    private ScrollView tutorialScrollView;
    private TextView tutorialText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amsler_grid_tutorial1);

        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tutorialScrollView = findViewById(R.id.tutorial_scroll_view);
        tutorialText = findViewById(R.id.tutorial_text);
        SessionManager sessionManager = SessionManager.getInstance(this);
        User user = sessionManager.getUser();
        int userId= user.getId();
        System.out.println("user id should be 21"+userId);

        ImageButton scrollUpButton = findViewById(R.id.scroll_up_button);
        scrollUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollText(-50);
            }
        });

        ImageButton scrollDownButton = findViewById(R.id.scroll_down_button);
        scrollDownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollText(50);
            }
        });

        Button startTestButton = findViewById(R.id.start_test_button);
        startTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AmslerGridTutorial1Activity.this, AmslerGridLeftEyeTestActivity.class);
                startActivity(intent);
            }
        });

        tutorialText.setText("Be in a place with good lighting and a steady surface to put your device.\n\n"
                + "Hold the device at a comfortable distance from your face, make sure the screen is centred and not angled.\n\n"
                + "Cover the specified eye with your hand or an eye patch.\n\n"
                + "Look at the centre of the grid on the screen with your eye.\n\n"
                + "Check for any distorted or missing areas on the grid.\n\n"
                + "If you see any distorted or missing areas, touch or click on the matching spot on the grid.");
        tutorialText.setPadding(0, 500, 0, 500);
        tutorialText.setTextSize(32);
        tutorialText.setTextColor(Color.BLACK);
        tutorialScrollView.post(new Runnable() {
            @Override
            public void run() {
                tutorialScrollView.scrollTo(0, 500); // Adjust the initial scroll position
            }
        });
    }

    private void scrollText(int dy) {
        int maxScrollY = tutorialText.getHeight() - tutorialScrollView.getHeight();
        int targetY = tutorialScrollView.getScrollY() + dy;
        if (targetY < 0) {
            targetY = 0;
        } else if (targetY > maxScrollY) {
            targetY = maxScrollY;
        }
        tutorialScrollView.scrollTo(0, targetY);
    }

}
