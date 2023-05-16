package com.android.mobile_project.ui.activity.login;

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

import javax.inject.Inject;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@MyCustomAnnotation.MyScope.ActivityScope
public class LoginViewModel extends BaseViewModel {

    private static final String TAG = LoginViewModel.class.getSimpleName();
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
    public LoginViewModel(HabitRepository habitRepository, HabitInWeekRepository habitInWeekRepository,
                          HistoryRepository historyRepository, RemainderRepository remainderRepository, UserRepository mUserRepository) {
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

    @RequiresApi(api = Build.VERSION_CODES.N)
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
        //NotificationWorker.enqueueWorkerWithHabit();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
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

}
