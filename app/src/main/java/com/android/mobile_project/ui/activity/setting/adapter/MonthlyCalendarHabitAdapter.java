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
import com.android.mobile_project.data.local.DataLocalManager;
import com.android.mobile_project.data.local.model.db.HistoryEntity;
import com.android.mobile_project.data.local.sqlite.HabitTrackerDatabase;
import com.android.mobile_project.databinding.RcvVerticalCalendarTextDateBinding;

import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.O)
public class MonthlyCalendarHabitAdapter extends RecyclerView.Adapter<MonthlyCalendarHabitAdapter.ViewHolder>{

    private final String today;
    private List<String> daysOfMonth;
    private String presentMonthYear;
    private Long habitId;

    private Context context;


    public MonthlyCalendarHabitAdapter(Context context, String today, List<String> daysOfMonth, String presentMonthYear, Long habitId){

        this.context = context;
        this.today = today;
        this.daysOfMonth = daysOfMonth;
        this.presentMonthYear = presentMonthYear;
        this.habitId = habitId;

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

        if(daysOfMonth.get(position) != ""){

            if(Integer.parseInt(daysOfMonth.get(position)) == Integer.parseInt(today)){

                holder.binding.date.setText(daysOfMonth.get(position));
                holder.binding.date.setTextColor(Color.parseColor("#FFFFFF"));
                holder.binding.date.setBackground(ContextCompat.getDrawable(context, R.drawable.bck_car_today));

            }else {

                String getLocalDateString = presentMonthYear + "-" + daysOfMonth.get(position);
                HistoryEntity entity = HabitTrackerDatabase.getInstance(context).historyDao().getHistoryByHabitIdAndDate(habitId, getLocalDateString);
                if(entity != null){

                    switch (entity.historyHabitsState){
                        case "null":
                            holder.binding.date.setText(daysOfMonth.get(position));
                            holder.binding.date.setTextColor(Color.parseColor("#FFFFFF"));
                            holder.binding.date.setBackground(ContextCompat.getDrawable(context, R.drawable.bck_car_null_date));
                            break;
                        case "false":
                            holder.binding.date.setText(daysOfMonth.get(position));
                            holder.binding.date.setTextColor(Color.parseColor("#FFFFFF"));
                            holder.binding.date.setBackground(ContextCompat.getDrawable(context, R.drawable.bck_car_false_date));
                            break;
                        case "true":
                            holder.binding.date.setText(daysOfMonth.get(position));
                            holder.binding.date.setTextColor(Color.parseColor("#FFFFFF"));
                            holder.binding.date.setBackground(ContextCompat.getDrawable(context, R.drawable.bck_car_true_date));
                            break;
                        default:
                            break;
                    }

                }else {
                    holder.binding.date.setText(daysOfMonth.get(position));
                    holder.binding.date.setTextColor(Color.parseColor("#FFFFFF"));
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

    class ViewHolder extends RecyclerView.ViewHolder{

        private RcvVerticalCalendarTextDateBinding binding;

        public ViewHolder(@NonNull RcvVerticalCalendarTextDateBinding binding) {
            super(binding.getRoot());

            this.binding = binding;

        }
    }

}
