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
import com.example.myeyehealth.model.Database;
import com.example.myeyehealth.utils.SessionActivity;
import com.example.myeyehealth.utils.SessionManager;
import com.example.myeyehealth.controller.UserMethods;
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


        mNameInput = findViewById(R.id.name_input);
        mEmailInput = findViewById(R.id.email_input);
        mPasswordInput = findViewById(R.id.password_input);
        mSaveButton = findViewById(R.id.save_button);
        mCancelButton = findViewById(R.id.cancel_button);


        db = new Database(this);
        sessionManager = SessionManager.getInstance(this);
        User user = sessionManager.getUser();
        mUserId = user.getId();
        System.out.println("UserID" +mUserId);


        mUser = db.getUserById(mUserId);
        if (mUser != null) {
            mNameInput.setText(mUser.getName());
            mEmailInput.setText(mUser.getEmail());
            mPasswordInput.setText(mUser.getPassword());
        }


        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserData();
            }
        });


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

        ImageButton backButton = findViewById(R.id.back_button);


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

        String updatedName = mNameInput.getText().toString();
        String updatedEmail = mEmailInput.getText().toString();
        String updatedPassword = mPasswordInput.getText().toString();


        if (updatedName.equals(mUser.getName()) && updatedEmail.equals(mUser.getEmail()) && updatedPassword.equals(mUser.getPassword())) {
            Toast.makeText(ProfileActivity.this, "No changes detected", Toast.LENGTH_SHORT).show();
            return;
        }


        if (!isValidEmail(updatedEmail)) {
            Toast.makeText(ProfileActivity.this, "Please input a valid email", Toast.LENGTH_SHORT).show();
            return;
        }

        mUser.setName(updatedName);
        mUser.setEmail(updatedEmail);
        mUser.setPassword(updatedPassword);


        db.updateUser(mUser);


        Toast.makeText(ProfileActivity.this, "User information updated", Toast.LENGTH_SHORT).show();
        refreshUserData();
    }

    private void refreshUserData() {

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

        if (email.isEmpty()) {
            return true;
        }


        String emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        Pattern pattern = Pattern.compile(emailRegex);

        if (!pattern.matcher(email).matches()) {
            return false;
        }


        UserMethods userMethods = new UserMethods(ProfileActivity.this);
        User existingUser = userMethods.getUserByEmail(email);

        if (existingUser != null && existingUser.getId() != mUser.getId()) {
            Toast.makeText(ProfileActivity.this, "Email address already in use", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

}

