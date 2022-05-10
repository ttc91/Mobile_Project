package com.android.mobile_project.ui.activity.main.fragment.home.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.mobile_project.data.local.DataLocalManager;
import com.android.mobile_project.data.local.model.db.HabitEntity;
import com.android.mobile_project.data.local.model.db.HabitInWeekEntity;
import com.android.mobile_project.data.local.sqlite.HabitTrackerDatabase;
import com.android.mobile_project.databinding.RcvHorizontalCalendarTextDateBinding;
import com.android.mobile_project.databinding.RcvItemHabitBinding;
import com.android.mobile_project.time.adapter.DailyCalendarAdapter;
import com.android.mobile_project.ui.activity.main.fragment.home.service.InitUIService;
import com.android.mobile_project.ui.activity.main.fragment.home.service.RecyclerViewInterface;
import com.android.mobile_project.ui.activity.setting.HabitSettingActivity;
import com.daimajia.swipe.SwipeLayout;

import java.util.List;

public class HabitAdapter extends RecyclerView.Adapter<HabitAdapter.ViewHolder>{

    private Context context;
    private List<HabitEntity> habitEntityList;
    private RecyclerViewClickListener recyclerViewClickListener;

    public HabitAdapter(Context context, List<HabitEntity> habitEntityList, RecyclerViewClickListener recyclerViewClickListener){
        this.context = context;
        this.habitEntityList = habitEntityList;
        this.recyclerViewClickListener = recyclerViewClickListener;
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

        List<HabitInWeekEntity> habitInWeekEntities = HabitTrackerDatabase.getInstance(context).habitInWeekDao()
                .getDayOfWeekHabitListByUserAndHabitId(DataLocalManager.getUserId(), habit.habitId);

        HabitInWeekEntity entity = habitInWeekEntities.get(0);

        if(entity.timerHour != null && entity.timerMinute != null && entity.timerSecond != null){
            holder.binding.timer.setVisibility(View.VISIBLE);
            holder.binding.hTimer.setText(String.valueOf(habitInWeekEntities.get(0).timerHour));
            holder.binding.mTimer.setText(String.valueOf(habitInWeekEntities.get(0).timerMinute));
            holder.binding.sTimer.setText(String.valueOf(habitInWeekEntities.get(0).timerSecond));
        }else {
            holder.binding.timer.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {


        if(habitEntityList == null){
            return 0;
        }
        return habitEntityList.size();
    }

    public interface RecyclerViewClickListener{
        public void onClick(View v, List<HabitEntity> habitEntityList, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        RcvItemHabitBinding binding;

        public ViewHolder(@NonNull RcvItemHabitBinding binding) {
            super(binding.getRoot());

            this.binding = binding;

            binding.getRoot().setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            recyclerViewClickListener.onClick(binding.getRoot(), habitEntityList, getAdapterPosition());
        }
    }

}
