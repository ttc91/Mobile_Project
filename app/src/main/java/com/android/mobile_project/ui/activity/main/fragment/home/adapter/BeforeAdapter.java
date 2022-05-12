package com.android.mobile_project.ui.activity.main.fragment.home.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.mobile_project.data.local.DataLocalManager;
import com.android.mobile_project.data.local.model.db.HabitEntity;
import com.android.mobile_project.data.local.model.db.HistoryEntity;
import com.android.mobile_project.data.local.sqlite.HabitTrackerDatabase;
import com.android.mobile_project.databinding.RcvItemHabitDoneBinding;
import com.android.mobile_project.databinding.RcvItemHabitFailedBinding;

import java.util.List;

public class BeforeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static int VIEW_TYPE_ITEM_DONE = 1;
    private static int VIEW_TYPE_ITEM_FAILED = 2;

    private List<HabitEntity> habitEntityList;
    private Context context;
    private String date;

    public BeforeAdapter(Context context, List<HabitEntity> habitEntityList, String date) {
        this.habitEntityList = habitEntityList;
        this.context = context;
        this.date = date;
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
        }
        return null;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        HabitEntity entity = habitEntityList.get(position);

        if(entity == null){
            return;
        }

        if(VIEW_TYPE_ITEM_DONE == holder.getItemViewType()){
            ViewHolder_1 viewHolder = (ViewHolder_1) holder;
            viewHolder.binding.hname.setText(entity.habitName);
        }else if(VIEW_TYPE_ITEM_FAILED == holder.getItemViewType()){
            ViewHolder_2 viewHolder = (ViewHolder_2) holder;
            viewHolder.binding.hname.setText(entity.habitName);
        }

    }

    @Override
    public int getItemCount() {
        if(habitEntityList != null){
            return habitEntityList.size();
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {

        HabitEntity habitEntity = habitEntityList.get(position);
        Log.e("Size", String.valueOf(habitEntityList.size()));
        HistoryEntity historyEntity = HabitTrackerDatabase.getInstance(context).historyDao().getHistoryByHabitIdAndDate(habitEntity.habitId, date);
        if(historyEntity.historyHabitsState == "true" || historyEntity.historyHabitsState.equals("true")){
            return VIEW_TYPE_ITEM_DONE;
        }else if (historyEntity.historyHabitsState == "false"|| historyEntity.historyHabitsState.equals("false")){
            return VIEW_TYPE_ITEM_FAILED;
        }else
            return 0;

    }

    public class ViewHolder_1 extends RecyclerView.ViewHolder{

        private RcvItemHabitDoneBinding binding;

        public ViewHolder_1(@NonNull RcvItemHabitDoneBinding binding) {
            super(binding.getRoot());

            this.binding = binding;

        }
    }

    public class ViewHolder_2 extends RecyclerView.ViewHolder{

        private RcvItemHabitFailedBinding binding;

        public ViewHolder_2(@NonNull RcvItemHabitFailedBinding binding) {
            super(binding.getRoot());

            this.binding = binding;

        }
    }

}
