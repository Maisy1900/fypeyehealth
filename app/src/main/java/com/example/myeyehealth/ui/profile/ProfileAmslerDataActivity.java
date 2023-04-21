package com.example.myeyehealth.ui.profile;
import com.example.myeyehealth.model.AmslerGridTestData;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myeyehealth.R;
import com.example.myeyehealth.data.SessionActivity;
import com.example.myeyehealth.model.User;
import java.util.List;
//omplicated than the Reminders one is that the Amsler grid test data needs to be displayed in a RecyclerView, which requires an adapter to manage the data and the view hierarchy.
//Using a RecyclerView helps manage a potentially large number of items more efficiently than the approach used in the Reminders example. RecyclerView only creates the views needed for the items visible on the screen and reuses them as the user scrolls, which is more memory-efficient and better for performance.
public class ProfileAmslerDataActivity extends SessionActivity implements com.example.myeyehealth.ui.profile.AmslerGridTestClickListener {
    private List<AmslerGridTestData> sortedAmslerGridTestData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amsler_data);
        sortedAmslerGridTestData = getSortedAmslerGridTestData();
        RecyclerView resultsRecyclerView = findViewById(R.id.results_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        resultsRecyclerView.setLayoutManager(layoutManager);

        List<AmslerGridTestData> sortedAmslerGridTestData = getSortedAmslerGridTestData(); // Replace this with your method to get the sorted data
        com.example.myeyehealth.ui.profile.AmslerGridTestAdapter amslerGridTestAdapter = new com.example.myeyehealth.ui.profile.AmslerGridTestAdapter(sortedAmslerGridTestData, this);
        resultsRecyclerView.setAdapter(amslerGridTestAdapter);
    }

    @Override
    protected void onLoggedIn(User user) {

    }

    @Override
    public void onAmslerGridTestClick(int position) {
        AmslerGridTestData data = sortedAmslerGridTestData.get(position);
        // Handle the click event, navigate to the Amsler Grid Test results page for the respective date
    }


    private List<AmslerGridTestData> getSortedAmslerGridTestData() {
        // Implement this method to get the sorted Amsler Grid Test data
        return null;
    }
}
