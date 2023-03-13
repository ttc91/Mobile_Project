package com.android.mobile_project.ui.activity.main.fragment.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.mobile_project.data.remote.model.HabitModel;
import com.android.mobile_project.databinding.RcvItemHabitFailedBinding;

import java.util.List;

public class FailedHabitAdapter extends RecyclerView.Adapter<FailedHabitAdapter.ViewHolder>{

    private final List<HabitModel> habitModelList;
    private final Context context;

    public FailedHabitAdapter(List<HabitModel> habitModelList, Context context) {
        this.habitModelList = habitModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        RcvItemHabitFailedBinding binding = RcvItemHabitFailedBinding.inflate(layoutInflater, parent, false);

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

        RcvItemHabitFailedBinding binding;

        public ViewHolder(@NonNull RcvItemHabitFailedBinding binding) {
            super(binding.getRoot());

            this.binding = binding;

        }
    }

}
