package com.android.mobile_project.ui.activity.login;

import android.os.Build;
import android.os.Handler;
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
import com.android.mobile_project.data.remote.model.UserModel;
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
import com.android.mobile_project.data.utils.mapper.UserMapper;
import com.android.mobile_project.ui.activity.base.BaseViewModel;
import com.android.mobile_project.ui.activity.login.service.DbService;
import com.android.mobile_project.ui.activity.login.service.InitService;
import com.android.mobile_project.ui.activity.login.service.ToastService;
import com.android.mobile_project.utils.dagger.custom.MyCustomAnnotation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@MyCustomAnnotation.MyScope.ActivityScope
public class LoginViewModel extends BaseViewModel {

    private final HabitRepository habitRepository;

    private final HabitInWeekRepository habitInWeekRepository;

    private final HistoryRepository historyRepository;

    private final RemainderRepository remainderRepository;
    private final UserRepository mUserRepository;

    private final MutableLiveData<Long> mUserIdMutableLiveData = new MutableLiveData<>();

    protected LiveData<Long> getUserIdMutableLiveData() {
        return mUserIdMutableLiveData;
    }

    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    protected void setDispose() {
        mCompositeDisposable.clear();
        mCompositeDisposable.dispose();
    }

    @Inject
    public LoginViewModel(HabitRepository habitRepository, HabitInWeekRepository habitInWeekRepository, HistoryRepository historyRepository, RemainderRepository remainderRepository, UserRepository mUserRepository) {
        this.habitRepository = habitRepository;
        this.habitInWeekRepository = habitInWeekRepository;
        this.historyRepository = historyRepository;
        this.remainderRepository = remainderRepository;
        this.mUserRepository = mUserRepository;
    }

    protected ToastService toastService;

    protected DbService dbService;

    protected InitService initService;

    protected void insertUser(UserModel userModel, DbService.InsertUserResult callback) {
        mCompositeDisposable.add(
                mUserRepository.getMUserDataSource().insert(UserMapper.getInstance().mapToEntity(userModel))
                        .observeOn(Schedulers.single())
                        .subscribeOn(Schedulers.io())
                        .subscribe(() -> {
                            Log.i("insertUser", "onComplete");
                            callback.onInsertUserSuccess();
                        }, throwable -> {
                            Log.e("insertUser", "onError", throwable);
                            callback.onInsertUserFailure();
                        })
        );
    }

    protected void getUserIdByName(String username, DbService.GetUserIdFromLocalResult callback) {

        mCompositeDisposable.add(
                mUserRepository.getMUserDataSource().getUserIdByName(username)
                        .observeOn(Schedulers.single())
                        .subscribeOn(Schedulers.io())
                        .subscribe(aLong -> {
                            Log.i("getUserIdByName", "onSuccess");
                            mUserIdMutableLiveData.postValue(aLong);
                            callback.onGetIdSuccess();
                        }, throwable -> {
                            Log.e("getUserIdByName", "onError", throwable);
                            callback.onGetIdFailure();
                        })
        );

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    protected void requestSignInToServer() {
        mUserRepository.getMRemoteUserDataSource().signIn().enqueue(new Callback<ResponseModel<JwtResponseModel>>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel<JwtResponseModel>> call, @NonNull Response<ResponseModel<JwtResponseModel>> response) {
                Log.i("LoginViewModel - signIn", "onComplete");
                DataLocalManager.getInstance().setToken(response.body().getObject().getToken());
                loadAllDataFromServer();
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel<JwtResponseModel>> call, @NonNull Throwable t) {
                Log.e("LoginViewModel - signIn", "onFailure");
            }
        });
    }

    private List<HabitEntity> habitModels = new ArrayList<>();
    private List<HabitInWeekEntity> habitInWeekModels = new ArrayList<>();
    private List<HistoryEntity> historyModels = new ArrayList<>();
    private List<RemainderEntity> remainderModels = new ArrayList<>();

    private final CountDownLatch latch = new CountDownLatch(3);

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void loadHabitFromServerIntoDB() {
        Long userId = mUserIdMutableLiveData.getValue();
        habitRepository.getMRemoteHabitDataSource().getAllHabit()
                .observeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .subscribe(new CustomSingleObserver<ResponseModel<HabitModel>>() {
                    @Override
                    public void onSuccess(@NonNull ResponseModel<HabitModel> habitModelResponseModel) {
                        if (habitModelResponseModel.getObjectList().size() == 0) {
                            latch.countDown();
                            latch.countDown();
                            return;
                        }
                        Log.d(TAG, "onSuccess: habitRepository" + habitModelResponseModel.getMessage() + habitModelResponseModel.getStatusCode());
                        //latch.countDown();
                        List<HabitModel> habitList = (List<HabitModel>) habitModelResponseModel.getObjectList();
                        habitList.forEach(item -> {
                            item.setUserId(userId);
                            item.setDayOfTimeId(item.getDateOfTime());
                        });
                        habitModels = HabitMapper.getInstance().mapToListEntity(habitList);
                        habitRepository.getMHabitDataSource()
                                .insertAll(habitModels.toArray(new HabitEntity[0]))
                                .observeOn(Schedulers.newThread())
                                .observeOn(Schedulers.newThread())
                                .subscribe(new CustomCompletableObserver() {
                                    @Override
                                    public void onComplete() {
                                        Log.d(TAG, "onComplete: habitRepository");
                                        //loadHabitInWeekFromServerIntoDB();
                                        loadHistoryFromServerIntoDB();
                                        loadReminderFromServerIntoDB();
                                    }
                                });
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: " + e.getMessage());
                        latch.countDown();
                        latch.countDown();
                        super.onError(e);
                    }
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void loadHabitInWeekFromServerIntoDB() {
        Long userId = mUserIdMutableLiveData.getValue();
        habitInWeekRepository.getMRemoteHabitInWeekDataSource()
                .getAllHabitInWeek()
                .observeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .subscribe(new CustomSingleObserver<ResponseModel<HabitInWeekModel>>() {
                    @Override
                    public void onSuccess(@NonNull ResponseModel<HabitInWeekModel> models) {
                        if (models.getObjectList().size() == 0) {
                            latch.countDown();
                            return;
                        }
                        Log.d(TAG, "onSuccess: habitInWeekRepository");
                        //latch.countDown();
                        List<HabitInWeekModel> habitInWeekList = (List<HabitInWeekModel>) models.getObjectList();
                        habitInWeekList.forEach(item -> {
                            item.setUserId(userId);
                        });
                        habitInWeekModels = HabitInWeekMapper.getInstance().mapToListEntity(habitInWeekList);
                        habitInWeekRepository.getMHabitInWeekDataSource()
                                .insertAll(habitInWeekModels.toArray(new HabitInWeekEntity[0]))
                                .observeOn(Schedulers.newThread())
                                .observeOn(Schedulers.newThread())
                                .subscribe(new CustomCompletableObserver() {
                                    @Override
                                    public void onComplete() {
                                        Log.d(TAG, "onComplete: habitInWeekRepository");
                                        latch.countDown();
                                    }
                                });
                    }

                    @Override
                    public void onError(Throwable e) {
                        latch.countDown();
                        super.onError(e);
                    }
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void loadHistoryFromServerIntoDB() {
        Long userId = mUserIdMutableLiveData.getValue();
        historyRepository.getMRemoteHistoryDataSource()
                .getAllHistory()
                .observeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .subscribe(new CustomSingleObserver<ResponseModel<HistoryModel>>() {
                    @Override
                    public void onSuccess(@NonNull ResponseModel<HistoryModel> models) {
                        if (models.getObjectList().size() == 0) {
                            latch.countDown();
                            return;
                        }
                        Log.d(TAG, "onSuccess: historyRepository");
                        List<HistoryModel> historyList = (List<HistoryModel>) models.getObjectList();
                        long i = 1L;
                        for (HistoryModel history : historyList) {
                            history.setHistoryId(i);
                            history.setUserId(userId);
                            i++;
                        }
                        historyModels = HistoryMapper.getInstance().mapToListEntity(historyList);
                        historyRepository.getMHistoryDataSource()
                                .insertAll(historyModels.toArray(new HistoryEntity[0]))
                                .observeOn(Schedulers.newThread())
                                .observeOn(Schedulers.newThread())
                                .subscribe(new CustomCompletableObserver() {
                                    @Override
                                    public void onComplete() {
                                        Log.d(TAG, "onComplete: historyRepository");
                                        latch.countDown();
                                    }
                                });
                    }

                    @Override
                    public void onError(Throwable e) {
                        latch.countDown();
                        super.onError(e);
                    }
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void loadReminderFromServerIntoDB() {
        //Long userId = mUserIdMutableLiveData.getValue();
        remainderRepository.getMRemoteRemainderDataSource()
                .getAllReminder()
                .observeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .subscribe(new CustomSingleObserver<ResponseModel<RemainderModel>>() {
                    @Override
                    public void onSuccess(@NonNull ResponseModel<RemainderModel> models) {
                        if (models.getObjectList().size() == 0) {
                            latch.countDown();
                            return;
                        }
                        Log.d(TAG, "onSuccess: remainderRepository ");
                        List<RemainderModel> reminderList = (List<RemainderModel>) models.getObjectList();
                        long i = 1L;
                        for (RemainderModel remind : reminderList) {
                            remind.setRemainderId(i);
                            i++;
                        }
                        remainderModels = RemainderMapper.getInstance().mapToListEntity(reminderList);
                        remainderRepository.getMRemainderDataSource()
                                .insertAll(remainderModels.toArray(new RemainderEntity[0]))
                                .observeOn(Schedulers.newThread())
                                .observeOn(Schedulers.newThread())
                                .subscribe(new CustomCompletableObserver() {
                                    @Override
                                    public void onComplete() {
                                        Log.d(TAG, "onComplete: remainderRepository");
                                        latch.countDown();
                                    }
                                });
                    }

                    @Override
                    public void onError(Throwable e) {
                        latch.countDown();
                        super.onError(e);
                    }
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void loadAllDataFromServer() {

        loadHabitFromServerIntoDB();
        loadHabitInWeekFromServerIntoDB();

        Log.d(TAG, "loadAllDataFromServer: before");
        try {
            latch.await();
            mLiveDataIsSuccess.postValue(true);
            Log.d(TAG, "loadAllDataFromServer: after await");
        } catch (InterruptedException e) {
            mLiveDataOnError.postValue(e);
            throw new RuntimeException(e);
        }

    }

}
