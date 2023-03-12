package com.example.myeyehealth.ui.account;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myeyehealth.R;
import com.example.myeyehealth.data.Database;
import com.example.myeyehealth.ui.MainMenuActivity;

public class CreateAccountMedicalInfoActivity extends AppCompatActivity {

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

                // Save the user's information to the database
                Database db = Database.getInstance(CreateAccountMedicalInfoActivity.this);
                db.addUser(name, email, password, docName, docEmail, carerName, carerEmail);
                db.close();

                Intent intent = new Intent(CreateAccountMedicalInfoActivity.this, MainMenuActivity.class);
                startActivity(intent);
                finish();
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
}
