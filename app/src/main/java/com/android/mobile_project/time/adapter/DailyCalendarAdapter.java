package com.android.mobile_project.time.adapter;

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
import com.android.mobile_project.databinding.RcvVerticalCalendarTextDateBinding;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DailyCalendarAdapter extends RecyclerView.Adapter<DailyCalendarAdapter.ViewHolder> {

    private final List<LocalDate> days;
    private static int selectedItemPosition;
    private static boolean isFirstInit = true;
    private OnClickItem onClickItem;

    private Context context;

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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        LocalDate date = days.get(position);

        DayOfWeek day = date.getDayOfWeek();
        String dayName = day.getDisplayName(TextStyle.FULL, Locale.getDefault());

        switch (dayName){
            case "Monday" :
                holder.binding.dateName.setText("MON.");
                break;
            case "Tuesday" :
                holder.binding.dateName.setText("TUE.");
                break;
            case "Wednesday" :
                holder.binding.dateName.setText("WED.");
                break;
            case "Thursday" :
                holder.binding.dateName.setText("THUR.");
                break;
            case "Friday" :
                holder.binding.dateName.setText("FRI.");
                break;
            case "Saturday" :
                holder.binding.dateName.setText("SAT.");
                break;
            default:
                holder.binding.dateName.setText("SUN.");
                break;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd");
        holder.binding.dateNum.setText(date.format(formatter));

        holder.binding.parentView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                selectedItemPosition = position;
                notifyDataSetChanged();
                onClickItem.onClick(view, days, position);
            }
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
        public void onClick(View view, List<LocalDate> dates, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        RcvHorizontalCalendarTextDateBinding binding;

        public ViewHolder(@NonNull RcvHorizontalCalendarTextDateBinding binding) {
            super(binding.getRoot());

            this.binding = binding;

        }
    }
}
