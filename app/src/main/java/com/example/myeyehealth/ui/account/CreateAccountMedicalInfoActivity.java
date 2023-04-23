package com.example.myeyehealth.ui.account;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myeyehealth.R;
import com.example.myeyehealth.data.BaseActivity;
import com.example.myeyehealth.data.UserMethods;
import com.example.myeyehealth.model.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateAccountMedicalInfoActivity extends BaseActivity {

    private EditText docNameInput;
    private EditText docEmailInput;
    private EditText carerNameInput;
    private EditText carerEmailInput;
    private Button completeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account_medical_info);

        docNameInput = findViewById(R.id.docname_label);
        docEmailInput = findViewById(R.id.second_input);
        carerNameInput = findViewById(R.id.carername_label);
        carerEmailInput = findViewById(R.id.careremail_input);
        completeButton = findViewById(R.id.complete_button);

        // Add a TextWatcher to the docNameInput EditText field
        docNameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateButton();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Add a TextWatcher to the docEmailInput EditText field
        docEmailInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateButton();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Add a TextWatcher to the carerNameInput EditText field
        carerNameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateButton();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Add a TextWatcher to the carerEmailInput EditText field
        carerEmailInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateButton();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Set an OnClickListener for the completeButton
        completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = getIntent().getStringExtra("name");
                String email = getIntent().getStringExtra("email");
                String password = getIntent().getStringExtra("password");
                String docName = docNameInput.getText().toString();
                String docEmail = docEmailInput.getText().toString();
                String carerName = carerNameInput.getText().toString();
                String carerEmail = carerEmailInput.getText().toString();

                // Check if all input fields are empty
                if (docName.isEmpty() && docEmail.isEmpty() && carerName.isEmpty() && carerEmail.isEmpty()) {
                    // If all input fields are empty, prompt the user to confirm if they want to skip medical information
                    new AlertDialog.Builder(CreateAccountMedicalInfoActivity.this)
                            .setTitle("Skip Medical Information")
                            .setMessage("Are you sure you want to skip entering your medical information?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // Create a new User object with the given details
                                    User user = new User(-1, name, email, password, docName, docEmail, carerName, carerEmail);

                                    // Save the user's information to the database and get the ID of the newly inserted row
                                    UserMethods userMethods = new UserMethods(CreateAccountMedicalInfoActivity.this);
                                    int userId = (int) userMethods.addUser(user);


                                    // Set the id of the user object to the generated id
                                    user.setId(userId);

                                    // Start the CreateAccountSuccessActivity and pass the user ID as an extra
                                    Intent intent = new Intent(CreateAccountMedicalInfoActivity.this, CreateAccountSuccessActivity.class);
                                    intent.putExtra("user_id", userId);
                                    startActivity(intent);
                                    finish();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // Do nothing, let the user continue entering their medical information
                                }
                            })
                            .show();
                } else {
                    // If any input fields are not empty, validate the inputs
                    if (!isValidEmail(docEmail) || !isValidEmail(carerEmail) || !isValidName(docName) || !isValidName(carerName)) {
                        Toast.makeText(CreateAccountMedicalInfoActivity.this, "Invalid input", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Create a new User object with the given details
                    User user = new User(-1, name, email, password, docName, docEmail, carerName, carerEmail);

                    // Save the user's information to the database and get the ID of the newly inserted row
                    UserMethods userMethods = new UserMethods(CreateAccountMedicalInfoActivity.this);
                    int userId = (int) userMethods.addUser(user);

                    // Set the id of the user object to the generated id
                    user.setId(userId);

                    // Start the CreateAccountSuccessActivity and pass the user ID as an extra
                    Intent intent = new Intent(CreateAccountMedicalInfoActivity.this, CreateAccountSuccessActivity.class);
                    intent.putExtra("user_id", userId);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private void updateButton() {
        if (!docNameInput.getText().toString().isEmpty() || !docEmailInput.getText().toString().isEmpty() ||
                !carerNameInput.getText().toString().isEmpty() || !carerEmailInput.getText().toString().isEmpty()) {
            completeButton.setText("Complete");
        } else {
            completeButton.setText("Skip");
        }
    }

    private boolean isValidEmail(String email) {
        if (email.isEmpty()) {
            return false;
        }
        String emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean isValidName(String name) {
        if (name.isEmpty()) {
            return false;
        }
        String nameRegex = "^[a-zA-Z\\s]+$";
        Pattern pattern = Pattern.compile(nameRegex);
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }
}