package com.example.myeyehealth.ui.profile;

import com.example.myeyehealth.data.AmslerGridMethods;
import com.example.myeyehealth.data.Database;
import com.example.myeyehealth.data.SessionManager;
import com.example.myeyehealth.model.AmslerGridTestData;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextThemeWrapper;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myeyehealth.R;
import com.example.myeyehealth.data.SessionActivity;
import com.example.myeyehealth.model.User;

import java.util.ArrayList;
import java.util.List;

public class ProfileAmslerDataActivity extends SessionActivity implements com.example.myeyehealth.ui.profile.AmslerGridTestClickListener {
    private List<AmslerGridTestData> sortedAmslerGridTestData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amsler_data);
        AmslerGridMethods am = new AmslerGridMethods(this);
        SessionManager sessionManager = SessionManager.getInstance(this);
        User currentUser = sessionManager.getUser();
        int userId = currentUser.getId();
        sortedAmslerGridTestData = am.getSortedAmslerGridTests(String.valueOf(userId));

        RecyclerView resultsRecyclerView = findViewById(R.id.results_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        resultsRecyclerView.setLayoutManager(layoutManager);

        com.example.myeyehealth.ui.profile.AmslerGridTestAdapter amslerGridTestAdapter = new com.example.myeyehealth.ui.profile.AmslerGridTestAdapter(sortedAmslerGridTestData, this);
        resultsRecyclerView.setAdapter(amslerGridTestAdapter);

        ContextThemeWrapper themeWrapper = new ContextThemeWrapper(this, R.style.Divider);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(themeWrapper, layoutManager.getOrientation());
        resultsRecyclerView.addItemDecoration(dividerItemDecoration);
    }

    @Override
    protected void onLoggedIn(User user) {
    }

    @Override
    public void onAmslerGridTestClick(int position) {
        AmslerGridTestData data = sortedAmslerGridTestData.get(position);
        // Handle the click event, navigate to the Amsler Grid Test results page for the respective date
        Intent intent = new Intent(this, AmslerGridTestResultActivity.class);
        intent.putExtra(AmslerGridTestResultActivity.TEST_ID_KEY, data.getTestId());
        startActivity(intent);
    }
}
