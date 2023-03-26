package com.android.mobile_project.ui.activity.main.fragment.home.adapter;

import android.annotation.SuppressLint;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.android.mobile_project.data.remote.model.HabitModel;
import com.android.mobile_project.data.remote.model.HistoryModel;
import com.android.mobile_project.databinding.RcvItemHabitBinding;
import com.android.mobile_project.databinding.RcvItemHabitDoneBinding;
import com.android.mobile_project.databinding.RcvItemHabitFailedBinding;
import com.android.mobile_project.ui.activity.main.fragment.home.IHomeViewModel;
import com.android.mobile_project.ui.activity.main.fragment.home.service.DbService;

import java.util.List;

public class BeforeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int VIEW_TYPE_ITEM_DONE = 1;

    private static final int VIEW_TYPE_ITEM_FAILED = 2;

    private static final int VIEW_TYPE_ITEM_HABIT = 3;

    private static final String VAL_NULL = "null";

    private static final String VAL_FALSE = "false";

    private static final String VAL_TRUE = "true";

    private final List<HabitModel> habitModelList;

    private final String date;

    private int result = 0;

    private final IHomeViewModel vm;

    @SuppressLint("NotifyDataSetChanged")
    public BeforeAdapter(List<HabitModel> habitModelList, String date, IHomeViewModel iHomeViewModel) {
        this.habitModelList = habitModelList;
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
            return new ViewHolderFailed(binding);
        }else if(VIEW_TYPE_ITEM_DONE == viewType){
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            RcvItemHabitDoneBinding binding = RcvItemHabitDoneBinding.inflate(inflater, parent, false);
            return new ViewHolderDone(binding);
        }else if(VIEW_TYPE_ITEM_HABIT == viewType){
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            RcvItemHabitBinding binding = RcvItemHabitBinding.inflate(inflater, parent, false);
            return new ViewHolderNull(binding);
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
            ViewHolderDone viewHolder = (ViewHolderDone) holder;
            viewHolder.binding.hname.setText(model.getHabitName());
        }else if(VIEW_TYPE_ITEM_FAILED == holder.getItemViewType()){
            ViewHolderFailed viewHolder = (ViewHolderFailed) holder;
            viewHolder.binding.hname.setText(model.getHabitName());
        }else if(VIEW_TYPE_ITEM_HABIT == holder.getItemViewType()){
            ViewHolderNull viewHolder = (ViewHolderNull) holder;
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
        if(date != null){
            vm.getHistoryByHabitIdAndDate(habitModel.getHabitId(), date, new DbService.GetHistoryByHabitIdAndDateResult() {
                @SuppressLint("LongLogTag")
                @Override
                public void onGetHistoryByHabitIdAndDateSuccess(HistoryModel model) {
                    Log.i("GetHistoryByHabitIdAndDateResult","onGetHistoryByHabitIdAndDateSuccess");
                    if(model.getHistoryHabitsState().equals(VAL_TRUE)){
                        result = VIEW_TYPE_ITEM_DONE;
                    }else if (model.getHistoryHabitsState().equals(VAL_FALSE)){
                        result = VIEW_TYPE_ITEM_FAILED;
                    }else if(model.getHistoryHabitsState().equals(VAL_NULL)){
                        result = VIEW_TYPE_ITEM_HABIT;
                    }
                }

                @SuppressLint("LongLogTag")
                @Override
                public void onGetHistoryByHabitIdAndDateFailure() {
                    Log.e("GetHistoryByHabitIdAndDateResult","onGetHistoryByHabitIdAndDateFailure");
                }
            });
        }
        return result;
    }

    public static class ViewHolderDone extends RecyclerView.ViewHolder{

        private final RcvItemHabitDoneBinding binding;

        public ViewHolderDone(@NonNull RcvItemHabitDoneBinding binding) {
            super(binding.getRoot());

            this.binding = binding;

        }
    }

    public static class ViewHolderFailed extends RecyclerView.ViewHolder{

        private final RcvItemHabitFailedBinding binding;

        public ViewHolderFailed(@NonNull RcvItemHabitFailedBinding binding) {
            super(binding.getRoot());

            this.binding = binding;

        }
    }

    public static class ViewHolderNull extends RecyclerView.ViewHolder{

        private final RcvItemHabitBinding binding;

        public ViewHolderNull(@NonNull RcvItemHabitBinding binding) {
            super(binding.getRoot());

            this.binding = binding;

        }
    }

}
