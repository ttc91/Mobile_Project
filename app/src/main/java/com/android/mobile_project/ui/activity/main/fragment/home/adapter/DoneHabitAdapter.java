package com.android.mobile_project.ui.activity.main.fragment.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.mobile_project.data.remote.model.HabitModel;
import com.android.mobile_project.databinding.RcvItemHabitDoneBinding;

import java.util.ArrayList;
import java.util.List;

public class DoneHabitAdapter extends RecyclerView.Adapter<DoneHabitAdapter.ViewHolder>{

    private List<HabitModel> habitModelList = new ArrayList<>();

    public DoneHabitAdapter(List<HabitModel> habitModelList){
        this.habitModelList = habitModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        RcvItemHabitDoneBinding binding = RcvItemHabitDoneBinding.inflate(layoutInflater, parent, false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        HabitModel model = habitModelList.get(position);

        holder.binding.hname.setText(model.getHabitName());

    }

    @Override
    public int getItemCount() {

        if (habitModelList == null)
            return 0;
        return habitModelList.size();

    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        RcvItemHabitDoneBinding binding;

        public ViewHolder(@NonNull RcvItemHabitDoneBinding binding) {
            super(binding.getRoot());

            this.binding = binding;

        }
    }

}
