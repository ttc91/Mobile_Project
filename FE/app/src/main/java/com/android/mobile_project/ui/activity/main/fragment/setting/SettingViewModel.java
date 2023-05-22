package com.android.mobile_project.ui.activity.main.fragment.setting;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.mobile_project.data.local.DataLocalManager;
import com.android.mobile_project.data.local.sqlite.entity.db.HabitEntity;
import com.android.mobile_project.data.local.sqlite.entity.db.HabitInWeekEntity;
import com.android.mobile_project.data.local.sqlite.entity.db.HistoryEntity;
import com.android.mobile_project.data.local.sqlite.entity.db.RemainderEntity;
import com.android.mobile_project.data.remote.model.HabitInWeekModel;
import com.android.mobile_project.data.remote.model.HabitModel;
import com.android.mobile_project.data.remote.model.HistoryModel;
import com.android.mobile_project.data.remote.model.RemainderModel;
import com.android.mobile_project.data.remote.model.api.JwtResponseModel;
import com.android.mobile_project.data.remote.model.api.base.ResponseModel;
import com.android.mobile_project.data.repository.HabitInWeekRepository;
import com.android.mobile_project.data.repository.HabitRepository;
import com.android.mobile_project.data.repository.HistoryRepository;
import com.android.mobile_project.data.repository.RemainderRepository;
import com.android.mobile_project.data.repository.UserRepository;
import com.android.mobile_project.data.utils.mapper.HabitInWeekMapper;
import com.android.mobile_project.data.utils.mapper.HabitMapper;
import com.android.mobile_project.data.utils.mapper.HistoryMapper;
import com.android.mobile_project.data.utils.mapper.RemainderMapper;
import com.android.mobile_project.ui.activity.base.BaseViewModel;
import com.android.mobile_project.ui.activity.main.fragment.setting.service.ApiService;
import com.android.mobile_project.ui.activity.main.fragment.setting.service.InitService;
import com.android.mobile_project.ui.activity.main.fragment.setting.service.ToastService;
import com.android.mobile_project.utils.dagger.custom.MyCustomAnnotation;
import com.android.mobile_project.utils.worker.NotificationWorker;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import javax.inject.Inject;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@MyCustomAnnotation.MyScope.FragmentScope
public class SettingViewModel extends BaseViewModel {

    private static final String TAG = SettingViewModel.class.getSimpleName();
    private final HabitRepository habitRepository;

    private final HabitInWeekRepository habitInWeekRepository;

    private final HistoryRepository historyRepository;

    private final RemainderRepository remainderRepository;

    private final UserRepository userRepository;

    protected InitService initService;

    protected ToastService toastService;

    protected Boolean isLogin = false;

    private final MutableLiveData<Long> mUserIdMutableLiveData = new MutableLiveData<>();

    protected LiveData<Long> getUserIdMutableLiveData() {
        return mUserIdMutableLiveData;
    }

    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private Context context;

    public void setContext(Context context) {
        this.context = context;
    }

    protected void setDispose() {
        mCompositeDisposable.clear();
        mCompositeDisposable.dispose();
    }


    @Inject
    public SettingViewModel(HabitRepository habitRepository, HabitInWeekRepository habitInWeekRepository, HistoryRepository historyRepository, RemainderRepository remainderRepository, UserRepository userRepository) {
        this.habitRepository = habitRepository;
        this.habitInWeekRepository = habitInWeekRepository;
        this.historyRepository = historyRepository;
        this.remainderRepository = remainderRepository;
        this.userRepository = userRepository;
    }

    @SuppressLint("LongLogTag")
    @RequiresApi(api = Build.VERSION_CODES.N)
    protected void synchronizeToServer(ApiService.SynchronizeToServerResult callback) {
        try {

            habitRepository.getMRemoteHabitDataSource().synchronize().enqueue(new Callback<ResponseModel<HabitModel>>() {
                @Override
                public void onResponse(@NonNull Call<ResponseModel<HabitModel>> call, @NonNull Response<ResponseModel<HabitModel>> response) {
                    Log.i("SettingViewModel-habitSync", "onComplete");
                    habitInWeekRepository.getMRemoteHabitInWeekDataSource().synchronize().enqueue(new Callback<ResponseModel<HabitInWeekModel>>() {
                        @Override
                        public void onResponse(@NonNull Call<ResponseModel<HabitInWeekModel>> call, @NonNull Response<ResponseModel<HabitInWeekModel>> response) {
                            Log.i("SettingViewModel-habitInWeekSync", "onComplete");
                            historyRepository.getMRemoteHistoryDataSource().synchronize().enqueue(new Callback<ResponseModel<HistoryModel>>() {
                                @Override
                                public void onResponse(@NonNull Call<ResponseModel<HistoryModel>> call, @NonNull Response<ResponseModel<HistoryModel>> response) {
                                    Log.i("SettingViewModel-historySync", "onComplete");
                                    remainderRepository.getMRemoteRemainderDataSource().synchronize().enqueue(new Callback<ResponseModel<RemainderModel>>() {
                                        @Override
                                        public void onResponse(@NonNull Call<ResponseModel<RemainderModel>> call, @NonNull Response<ResponseModel<RemainderModel>> response) {
                                            Log.i("SettingViewModel-remainderSync", "onComplete");
                                        }

                                        @Override
                                        public void onFailure(@NonNull Call<ResponseModel<RemainderModel>> call, @NonNull Throwable t) {
                                            Log.e("SettingViewModel-remainderSync", "onFailure", t);
                                        }
                                    });
                                }

                                @Override
                                public void onFailure(@NonNull Call<ResponseModel<HistoryModel>> call, @NonNull Throwable t) {
                                    Log.e("SettingViewModel-historySync", "onFailure", t);
                                }
                            });
                        }

                        @Override
                        public void onFailure(@NonNull Call<ResponseModel<HabitInWeekModel>> call, @NonNull Throwable t) {
                            Log.e("SettingViewModel-habitInWeekSync", "onFailure", t);
                        }
                    });
                }

                @Override
                public void onFailure(@NonNull Call<ResponseModel<HabitModel>> call, @NonNull Throwable t) {
                    Log.e("SettingViewModel-habitSync", "onFailure", t);
                }
            });

            callback.onSynchronizeToServerSuccess();
        } catch (Exception e) {
            Log.e("onSynchronizeToServerFailure", e.getMessage());
            callback.onSynchronizeToServerFailure();
        }
    }

    private void requestSignInToServer() {
        Log.d(TAG, "requestSignInToServer: ");
        userRepository.getMRemoteUserDataSource().signIn().enqueue(new Callback<ResponseModel<JwtResponseModel>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(@NonNull Call<ResponseModel<JwtResponseModel>> call, @NonNull Response<ResponseModel<JwtResponseModel>> response) {
                Log.i("SettingViewModel - signIn", "onComplete");
                DataLocalManager.getInstance().setToken(response.body().getObject().getToken());
                deleteAllDB();
                loadAllDataFromServer();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(@NonNull Call<ResponseModel<JwtResponseModel>> call, @NonNull Throwable t) {
                Log.e("SettingViewModel - signIn", "onFailure");
            }
        });
    }

    protected void getUserIdByName(String username) {
        Log.d(TAG, "getUserIdByName: ");
        mCompositeDisposable.add(
                userRepository.getMUserDataSource().getUserIdByName(username)
                        .observeOn(Schedulers.single())
                        .subscribeOn(Schedulers.io())
                        .subscribe(aLong -> {
                            Log.i(TAG, "getUserIdByName onSuccess");
                            mUserIdMutableLiveData.postValue(aLong);
                            requestSignInToServer();
                        }, throwable -> {
                            Log.e(TAG, "getUserIdByName onError", throwable);
                        })
        );

    }

    private List<HabitModel> habitModels = new ArrayList<>();
    private List<HabitInWeekModel> habitInWeekModels = new ArrayList<>();
    private List<HistoryModel> historyModels = new ArrayList<>();
    private List<RemainderModel> remainderModels = new ArrayList<>();

    private final CountDownLatch mRemoteLatch = new CountDownLatch(4);
    private final CountDownLatch mLocalLatch = new CountDownLatch(4);

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void loadHabitFromServer() {
        Long userId = mUserIdMutableLiveData.getValue();
        habitRepository.getMRemoteHabitDataSource().getAllHabit()
                .observeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .subscribe(new CustomSingleObserver<ResponseModel<HabitModel>>() {
                    @Override
                    public void onSuccess(@NonNull ResponseModel<HabitModel> habitModelResponseModel) {
                        if (habitModelResponseModel.getObjectList().size() == 0) {
                            mRemoteLatch.countDown();
                            return;
                        }
                        Log.d(TAG, "onSuccess: habitRepository" + habitModelResponseModel.getMessage() + habitModelResponseModel.getStatusCode());
                        //latch.countDown();
                        habitModels = (List<HabitModel>) habitModelResponseModel.getObjectList();
                        habitModels.forEach(item -> {
                            item.setUserId(userId);
                            item.setDayOfTimeId(item.getDateOfTime());
                        });
                        mRemoteLatch.countDown();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: " + e.getMessage());
                        mRemoteLatch.countDown();
                        super.onError(e);
                    }
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void loadHabitInWeekFromServer() {
        Long userId = mUserIdMutableLiveData.getValue();
        habitInWeekRepository.getMRemoteHabitInWeekDataSource()
                .getAllHabitInWeek()
                .observeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .subscribe(new CustomSingleObserver<ResponseModel<HabitInWeekModel>>() {
                    @Override
                    public void onSuccess(@NonNull ResponseModel<HabitInWeekModel> models) {
                        if (models.getObjectList().size() == 0) {
                            mRemoteLatch.countDown();
                            return;
                        }
                        Log.d(TAG, "onSuccess: habitInWeekRepository");
                        //latch.countDown();
                        habitInWeekModels = (List<HabitInWeekModel>) models.getObjectList();
                        habitInWeekModels.forEach(item -> {
                            item.setUserId(userId);
                        });
                        mRemoteLatch.countDown();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mRemoteLatch.countDown();
                        super.onError(e);
                    }
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void loadHistoryFromServer() {
        Long userId = mUserIdMutableLiveData.getValue();
        historyRepository.getMRemoteHistoryDataSource()
                .getAllHistory()
                .observeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .subscribe(new CustomSingleObserver<ResponseModel<HistoryModel>>() {
                    @Override
                    public void onSuccess(@NonNull ResponseModel<HistoryModel> models) {
                        if (models.getObjectList().size() == 0) {
                            mRemoteLatch.countDown();
                            return;
                        }
                        Log.d(TAG, "onSuccess: historyRepository");
                        historyModels = (List<HistoryModel>) models.getObjectList();
                        long i = 1L;
                        for (HistoryModel history : historyModels) {
                            history.setHistoryId(i);
                            history.setUserId(userId);
                            i++;
                        }
                        mRemoteLatch.countDown();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mRemoteLatch.countDown();
                        super.onError(e);
                    }
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void loadReminderFromServer() {
        //Long userId = mUserIdMutableLiveData.getValue();
        remainderRepository.getMRemoteRemainderDataSource()
                .getAllReminder()
                .observeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .subscribe(new CustomSingleObserver<ResponseModel<RemainderModel>>() {
                    @Override
                    public void onSuccess(@NonNull ResponseModel<RemainderModel> models) {
                        if (models.getObjectList().size() == 0) {
                            mRemoteLatch.countDown();
                            return;
                        }
                        Log.d(TAG, "onSuccess: remainderRepository ");
                        remainderModels = (List<RemainderModel>) models.getObjectList();
                        long i = 1L;
                        for (RemainderModel remind : remainderModels) {
                            remind.setRemainderId(i);
                            i++;
                        }
                        mRemoteLatch.countDown();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mRemoteLatch.countDown();
                        super.onError(e);
                    }
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void insertAllDataIntoDB() {
        habitRepository.getMHabitDataSource()
                .insertAll(HabitMapper.getInstance().mapToListEntity(habitModels)
                        .toArray(new HabitEntity[0]))
                .observeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .subscribe(new CustomCompletableObserver() {
                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: habitRepository");
                        mLocalLatch.countDown();
                        habitInWeekRepository.getMHabitInWeekDataSource()
                                .insertAll(HabitInWeekMapper.getInstance().mapToListEntity(habitInWeekModels)
                                        .toArray(new HabitInWeekEntity[0]))
                                .observeOn(Schedulers.newThread())
                                .observeOn(Schedulers.newThread())
                                .subscribe(new CustomCompletableObserver() {
                                    @Override
                                    public void onComplete() {
                                        Log.d(TAG, "onComplete: habitInWeekRepository");
                                        mLocalLatch.countDown();
                                    }
                                });

                        historyRepository.getMHistoryDataSource()
                                .insertAll(HistoryMapper.getInstance().mapToListEntity(historyModels)
                                        .toArray(new HistoryEntity[0]))
                                .observeOn(Schedulers.newThread())
                                .observeOn(Schedulers.newThread())
                                .subscribe(new CustomCompletableObserver() {
                                    @Override
                                    public void onComplete() {
                                        Log.d(TAG, "onComplete: historyRepository");
                                        mLocalLatch.countDown();
                                    }
                                });

                        remainderRepository.getMRemainderDataSource()
                                .insertAll(RemainderMapper.getInstance().mapToListEntity(remainderModels)
                                        .toArray(new RemainderEntity[0]))
                                .observeOn(Schedulers.newThread())
                                .observeOn(Schedulers.newThread())
                                .subscribe(new CustomCompletableObserver() {
                                    @Override
                                    public void onComplete() {
                                        Log.d(TAG, "onComplete: remainderRepository");
                                        mLocalLatch.countDown();
                                    }
                                });
                    }
                });
        NotificationWorker.cancelAllWorkers(context);
        for (RemainderModel remainderModel: remainderModels) {
            HabitModel habitModel = new HabitModel();
            List<HabitInWeekModel> habitInWeekModelList = new ArrayList<>();
            for (HabitModel habitModel1: habitModels) {
                if (habitModel1.getHabitId().equals(remainderModel.getHabitId())) {
                    habitModel = habitModel1;
                }
            }
            for (HabitInWeekModel habitInWeekModel: habitInWeekModels) {
                if (habitInWeekModel.getHabitId().equals(remainderModel.getHabitId())) {
                    habitInWeekModelList.add(habitInWeekModel);
                }
            }
            NotificationWorker.enqueueWorkerWithHabit(context, habitModel, remainderModel, habitInWeekModelList);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void loadAllDataFromServer() {

        loadHabitFromServer();
        loadHabitInWeekFromServer();
        loadHistoryFromServer();
        loadReminderFromServer();

        Log.d(TAG, "loadAllDataFromServer: before");
        try {
            mRemoteLatch.await();
            insertAllDataIntoDB();
            mLocalLatch.await();
            mLiveDataIsSuccess.postValue(true);
            Log.d(TAG, "loadAllDataFromServer: after await");
        } catch (InterruptedException e) {
            mLiveDataOnError.postValue(e);
            throw new RuntimeException(e);
        }

    }

    private void deleteAllDB() {
        habitInWeekRepository.getMHabitInWeekDataSource()
                .deleteAll()
                .observeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .subscribe(new CustomCompletableObserver() {
                    @Override
                    public void onComplete() {
                        Log.d(TAG, "deleteAll: habitInWeekRepository");
                    }
                });
        historyRepository.getMHistoryDataSource()
                .deleteAll()
                .observeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .subscribe(new CustomCompletableObserver() {
                    @Override
                    public void onComplete() {
                        Log.d(TAG, "deleteAll: historyRepository");
                    }
                });

        remainderRepository.getMRemainderDataSource()
                .deleteAll()
                .observeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .subscribe(new CustomCompletableObserver() {
                    @Override
                    public void onComplete() {
                        Log.d(TAG, "deleteAll: remainderRepository");
                    }
                });
        habitRepository.getMHabitDataSource()
                .deleteAll()
                .observeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .subscribe(new CustomCompletableObserver() {
                    @Override
                    public void onComplete() {
                        Log.d(TAG, "deleteAll: habitRepository");
                    }
                });
    }

}
