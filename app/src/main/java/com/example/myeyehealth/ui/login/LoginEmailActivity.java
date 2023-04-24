package com.example.myeyehealth.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.myeyehealth.R;
import com.example.myeyehealth.utils.BaseActivity;

public class LoginEmailActivity extends BaseActivity {

    // define variables to store the views
    private EditText emailInput;
    private Button confirmButton;
    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_email);

        // initialize the views by finding them using their IDs
        emailInput = findViewById(R.id.email_input);
        confirmButton = findViewById(R.id.confirm_button);
        backButton = findViewById(R.id.back_button);

        // set a click listener on the confirm button
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get the email input text
                String email = emailInput.getText().toString().trim();

                // create an intent to navigate to the LoginPasswordActivity
                Intent intent = new Intent(LoginEmailActivity.this, LoginPasswordActivity.class);
                // put the email as an extra in the intent
                intent.putExtra("email", email);
                startActivity(intent);
            }
        });

        // set a click listener on the back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // finish this activity to go back to the previous activity in the stack
                finish();
            }
        });
    }
}
