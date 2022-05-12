package com.android.mobile_project.ui.activity.main.fragment.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.mobile_project.data.local.model.db.HabitEntity;
import com.android.mobile_project.databinding.RcvItemHabitAfterBinding;
import com.android.mobile_project.databinding.RcvItemHabitFailedBinding;

import java.util.List;

public class AfterAdapter extends RecyclerView.Adapter<AfterAdapter.ViewHolder>{

    private List<HabitEntity> habitEntityList;

    private Context context;

    public AfterAdapter(Context context, List<HabitEntity> habitEntityList){
        this.habitEntityList = habitEntityList;
        this.context = context;
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

        HabitEntity entity = habitEntityList.get(position);
        holder.binding.hname.setText(entity.habitName);

    }

    @Override
    public int getItemCount() {
        if(habitEntityList != null){
            return habitEntityList.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private RcvItemHabitAfterBinding binding;

        public ViewHolder(@NonNull RcvItemHabitAfterBinding binding) {
            super(binding.getRoot());

            this.binding = binding;

        }
    }

}
