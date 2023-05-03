package com.android.mobile_project.ui.activity.main.fragment.home.adapter;

import android.annotation.SuppressLint;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.android.mobile_project.data.remote.model.HabitInWeekModel;
import com.android.mobile_project.data.remote.model.HabitModel;
import com.android.mobile_project.databinding.RcvItemHabitBinding;
import com.android.mobile_project.utils.constant.TimeConstant;

import java.util.List;

public class HabitAdapter extends RecyclerView.Adapter<HabitAdapter.ViewHolder> {

    public final String TAG = this.getClass().getSimpleName();
    private List<HabitModel> habitModelList;
    private List<HabitInWeekModel> habitInWeekModelList;
    private final RecyclerViewClickListener recyclerViewClickListener;
    private HabitInWeekModel habitInWeek;


    public HabitAdapter(List<HabitModel> habitModelList, List<HabitInWeekModel> habitInWeekModels, RecyclerViewClickListener recyclerViewClickListener) {
        this.habitModelList = habitModelList;
        this.habitInWeekModelList = habitInWeekModels;
        this.recyclerViewClickListener = recyclerViewClickListener;
    }


    public void clear() {
        this.habitModelList.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RcvItemHabitBinding binding = RcvItemHabitBinding.inflate(inflater, parent, false);

        return new ViewHolder(binding);
    }

    @SuppressLint("DefaultLocale")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        HabitModel habit = habitModelList.get(position);
        String[] timeValue = TimeConstant.getTimeDisplayValue();
        String format = "%02d";

        holder.binding.hname.setText(habit.getHabitName());

        habitInWeek = getHabitInWeek(habit.getHabitId(), habitInWeekModelList);

        if (habitInWeek != null && habitInWeek.isTimerHabit()) {
            holder.binding.timer.setVisibility(View.VISIBLE);
            holder.binding.hTimer.setText(String.format(format, habitInWeek.getTimerHour()));
            holder.binding.mTimer.setText(String.format(format, habitInWeek.getTimerMinute()));
            holder.binding.sTimer.setText(String.format(format, habitInWeek.getTimerSecond()));
        } else {
            holder.binding.timer.setVisibility(View.GONE);
        }

    }

    private HabitInWeekModel getHabitInWeek(Long id, List<HabitInWeekModel> inWeekModelList) {
        for (HabitInWeekModel habitInWeekModel : inWeekModelList) {
            if (habitInWeekModel.getHabitId().equals(id)) {
                return habitInWeekModel;
            }
        }
        return null;
    }

    @Override
    public int getItemCount() {
        if (habitModelList == null) {
            return 0;
        }
        return habitModelList.size();
    }

    public interface RecyclerViewClickListener {
        void onClick(View v, List<HabitModel> habitModelList, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        RcvItemHabitBinding binding;

        public ViewHolder(@NonNull RcvItemHabitBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Log.d(TAG, "onClick: " + habitModelList.size() + " - " + getAdapterPosition());
            recyclerViewClickListener.onClick(binding.getRoot(), habitModelList, getAdapterPosition());
        }
    }

}
