package com.android.mobile_project.utils.dagger.module;

import com.android.mobile_project.data.remote.api.HabitAPI;
import com.android.mobile_project.data.remote.api.HabitInWeekAPI;
import com.android.mobile_project.data.remote.api.HistoryAPI;
import com.android.mobile_project.data.remote.api.RemainderAPI;
import com.android.mobile_project.data.remote.api.UserAPI;
import com.android.mobile_project.data.utils.constant.ApiPaths;
import com.android.mobile_project.utils.dagger.custom.MyCustomAnnotation;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class RetrofitModule {

    private final Retrofit retrofit;

    public RetrofitModule() {
        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor())
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true);
        retrofit = new Retrofit.Builder()
                .baseUrl(ApiPaths.API_HOST_DOMAIN)
                .client(client.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    public HabitAPI provideHabitAPI(){
        return retrofit.create(HabitAPI.class);
    }

    @Provides
    @Singleton
    public HabitInWeekAPI provideHabitInWeekAPI(){
        return retrofit.create(HabitInWeekAPI.class);
    }

    @Provides
    @Singleton
    public HistoryAPI provideHistoryAPI(){
        return retrofit.create(HistoryAPI.class);
    }

    @Provides
    @Singleton
    public RemainderAPI provideRemainderAPI(){
        return retrofit.create(RemainderAPI.class);
    }

    @Provides
    @Singleton
    public UserAPI provideUserAPI(){
        return retrofit.create(UserAPI.class);
    }

}
