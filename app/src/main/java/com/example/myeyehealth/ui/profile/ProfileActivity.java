package com.example.myeyehealth.ui.profile;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.myeyehealth.R;
import com.example.myeyehealth.data.Database;
import com.example.myeyehealth.data.SessionActivity;
import com.example.myeyehealth.data.SessionManager;
import com.example.myeyehealth.data.UserMethods;
import com.example.myeyehealth.model.User;
import com.example.myeyehealth.ui.LoginActivity;

import java.util.regex.Pattern;

public class ProfileActivity extends SessionActivity {

    private EditText mNameInput;
    private EditText mEmailInput;
    private EditText mPasswordInput;
    private Button mSaveButton;
    private Button mCancelButton;

    private Database db;
    private int mUserId;
    private User mUser;
    private SessionManager sessionManager;
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
        sessionManager = SessionManager.getInstance(this);
        User user = sessionManager.getUser();
        mUserId = user.getId(); // Get the user ID from the User object
        System.out.println("UserID" +mUserId);

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

        // Sign out button
        Button signOutButton = findViewById(R.id.sign_out_button);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.logout();
                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        Button deactivateButton = findViewById(R.id.deactivate_button);
        deactivateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show a confirmation dialog before proceeding
                new AlertDialog.Builder(ProfileActivity.this)
                        .setTitle("Deactivate Account")
                        .setMessage("Are you sure you want to deactivate your account? This action cannot be undone.")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Delete the user account from the database
                                UserMethods userMethods = new UserMethods(ProfileActivity.this);
                                userMethods.deleteUser(mUser.getId());

                                // Log the user out and navigate to LoginActivity
                                sessionManager.logout();
                                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });
    // Initialize the back button
        ImageButton backButton = findViewById(R.id.back_button);

    // Set up a click listener for the back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    @Override
    protected void onLoggedIn(User user) {

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
            Toast.makeText(ProfileActivity.this, "Please input a valid email", Toast.LENGTH_SHORT).show();
            return;
        }

        // Update the user object
        mUser.setName(updatedName);
        mUser.setEmail(updatedEmail);
        mUser.setPassword(updatedPassword);

        // Update the user data in the database
        db.updateUser(mUser);

        // Show a success message and refresh the user data
        Toast.makeText(ProfileActivity.this, "User information updated", Toast.LENGTH_SHORT).show();
        refreshUserData();
    }

    private void refreshUserData() {
        // Fetch the updated user data and update the EditText fields
        mUser = db.getUserById(mUserId);
        if (mUser != null) {
            mNameInput.setText(mUser.getName());
            mEmailInput.setText(mUser.getEmail());
            mPasswordInput.setText(mUser.getPassword());
        }
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

