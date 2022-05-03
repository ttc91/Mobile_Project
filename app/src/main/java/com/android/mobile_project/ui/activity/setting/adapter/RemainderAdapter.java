package com.android.mobile_project.ui.activity.setting.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.mobile_project.R;
import com.android.mobile_project.data.local.model.db.HabitEntity;
import com.android.mobile_project.data.local.model.db.RemainderEntity;
import com.android.mobile_project.data.local.sqlite.HabitTrackerDatabase;
import com.android.mobile_project.databinding.LayoutRemainderDialogBinding;
import com.android.mobile_project.databinding.LayoutReminderItemBinding;
import com.android.mobile_project.ui.activity.setting.HabitSettingActivity;
import com.android.mobile_project.ui.dialog.RemainderDialog;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.util.List;

public class RemainderAdapter extends RecyclerView.Adapter<RemainderAdapter.ViewHolder>{

    private Context context;
    private List<RemainderEntity> remainderEntityList;
    private FragmentManager manager;
    private HabitEntity habitEntity;
    private Boolean change;

    public RemainderAdapter(Context context, List<RemainderEntity> remainderEntityList, FragmentManager manager, HabitEntity habitEntity){
        this.context = context;
        this.remainderEntityList = remainderEntityList;
        this.manager = manager;
        this.habitEntity = habitEntity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        LayoutReminderItemBinding binding = LayoutReminderItemBinding.inflate(inflater, parent, false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        RemainderEntity entity = remainderEntityList.get(position);
        holder.binding.tHour.setText(String.valueOf(entity.hourTime));
        holder.binding.tMinute.setText(String.valueOf(entity.minutesTime));

        holder.binding.btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HabitTrackerDatabase.getInstance(context).remainderDao().deleteRemainder(entity);
                remainderEntityList.remove(entity);
                notifyDataSetChanged();

            }
        });

        holder.binding.rItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RemainderDialog dialog = new RemainderDialog(entity, context);
                dialog.show(manager, "");

            }
        });

    }

    @Override
    public int getItemCount() {

        if(remainderEntityList == null)
            return 0;
        return remainderEntityList.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private LayoutReminderItemBinding binding;

        public ViewHolder(@NonNull LayoutReminderItemBinding binding) {
            super(binding.getRoot());

            this.binding = binding;

        }
    }

}
