package com.android.mobile_project.ui.activity.main.fragment.home.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.mobile_project.data.remote.model.HabitModel;
import com.android.mobile_project.databinding.RcvItemHabitAfterBinding;

import java.util.List;

public class AfterAdapter extends RecyclerView.Adapter<AfterAdapter.ViewHolder>{

    private final List<HabitModel> habitModelList;

    public AfterAdapter(List<HabitModel> habitModelList){
        this.habitModelList = habitModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RcvItemHabitAfterBinding binding = RcvItemHabitAfterBinding.inflate(inflater, parent, false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        HabitModel model = habitModelList.get(position);
        holder.binding.hname.setText(model.getHabitName());

    }

    @Override
    public int getItemCount() {
        if(habitModelList != null){
            return habitModelList.size();
        }
        return 0;
    }

    static public class ViewHolder extends RecyclerView.ViewHolder{

        final private RcvItemHabitAfterBinding binding;

        public ViewHolder(@NonNull RcvItemHabitAfterBinding binding) {
            super(binding.getRoot());

            this.binding = binding;

        }
    }

}
