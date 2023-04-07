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
import com.android.mobile_project.data.remote.model.HistoryModel;
import com.android.mobile_project.databinding.FragmentHomeBinding;
import com.android.mobile_project.ui.activity.create.CreateHabitActivity;
import com.android.mobile_project.ui.activity.main.MainActivity;
import com.android.mobile_project.ui.activity.main.fragment.home.service.DbService;
import com.android.mobile_project.ui.activity.setting.HabitSettingActivity;
import com.android.mobile_project.utils.dagger.component.sub.main.fragment.HomeComponent;
import com.android.mobile_project.utils.time.adapter.DailyCalendarAdapter;
import com.android.mobile_project.utils.time.utils.TimeUtils;
import com.android.mobile_project.ui.InitLayout;
import com.android.mobile_project.ui.activity.main.fragment.home.adapter.AfterAdapter;
import com.android.mobile_project.ui.activity.main.fragment.home.adapter.BeforeAdapter;
import com.android.mobile_project.ui.activity.main.fragment.home.adapter.DoneHabitAdapter;
import com.android.mobile_project.ui.activity.main.fragment.home.adapter.FailedHabitAdapter;
import com.android.mobile_project.ui.activity.main.fragment.home.adapter.HabitAdapter;
import com.android.mobile_project.ui.activity.main.fragment.home.service.InitUIService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class HomeFragment extends Fragment implements InitLayout, View.OnClickListener {

    private static final String TAG = HomeFragment.class.getSimpleName();

    private FragmentHomeBinding binding;

    public HomeComponent component;

    @Inject
    HomeViewModel viewModel;

    private static final String DAY_FORMAT = "yyyy-MM-dd";

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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.i("HomeFragment", "onCreateView");

        View v = initContentView();
        initViewModel();

        return v;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.i("HomeFragment", "onViewCreated");

        viewModel.setDate(binding.tvDate);
        viewModel.setMonth(binding.titleMonth);
        viewModel.getCurrentDayOfWeek();
        Log.d(TAG, "getCurrentDayOfWeek: " + viewModel.getDayOfWeekId());
        initDailyCalendar();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onResume() {
        super.onResume();
        initHabitToday();
    }

    /*
     * Khởi tạo danh sách Habit ngày hôm nay
     * */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initHabitToday() {
        //Gọi đồng thời load Habit, Habit In Week, History
        String historyTime = LocalDate.now().format(DateTimeFormatter.ofPattern(DAY_FORMAT));
        viewModel.getHabitAndHistory(viewModel.getDayOfWeekId(), historyTime);
        viewModel.getLiveDataIsSuccess().observe(getViewLifecycleOwner(), isSuccess -> {
            if (isSuccess) {
                Log.d(TAG, "getHabitAndHistory history: " + viewModel.getHistoryModels().size()
                        + " -- habitinweek: " + viewModel.getHabitInWeekModelList().size()
                        + " -- habit: " + viewModel.getHabitsOfUser().size());
                if (viewModel.getHistoryModels().size() < viewModel.getHabitInWeekModelList().size()) {
                    viewModel.insertHistoriesList(historyTime, viewModel.getHistoryModels(), viewModel.getHabitInWeekModelList());
                    viewModel.getHistoryInsertLiveData().observe(getViewLifecycleOwner(), new Observer<List<HistoryModel>>() {
                        @Override
                        public void onChanged(List<HistoryModel> historyModels1) {
                            viewModel.getHabitListByHistoryStatus(historyModels1, viewModel.getHabitsOfUser());
                            initHabitModelList();
                            initHabitDoneModeList();
                            initHabitFailedModelList();
                        }
                    });
                } else {
                    viewModel.getHabitListByHistoryStatus(viewModel.getHistoryModels(), viewModel.getHabitsOfUser());
                    initHabitModelList();
                    initHabitDoneModeList();
                    initHabitFailedModelList();
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initDailyCalendar() {
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
    public View initContentView() {
        binding = FragmentHomeBinding.inflate(getLayoutInflater());
        binding.setA(this);
        return binding.getRoot();
    }

    @SuppressLint("NotifyDataSetChanged")
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initHabitModelList() {
        if (viewModel.getHabitModelList() == null || viewModel.getHabitModelList().isEmpty()) {
            return;
        }
        Log.d(TAG, "initHabitModelList: " + viewModel.getHabitModelList().size());

        viewModel.recyclerViewClickListener = (v, habitModelList, position) -> {

            HabitModel model = habitModelList.get(position);

            Intent intent = new Intent(getContext(), HabitSettingActivity.class);
            intent.putExtra("habitId", model.getHabitId());
            startActivity(intent);

        };

        viewModel.setmHabitAdapter(new HabitAdapter(getContext(), viewModel.getHabitModelList(), viewModel.recyclerViewClickListener));
        viewModel.getmHabitAdapter().notifyDataSetChanged();

        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        binding.rcvHabitList.setAdapter(viewModel.getmHabitAdapter());
        binding.rcvHabitList.setLayoutManager(manager);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallbackHabitNullList);
        itemTouchHelper.attachToRecyclerView(binding.rcvHabitList);
        if (viewModel.getHabitModelList().size() > 0) {
            binding.tTodo.setVisibility(View.VISIBLE);
            binding.rcvHabitList.setVisibility(View.VISIBLE);
        } else {
            binding.tTodo.setVisibility(View.GONE);
            binding.rcvHabitList.setVisibility(View.GONE);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initHabitDoneModeList() {
        if (viewModel.getHabitModelDoneList() == null || viewModel.getHabitModelDoneList().isEmpty()) {
            return;
        }
        Log.d(TAG, "initHabitDoneModeList: " + viewModel.getHabitModelDoneList().size());
        viewModel.setDoneHabitAdapter(new DoneHabitAdapter(getContext(), viewModel.getHabitModelDoneList()));
        viewModel.getDoneHabitAdapter().notifyDataSetChanged();

        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        binding.rcvHabitDoneList.setAdapter(viewModel.getDoneHabitAdapter());
        binding.rcvHabitDoneList.setLayoutManager(manager);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallbackHabitDoneList);
        itemTouchHelper.attachToRecyclerView(binding.rcvHabitDoneList);
        if (viewModel.getHabitModelDoneList().size() > 0) {
            binding.tDone.setVisibility(View.VISIBLE);
            binding.rcvHabitDoneList.setVisibility(View.VISIBLE);
        } else {
            binding.tDone.setVisibility(View.GONE);
            binding.rcvHabitDoneList.setVisibility(View.GONE);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initHabitFailedModelList() {
        if (viewModel.getHabitModelFailedList() == null || viewModel.getHabitModelFailedList().isEmpty()) {
            return;
        }
        Log.d(TAG, "initHabitFailedModelList: " + viewModel.getHabitModelFailedList().size());
        viewModel.setFailedHabitAdapter(new FailedHabitAdapter(viewModel.getHabitModelFailedList(), getContext()));
        viewModel.getFailedHabitAdapter().notifyDataSetChanged();

        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        binding.rcvHabitFailedList.setAdapter(viewModel.getFailedHabitAdapter());
        binding.rcvHabitFailedList.setLayoutManager(manager);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallbackHabitFailedList);
        itemTouchHelper.attachToRecyclerView(binding.rcvHabitFailedList);

        if (viewModel.getHabitModelFailedList().size() > 0) {
            binding.tFailed.setVisibility(View.VISIBLE);
            binding.rcvHabitFailedList.setVisibility(View.VISIBLE);
        } else {
            binding.tFailed.setVisibility(View.GONE);
            binding.rcvHabitFailedList.setVisibility(View.GONE);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initHabitBeforeModelList() {
        viewModel.setHabitModelBeforeList(new ArrayList<>());
        //viewModel.setBeforeAdapter(new BeforeAdapter(viewModel.getHabitModelBeforeList(), null));
        viewModel.getBeforeAdapter().notifyDataSetChanged();

        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        binding.rcvBefore.setAdapter(viewModel.getBeforeAdapter());
        binding.rcvBefore.setLayoutManager(manager);

        if (viewModel.getHabitModelBeforeList().size() > 0) {
            binding.rcvBefore.setVisibility(View.VISIBLE);
        } else {
            binding.rcvBefore.setVisibility(View.GONE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initHabitAfterModelList(List<HabitModel> habitModels) {
        //viewModel.setHabitModelAfterList(new ArrayList<>());
        viewModel.setAfterAdapter(new AfterAdapter(habitModels));
        viewModel.getAfterAdapter().notifyDataSetChanged();

        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        binding.rcvAfter.setAdapter(viewModel.getAfterAdapter());
        binding.rcvAfter.setLayoutManager(manager);

        if (habitModels.size() > 0) {
            binding.rcvAfter.setVisibility(View.VISIBLE);
        } else {
            binding.rcvAfter.setVisibility(View.GONE);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void initViewModel() {

        binding.setVm(viewModel);

        viewModel.setOnClickItem((view, date) -> {

            TimeUtils utils = new TimeUtils();
            if (LocalDate.parse(date).isBefore(utils.getSelectedDate()) ||
                    LocalDate.parse(date).isAfter(utils.getSelectedDate())) {
                Log.d(TAG, "setOnClickItem: diff today" + date);
                
                viewModel.getHabitsWhenClickDailyCalendar1(date);
                //Before
                viewModel.getHistoryBeforeLD().observe(getViewLifecycleOwner(), historyModels -> {
                    if (historyModels.size() > 0) {
                        initHabitModelList();
                        initHabitDoneModeList();
                        initHabitFailedModelList();
                    }
                });

                //After
                viewModel.getHabitAfterLD().observe(getViewLifecycleOwner(), habitInWeekModels -> {
                    if (habitInWeekModels.size() > 0) {
                        viewModel.getHabitListAfterDay(habitInWeekModels);
                        initHabitModelList();
                        initHabitDoneModeList();
                        initHabitFailedModelList();
                    }
                });
                binding.tTodo.setVisibility(View.GONE);
                binding.tFailed.setVisibility(View.GONE);
                binding.tDone.setVisibility(View.GONE);
                binding.rcvHabitList.setVisibility(View.GONE);
                binding.rcvHabitFailedList.setVisibility(View.GONE);
                binding.rcvHabitDoneList.setVisibility(View.GONE);
            } else {
                Log.d(TAG, "initViewModel: TOday");
//                binding.tTodo.setVisibility(View.VISIBLE);
//                binding.tFailed.setVisibility(View.VISIBLE);
//                binding.tDone.setVisibility(View.VISIBLE);
//                binding.rcvHabitList.setVisibility(View.VISIBLE);
//                binding.rcvHabitFailedList.setVisibility(View.VISIBLE);
//                binding.rcvHabitDoneList.setVisibility(View.VISIBLE);
                initHabitToday();
            }

        });


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View view) {

        int id = view.getId();

        if (id == R.id.btn_ch) {
            clickCreateHabit();
        } else if (id == R.id.t_todo) {
            if (!viewModel.isHideToDo()) {
                viewModel.setHideToDo(true);
                binding.rcvHabitList.setVisibility(View.GONE);
            } else {
                viewModel.setHideToDo(false);
                binding.rcvHabitList.setVisibility(View.VISIBLE);
            }
        } else if (id == R.id.t_done) {
            if (!viewModel.isHideDone()) {
                viewModel.setHideDone(true);
                binding.rcvHabitDoneList.setVisibility(View.GONE);
            } else {
                viewModel.setHideDone(false);
                binding.rcvHabitDoneList.setVisibility(View.VISIBLE);
            }
        } else if (id == R.id.t_failed) {
            if (!viewModel.isHideFailed()) {
                viewModel.setHideFailed(true);
                binding.rcvHabitFailedList.setVisibility(View.GONE);
            } else {
                viewModel.setHideFailed(false);
                binding.rcvHabitFailedList.setVisibility(View.VISIBLE);
            }
        }

    }

    private void clickCreateHabit() {
        Intent intent = new Intent(getContext(), CreateHabitActivity.class);
        startActivity(intent);
    }

    ItemTouchHelper.SimpleCallback simpleCallbackHabitNullList = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

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
                    viewModel.updateHistory(position, HabitAdapter.class, VAL_FALSE);
                    break;

                case ItemTouchHelper.RIGHT:
                    viewModel.updateHistory(position, HabitAdapter.class, VAL_TRUE);
                    break;

            }

        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX,
                                float dY, int actionState, boolean isCurrentlyActive) {

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

//                        HabitModel habitModel = viewModel.getHabitModelList().get(position);
//                        List<HabitInWeekModel> models = viewModel.getDayOfWeekHabitListByUserAndHabitId(habitModel.getHabitId());
//
//                        HabitInWeekModel model = models.get(0);
//
//                        if(model.getTimerSecond() == null && model.getTimerMinute() == null && model.getTimerHour() == null){
//                            icon = BitmapFactory.decodeResource(getResources(), R.drawable.btn_green_check);
//
//                            p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
//
//                            c.drawBitmap(icon,
//                                    (float) itemView.getLeft() + 10,
//                                    (float) itemView.getTop() + ((float) itemView.getBottom() - (float) itemView.getTop() - icon.getHeight())/2,
//                                    p);
//                        }else {
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.btn_purple_clock);

                        p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));

                        c.drawBitmap(icon,
                                (float) itemView.getLeft() + 10,
                                (float) itemView.getTop() + ((float) itemView.getBottom() - (float) itemView.getTop() - icon.getHeight()) / 2,
                                p);
//                        }

                    }
                }

                final float alpha = ALPHA_FULL - Math.abs(dX) / (float) viewHolder.itemView.getWidth();
                viewHolder.itemView.setAlpha(alpha);
                viewHolder.itemView.setTranslationX(dX);

            } else {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }


        }
    };

    ItemTouchHelper.SimpleCallback simpleCallbackHabitDoneList = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
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
                    viewModel.updateHistory(position, DoneHabitAdapter.class, VAL_NULL);
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
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            int position = viewHolder.getAdapterPosition();

            switch (direction) {
                case ItemTouchHelper.LEFT:
                    break;
                case ItemTouchHelper.RIGHT:
                    viewModel.updateHistory(position, FailedHabitAdapter.class, VAL_NULL);
                    break;
            }

        }

        @Override
        public void onChildDrawOver(@NonNull Canvas c, @NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

}
