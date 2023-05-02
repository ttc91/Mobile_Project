package com.android.mobile_project.ui.activity.main.fragment.setting;

import android.annotation.SuppressLint;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.android.mobile_project.data.local.DataLocalManager;
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
import com.android.mobile_project.ui.activity.main.fragment.setting.service.ApiService;
import com.android.mobile_project.ui.activity.main.fragment.setting.service.InitService;
import com.android.mobile_project.ui.activity.main.fragment.setting.service.ToastService;
import com.android.mobile_project.utils.dagger.custom.MyCustomAnnotation;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@MyCustomAnnotation.MyScope.FragmentScope
public class SettingViewModel {

    private final HabitRepository habitRepository;

    private final HabitInWeekRepository habitInWeekRepository;

    private final HistoryRepository historyRepository;

    private final RemainderRepository remainderRepository;

    private final UserRepository userRepository;

    protected InitService initService;

    protected ToastService toastService;

    protected Boolean isLogin = false;

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

    protected void requestSignInToServer(){
        userRepository.getMRemoteUserDataSource().signIn().enqueue(new Callback<ResponseModel<JwtResponseModel>>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(@NonNull Call<ResponseModel<JwtResponseModel>> call, @NonNull Response<ResponseModel<JwtResponseModel>> response) {
                Log.i("SettingViewModel - signIn", "onComplete");
                DataLocalManager.getInstance().setToken(response.body().getObject().getToken());
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(@NonNull Call<ResponseModel<JwtResponseModel>> call, @NonNull Throwable t) {
                Log.e("SettingViewModel - signIn", "onFailure");
            }
        });
    }

}
