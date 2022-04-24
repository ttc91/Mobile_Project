package com.android.mobile_project.ui.activity.main.fragment.home.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.mobile_project.data.local.model.db.HabitEntity;
import com.android.mobile_project.databinding.RcvHorizontalCalendarTextDateBinding;
import com.android.mobile_project.databinding.RcvItemHabitBinding;
import com.android.mobile_project.time.adapter.DailyCalendarAdapter;

import java.util.List;

public class HabitAdapter extends RecyclerView.Adapter<HabitAdapter.ViewHolder>{

    private Context context;
    private List<HabitEntity> habitEntityList;

    public HabitAdapter(Context context, List<HabitEntity> habitEntityList){
        this.context = context;
        this.habitEntityList = habitEntityList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RcvItemHabitBinding binding = RcvItemHabitBinding.inflate(inflater, parent, false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        HabitEntity habit = habitEntityList.get(position);

        holder.binding.hname.setText(habit.habitName);

    }

    @Override
    public int getItemCount() {


        if(habitEntityList == null){
            return 0;
        }
        return habitEntityList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RcvItemHabitBinding binding;

        public ViewHolder(@NonNull RcvItemHabitBinding binding) {
            super(binding.getRoot());

            this.binding = binding;

        }
    }

}
