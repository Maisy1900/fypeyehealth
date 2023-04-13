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
import com.example.myeyehealth.data.AmslerGridMethods;
import com.example.myeyehealth.data.SessionActivity;
import com.example.myeyehealth.data.SessionManager;
import com.example.myeyehealth.data.UserMethods;
import com.example.myeyehealth.model.User;
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

        // Get references to the ImageButtons and menu buttons
        ScrollUpButton = findViewById(R.id.scroll_up_button);
        ScrollDownButton = findViewById(R.id.scroll_down_button);

        mMenuButtons = new Button[]{
                findViewById(R.id.button1),
                findViewById(R.id.button2),
                findViewById(R.id.button3),
                findViewById(R.id.button4),
                findViewById(R.id.button5)
        };

        // Initialise the menu buttons' tags and texts
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

        onLoggedIn(null);
    }

    protected void onLoggedIn(User user) {
        int specificUserId = 21;
        // Create an instance of UserMethods
        UserMethods userMethods = new UserMethods(this);
        // Fetch the user with the specific ID from the database
        user = userMethods.getUserById(specificUserId);
        if (user != null) {
            // Create a session for the specific user
            SessionManager sessionManager = SessionManager.getInstance(this);
            sessionManager.createLoginSession(user);

           // System.out.println("userid: " + user.getId());

            int userID = user.getId();
            AmslerGridMethods amslerResultMethods = new AmslerGridMethods(this);
            HashMap<Integer, List<HashMap<String, String>>> pastFiveTests = amslerResultMethods.getPastFiveTests(userID);
            //printFormattedResults(pastFiveTests);



        } else {
            // Handle the case when the user is not found

        }
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

            // Update the intent associated with the button
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
    private void printFormattedResults(HashMap<Integer, List<HashMap<String, String>>> groupedResults) {
        int testNumber = 1;
        for (Map.Entry<Integer, List<HashMap<String, String>>> entry : groupedResults.entrySet()) {
            List<HashMap<String, String>> results = entry.getValue();

            System.out.println("Test Number: " + entry.getKey() + " | Date: " + results.get(0).get("testDate"));


            HashMap<String, StringBuilder> coordinates = new HashMap<>();
            coordinates.put("L", new StringBuilder());
            coordinates.put("R", new StringBuilder());

            int leftIndex = 1;
            int rightIndex = 1;
            for (HashMap<String, String> result : results) {
                String gridType = result.get("grid");
                String xCoord = result.get("xCoord");
                String yCoord = result.get("yCoord");

                if (gridType.equals("L")) {
                    coordinates.get("L").append(leftIndex).append(":(").append(xCoord).append(",").append(yCoord).append("), ");
                    leftIndex++;
                } else if (gridType.equals("R")) {
                    coordinates.get("R").append(rightIndex).append(":(").append(xCoord).append(",").append(yCoord).append("), ");
                    rightIndex++;
                }
            }

            if (coordinates.get("L").length() > 0) {
                System.out.println("Left Coordinates: " + coordinates.get("L").substring(0, coordinates.get("L").length() - 2));
            }
            if (coordinates.get("R").length() > 0) {
                System.out.println("Right Coordinates: " + coordinates.get("R").substring(0, coordinates.get("R").length() - 2));
            }

            System.out.println();

            testNumber++;
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