package com.android.mobile_project.ui.activity.main.fragment.home.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.android.mobile_project.data.local.sqlite.entity.db.HabitEntity;
import com.android.mobile_project.data.local.sqlite.entity.db.HistoryEntity;
import com.android.mobile_project.data.local.sqlite.HabitTrackerDatabase;
import com.android.mobile_project.data.remote.model.HabitModel;
import com.android.mobile_project.data.remote.model.HistoryModel;
import com.android.mobile_project.databinding.RcvItemHabitBinding;
import com.android.mobile_project.databinding.RcvItemHabitDoneBinding;
import com.android.mobile_project.databinding.RcvItemHabitFailedBinding;
import com.android.mobile_project.ui.activity.main.fragment.home.HomeViewModel;
import com.android.mobile_project.ui.activity.main.fragment.home.IHomeViewModel;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

public class BeforeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int VIEW_TYPE_ITEM_DONE = 1;
    private static final int VIEW_TYPE_ITEM_FAILED = 2;
    private static final int VIEW_TYPE_ITEM_HABIT = 3;

    private final List<HabitModel> habitModelList;
    private Context context;
    private final String date;

    private IHomeViewModel vm;

    @SuppressLint("NotifyDataSetChanged")
    public BeforeAdapter(Context context, List<HabitModel> habitModelList, String date) {
        this.habitModelList = habitModelList;
        this.context = context;
        this.date = date;
        notifyDataSetChanged();
    }

    public BeforeAdapter(Context context, List<HabitModel> habitModelList, String date, IHomeViewModel iHomeViewModel) {
        this.habitModelList = habitModelList;
        this.context = context;
        this.date = date;
        vm = iHomeViewModel;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(VIEW_TYPE_ITEM_FAILED == viewType){
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            RcvItemHabitFailedBinding binding = RcvItemHabitFailedBinding.inflate(inflater, parent, false);
            return new ViewHolder_2(binding);
        }else if(VIEW_TYPE_ITEM_DONE == viewType){
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            RcvItemHabitDoneBinding binding = RcvItemHabitDoneBinding.inflate(inflater, parent, false);
            return new ViewHolder_1(binding);
        }else if(VIEW_TYPE_ITEM_HABIT == viewType){
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            RcvItemHabitBinding binding = RcvItemHabitBinding.inflate(inflater, parent, false);
            return new ViewHolder_3(binding);
        }
        return null;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        HabitModel model = habitModelList.get(position);

        if(model == null){
            return;
        }

        if(VIEW_TYPE_ITEM_DONE == holder.getItemViewType()){
            ViewHolder_1 viewHolder = (ViewHolder_1) holder;
            viewHolder.binding.hname.setText(model.getHabitName());
        }else if(VIEW_TYPE_ITEM_FAILED == holder.getItemViewType()){
            ViewHolder_2 viewHolder = (ViewHolder_2) holder;
            viewHolder.binding.hname.setText(model.getHabitName());
        }else if(VIEW_TYPE_ITEM_HABIT == holder.getItemViewType()){
            ViewHolder_3 viewHolder = (ViewHolder_3) holder;
            viewHolder.binding.hname.setText(model.getHabitName());
        }

    }

    @Override
    public int getItemCount() {
        if(habitModelList != null){
            return habitModelList.size();
        }
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int getItemViewType(int position) {

        HabitModel habitModel = habitModelList.get(position);
        HistoryModel historyModel = vm.getHistoryByHabitIdAndDate(habitModel.getHabitId(), date);
        if(Objects.equals(historyModel.getHistoryHabitsState(), "true") || historyModel.getHistoryHabitsState().equals("true")){
            return VIEW_TYPE_ITEM_DONE;
        }else if (Objects.equals(historyModel.getHistoryHabitsState(), "false") || historyModel.getHistoryHabitsState().equals("false")){
            return VIEW_TYPE_ITEM_FAILED;
        }else if(Objects.equals(historyModel.getHistoryHabitsState(), "null") || historyModel.getHistoryHabitsState().equals("null")){
            return VIEW_TYPE_ITEM_HABIT;
        }
        else
            return 0;

    }

    public static class ViewHolder_1 extends RecyclerView.ViewHolder{

        private final RcvItemHabitDoneBinding binding;

        public ViewHolder_1(@NonNull RcvItemHabitDoneBinding binding) {
            super(binding.getRoot());

            this.binding = binding;

        }
    }

    public static class ViewHolder_2 extends RecyclerView.ViewHolder{

        private final RcvItemHabitFailedBinding binding;

        public ViewHolder_2(@NonNull RcvItemHabitFailedBinding binding) {
            super(binding.getRoot());

            this.binding = binding;

        }
    }

    public static class ViewHolder_3 extends RecyclerView.ViewHolder{

        private final RcvItemHabitBinding binding;

        public ViewHolder_3(@NonNull RcvItemHabitBinding binding) {
            super(binding.getRoot());

            this.binding = binding;

        }
    }

}
