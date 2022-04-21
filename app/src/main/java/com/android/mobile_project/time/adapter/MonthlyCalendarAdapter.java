package com.android.mobile_project.time.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.android.mobile_project.R;
import com.android.mobile_project.databinding.RcvVerticalCalendarTextDateBinding;

import java.util.List;

public class MonthlyCalendarAdapter extends RecyclerView.Adapter<MonthlyCalendarAdapter.ViewHolder>{

    private final String today;
    private List<String> daysOfMonth;

    private Context context;

    public MonthlyCalendarAdapter(Context context, String today, List<String> daysOfMonth){

        this.context = context;
        this.today = today;
        this.daysOfMonth = daysOfMonth;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RcvVerticalCalendarTextDateBinding binding = RcvVerticalCalendarTextDateBinding.inflate(inflater, parent, false);

        return new ViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if(daysOfMonth.get(position) != ""){

            if(Integer.parseInt(daysOfMonth.get(position)) == Integer.parseInt(today)){

                holder.binding.date.setText(daysOfMonth.get(position));
                holder.binding.date.setTextColor(Color.parseColor("#FFFFFF"));
                holder.binding.date.setBackground(ContextCompat.getDrawable(context, R.drawable.bck_car_today));

            }else {

                holder.binding.date.setText(daysOfMonth.get(position));
                holder.binding.date.setTextColor(Color.parseColor("#FFFFFF"));
                holder.binding.date.setBackground(ContextCompat.getDrawable(context, R.drawable.bck_car_date));

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
