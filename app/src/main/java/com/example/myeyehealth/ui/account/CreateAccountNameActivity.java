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

public class CreateAccountNameActivity extends BaseActivity {
    private EditText nameInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account_name);

        nameInput = findViewById(R.id.name_input);
        Button confirmButton = findViewById(R.id.confirm_button);
        ImageButton backButton = findViewById(R.id.back_button);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameInput.getText().toString();
                if (name.isEmpty()) {

                    Toast.makeText(CreateAccountNameActivity.this, "Please enter a name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!isValidName(name)) {

                    Toast.makeText(CreateAccountNameActivity.this, "Please enter a valid name", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(CreateAccountNameActivity.this, CreateAccountEmailActivity.class);
                intent.putExtra("name", name);
                startActivity(intent);
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

    private boolean isValidName(String name) {
        String pattern = "^[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*$";
        return name.matches(pattern);
    }


    @Override
    public void onBackPressed() {

        removeExtraDataFromIntent();
        super.onBackPressed();
    }

    private void removeExtraDataFromIntent() {
        Intent intent = getIntent();
        intent.removeExtra("name");
        setIntent(intent);
    }
}

