package com.android.mobile_project.ui.activity.main.fragment.home.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.mobile_project.databinding.RcvItemHabitBinding;

public class HabitAdapter extends RecyclerView.Adapter<HabitAdapter.ViewHolder>{



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RcvItemHabitBinding binding;

        public ViewHolder(@NonNull RcvItemHabitBinding binding) {
            super(binding.getRoot());

            this.binding = binding;

        }
    }

}
