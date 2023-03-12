package com.example.myeyehealth;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myeyehealth.data.Database;

public class MainActivity extends AppCompatActivity {

    private Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}