package com.example.myeyehealth.ui;

import android.os.Bundle;

import com.example.myeyehealth.R;
import com.example.myeyehealth.data.SessionActivity;

public class ProfileActivity extends SessionActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
    }
}