package com.android.mobile_project.ui.activity.main.fragment.home;

import static com.google.android.material.color.MaterialColors.ALPHA_FULL;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.android.mobile_project.data.remote.model.DayOfWeekModel;
import com.android.mobile_project.data.remote.model.HabitInWeekModel;
import com.android.mobile_project.data.remote.model.HabitModel;
import com.android.mobile_project.data.remote.model.HistoryModel;
import com.android.mobile_project.databinding.FragmentHomeBinding;
import com.android.mobile_project.ui.activity.main.MainActivity;
import com.android.mobile_project.utils.dagger.component.sub.main.fragment.HomeComponent;
import com.android.mobile_project.utils.time.adapter.DailyCalendarAdapter;
import com.android.mobile_project.utils.time.utils.TimeUtils;
import com.android.mobile_project.ui.InitLayout;
import com.android.mobile_project.ui.activity.count.CountDownActivity;
import com.android.mobile_project.ui.activity.create.CreateHabitActivity;
import com.android.mobile_project.ui.activity.main.fragment.home.adapter.AfterAdapter;
import com.android.mobile_project.ui.activity.main.fragment.home.adapter.BeforeAdapter;
import com.android.mobile_project.ui.activity.main.fragment.home.adapter.DoneHabitAdapter;
import com.android.mobile_project.ui.activity.main.fragment.home.adapter.FailedHabitAdapter;
import com.android.mobile_project.ui.activity.main.fragment.home.adapter.HabitAdapter;
import com.android.mobile_project.ui.activity.main.fragment.home.service.InitUIService;
import com.android.mobile_project.ui.activity.setting.HabitSettingActivity;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

public class HomeFragment extends Fragment implements InitLayout, View.OnClickListener{

    private FragmentHomeBinding binding;

    public HomeComponent component;

    @Inject
    HomeViewModel viewModel;

    @Override
    public void onAttach(@NonNull Context context) {
        component = ((MainActivity)getActivity()).component.mHomeComponent().create();
        component.inject(this);
        super.onAttach(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = initContentView();
        initViewModel();

        viewModel.setDate(binding.tvDate);
        viewModel.setMonth(binding.titleMonth);

        viewModel.updateService.updateHabitLongestSteak();

        viewModel.initUIService.initDailyCalendar();
        viewModel.initUIService.initHistoryListOfDay();
        viewModel.initUIService.initHabitListUI();

        return v;
    }

    @Override
    public View initContentView() {
        binding = FragmentHomeBinding.inflate(getLayoutInflater());
        binding.setA(this);
        return binding.getRoot();
    }

    @SuppressLint("NotifyDataSetChanged")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void initViewModel() {

        binding.setVm(viewModel);

        viewModel.getCurrentDayOfWeek();

        viewModel.setOnClickItem((view, dates, position) -> {

            LocalDate date = dates.get(position);

            TimeUtils utils = new TimeUtils();
            if(date.isAfter(utils.getSelectedDate())){

                DayOfWeek day = date.getDayOfWeek();
                String dayName = day.getDisplayName(TextStyle.FULL, Locale.getDefault());

                DayOfWeekModel dayOfWeekModel = viewModel.getDayOfWeekModel(dayName);
                dayOfWeekModel.getDayOfWeekId();

                binding.rcvBefore.setVisibility(View.GONE);
                binding.rcvHabitList.setVisibility(View.GONE);
                binding.rcvHabitFailedList.setVisibility(View.GONE);
                binding.rcvHabitDoneList.setVisibility(View.GONE);
                binding.tTodo.setVisibility(View.GONE);
                binding.tFailed.setVisibility(View.GONE);
                binding.tDone.setVisibility(View.GONE);

                viewModel.setHideDone(false);
                viewModel.setHideFailed(false);
                viewModel.setHideToDo(false);

                List<HabitModel> list = new ArrayList<>();

                for(HabitInWeekModel model : viewModel.getHabitInWeekModels()){
                    list.add(viewModel.getHabitByUserIdAndHabitId(model.getHabitId()));
                }

                AfterAdapter adapter  = new AfterAdapter(getContext(), list);
                adapter.notifyDataSetChanged();
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);

                binding.rcvAfter.setLayoutManager(layoutManager);
                binding.rcvAfter.setAdapter(adapter);

                binding.rcvAfter.setVisibility(View.VISIBLE);

            }else if(date.isBefore(utils.getSelectedDate())){

                binding.rcvAfter.setVisibility(View.GONE);
                binding.rcvHabitList.setVisibility(View.GONE);
                binding.rcvHabitFailedList.setVisibility(View.GONE);
                binding.rcvHabitDoneList.setVisibility(View.GONE);
                binding.tTodo.setVisibility(View.GONE);
                binding.tFailed.setVisibility(View.GONE);
                binding.tDone.setVisibility(View.GONE);

                viewModel.setHideDone(false);
                viewModel.setHideFailed(false);
                viewModel.setHideToDo(false);

                List<HabitModel> list = new ArrayList<>();

                for(HistoryModel model : viewModel.getHistoryByDate(date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))){
                    list.add(viewModel.getHabitByUserIdAndHabitId(model.getHabitId()));
                }

                BeforeAdapter adapter  = new BeforeAdapter(getContext(), list, date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), viewModel);
                adapter.notifyDataSetChanged();
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);

                binding.rcvBefore.setLayoutManager(layoutManager);
                binding.rcvBefore.setAdapter(adapter);

                binding.rcvBefore.setVisibility(View.VISIBLE);

            }else {

                binding.rcvAfter.setVisibility(View.GONE);
                binding.rcvBefore.setVisibility(View.GONE);

                if(!viewModel.getHabitModelFailedList().isEmpty()){
                    binding.rcvHabitFailedList.setVisibility(View.VISIBLE);
                    binding.tFailed.setVisibility(View.VISIBLE);
                    viewModel.setHideFailed(true);
                }

                if(!viewModel.getHabitModelDoneList().isEmpty()){
                    binding.rcvHabitDoneList.setVisibility(View.VISIBLE);
                    binding.tDone.setVisibility(View.VISIBLE);
                    viewModel.setHideDone(true);
                }

                if(!viewModel.getHabitModelList().isEmpty()){
                    binding.rcvHabitList.setVisibility(View.VISIBLE);
                    binding.tTodo.setVisibility(View.VISIBLE);
                    viewModel.setHideToDo(true);
                }

            }

        });

        viewModel.initUIService = new InitUIService() {
            @SuppressLint("NotifyDataSetChanged")
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void initDailyCalendar() {

                TimeUtils utils = new TimeUtils();
                viewModel.setDays(utils.getSixtyDaysArray());

                viewModel.dailyCalendarAdapter = new DailyCalendarAdapter(getContext(), viewModel.getDays(), viewModel.getOnClickItem());
                viewModel.dailyCalendarAdapter.notifyDataSetChanged();
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);

                binding.rvHorCalendar.setLayoutManager(layoutManager);
                binding.rvHorCalendar.setAdapter(viewModel.dailyCalendarAdapter);
                binding.rvHorCalendar.smoothScrollToPosition(viewModel.getDays().size() / 2 + 1);

                SnapHelper helper = new LinearSnapHelper();
                helper.attachToRecyclerView(binding.rvHorCalendar);

            }

            @Override
            public void initHabitListUI() {

                for(HistoryModel model : viewModel.getHistoryModelList()){

                    if(model.getHistoryHabitsState().equals("true")){
                        viewModel.getHabitModelDoneList().add(viewModel.getHabitByUserIdAndHabitId(model.getHabitId()));
                    }

                    if(model.getHistoryHabitsState().equals("false")){
                        viewModel.getHabitModelDoneList().add(viewModel.getHabitByUserIdAndHabitId(model.getHabitId()));
                    }

                    if(model.getHistoryHabitsState().equals("null")) {
                        viewModel.getHabitModelDoneList().add(viewModel.getHabitByUserIdAndHabitId(model.getHabitId()));
                    }
                }

                if(viewModel.getHabitModelList().size() > 0){

                    binding.tTodo.setVisibility(View.VISIBLE);
                    binding.rcvHabitList.setVisibility(View.VISIBLE);

                    viewModel.setAdapter(new HabitAdapter(getContext(), viewModel.getHabitModelList(), viewModel.recyclerViewClickListener, viewModel));
                    viewModel.getAdapter().notifyDataSetChanged();
                    RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);

                    binding.rcvHabitList.setAdapter(viewModel.getAdapter());
                    binding.rcvHabitList.setLayoutManager(manager);

                    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
                    itemTouchHelper.attachToRecyclerView(binding.rcvHabitList);

                }

                if(viewModel.getHabitModelDoneList().size() > 0){

                    binding.tDone.setVisibility(View.VISIBLE);
                    binding.rcvHabitDoneList.setVisibility(View.VISIBLE);

                    viewModel.setDoneHabitAdapter(new DoneHabitAdapter(getContext(), viewModel.getHabitModelDoneList()));
                    viewModel.getDoneHabitAdapter().notifyDataSetChanged();
                    RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);

                    binding.rcvHabitDoneList.setAdapter(viewModel.getDoneHabitAdapter());
                    binding.rcvHabitDoneList.setLayoutManager(manager);

                    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback1);
                    itemTouchHelper.attachToRecyclerView(binding.rcvHabitDoneList);

                }

                if(viewModel.getHabitModelFailedList().size() > 0){

                    binding.tFailed.setVisibility(View.VISIBLE);
                    binding.rcvHabitFailedList.setVisibility(View.VISIBLE);

                    viewModel.setFailedHabitAdapter(new FailedHabitAdapter(viewModel.getHabitModelFailedList(), getContext()));
                    viewModel.getFailedHabitAdapter().notifyDataSetChanged();
                    RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);

                    binding.rcvHabitFailedList.setAdapter(viewModel.getFailedHabitAdapter());
                    binding.rcvHabitFailedList.setLayoutManager(manager);

                    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback2);
                    itemTouchHelper.attachToRecyclerView(binding.rcvHabitFailedList);

                }

            }

            @Override
            public void initHistoryListOfDay() {

                String historyTime = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                viewModel.setHistoryModelList(viewModel.getHistoryByDate(historyTime));
                List<HabitInWeekModel> habitInWeekModels = viewModel.getHabitInWeekModels();

                if(viewModel.getHistoryModelList().size() == 0){

                    List<HabitModel> list = new ArrayList<>();

                    for(HabitInWeekModel model : habitInWeekModels){
                        list.add(viewModel.getHabitByUserIdAndHabitId(model.getHabitId()));
                    }

                    for(HabitModel model : list){

                        HistoryModel historyModel = new HistoryModel();
                        historyModel.setHistoryDate(historyTime);
                        historyModel.setUserId(DataLocalManager.getUserId());
                        historyModel.setHistoryHabitsState("null");
                        historyModel.setHabitId(model.getHabitId());

                        viewModel.getHistoryModelList().add(historyModel);
                        viewModel.insertHistory(historyModel);

                    }

                }else {

                    List<HabitModel> list = new ArrayList<>();

                    for(HabitInWeekModel model : habitInWeekModels){
                        list.add(viewModel.getHabitByUserIdAndHabitId(model.getHabitId()));
                    }

                    for(HabitModel model : list){

                        HistoryModel historyModel = viewModel.getHistoryByHabitIdAndDate(model.getHabitId(), historyTime);

                        if(historyModel == null){
                            historyModel = new HistoryModel();
                            historyModel.setHistoryDate(historyTime);
                            historyModel.setUserId(DataLocalManager.getUserId());
                            historyModel.setHistoryHabitsState("null");
                            historyModel.setHabitId(model.getHabitId());

                            viewModel.getHistoryModelList().add(historyModel);
                            viewModel.insertHistory(historyModel);

                        }

                    }

                }
            }

        };

        viewModel.recyclerViewClickListener = (v, habitModelList, position) -> {

            HabitModel model = habitModelList.get(position);

            Intent intent = new Intent(getContext(), HabitSettingActivity.class);
            intent.putExtra("habitId", model.getHabitId());
            startActivity(intent);

        };

        viewModel.updateService = () -> {

            TimeUtils utils = new TimeUtils();
            LocalDate yesterday = utils.getSelectedDate().minus(1, ChronoUnit.DAYS);

            for(HistoryModel model : viewModel.getHistoryByDate(yesterday.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))){
                if (model.getHistoryHabitsState().equals("true")){
                    HabitModel habitModel = viewModel.getHabitByUserIdAndHabitId(model.getHabitId());
                    habitModel.setNumOfLongestSteak(habitModel.getNumOfLongestSteak() + 1);
                    viewModel.updateHabit(habitModel);
                }
            }

        };

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View view) {

        int id = view.getId();

        if(id == R.id.btn_ch){
            clickCreateHabit();
        }else if(id == R.id.t_todo){
            if(!viewModel.isHideToDo()){
                viewModel.setHideToDo(true);
                binding.rcvHabitList.setVisibility(View.GONE);
            }else {
                viewModel.setHideToDo(false);
                binding.rcvHabitList.setVisibility(View.VISIBLE);
            }
        }else if(id == R.id.t_done){
            if(!viewModel.isHideDone()){
                viewModel.setHideDone(true);
                binding.rcvHabitDoneList.setVisibility(View.GONE);
            }else {
                viewModel.setHideDone(false);
                binding.rcvHabitDoneList.setVisibility(View.VISIBLE);
            }
        }else if(id == R.id.t_failed){
            if(!viewModel.isHideFailed()){
                viewModel.setHideFailed(true);
                binding.rcvHabitFailedList.setVisibility(View.GONE);
            }else {
                viewModel.setHideFailed(false);
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

        @SuppressLint("NotifyDataSetChanged")
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            int position = viewHolder.getAdapterPosition();

            switch (direction){
                case ItemTouchHelper.LEFT:

                    HabitModel habit = viewModel.getHabitModelList().get(viewHolder.getAdapterPosition());

                    String historyTime = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                    HistoryModel historyModel = viewModel.getHistoryByHabitIdAndDate(viewModel.getHabitModelList().get(position).getHabitId(), historyTime);

                    historyModel.setHistoryHabitsState("false");
                    viewModel.updateHistory(historyModel);

                    viewModel.getHabitModelList().remove(position);
                    viewModel.getAdapter().notifyItemRemoved(position);

                    if(viewModel.getHabitModelList().size() == 0){
                        binding.tTodo.setVisibility(View.GONE);
                        binding.rcvHabitList.setVisibility(View.GONE);
                        viewModel.setHideToDo(true);
                    }

                    if(viewModel.getHabitModelFailedList().size() == 0){

                        viewModel.getHabitModelFailedList().add(habit);

                        binding.tFailed.setVisibility(View.VISIBLE);
                        binding.rcvHabitFailedList.setVisibility(View.VISIBLE);
                        viewModel.setHideFailed(false);

                        viewModel.setFailedHabitAdapter(new FailedHabitAdapter(viewModel.getHabitModelFailedList(), getContext()));
                        viewModel.getFailedHabitAdapter().notifyDataSetChanged();
                        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);

                        binding.rcvHabitFailedList.setAdapter(viewModel.getFailedHabitAdapter());
                        binding.rcvHabitFailedList.setLayoutManager(manager);

                        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback2);
                        itemTouchHelper.attachToRecyclerView(binding.rcvHabitFailedList);

                    }else {

                        binding.tFailed.setVisibility(View.VISIBLE);
                        binding.rcvHabitFailedList.setVisibility(View.VISIBLE);
                        viewModel.setHideFailed(false);

                        viewModel.getHabitModelFailedList().add(habit);
                        viewModel.getFailedHabitAdapter().notifyItemInserted(viewModel.getHabitModelFailedList().size());
                    }


                    break;

                case ItemTouchHelper.RIGHT:

                    HabitModel habitModel = viewModel.getHabitModelList().get(viewHolder.getAdapterPosition());
                    List<HabitInWeekModel> models = viewModel.getDayOfWeekHabitListByUserAndHabitId(habitModel.getHabitId());

                    String history = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                    HabitInWeekModel model = models.get(0);

                    if(model.getTimerSecond() == null && model.getTimerMinute() == null && model.getTimerHour() == null){

                        HistoryModel h = viewModel.getHistoryByHabitIdAndDate(viewModel.getHabitModelList().get(position).getHabitId(), history);

                        h.setHistoryHabitsState("true");
                        viewModel.updateHistory(h);

                        viewModel.getHabitModelList().remove(position);
                        viewModel.getAdapter().notifyItemRemoved(position);

                        if(viewModel.getHabitModelList().size() == 0){
                            binding.tTodo.setVisibility(View.GONE);
                            binding.rcvHabitList.setVisibility(View.GONE);
                            viewModel.setHideToDo(true);
                        }

                        if(viewModel.getHabitModelDoneList().size() == 0){

                            viewModel.getHabitModelDoneList().add(habitModel);

                            binding.tDone.setVisibility(View.VISIBLE);
                            binding.rcvHabitDoneList.setVisibility(View.VISIBLE);
                            viewModel.setHideDone(false);

                            viewModel.setDoneHabitAdapter(new DoneHabitAdapter(getContext(), viewModel.getHabitModelDoneList()));
                            viewModel.getDoneHabitAdapter().notifyDataSetChanged();
                            RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);

                            binding.rcvHabitDoneList.setAdapter(viewModel.getDoneHabitAdapter());
                            binding.rcvHabitDoneList.setLayoutManager(manager);

                            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback1);
                            itemTouchHelper.attachToRecyclerView(binding.rcvHabitDoneList);

                        }else {

                            binding.tDone.setVisibility(View.VISIBLE);
                            binding.rcvHabitDoneList.setVisibility(View.VISIBLE);
                            viewModel.setHideFailed(false);

                            viewModel.getHabitModelDoneList().add(habitModel);
                            viewModel.getDoneHabitAdapter().notifyItemInserted(viewModel.getHabitModelDoneList().size());
                        }

                    }else {

                        viewModel.getAdapter().notifyItemChanged(viewHolder.getAdapterPosition());
                        Intent intent = new Intent(getContext(), CountDownActivity.class);
                        intent.putExtra("habitId", habitModel.getHabitId());
                        startActivity(intent);
                    }
                    break;
            }

        }

        @RequiresApi(api = Build.VERSION_CODES.O)
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

                        HabitModel habitModel = viewModel.getHabitModelList().get(position);
                        List<HabitInWeekModel> models = viewModel.getDayOfWeekHabitListByUserAndHabitId(habitModel.getHabitId());

                        HabitInWeekModel model = models.get(0);

                        if(model.getTimerSecond() == null && model.getTimerMinute() == null && model.getTimerHour() == null){
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

    ItemTouchHelper.SimpleCallback simpleCallback1 = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @SuppressLint("NotifyDataSetChanged")
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            int position = viewHolder.getAdapterPosition();

            switch (direction) {
                case ItemTouchHelper.LEFT:
                    break;
                case ItemTouchHelper.RIGHT:

                    String history = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                    HabitModel habitModel = viewModel.getHabitModelDoneList().get(viewHolder.getAdapterPosition());

                    viewModel.getHabitModelDoneList().remove(habitModel);
                    viewModel.getDoneHabitAdapter().notifyItemRemoved(position);

                    HistoryModel historyModel = viewModel.getHistoryByHabitIdAndDate(habitModel.getHabitId(), history);
                    historyModel.setHistoryHabitsState("null");
                    viewModel.updateHistory(historyModel);

                    if(viewModel.getHabitModelList().size() == 0){

                        viewModel.getHabitModelList().add(habitModel);

                        viewModel.setAdapter(new HabitAdapter(getContext(), viewModel.getHabitModelList(), viewModel.recyclerViewClickListener));
                        viewModel.getAdapter().notifyDataSetChanged();
                        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);

                        binding.rcvHabitList.setAdapter(viewModel.getAdapter());
                        binding.rcvHabitList.setLayoutManager(manager);

                        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
                        itemTouchHelper.attachToRecyclerView(binding.rcvHabitList);

                        binding.tTodo.setVisibility(View.VISIBLE);
                        binding.rcvHabitList.setVisibility(View.VISIBLE);
                        viewModel.setHideToDo(false);

                    }else {
                        viewModel.getHabitModelList().add(habitModel);
                        viewModel.getAdapter().notifyItemInserted(viewModel.getHabitModelList().size());
                    }

                    if(viewModel.getHabitModelDoneList().size() == 0){
                        binding.tDone.setVisibility(View.GONE);
                        binding.rcvHabitDoneList.setVisibility(View.GONE);
                        viewModel.setHideDone(true);
                    }

                    break;

            }

        }

        @Override
        public void onChildDrawOver(@NonNull Canvas c, @NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        }
    };

    ItemTouchHelper.SimpleCallback simpleCallback2 = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @SuppressLint("NotifyDataSetChanged")
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            int position = viewHolder.getAdapterPosition();

            switch (direction) {
                case ItemTouchHelper.LEFT:
                    break;
                case ItemTouchHelper.RIGHT:

                    String historyDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                    HabitModel habitModel = viewModel.getHabitModelFailedList().get(viewHolder.getAdapterPosition());

                    viewModel.getHabitModelFailedList().remove(habitModel);
                    viewModel.getFailedHabitAdapter().notifyItemRemoved(position);

                    HistoryModel historyModel = viewModel.getHistoryByHabitIdAndDate(habitModel.getHabitId(), historyDate);
                    historyModel.setHistoryHabitsState("null");
                    viewModel.updateHistory(historyModel);

                    if(viewModel.getHabitModelList().size() == 0){

                        viewModel.getHabitModelList().add(habitModel);

                        viewModel.setAdapter(new HabitAdapter(getContext(), viewModel.getHabitModelList(), viewModel.recyclerViewClickListener));
                        viewModel.getAdapter().notifyDataSetChanged();
                        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);

                        binding.rcvHabitList.setAdapter(viewModel.getAdapter());
                        binding.rcvHabitList.setLayoutManager(manager);

                        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
                        itemTouchHelper.attachToRecyclerView(binding.rcvHabitList);

                        binding.tTodo.setVisibility(View.VISIBLE);
                        binding.rcvHabitList.setVisibility(View.VISIBLE);
                        viewModel.setHideToDo(false);

                    }else {
                        viewModel.getHabitModelList().add(habitModel);
                        viewModel.getAdapter().notifyItemInserted(viewModel.getHabitModelList().size());
                    }

                    if(viewModel.getHabitModelFailedList().size() == 0){
                        binding.tFailed.setVisibility(View.GONE);
                        binding.rcvHabitFailedList.setVisibility(View.GONE);
                        viewModel.setHideFailed(true);
                    }

                    break;
            }

        }

        @Override
        public void onChildDrawOver(@NonNull Canvas c, @NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

}
