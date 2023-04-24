package com.example.myeyehealth.ui.account;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.myeyehealth.R;
import com.example.myeyehealth.utils.BaseActivity;
import com.example.myeyehealth.controller.UserMethods;
import com.example.myeyehealth.model.User;

public class CreateAccountConfirmPasswordActivity extends BaseActivity {
    private EditText confirmedPasswordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account_confirm_password);

        confirmedPasswordInput = findViewById(R.id.confirmed_password_input);
        Button confirmButton = findViewById(R.id.confirm_button);
        ImageButton backButton = findViewById(R.id.back_button);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = getIntent().getStringExtra("name");
                String email = getIntent().getStringExtra("email");
                String password = getIntent().getStringExtra("password");
                String confirmedPassword = confirmedPasswordInput.getText().toString();

                if (confirmedPassword.equals(password)) {
                    // Create a new User object with the given details
                    User user = new User(-1, name, email, password, "", "", "", "");

                    // Save the user's information to the database and get the ID of the newly inserted row
                    UserMethods userMethods = new UserMethods(CreateAccountConfirmPasswordActivity.this);
                    int userId = (int) userMethods.addUser(user);

                    // Set the id of the user object to the generated id
                    user.setId(userId);

                    // Start the CreateAccountSuccessActivity and pass the user ID as an extra
                    Intent intent = new Intent(CreateAccountConfirmPasswordActivity.this, CreateAccountSuccessActivity.class);
                    intent.putExtra("user_id", userId);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(CreateAccountConfirmPasswordActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeExtraDataFromIntent();
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        removeExtraDataFromIntent();
        super.onBackPressed();
    }

    private void removeExtraDataFromIntent() {
        Intent intent = getIntent();
        intent.removeExtra("name");
        intent.removeExtra("email");
        intent.removeExtra("password");
        setIntent(intent);
    }
}
