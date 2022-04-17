package com.android.mobile_project.time.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class VerticalCalendarAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<String> daysOfMonth;
    private final String today;

    private Context context;

    public VerticalCalendarAdapter(Context context, String today, List<String> daysOfMonth){

        this.context = context;
        this.today = today;
        this.daysOfMonth = daysOfMonth;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


        }
    }

}
