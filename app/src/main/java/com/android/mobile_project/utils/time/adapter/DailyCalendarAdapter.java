package com.android.mobile_project.utils.time.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.mobile_project.R;
import com.android.mobile_project.databinding.RcvHorizontalCalendarTextDateBinding;
import com.android.mobile_project.utils.constant.DailyCalendarEnum;
import com.android.mobile_project.utils.constant.DateConstant;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

public class DailyCalendarAdapter extends RecyclerView.Adapter<DailyCalendarAdapter.ViewHolder> {

    private final List<LocalDate> days;
    private static int selectedItemPosition;
    private static boolean isFirstInit = true;
    private final OnClickItem onClickItem;

    private final Context context;

    public DailyCalendarAdapter(Context context, List<LocalDate> days, OnClickItem onClickItem) {
        this.days = days;
        this.context = context;
        this.onClickItem = onClickItem;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RcvHorizontalCalendarTextDateBinding binding = RcvHorizontalCalendarTextDateBinding.inflate(inflater, parent, false);

        return new ViewHolder(binding);
    }

    @SuppressLint("NotifyDataSetChanged")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        LocalDate date = days.get(position);

        DayOfWeek day = date.getDayOfWeek();
        String dayName = day.getDisplayName(TextStyle.FULL, Locale.getDefault());

        switch (dayName){
            case DateConstant.DateName.DATE_MONDAY :
                holder.binding.dateName.setText(DailyCalendarEnum.MONDAY.getValue());
                break;
            case DateConstant.DateName.DATE_TUESDAY :
                holder.binding.dateName.setText(DailyCalendarEnum.TUESDAY.getValue());
                break;
            case DateConstant.DateName.DATE_WEDNESDAY :
                holder.binding.dateName.setText(DailyCalendarEnum.WEDNESDAY.getValue());
                break;
            case DateConstant.DateName.DATE_THURSDAY :
                holder.binding.dateName.setText(DailyCalendarEnum.THURSDAY.getValue());
                break;
            case DateConstant.DateName.DATE_FRIDAY:
                holder.binding.dateName.setText(DailyCalendarEnum.FRIDAY.getValue());
                break;
            case DateConstant.DateName.DATE_SATURDAY :
                holder.binding.dateName.setText(DailyCalendarEnum.SATURDAY.getValue());
                break;
            default:
                holder.binding.dateName.setText(DailyCalendarEnum.SUNDAY.getValue());
                break;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd");
        holder.binding.dateNum.setText(date.format(formatter));

        holder.binding.parentView.setOnClickListener(view -> {
            selectedItemPosition = position;
            notifyDataSetChanged();
            onClickItem.onClick(view, days, position);
        });

        if(isFirstInit){
            selectedItemPosition = 29;
            isFirstInit = false;
        }

        if(selectedItemPosition == position){
            holder.binding.parentView.setBackground(ContextCompat.getDrawable(context, R.drawable.bck_date_horizontal_car) );
            holder.binding.dateNum.setTextColor(Color.parseColor("#7FC779"));
            holder.binding.dateName.setTextColor(Color.parseColor("#7FC779"));
        }else {
            holder.binding.parentView.setBackgroundResource(0);
            holder.binding.dateNum.setTextColor(Color.parseColor("#000000"));
            holder.binding.dateName.setTextColor(Color.parseColor("#000000"));
        }

    }

    @Override
    public int getItemCount() {
        if (days != null){
            return days.size();
        }
        return 0;
    }

    public interface OnClickItem{
        void onClick(View view, List<LocalDate> dates, int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        RcvHorizontalCalendarTextDateBinding binding;

        public ViewHolder(@NonNull RcvHorizontalCalendarTextDateBinding binding) {
            super(binding.getRoot());

            this.binding = binding;

        }
    }
}
