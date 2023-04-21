package com.example.myeyehealth.ui.profile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myeyehealth.R;
import com.example.myeyehealth.model.AmslerGridTestData;

import java.text.DateFormat;
import java.util.List;

public class AmslerGridTestAdapter extends RecyclerView.Adapter<AmslerGridTestAdapter.AmslerGridTestViewHolder> {

    private List<AmslerGridTestData> amslerGridTestDataList;
    private AmslerGridTestClickListener clickListener;

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
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
        holder.dateTextView.setText(dateFormat.format(data.getDate())); // Assuming 'getDate()' method returns a Date object
    }

    @Override
    public int getItemCount() {
        return amslerGridTestDataList.size();
    }

    public static class AmslerGridTestViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView dateTextView;
        AmslerGridTestClickListener clickListener;

        public AmslerGridTestViewHolder(@NonNull View itemView, AmslerGridTestClickListener clickListener) {
            super(itemView);
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
