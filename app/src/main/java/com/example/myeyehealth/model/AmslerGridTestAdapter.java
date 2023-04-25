package com.example.myeyehealth.model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myeyehealth.R;
import com.example.myeyehealth.ui.profile.AmslerGridTestClickListener;

import java.text.DateFormat;
import java.util.List;
import java.util.Locale;

public class AmslerGridTestAdapter extends RecyclerView.Adapter<AmslerGridTestAdapter.AmslerGridTestViewHolder> {
//recycle view responsible for the list of amsler grid test data
    //binds the AmslerGridTestData objects to the recycler view items handling the click events
    private List<AmslerGridTestData> amslerGridTestDataList;
    private AmslerGridTestClickListener clickListener;
//checks for click events
    //on the given data list
    public AmslerGridTestAdapter(List<AmslerGridTestData> amslerGridTestDataList, AmslerGridTestClickListener clickListener) {
        this.amslerGridTestDataList = amslerGridTestDataList;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public AmslerGridTestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.amsler_grid_test_item, parent, false);
        return new AmslerGridTestViewHolder(itemView, clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AmslerGridTestViewHolder holder, int position) {
        AmslerGridTestData data = amslerGridTestDataList.get(position);
        holder.testNumberTextView.setText(String.valueOf(data.getTestNumber()));

        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.UK);
        holder.dateTextView.setText(dateFormat.format(data.getDate()));
    }


    @Override
    public int getItemCount() {
        return amslerGridTestDataList.size();
    }

    //references the view items and handles click events using the click listener.
    public static class AmslerGridTestViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView testNumberTextView;
        TextView dateTextView;
        AmslerGridTestClickListener clickListener;

        public AmslerGridTestViewHolder(@NonNull View itemView, AmslerGridTestClickListener clickListener) {
            super(itemView);
            testNumberTextView = itemView.findViewById(R.id.amsler_grid_test_number);
            dateTextView = itemView.findViewById(R.id.amsler_grid_test_date);
            this.clickListener = clickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onAmslerGridTestClick(getAdapterPosition());
        }
    }
}