package com.example.myeyehealth.ui.account;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myeyehealth.R;

public class CreateAccountNameActivity extends AppCompatActivity {
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
                    // Display an error message or disable the confirm button
                    Toast.makeText(CreateAccountNameActivity.this, "Please enter a name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!isValidName(name)) {
                    // Display an error message or disable the confirm button
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
                // remove the "name" extra data from the Intent object before finishing the activity
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
        // remove the "name" extra data from the Intent object before calling super.onBackPressed()
        removeExtraDataFromIntent();
        super.onBackPressed();
    }

    /**
     * Removes the "name" extra data from the Intent object.
     */
    private void removeExtraDataFromIntent() {
        Intent intent = getIntent();
        intent.removeExtra("name");
        setIntent(intent);
    }
}

