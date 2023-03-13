package com.android.mobile_project.ui.activity.setting.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.mobile_project.R;
import com.android.mobile_project.data.remote.model.HistoryModel;
import com.android.mobile_project.databinding.RcvVerticalCalendarTextDateBinding;
import com.android.mobile_project.ui.activity.setting.IHabitSettingViewModel;

import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.O)
public class MonthlyCalendarHabitAdapter extends RecyclerView.Adapter<MonthlyCalendarHabitAdapter.ViewHolder>{

    private final static String STATE_VALUE_NULL = "null";
    private final static String STATE_VALUE_TRUE = "true";
    private final static String STATE_VALUE_FALSE = "false";

    private final String today;
    private final List<String> daysOfMonth;
    private final String presentMonthYear;
    private final Long habitId;

    private final Context context;

    private final IHabitSettingViewModel vm;

    public MonthlyCalendarHabitAdapter(Context context, String today, List<String> daysOfMonth, String presentMonthYear,
                                       Long habitId, IHabitSettingViewModel vm){
        this.context = context;
        this.today = today;
        this.daysOfMonth = daysOfMonth;
        this.presentMonthYear = presentMonthYear;
        this.habitId = habitId;
        this.vm = vm;
    }


    @NonNull
    @Override
    public MonthlyCalendarHabitAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RcvVerticalCalendarTextDateBinding binding = RcvVerticalCalendarTextDateBinding.inflate(inflater, parent, false);

        return new ViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull MonthlyCalendarHabitAdapter.ViewHolder holder, int position) {

        if(!daysOfMonth.get(position).equals("")){

            if(Integer.parseInt(daysOfMonth.get(position)) == Integer.parseInt(today)){

                holder.binding.date.setText(daysOfMonth.get(position));
                holder.binding.date.setTextColor(Color.parseColor(String.valueOf(R.color.white)));
                holder.binding.date.setBackground(ContextCompat.getDrawable(context, R.drawable.bck_car_today));

            }else {

                String getLocalDateString = presentMonthYear + "-" + daysOfMonth.get(position);
                HistoryModel model = vm.getHistoryByHabitIdAndDate(habitId, getLocalDateString);
                if(model != null){

                    switch (model.getHistoryHabitsState()){
                        case STATE_VALUE_NULL:
                            holder.binding.date.setText(daysOfMonth.get(position));
                            holder.binding.date.setTextColor(Color.parseColor(String.valueOf(R.color.white)));
                            holder.binding.date.setBackground(ContextCompat.getDrawable(context, R.drawable.bck_car_null_date));
                            break;
                        case STATE_VALUE_FALSE:
                            holder.binding.date.setText(daysOfMonth.get(position));
                            holder.binding.date.setTextColor(Color.parseColor(String.valueOf(R.color.white)));
                            holder.binding.date.setBackground(ContextCompat.getDrawable(context, R.drawable.bck_car_false_date));
                            break;
                        case STATE_VALUE_TRUE:
                            holder.binding.date.setText(daysOfMonth.get(position));
                            holder.binding.date.setTextColor(Color.parseColor(String.valueOf(R.color.white)));
                            holder.binding.date.setBackground(ContextCompat.getDrawable(context, R.drawable.bck_car_true_date));
                            break;
                        default:
                            break;
                    }

                }else {
                    holder.binding.date.setText(daysOfMonth.get(position));
                    holder.binding.date.setTextColor(Color.parseColor(String.valueOf(R.color.white)));
                    holder.binding.date.setBackground(ContextCompat.getDrawable(context, R.drawable.bck_car_date));
                }

            }

        }else {
            holder.binding.date.setText("");
        }

    }

    @Override
    public int getItemCount() {
        if (daysOfMonth != null){
            return daysOfMonth.size();
        }
        return 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        private final RcvVerticalCalendarTextDateBinding binding;

        public ViewHolder(@NonNull RcvVerticalCalendarTextDateBinding binding) {
            super(binding.getRoot());

            this.binding = binding;

        }
    }

}
