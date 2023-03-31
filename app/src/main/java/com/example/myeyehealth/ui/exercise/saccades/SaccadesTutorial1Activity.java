package com.example.myeyehealth.ui.exercise.saccades;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myeyehealth.R;

public class SaccadesTutorial1Activity extends AppCompatActivity {
    private ScrollView tutorialScrollView;
    private TextView tutorialText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saccades_tutorial1);

        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tutorialScrollView = findViewById(R.id.tutorial_scroll_view);
        tutorialText = findViewById(R.id.tutorial_text);


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
                Intent intent = new Intent(SaccadesTutorial1Activity.this, SaccadesExerciseActivity.class);
                startActivity(intent);
            }
        });

        TextView tutorialText = findViewById(R.id.tutorial_text);
        tutorialText.setText("Be in a place with good lighting and a steady surface to put your device.\n\n"
                + "Hold the device at a comfortable distance from your face, make sure the screen is centred and not angled.\n\n"
                + "Focus you eyes on the large dot at the centre of the screen.\n\n"
                + "Smaller peripheral dots will appear around the central dot.\n\n"
                + "Tap the first indicated dot, then quickly move your gaze to the next indicated dot as it changes colour.\n\n"
                + "Tap on each consecutive dot in the sequence to complete the exercise.");
        tutorialText.setPadding(0, 500, 0, 500);
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
