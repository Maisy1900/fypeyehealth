package com.example.myeyehealth.ui.profile;
import com.example.myeyehealth.data.AmslerGridMethods;
import com.example.myeyehealth.data.Database;
import com.example.myeyehealth.model.AmslerGridTestData;

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
//omplicated than the Reminders one is that the Amsler grid test data needs to be displayed in a RecyclerView, which requires an adapter to manage the data and the view hierarchy.
//Using a RecyclerView helps manage a potentially large number of items more efficiently than the approach used in the Reminders example. RecyclerView only creates the views needed for the items visible on the screen and reuses them as the user scrolls, which is more memory-efficient and better for performance.
public class ProfileAmslerDataActivity extends SessionActivity implements com.example.myeyehealth.ui.profile.AmslerGridTestClickListener {
    private List<AmslerGridTestData> sortedAmslerGridTestData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amsler_data);
        AmslerGridMethods am = new AmslerGridMethods(this);
        sortedAmslerGridTestData = am.getSortedAmslerGridTests();
        RecyclerView resultsRecyclerView = findViewById(R.id.results_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        resultsRecyclerView.setLayoutManager(layoutManager);

        // Use the sortedAmslerGridTestData variable directly, instead of calling the method again
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
    }

}
