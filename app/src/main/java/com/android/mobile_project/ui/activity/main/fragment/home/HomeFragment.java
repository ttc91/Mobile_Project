package com.android.mobile_project.ui.activity.main.fragment.home;

import static com.google.android.material.color.MaterialColors.ALPHA_FULL;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.android.mobile_project.R;
import com.android.mobile_project.data.local.DataLocalManager;
import com.android.mobile_project.data.local.model.db.DayOfWeekEntity;
import com.android.mobile_project.data.local.model.db.HabitEntity;
import com.android.mobile_project.data.local.model.db.HabitInWeekEntity;
import com.android.mobile_project.data.local.model.db.HistoryEntity;
import com.android.mobile_project.data.local.sqlite.HabitTrackerDatabase;
import com.android.mobile_project.databinding.FragmentHomeBinding;
import com.android.mobile_project.time.adapter.DailyCalendarAdapter;
import com.android.mobile_project.time.utils.TimeUtils;
import com.android.mobile_project.ui.InitLayout;
import com.android.mobile_project.ui.activity.count.CountDownActivity;
import com.android.mobile_project.ui.activity.create.CreateHabitActivity;
import com.android.mobile_project.ui.activity.main.fragment.home.adapter.HabitAdapter;
import com.android.mobile_project.ui.activity.main.fragment.home.service.InitUIService;
import com.android.mobile_project.ui.activity.main.fragment.home.service.RecyclerViewInterface;
import com.android.mobile_project.ui.activity.setting.HabitSettingActivity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements InitLayout, View.OnClickListener{

    private FragmentHomeBinding binding;
    private HomeViewModel viewModel;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = initContentView();
        initViewModel();

        viewModel.setDate(binding.tvDate);
        viewModel.setMonth(binding.titleMonth);

        viewModel.initUIService.initDailyCalendar();
        viewModel.initUIService.initHabitListUI();

        List<HistoryEntity> list = new ArrayList<>();
        list = HabitTrackerDatabase.getInstance(getContext()).historyDao().getAllHistoryList();
        for (HistoryEntity entity : list){
            Log.e("Habit id", String.valueOf(entity.habitId));
            Log.e("State", String.valueOf(entity.historyHabitsState));
            Log.e("Date", String.valueOf(entity.historyDate));
        }

        return v;
    }

    @Override
    public View initContentView() {

        binding = FragmentHomeBinding.inflate(getLayoutInflater());
        binding.setA(this);
        return binding.getRoot();

    }

    @Override
    public void initViewModel() {

        viewModel = new HomeViewModel();
        binding.setVm(viewModel);

        viewModel.initUIService = new InitUIService() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void initDailyCalendar() {

                com.android.mobile_project.time.utils.TimeUtils utils = new TimeUtils();
                ArrayList<LocalDate> days = utils.getSixtyDaysArray();

                DailyCalendarAdapter adapter = new DailyCalendarAdapter(getContext(), days);
                adapter.notifyDataSetChanged();
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);

                binding.rvHorCalendar.setLayoutManager(layoutManager);
                binding.rvHorCalendar.setAdapter(adapter);
                binding.rvHorCalendar.smoothScrollToPosition(days.size() / 2 + 1);

                SnapHelper helper = new LinearSnapHelper();
                helper.attachToRecyclerView(binding.rvHorCalendar);

            }

            @Override
            public void initHabitListUI() {

                viewModel.habitEntityList = HabitTrackerDatabase.getInstance(getContext()).habitDao().getHabitListByUserId(DataLocalManager.getUserId());

                if(viewModel.habitEntityList.size() > 0){
                    binding.tTodo.setVisibility(View.VISIBLE);
                }

                viewModel.adapter = new HabitAdapter(getContext(), viewModel.habitEntityList, viewModel.recyclerViewClickListener);
                viewModel.adapter.notifyDataSetChanged();
                RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);

                binding.rcvHabitList.setAdapter(viewModel.adapter);
                binding.rcvHabitList.setLayoutManager(manager);

                ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
                itemTouchHelper.attachToRecyclerView(binding.rcvHabitList);



            }
        };

        viewModel.recyclerViewClickListener = new HabitAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, List<HabitEntity> habitEntityList, int position) {

                HabitEntity entity = habitEntityList.get(position);

                Intent intent = new Intent(getContext(), HabitSettingActivity.class);
                intent.putExtra("habitId", entity.habitId);
                startActivity(intent);

            }
        };

    }

    @Override
    public void onClick(View view) {

        int id = view.getId();

        if(id == R.id.btn_ch){
            clickCreateHabit();
        }

    }

    public void clickCreateHabit(){

        Intent intent = new Intent(getContext(), CreateHabitActivity.class);
        startActivity(intent);

    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            int position = viewHolder.getAdapterPosition();


            switch (direction){
                case ItemTouchHelper.LEFT:

                    LocalDate local = LocalDate.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    String historyTime = local.format(formatter);

                    HistoryEntity historyEntity = HabitTrackerDatabase.getInstance(getContext()).historyDao()
                            .getHistoryByHabitIdAndDate(viewModel.habitEntityList.get(position).habitId, historyTime);

                    if(historyEntity != null){
                        historyEntity.historyHabitsState = false;
                        HabitTrackerDatabase.getInstance(getContext()).historyDao().updateHistory(historyEntity);
                    }else {
                        historyEntity = new HistoryEntity();
                        historyEntity.historyDate = historyTime;
                        historyEntity.historyHabitsState = false;
                        historyEntity.userId = DataLocalManager.getUserId();
                        historyEntity.habitId = viewModel.habitEntityList.get(position).habitId;

                        HabitTrackerDatabase.getInstance(getContext()).historyDao().insertHistory(historyEntity);
                    }

                    Log.e("CHECK", "OK");
                    viewModel.habitEntityList.remove(position);
                    viewModel.adapter.notifyItemRemoved(position);

                    break;

                case ItemTouchHelper.RIGHT:

                    HabitEntity habitEntity = viewModel.habitEntityList.get(viewHolder.getAdapterPosition());
                    List<HabitInWeekEntity> entities = HabitTrackerDatabase.getInstance(getContext()).habitInWeekDao().
                            getDayOfWeekHabitListByUserAndHabitId(DataLocalManager.getUserId(), habitEntity.habitId);

                    LocalDateTime time = LocalDateTime.now();
                    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    String history = time.format(timeFormatter);

                    HabitInWeekEntity entity = entities.get(0);

                    if(entity.timerSecond == null && entity.timerMinute == null && entity.timerHour == null){

                        HistoryEntity h = HabitTrackerDatabase.getInstance(getContext()).historyDao()
                                .getHistoryByHabitIdAndDate(viewModel.habitEntityList.get(position).habitId, history);

                        if(h != null){
                            h.historyHabitsState = true;
                            HabitTrackerDatabase.getInstance(getContext()).historyDao().updateHistory(h);
                        }else {
                            h = new HistoryEntity();
                            h.historyDate = history;
                            h.historyHabitsState = true;
                            h.userId = DataLocalManager.getUserId();
                            h.habitId = viewModel.habitEntityList.get(position).habitId;

                            HabitTrackerDatabase.getInstance(getContext()).historyDao().insertHistory(h);
                        }

                        viewModel.habitEntityList.remove(position);
                        viewModel.adapter.notifyItemRemoved(position);
                    }else {
                        Intent intent = new Intent(getContext(), CountDownActivity.class);
                        startActivity(intent);
                    }
                    break;
            }

        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX,
                                float dY, int actionState, boolean isCurrentlyActive) {

            if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){

                View itemView = viewHolder.itemView;
                Paint p = new Paint();
                Bitmap icon;

                if(dX < 0){

                    icon = BitmapFactory.decodeResource(getResources(), R.drawable.btn_red_close);

                    p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));

                    c.drawBitmap(icon,
                            (float) itemView.getRight() - icon.getWidth() - 10,
                            (float) itemView.getTop() + ((float) itemView.getBottom() - (float) itemView.getTop() - icon.getHeight())/2,
                            p);

                }

                if(dX > 0) {

                    Log.e("Length", String.valueOf(viewModel.habitEntityList.size()));
                    Log.e("Position", String.valueOf(viewHolder.getAdapterPosition()));

                    int position = viewHolder.getAdapterPosition();

                    if(position != -1){

                        HabitEntity habitEntity = viewModel.habitEntityList.get(position);
                        List<HabitInWeekEntity> entities = HabitTrackerDatabase.getInstance(getContext()).habitInWeekDao().
                                getDayOfWeekHabitListByUserAndHabitId(DataLocalManager.getUserId(), habitEntity.habitId);

                        HabitInWeekEntity entity = entities.get(0);

                        if(entity.timerSecond == null && entity.timerMinute == null && entity.timerHour == null){
                            icon = BitmapFactory.decodeResource(getResources(), R.drawable.btn_green_check);

                            p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));

                            c.drawBitmap(icon,
                                    (float) itemView.getLeft() + 10,
                                    (float) itemView.getTop() + ((float) itemView.getBottom() - (float) itemView.getTop() - icon.getHeight())/2,
                                    p);
                        }else {
                            icon = BitmapFactory.decodeResource(getResources(), R.drawable.btn_purple_clock);

                            p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));

                            c.drawBitmap(icon,
                                    (float) itemView.getLeft() + 10,
                                    (float) itemView.getTop() + ((float) itemView.getBottom() - (float) itemView.getTop() - icon.getHeight())/2,
                                    p);
                        }

                    }

                    final float alpha = ALPHA_FULL - Math.abs(dX) / (float) viewHolder.itemView.getWidth();
                    viewHolder.itemView.setAlpha(alpha);
                    viewHolder.itemView.setTranslationX(dX);

                }

                return;
            }else {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }


        }
    };

}
