package com.android.mobile_project.ui.activity.main.fragment.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.mobile_project.data.local.model.db.HabitEntity;
import com.android.mobile_project.databinding.RcvItemHabitDoneBinding;

import java.util.List;

public class DoneHabitAdapter extends RecyclerView.Adapter<DoneHabitAdapter.ViewHolder>{

    private List<HabitEntity> habitEntityList;
    private Context context;

    public DoneHabitAdapter(Context context, List<HabitEntity> habitEntityList){
        this.context = context;
        this.habitEntityList = habitEntityList;
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

        HabitEntity entity = habitEntityList.get(position);

        holder.binding.hname.setText(entity.habitName);

    }

    @Override
    public int getItemCount() {

        if (habitEntityList == null)
            return 0;
        return habitEntityList.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        RcvItemHabitDoneBinding binding;

        public ViewHolder(@NonNull RcvItemHabitDoneBinding binding) {
            super(binding.getRoot());

            this.binding = binding;

        }
    }

}
