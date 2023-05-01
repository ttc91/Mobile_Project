package com.android.mobile_project.ui.activity.setting.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.mobile_project.data.remote.model.RemainderModel;
import com.android.mobile_project.databinding.LayoutReminderItemBinding;
import com.android.mobile_project.ui.activity.setting.IHabitSettingViewModel;
import com.android.mobile_project.ui.activity.setting.service.DbService;
import com.android.mobile_project.ui.dialog.RemainderDialog;
import com.android.mobile_project.utils.constant.TimeConstant;

import java.util.List;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class RemainderAdapter extends RecyclerView.Adapter<RemainderAdapter.ViewHolder>{

    private final Context context;
    private final List<RemainderModel> remainderModelList;
    private final FragmentManager manager;
    private final IHabitSettingViewModel vm;

    @SuppressLint("NotifyDataSetChanged")
    public RemainderAdapter(Context context, List<RemainderModel> remainderModelList, FragmentManager manager, IHabitSettingViewModel vm){
        this.context = context;
        this.remainderModelList = remainderModelList;
        this.manager = manager;
        this.vm = vm;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        LayoutReminderItemBinding binding = LayoutReminderItemBinding.inflate(inflater, parent, false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        String[] timeValue = TimeConstant.getTimeDisplayValue();
        RemainderModel model = remainderModelList.get(position);
        holder.binding.tHour.setText(String.valueOf(timeValue[model.getHourTime().intValue()]));
        holder.binding.tMinute.setText(String.valueOf(timeValue[model.getMinutesTime().intValue()]));

        holder.binding.btnClose.setOnClickListener(view -> {
            vm.deleteRemainderByTimerHourAndTimerMinutesAndId(model.getHourTime(), model.getMinutesTime(), new DbService.DeleteRemainderResult() {
                @SuppressLint({"LongLogTag", "NotifyDataSetChanged"})
                @Override
                public void onDeleteRemainderSuccess(CompositeDisposable disposable) {
                    Log.i("RemainderAdapter-deleteRemainder", "onDeleteRemainderSuccess");
                    disposable.clear();
                    notifyItemRemoved(position);
                }

                @SuppressLint("LongLogTag")
                @Override
                public void onDeleteRemainderFailure(CompositeDisposable disposable) {
                    Log.i("RemainderAdapter-deleteRemainder", "onDeleteRemainderFailure");
                    disposable.clear();
                }
            });
            remainderModelList.remove(model);
        });

        holder.binding.rItem.setOnClickListener(view -> {
            RemainderDialog dialog = new RemainderDialog(position, model.getHourTime(), model.getMinutesTime(), context, vm);
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
