package com.android.mobile_project.ui.activity.main.fragment.home;

import static com.google.android.material.color.MaterialColors.ALPHA_FULL;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.android.mobile_project.R;
import com.android.mobile_project.data.local.DataLocalManager;
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
import com.android.mobile_project.ui.activity.main.fragment.home.adapter.DoneHabitAdapter;
import com.android.mobile_project.ui.activity.main.fragment.home.adapter.FailedHabitAdapter;
import com.android.mobile_project.ui.activity.main.fragment.home.adapter.HabitAdapter;
import com.android.mobile_project.ui.activity.main.fragment.home.service.InitUIService;
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
        viewModel.initUIService.initHistoryListOfDay();
        viewModel.initUIService.initHabitListUI();

//        LocalDate local = LocalDate.now();
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        String historyTime = local.format(formatter);
//
//        List<HistoryEntity> list = new ArrayList<>();
//        list = HabitTrackerDatabase.getInstance(getContext()).historyDao().getHistoryByDate(historyTime);
//        Log.e("Size", String.valueOf(list.size()));
//        for (HistoryEntity entity : list){
//            Log.e("Habit id", String.valueOf(entity.habitId));
//            Log.e("State", String.valueOf(entity.historyHabitsState));
//            Log.e("Date", String.valueOf(entity.historyDate));
//        }

        return v;
    }

    @Override
    public View initContentView() {

        binding = FragmentHomeBinding.inflate(getLayoutInflater());
        binding.setA(this);
        return binding.getRoot();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
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

                for(HistoryEntity entity : viewModel.historyEntityList){

                    if(entity.historyHabitsState == "true" || entity.historyHabitsState.equals("true")){
                        HabitEntity habit = HabitTrackerDatabase.getInstance(getContext()).habitDao().getHabitByUserIdAndHabitId(DataLocalManager.getUserId(), entity.habitId);
                        viewModel.habitEntityDoneList.add(habit);
                    }

                    if(entity.historyHabitsState == "false" || entity.historyHabitsState.equals("false")){
                        HabitEntity habit = HabitTrackerDatabase.getInstance(getContext()).habitDao().getHabitByUserIdAndHabitId(DataLocalManager.getUserId(), entity.habitId);
                        viewModel.habitEntityFailedList.add(habit);
                    }

                    if(entity.historyHabitsState == "null" || entity.historyHabitsState.equals("null")) {
                        HabitEntity habit = HabitTrackerDatabase.getInstance(getContext()).habitDao().getHabitByUserIdAndHabitId(DataLocalManager.getUserId(), entity.habitId);
                        viewModel.habitEntityList.add(habit);
                    }
                }

                Log.e("Done list", String.valueOf(viewModel.habitEntityDoneList.size()));
                Log.e("List", String.valueOf(viewModel.habitEntityList.size()));
                Log.e("Failed list", String.valueOf(viewModel.habitEntityFailedList.size()));

                if(viewModel.habitEntityList.size() > 0){

                    binding.tTodo.setVisibility(View.VISIBLE);
                    binding.rcvHabitList.setVisibility(View.VISIBLE);

                    viewModel.adapter = new HabitAdapter(getContext(), viewModel.habitEntityList, viewModel.recyclerViewClickListener);
                    viewModel.adapter.notifyDataSetChanged();
                    RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);

                    binding.rcvHabitList.setAdapter(viewModel.adapter);
                    binding.rcvHabitList.setLayoutManager(manager);

                    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
                    itemTouchHelper.attachToRecyclerView(binding.rcvHabitList);

                }

                if(viewModel.habitEntityDoneList.size() > 0){

                    binding.tDone.setVisibility(View.VISIBLE);
                    binding.rcvHabitDoneList.setVisibility(View.VISIBLE);

                    viewModel.doneHabitAdapter = new DoneHabitAdapter(getContext(), viewModel.habitEntityDoneList);
                    viewModel.doneHabitAdapter.notifyDataSetChanged();
                    RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);

                    binding.rcvHabitDoneList.setAdapter(viewModel.doneHabitAdapter);
                    binding.rcvHabitDoneList.setLayoutManager(manager);

                }

                if(viewModel.habitEntityFailedList.size() > 0){

                    binding.tFailed.setVisibility(View.VISIBLE);
                    binding.rcvHabitFailedList.setVisibility(View.VISIBLE);

                    viewModel.failedHabitAdapter = new FailedHabitAdapter(viewModel.habitEntityFailedList, getContext());
                    viewModel.failedHabitAdapter.notifyDataSetChanged();
                    RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);

                    binding.rcvHabitFailedList.setAdapter(viewModel.failedHabitAdapter);
                    binding.rcvHabitFailedList.setLayoutManager(manager);

                }

            }

            @Override
            public void initHistoryListOfDay() {

                LocalDate local = LocalDate.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String historyTime = local.format(formatter);

                viewModel.historyEntityList = HabitTrackerDatabase.getInstance(getContext()).historyDao().getHistoryByDate(historyTime);

                if(viewModel.historyEntityList.size() == 0){

                    List<HabitEntity> list = HabitTrackerDatabase.getInstance(getContext()).habitDao().getHabitListByUserId(DataLocalManager.getUserId());
                    for(HabitEntity entity : list){

                        HistoryEntity historyEntity = new HistoryEntity();
                        historyEntity.historyDate = historyTime;
                        historyEntity.userId = DataLocalManager.getUserId();
                        historyEntity.historyHabitsState = "null";
                        historyEntity.habitId = entity.habitId;

                        viewModel.historyEntityList.add(historyEntity);

                        HabitTrackerDatabase.getInstance(getContext()).historyDao().insertHistory(historyEntity);

                    }

                }else {

                    List<HabitEntity> list = HabitTrackerDatabase.getInstance(getContext()).habitDao().getHabitListByUserId(DataLocalManager.getUserId());
                    for(HabitEntity entity : list){

                        HistoryEntity historyEntity = HabitTrackerDatabase.getInstance(getContext()).historyDao().getHistoryByHabitIdAndDate(entity.habitId, historyTime);
                        if(historyEntity == null){
                            historyEntity = new HistoryEntity();
                            historyEntity.historyDate = historyTime;
                            historyEntity.userId = DataLocalManager.getUserId();
                            historyEntity.historyHabitsState = "null";
                            historyEntity.habitId = entity.habitId;

                            viewModel.historyEntityList.add(historyEntity);

                            HabitTrackerDatabase.getInstance(getContext()).historyDao().insertHistory(historyEntity);
                        }

                    }

                }


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
        }else if(id == R.id.t_todo){
            if(viewModel.hideToDo == false){
                viewModel.hideToDo = true;
                binding.rcvHabitList.setVisibility(View.GONE);
            }else {
                viewModel.hideToDo = false;
                binding.rcvHabitList.setVisibility(View.VISIBLE);
            }
        }else if(id == R.id.t_done){
            if(viewModel.hideDone == false){
                viewModel.hideDone = true;
                binding.rcvHabitDoneList.setVisibility(View.GONE);
            }else {
                viewModel.hideDone = false;
                binding.rcvHabitDoneList.setVisibility(View.VISIBLE);
            }
        }else if(id == R.id.t_failed){
            if(viewModel.hideFailed == false){
                viewModel.hideFailed = true;
                binding.rcvHabitFailedList.setVisibility(View.GONE);
            }else {
                viewModel.hideFailed = false;
                binding.rcvHabitFailedList.setVisibility(View.VISIBLE);
            }
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

                    HabitEntity habit = viewModel.habitEntityList.get(viewHolder.getAdapterPosition());

                    LocalDate local = LocalDate.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    String historyTime = local.format(formatter);

                    HistoryEntity historyEntity = HabitTrackerDatabase.getInstance(getContext()).historyDao()
                            .getHistoryByHabitIdAndDate(viewModel.habitEntityList.get(position).habitId, historyTime);

                    historyEntity.historyHabitsState = "false";
                    HabitTrackerDatabase.getInstance(getContext()).historyDao().updateHistory(historyEntity);

                    viewModel.habitEntityList.remove(position);
                    viewModel.adapter.notifyItemRemoved(position);

                    if(viewModel.habitEntityList.size() == 0){
                        binding.tTodo.setVisibility(View.GONE);
                        binding.rcvHabitList.setVisibility(View.GONE);
                        viewModel.hideToDo = false;
                    }

                    if(viewModel.habitEntityFailedList.size() == 0){

                        viewModel.habitEntityFailedList.add(habit);

                        binding.tFailed.setVisibility(View.VISIBLE);
                        binding.rcvHabitDoneList.setVisibility(View.VISIBLE);

                        viewModel.failedHabitAdapter = new FailedHabitAdapter(viewModel.habitEntityFailedList, getContext());
                        viewModel.failedHabitAdapter.notifyDataSetChanged();
                        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);

                        binding.rcvHabitFailedList.setAdapter(viewModel.failedHabitAdapter);
                        binding.rcvHabitFailedList.setLayoutManager(manager);

                    }else {
                        viewModel.habitEntityFailedList.add(habit);
                        viewModel.failedHabitAdapter.notifyItemInserted(viewModel.habitEntityFailedList.size());
                    }


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

                        h.historyHabitsState = "true";
                        HabitTrackerDatabase.getInstance(getContext()).historyDao().updateHistory(h);

                        viewModel.habitEntityList.remove(position);
                        viewModel.adapter.notifyItemRemoved(position);

                        if(viewModel.habitEntityList.size() == 0){
                            binding.tTodo.setVisibility(View.GONE);
                            binding.rcvHabitList.setVisibility(View.GONE);
                            viewModel.hideToDo = false;
                        }

                        if(viewModel.habitEntityDoneList.size() == 0){

                            viewModel.habitEntityDoneList.add(habitEntity);

                            binding.tDone.setVisibility(View.VISIBLE);
                            binding.rcvHabitFailedList.setVisibility(View.VISIBLE);

                            viewModel.doneHabitAdapter = new DoneHabitAdapter(getContext(), viewModel.habitEntityDoneList);
                            viewModel.doneHabitAdapter.notifyDataSetChanged();
                            RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);

                            binding.rcvHabitDoneList.setAdapter(viewModel.doneHabitAdapter);
                            binding.rcvHabitDoneList.setLayoutManager(manager);

                        }else {
                            viewModel.habitEntityDoneList.add(habitEntity);
                            viewModel.doneHabitAdapter.notifyItemInserted(viewModel.habitEntityDoneList.size());
                        }

                    }else {
                        viewModel.adapter.notifyItemChanged(viewHolder.getAdapterPosition());
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
                }

                final float alpha = ALPHA_FULL - Math.abs(dX) / (float) viewHolder.itemView.getWidth();
                viewHolder.itemView.setAlpha(alpha);
                viewHolder.itemView.setTranslationX(dX);

            }else {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }


        }
    };

    ItemTouchHelper.SimpleCallback simpleCallback_1 = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        }
    };

}
