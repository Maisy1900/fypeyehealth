package com.example.myeyehealth.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myeyehealth.R;
import com.example.myeyehealth.data.Database;
import com.example.myeyehealth.data.UserMethods;
import com.example.myeyehealth.model.User;
import com.example.myeyehealth.ui.account.CreateAccountEmailActivity;

import java.util.regex.Pattern;

public class ProfileActivity extends AppCompatActivity {

    private EditText mNameInput;
    private EditText mEmailInput;
    private EditText mPasswordInput;
    private Button mSaveButton;
    private Button mCancelButton;

    private Database db;
    private int mUserId;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize the EditText fields and buttons
        mNameInput = findViewById(R.id.name_input);
        mEmailInput = findViewById(R.id.email_input);
        mPasswordInput = findViewById(R.id.password_input);
        mSaveButton = findViewById(R.id.save_button);
        mCancelButton = findViewById(R.id.cancel_button);

        // Initialize the DatabaseHelper and set the user ID
        db = new Database(this);
        mUserId = 1; // Replace this with the actual user ID

        // Fetch the user data and update the EditText fields
        mUser = db.getUserById(mUserId);
        if (mUser != null) {
            mNameInput.setText(mUser.getName());
            mEmailInput.setText(mUser.getEmail());
            mPasswordInput.setText(mUser.getPassword());
        }

        // Set up a click listener for the save button
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserData();
            }
        });

        // Set up a click listener for the cancel button
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                revertChanges();
            }
        });
        Button amslerGridDataButton = findViewById(R.id.amsler_grid_data_button);
        amslerGridDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, ProfileAmslerDataActivity.class);
                startActivity(intent);
            }
        });

    }

    private void saveUserData() {
        // Get the updated data from the EditText fields
        String updatedName = mNameInput.getText().toString();
        String updatedEmail = mEmailInput.getText().toString();
        String updatedPassword = mPasswordInput.getText().toString();

        // Check if any values have changed
        if (updatedName.equals(mUser.getName()) && updatedEmail.equals(mUser.getEmail()) && updatedPassword.equals(mUser.getPassword())) {
            Toast.makeText(ProfileActivity.this, "No changes detected", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate the updated data
        if (!isValidEmail(updatedEmail)) {
            return;
        }

        // Update the user object
        mUser.setName(updatedName);
        mUser.setEmail(updatedEmail);
        mUser.setPassword(updatedPassword);

        // Update the user data in the database
        db.updateUser(mUser);
    }

    private void revertChanges() {
        mNameInput.setText(mUser.getName());
        mEmailInput.setText(mUser.getEmail());
        mPasswordInput.setText(mUser.getPassword());
    }

    private boolean isValidEmail(String email) {
        // Check if the email field is empty
        if (email.isEmpty()) {
            return true;
        }

        // Use a regular expression to validate the email format
        String emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        Pattern pattern = Pattern.compile(emailRegex);

        if (!pattern.matcher(email).matches()) {
            return false;
        }

        // Check if the email already exists in the database, excluding the current user's email
        UserMethods userMethods = new UserMethods(ProfileActivity.this);
        User existingUser = userMethods.getUserByEmail(email);

        if (existingUser != null && existingUser.getId() != mUser.getId()) {
            Toast.makeText(ProfileActivity.this, "Email address already in use", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

}