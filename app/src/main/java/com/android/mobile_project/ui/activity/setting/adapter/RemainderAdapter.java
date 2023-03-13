package com.android.mobile_project.ui.activity.setting.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.mobile_project.data.remote.model.RemainderModel;
import com.android.mobile_project.databinding.LayoutReminderItemBinding;
import com.android.mobile_project.ui.activity.setting.IHabitSettingViewModel;
import com.android.mobile_project.ui.dialog.RemainderDialog;

import java.util.List;

public class RemainderAdapter extends RecyclerView.Adapter<RemainderAdapter.ViewHolder>{

    private final Context context;
    private final List<RemainderModel> remainderModelList;
    private final FragmentManager manager;
    private final IHabitSettingViewModel vm;

    public RemainderAdapter(Context context, List<RemainderModel> remainderModelList, FragmentManager manager, IHabitSettingViewModel vm){
        this.context = context;
        this.remainderModelList = remainderModelList;
        this.manager = manager;
        this.vm = vm;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        LayoutReminderItemBinding binding = LayoutReminderItemBinding.inflate(inflater, parent, false);

        return new ViewHolder(binding);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        RemainderModel model = remainderModelList.get(position);
        holder.binding.tHour.setText(String.valueOf(model.getHourTime()));
        holder.binding.tMinute.setText(String.valueOf(model.getMinutesTime()));

        holder.binding.btnClose.setOnClickListener(view -> {
            vm.deleteRemainder(model);
            remainderModelList.remove(model);
            notifyDataSetChanged();

        });

        holder.binding.rItem.setOnClickListener(view -> {

            RemainderDialog dialog = new RemainderDialog(model, context, vm);
            dialog.show(manager, "");

        });

    }

    @Override
    public int getItemCount() {

        if(remainderModelList == null)
            return 0;
        return remainderModelList.size();

    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private final LayoutReminderItemBinding binding;

        public ViewHolder(@NonNull LayoutReminderItemBinding binding) {
            super(binding.getRoot());

            this.binding = binding;

        }
    }

}
