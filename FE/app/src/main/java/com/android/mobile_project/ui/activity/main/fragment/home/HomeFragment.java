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
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.android.mobile_project.R;
import com.android.mobile_project.data.remote.model.HabitInWeekModel;
import com.android.mobile_project.data.remote.model.HabitModel;
import com.android.mobile_project.databinding.FragmentHomeBinding;
import com.android.mobile_project.ui.InitLayout;
import com.android.mobile_project.ui.activity.count.CountDownActivity;
import com.android.mobile_project.ui.activity.create.CreateHabitActivity;
import com.android.mobile_project.ui.activity.main.CounterStepActivity;
import com.android.mobile_project.ui.activity.main.MainActivity;
import com.android.mobile_project.ui.activity.main.fragment.home.adapter.DoneHabitAdapter;
import com.android.mobile_project.ui.activity.main.fragment.home.adapter.FailedHabitAdapter;
import com.android.mobile_project.ui.activity.main.fragment.home.adapter.HabitAdapter;
import com.android.mobile_project.ui.activity.setting.HabitSettingActivity;
import com.android.mobile_project.utils.dagger.component.sub.main.fragment.HomeComponent;
import com.android.mobile_project.utils.time.adapter.DailyCalendarAdapter;
import com.android.mobile_project.utils.time.utils.TimeUtils;

import java.time.LocalDate;
import java.util.Optional;

import javax.inject.Inject;

@RequiresApi(api = Build.VERSION_CODES.O)
public class HomeFragment extends Fragment implements InitLayout, View.OnClickListener {

    private static final String TAG = HomeFragment.class.getSimpleName();

    private FragmentHomeBinding binding;

    public HomeComponent component;

    private final TimeUtils utils = TimeUtils.getInstance();

    @Inject
    HomeViewModel viewModel;

    private static final String VAL_TRUE = "true";

    private static final String VAL_FALSE = "false";

    private static final String VAL_NULL = "null";

    @Override
    public void onAttach(@NonNull Context context) {
        Log.i("HomeFragment", "onAttach");
        component = ((MainActivity) getActivity()).component.mHomeComponent().create();
        component.inject(this);
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        View v = initContentView();
        initViewModel();
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated: ");
        initView();
        initDailyCalendar();
        initObserve();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void initViewModel() {
        binding.setVm(viewModel);
    }

    @Override
    public void onResume() {
        super.onResume();
        initHabitList(utils.getDateTodayString());
        if (viewModel.isSelectedToday()) {
            initHabitList(utils.getDateTodayString());
            Log.d(TAG, "onResume: isToday");
        } else {
            initHabitList(viewModel.getCalendarBarDate());
            Log.d(TAG, "onResume: notToday");
        }
    }

    private void initView() {
        viewModel.setDate(binding.tvDate);
        viewModel.setMonth(binding.titleMonth);
        viewModel.getCurrentDayOfWeek();
        viewModel.setCalendarBarDate(utils.getDateTodayString());
        viewModel.setOnClickItem(new DailyCalendarAdapter.OnClickItem() {
            @Override
            public void onClick(View view, String date) {
                if (date.equalsIgnoreCase(viewModel.getCalendarBarDate())) {
                    return;
                }
                viewModel.setCalendarBarDate(date);
                HomeFragment.this.initHabitList(date);
            }
        });
    }

    private void initDailyCalendar() {
        viewModel.setDays(utils.getSixtyDaysArray());

        viewModel.dailyCalendarAdapter = new DailyCalendarAdapter(getContext(), viewModel.getDays(), viewModel.getOnClickItem());
        viewModel.dailyCalendarAdapter.notifyDataSetChanged();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        layoutManager.scrollToPosition(utils.getPositionOfTodayInArray());
        binding.rvHorCalendar.setLayoutManager(layoutManager);
        binding.rvHorCalendar.setAdapter(viewModel.dailyCalendarAdapter);
        //binding.rvHorCalendar.smoothScrollToPosition(viewModel.getDays().size() / 2 + 1);
        SnapHelper helper = new LinearSnapHelper();
        helper.attachToRecyclerView(binding.rvHorCalendar);
        binding.titleDate.setOnClickListener(view -> {
            binding.rvHorCalendar.smoothScrollToPosition(utils.getPositionOfTodayInArray());
            if (utils.getDateTodayString().equalsIgnoreCase(viewModel.getCalendarBarDate())) {
                return;
            }
            viewModel.dailyCalendarAdapter.resetDaySelected();
            viewModel.setCalendarBarDate(utils.getDateTodayString());
            initHabitList(utils.getDateTodayString());
        });
    }

    private void initObserve() {
        viewModel.getHabitBeforeLD().observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean) {
                Log.d(TAG, "onChanged: isBefore");
                viewModel.getHabitListByHistoryStatus();
                initHabitModelList();
                initHabitDoneModeList();
                initHabitFailedModelList();
                visibleListHabit();
                viewModel.unDisposable();
            }
        });
        viewModel.getHabitAfterLD().observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean) {
                Log.d(TAG, "onChanged: isAfter" + viewModel.getmHabitInWeekModelList().size());
                viewModel.getHabitListAfterDay(viewModel.getmHabitInWeekModelList());
                initHabitModelList();
                initHabitDoneModeList();
                initHabitFailedModelList();
                visibleListHabit();
                viewModel.unDisposable();
            }
        });
        viewModel.getHabitTodayLD().observe(getViewLifecycleOwner(), isSuccess -> {
            Log.d(TAG, "onChanged: isToday");
            if (isSuccess) {
                viewModel.getHabitListByHistoryStatus();
                initHabitModelList();
                initHabitDoneModeList();
                initHabitFailedModelList();
                visibleListHabit();
            }
        });
        viewModel.getInsertHistoryLD().observe(getViewLifecycleOwner(), isSuccess -> {
            Log.d(TAG, "onChanged: insertHistory");
            if (isSuccess) {
                viewModel.getHabitListByHistoryStatus();
                initHabitModelList();
                initHabitDoneModeList();
                initHabitFailedModelList();
                visibleListHabit();
            }
        });
        viewModel.getLiveDataIsSuccess().observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean) {
                visibleListHabit();
            }
        });
        viewModel.getCountDownTimerLD().observe(getViewLifecycleOwner(), new Observer<HabitInWeekModel>() {
            @Override
            public void onChanged(HabitInWeekModel habitInWeekModel) {
                transitionCountDownActivity(habitInWeekModel);
            }
        });
    }

    private void initHabitList(String date) {
        viewModel.clearHabitList();
        viewModel.getHabitAndHistory(date);
    }

    @Override
    public View initContentView() {
        binding = FragmentHomeBinding.inflate(getLayoutInflater());
        binding.setA(this);
        return binding.getRoot();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void initHabitModelList() {
        viewModel.recyclerViewClickListener = (v, habitModelList, position) -> {

            HabitModel model = habitModelList.get(position);

            Intent intent = new Intent(getContext(), HabitSettingActivity.class);
            intent.putExtra("habitId", model.getHabitId());
            startActivity(intent);

        };
        viewModel.setmHabitAdapter(new HabitAdapter(viewModel.getmHabitModelList(), viewModel.getmHabitInWeekModelList(), viewModel.recyclerViewClickListener));
        viewModel.getmHabitAdapter().notifyDataSetChanged();

        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        binding.rcvHabitList.setAdapter(viewModel.getmHabitAdapter());
        binding.rcvHabitList.setLayoutManager(manager);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallbackHabitNullList);
        itemTouchHelper.attachToRecyclerView(binding.rcvHabitList);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void initHabitDoneModeList() {
        viewModel.setmDoneHabitAdapter(new DoneHabitAdapter(viewModel.getmHabitModelDoneList()));
        viewModel.getmDoneHabitAdapter().notifyDataSetChanged();

        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        binding.rcvHabitDoneList.setAdapter(viewModel.getmDoneHabitAdapter());
        binding.rcvHabitDoneList.setLayoutManager(manager);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallbackHabitDoneList);
        itemTouchHelper.attachToRecyclerView(binding.rcvHabitDoneList);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void initHabitFailedModelList() {
        viewModel.setmFailedHabitAdapter(new FailedHabitAdapter(viewModel.getmHabitModelFailedList()));
        viewModel.getmFailedHabitAdapter().notifyDataSetChanged();

        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        binding.rcvHabitFailedList.setAdapter(viewModel.getmFailedHabitAdapter());
        binding.rcvHabitFailedList.setLayoutManager(manager);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallbackHabitFailedList);
        itemTouchHelper.attachToRecyclerView(binding.rcvHabitFailedList);
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();

        switch (id) {
            case R.id.btn_ch:
                clickCreateHabit();
                break;
            case R.id.t_todo:
                if (!viewModel.isHideToDo()) {
                    viewModel.setHideToDo(true);
                    binding.rcvHabitList.setVisibility(View.GONE);
                } else {
                    viewModel.setHideToDo(false);
                    binding.rcvHabitList.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.t_done:
                if (!viewModel.isHideDone()) {
                    viewModel.setHideDone(true);
                    binding.rcvHabitDoneList.setVisibility(View.GONE);
                } else {
                    viewModel.setHideDone(false);
                    binding.rcvHabitDoneList.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.t_failed:
                if (!viewModel.isHideFailed()) {
                    viewModel.setHideFailed(true);
                    binding.rcvHabitFailedList.setVisibility(View.GONE);
                } else {
                    viewModel.setHideFailed(false);
                    binding.rcvHabitFailedList.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.btn_step_counter:
                clickStepCounter();
                break;
        }

    }

    private void clickStepCounter() {
        Intent intent = new Intent(getContext(), CounterStepActivity.class);
        startActivity(intent);
    }

    private void clickCreateHabit() {
        Intent intent = new Intent(getContext(), CreateHabitActivity.class);
        startActivity(intent);
    }

    private void transitionCountDownActivity(HabitInWeekModel habitInWeekModel) {
        Intent intent = new Intent(getContext(), CountDownActivity.class);
        intent.putExtra("habitInWeek", habitInWeekModel);
        startActivity(intent);
    }

    ItemTouchHelper.SimpleCallback simpleCallbackHabitNullList = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            int position = viewHolder.getAdapterPosition();

            switch (direction) {
                case ItemTouchHelper.LEFT:
                    if (viewModel.isSelectedTheDayBefore()) {
                        viewModel.updateHistory(position, HabitAdapter.class, VAL_FALSE, viewModel.getCalendarBarDate(), false);
                    } else if (viewModel.isSelectedToday()) {
                        viewModel.updateHistory(position, HabitAdapter.class, VAL_FALSE, utils.getDateTodayString(), false);
                    }
                    break;
                case ItemTouchHelper.RIGHT:
                    if (viewModel.isSelectedTheDayBefore()) {
                        viewModel.updateHistory(position, HabitAdapter.class, VAL_TRUE, viewModel.getCalendarBarDate(), true);
                    } else if (viewModel.isSelectedToday()) {
                        viewModel.updateHistory(position, HabitAdapter.class, VAL_TRUE, utils.getDateTodayString(), true);
                    }
                    break;

            }

        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX,
                                float dY, int actionState, boolean isCurrentlyActive) {
            if (!LocalDate.parse(viewModel.getCalendarBarDate()).isAfter(utils.getSelectedDate())) {
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                    View itemView = viewHolder.itemView;
                    Paint p = new Paint();
                    Bitmap icon;

                    if (dX < 0) {
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.btn_red_close);
                        p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
                        c.drawBitmap(icon,
                                (float) itemView.getRight() - icon.getWidth() - 10,
                                (float) itemView.getTop() + ((float) itemView.getBottom() - (float) itemView.getTop() - icon.getHeight()) / 2,
                                p
                        );
                    }

                    if (dX > 0) {
                        int position = viewHolder.getAdapterPosition();
                        if (position != -1) {
                            if(viewModel.getmHabitModelList().size() > 0){
                                HabitModel habitModel = viewModel.getmHabitModelList().get(position);
                                Optional<HabitInWeekModel> habitInWeekModel = viewModel.getmHabitInWeekModelList().stream()
                                        .filter(model -> model.getHabitId().equals(habitModel.getHabitId())).findFirst();
                                if(habitInWeekModel.isPresent() && habitInWeekModel.get().getTimerHour() == 0L &&
                                        habitInWeekModel.get().getTimerMinute() == 0L && habitInWeekModel.get().getTimerSecond() == 0L){
                                    icon = BitmapFactory.decodeResource(getResources(), R.drawable.btn_green_check);
                                }else{
                                    icon = BitmapFactory.decodeResource(getResources(), R.drawable.btn_purple_clock);
                                }
                                p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
                                c.drawBitmap(icon,
                                        (float) itemView.getLeft() + 10,
                                        (float) itemView.getTop() + ((float) itemView.getBottom() - (float) itemView.getTop() - icon.getHeight()) / 2,
                                        p);
                            }
                        }
                    }

                    final float alpha = ALPHA_FULL - Math.abs(dX) / (float) viewHolder.itemView.getWidth();
                    viewHolder.itemView.setAlpha(alpha);
                    viewHolder.itemView.setTranslationX(dX);

                } else {
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                }
            }

        }
    };

    ItemTouchHelper.SimpleCallback simpleCallbackHabitDoneList = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            int position = viewHolder.getAdapterPosition();

            switch (direction) {
                case ItemTouchHelper.LEFT:
                    break;
                case ItemTouchHelper.RIGHT:
                    if (viewModel.isSelectedTheDayBefore()) {
                        viewModel.updateHistory(position, DoneHabitAdapter.class, VAL_NULL, viewModel.getCalendarBarDate(), false);
                    } else if (viewModel.isSelectedToday()) {
                        viewModel.updateHistory(position, DoneHabitAdapter.class, VAL_NULL, utils.getDateTodayString(), false);
                    }
                    break;
            }

        }

        @Override
        public void onChildDrawOver(@NonNull Canvas c, @NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        }
    };

    ItemTouchHelper.SimpleCallback simpleCallbackHabitFailedList = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            int position = viewHolder.getAdapterPosition();

            switch (direction) {
                case ItemTouchHelper.LEFT:
                    break;
                case ItemTouchHelper.RIGHT:
                    if (viewModel.isSelectedTheDayBefore()) {
                        viewModel.updateHistory(position, FailedHabitAdapter.class, VAL_NULL, viewModel.getCalendarBarDate(), false);
                    } else if (viewModel.isSelectedToday()) {
                        viewModel.updateHistory(position, FailedHabitAdapter.class, VAL_NULL, utils.getDateTodayString(), false);
                    }
                    break;
            }

        }

        @Override
        public void onChildDrawOver(@NonNull Canvas c, @NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    private void visibleListHabit() {
        if (viewModel.getmHabitModelList().size() == 0) {
            binding.tTodo.setVisibility(View.GONE);
            binding.rcvHabitList.setVisibility(View.GONE);
        } else {
            binding.tTodo.setVisibility(View.VISIBLE);
            binding.rcvHabitList.setVisibility(View.VISIBLE);
        }

        if (viewModel.getmHabitModelDoneList().size() == 0) {
            binding.tDone.setVisibility(View.GONE);
            binding.rcvHabitDoneList.setVisibility(View.GONE);
        } else {
            binding.tDone.setVisibility(View.VISIBLE);
            binding.rcvHabitDoneList.setVisibility(View.VISIBLE);
        }

        if (viewModel.getmHabitModelFailedList().size() == 0) {
            binding.tFailed.setVisibility(View.GONE);
            binding.rcvHabitFailedList.setVisibility(View.GONE);
        } else {
            binding.tFailed.setVisibility(View.VISIBLE);
            binding.rcvHabitFailedList.setVisibility(View.VISIBLE);
        }
    }
}
