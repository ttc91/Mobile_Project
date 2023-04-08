package com.android.mobile_project.ui.activity.main.fragment.home.adapter;

import android.content.Context;
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
import com.android.mobile_project.ui.activity.main.fragment.home.HomeViewModel;
import com.android.mobile_project.ui.activity.main.fragment.home.IHomeViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class HabitAdapter extends RecyclerView.Adapter<HabitAdapter.ViewHolder>{

    public final String TAG = this.getClass().getSimpleName();
    private List<HabitModel> habitModelList = new ArrayList<>();
    private final RecyclerViewClickListener recyclerViewClickListener;

    private IHomeViewModel vm;

    public HabitAdapter(List<HabitModel> habitModelList, RecyclerViewClickListener recyclerViewClickListener){
        this.habitModelList = habitModelList;
        this.recyclerViewClickListener = recyclerViewClickListener;
    }

    public HabitAdapter(List<HabitModel> habitModelList, RecyclerViewClickListener recyclerViewClickListener, IHomeViewModel iHomeViewModel){
        this.habitModelList = habitModelList;
        this.recyclerViewClickListener = recyclerViewClickListener;
        vm = iHomeViewModel;
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        HabitModel habit = habitModelList.get(position);

        holder.binding.hname.setText(habit.getHabitName());

        //List<HabitInWeekModel> habitInWeekModels = vm.getDayOfWeekHabitListByUserAndHabitId(habit.getHabitId());

//        HabitInWeekModel model = habitInWeekModels.get(0);
//
//        if(model.getTimerHour() != null && model.getTimerMinute() != null && model.getTimerSecond() != null){
//            holder.binding.timer.setVisibility(View.VISIBLE);
//            holder.binding.hTimer.setText(String.valueOf(habitInWeekModels.get(0).getTimerHour()));
//            holder.binding.mTimer.setText(String.valueOf(habitInWeekModels.get(0).getTimerMinute()));
//            holder.binding.sTimer.setText(String.valueOf(habitInWeekModels.get(0).getTimerSecond()));
//        }else {
//            holder.binding.timer.setVisibility(View.GONE);
//        }

    }

    @Override
    public int getItemCount() {
        if(habitModelList == null){
            return 0;
        }
        return habitModelList.size();
    }

    public interface RecyclerViewClickListener{
        void onClick(View v, List<HabitModel> habitModelList, int position);
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
            Log.d(TAG, "onClick: " + habitModelList.size() + " - " + getAdapterPosition());
            recyclerViewClickListener.onClick(binding.getRoot(), habitModelList, getAdapterPosition());
        }
    }

}
