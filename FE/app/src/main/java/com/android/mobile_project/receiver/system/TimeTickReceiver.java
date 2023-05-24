package com.android.mobile_project.receiver.system;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.android.mobile_project.MyApplication;
import com.android.mobile_project.data.local.DataLocalManager;
import com.android.mobile_project.data.local.sqlite.dao.HabitDAO;
import com.android.mobile_project.data.local.sqlite.dao.HabitInWeekDAO;
import com.android.mobile_project.data.local.sqlite.dao.HistoryDAO;
import com.android.mobile_project.data.local.sqlite.dao.RemainderDAO;
import com.android.mobile_project.data.remote.api.HabitAPI;
import com.android.mobile_project.data.remote.api.HabitInWeekAPI;
import com.android.mobile_project.data.remote.api.HistoryAPI;
import com.android.mobile_project.data.remote.api.RemainderAPI;
import com.android.mobile_project.data.remote.model.HabitInWeekModel;
import com.android.mobile_project.data.remote.model.HabitModel;
import com.android.mobile_project.data.remote.model.HistoryModel;
import com.android.mobile_project.data.remote.model.RemainderModel;
import com.android.mobile_project.data.remote.model.api.base.ResponseModel;
import com.android.mobile_project.data.remote.persistence.RemoteHabitDataSource;
import com.android.mobile_project.data.remote.persistence.RemoteHabitInWeekDataSource;
import com.android.mobile_project.data.remote.persistence.RemoteHistoryDataSource;
import com.android.mobile_project.data.remote.persistence.RemoteRemainderDataSource;
import com.android.mobile_project.utils.dagger.component.sub.receiver.TimeTickReceiverComponent;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TimeTickReceiver extends BroadcastReceiver {

    private static final String TAG = TimeTickReceiver.class.getSimpleName();

    @Inject
    HabitDAO mHabitDAO;

    @Inject
    HabitInWeekDAO mHabitInWeekDAO;

    @Inject
    HistoryDAO mHistoryDAO;

    @Inject
    RemainderDAO mRemainderDAO;

    @Inject
    HabitAPI mHabitAPI;

    @Inject
    HabitInWeekAPI mHabitInWeekAPI;

    @Inject
    HistoryAPI mHistoryAPI;

    @Inject
    RemainderAPI mRemainderAPI;

    public TimeTickReceiverComponent component;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equals(Intent.ACTION_TIME_TICK)){
            Log.i(TAG, "onReceive");

            component = ((MyApplication) context.getApplicationContext()).provideTimeTickReceiverComponent();
            component.inject(this);

            Log.i(TAG, "tick");
            Log.i(TAG, "Current count "
                    + DataLocalManager.getInstance().getCountToSynchronizeServer());
            Log.i(TAG, "Login state " + DataLocalManager.getInstance().getLoginState());

            if(DataLocalManager.getInstance().getLoginState().equals("true")){
                Long currentCount = DataLocalManager.getInstance().getCountToSynchronizeServer();
                if(currentCount == 3L){
                    if(DataLocalManager.getInstance().getUserStateChangeData().equals("true")){

                        RemoteHabitDataSource remoteHabitDataSource = new RemoteHabitDataSource(mHabitAPI, mHabitDAO);
                        RemoteHabitInWeekDataSource remoteHabitInWeekDataSource = new RemoteHabitInWeekDataSource(mHabitInWeekAPI, mHabitInWeekDAO);
                        RemoteHistoryDataSource remoteHistoryDataSource = new RemoteHistoryDataSource(mHistoryAPI, mHistoryDAO);
                        RemoteRemainderDataSource remoteRemainderDataSource = new RemoteRemainderDataSource(mRemainderAPI, mRemainderDAO);

                        Log.i(TAG, "Doing synchronize");
                        DataLocalManager.getInstance().setUserStateChangeData("false");
                        Log.i(TAG, "new user change state " +  DataLocalManager.getInstance().getUserStateChangeData());

                        remoteHabitDataSource.synchronize().enqueue(new Callback<ResponseModel<HabitModel>>() {
                            @Override
                            public void onResponse(@NonNull Call<ResponseModel<HabitModel>> call, @NonNull Response<ResponseModel<HabitModel>> response) {
                                Log.i(TAG + " habitSync", "onComplete");
                                remoteHabitInWeekDataSource.synchronize().enqueue(new Callback<ResponseModel<HabitInWeekModel>>() {
                                    @Override
                                    public void onResponse(@NonNull Call<ResponseModel<HabitInWeekModel>> call, @NonNull Response<ResponseModel<HabitInWeekModel>> response) {
                                        Log.i(TAG + " habitInWeekSync", "onComplete");
                                        remoteHistoryDataSource.synchronize().enqueue(new Callback<ResponseModel<HistoryModel>>() {
                                            @Override
                                            public void onResponse(@NonNull Call<ResponseModel<HistoryModel>> call, @NonNull Response<ResponseModel<HistoryModel>> response) {
                                                Log.i(TAG + " historySync", "onComplete");
                                                remoteRemainderDataSource.synchronize().enqueue(new Callback<ResponseModel<RemainderModel>>() {
                                                    @Override
                                                    public void onResponse(@NonNull Call<ResponseModel<RemainderModel>> call, @NonNull Response<ResponseModel<RemainderModel>> response) {
                                                        Log.i(TAG + " remainderSync", "onComplete");
                                                    }
                                                    @Override
                                                    public void onFailure(@NonNull Call<ResponseModel<RemainderModel>> call, @NonNull Throwable t) {
                                                        Log.e(TAG + " remainderSync", "onFailure", t);
                                                    }
                                                });
                                            }
                                            @Override
                                            public void onFailure(@NonNull Call<ResponseModel<HistoryModel>> call, @NonNull Throwable t) {
                                                Log.e(TAG + " historySync", "onFailure", t);
                                            }
                                        });
                                    }
                                    @Override
                                    public void onFailure(@NonNull Call<ResponseModel<HabitInWeekModel>> call, @NonNull Throwable t) {
                                        Log.e(TAG + " habitInWeekSync", "onFailure", t);
                                    }
                                });
                            }
                            @Override
                            public void onFailure(@NonNull Call<ResponseModel<HabitModel>> call, @NonNull Throwable t) {
                                Log.e(TAG + " habitSync", "onFailure", t);
                            }
                        });
                    }

                    DataLocalManager.getInstance().setCountToSynchronizeServer(0L);
                    Log.i(TAG, "Reset count "
                            + DataLocalManager.getInstance().getCountToSynchronizeServer());
                }else {
                    DataLocalManager.getInstance().setCountToSynchronizeServer(currentCount + 1L);
                    Log.i(TAG, "After count "
                            + DataLocalManager.getInstance().getCountToSynchronizeServer());
                }
            }

        }

    }
}
