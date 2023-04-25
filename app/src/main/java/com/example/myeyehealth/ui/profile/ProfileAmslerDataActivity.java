package com.example.myeyehealth.ui.profile;

import com.example.myeyehealth.controller.AmslerGridMethods;
import com.example.myeyehealth.model.AmslerGridTestAdapter;
import com.example.myeyehealth.utils.SessionManager;
import com.example.myeyehealth.model.AmslerGridTestData;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ScrollView;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myeyehealth.R;
import com.example.myeyehealth.utils.SessionActivity;
import com.example.myeyehealth.model.User;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ProfileAmslerDataActivity extends SessionActivity implements com.example.myeyehealth.ui.profile.AmslerGridTestClickListener {
    private List<AmslerGridTestData> sortedAmslerGridTestData;
    private ScrollView resultsScrollView;
    private ImageButton scrollUpButton;
    private ImageButton scrollDownButton;
    private ImageButton backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amsler_data);
        AmslerGridMethods am = new AmslerGridMethods(this);
        SessionManager sessionManager = SessionManager.getInstance(this);
        User currentUser = sessionManager.getUser();
        int userId = currentUser.getId();
        sortedAmslerGridTestData = am.getSortedAmslerGridTests(String.valueOf(userId));
        resultsScrollView = findViewById(R.id.results_scroll_view);
        scrollUpButton = findViewById(R.id.scroll_up_button);
        scrollDownButton = findViewById(R.id.scroll_down_button);

        RecyclerView resultsRecyclerView = findViewById(R.id.results_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        resultsRecyclerView.setLayoutManager(layoutManager);

        AmslerGridTestAdapter amslerGridTestAdapter = new AmslerGridTestAdapter(sortedAmslerGridTestData, this);
        resultsRecyclerView.setAdapter(amslerGridTestAdapter);

        ContextThemeWrapper themeWrapper = new ContextThemeWrapper(this, R.style.Divider);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(themeWrapper, layoutManager.getOrientation());
        resultsRecyclerView.addItemDecoration(dividerItemDecoration);

        resultsScrollView.post(new Runnable() {
            @Override
            public void run() {
                resultsScrollView.scrollTo(0, 500);
            }
        });


        scrollUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollResults(-50);
            }
        });

        scrollDownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollResults(50);
            }
        });
        resultsRecyclerView.setPadding(0, 500, 0, 1500);
        backButton = findViewById(R.id.back_button);

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
    private void scrollResults(int dy) {
        int maxScrollY = resultsScrollView.getChildAt(0).getMeasuredHeight() - resultsScrollView.getHeight();
        int targetY = resultsScrollView.getScrollY() + dy;
        if (targetY < 0) {
            targetY = 0;
        } else if (targetY > maxScrollY) {
            targetY = maxScrollY;
        }
        resultsScrollView.scrollTo(0, targetY);
    }

    @Override
    public void onAmslerGridTestClick(int position) {
        AmslerGridTestData data = sortedAmslerGridTestData.get(position);


        Date testDate = data.getDate();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = sdf.format(testDate);

        Intent intent = new Intent(this, AmslerGridTestResultActivity.class);
        intent.putExtra(AmslerGridTestResultActivity.TEST_ID_KEY, data.getTestId());
        intent.putExtra(AmslerGridTestResultActivity.TEST_DATE_KEY, formattedDate);
        startActivity(intent);
    }
}
