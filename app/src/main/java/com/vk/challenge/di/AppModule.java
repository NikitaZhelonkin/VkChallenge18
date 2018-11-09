package com.vk.challenge.di;

import android.content.Context;
import android.support.annotation.NonNull;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vk.challenge.BuildConfig;
import com.vk.challenge.data.api.HttpInterceptor;
import com.vk.challenge.data.api.LoggingInterceptor;
import com.vk.challenge.data.api.VkApiService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Module
public class AppModule {

    private Context mContext;

    public AppModule(Context context) {
        mContext = context;
    }

    @Provides
    @Singleton
    @NonNull
    ObjectMapper provideObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }

    @Provides
    @Singleton
    @NonNull
    OkHttpClient provideHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(new LoggingInterceptor());
        }
        builder.addInterceptor(new HttpInterceptor());
        return builder.build();
    }

    @Provides
    @Singleton
    @NonNull
    VkApiService provideApiService(OkHttpClient httpClient, ObjectMapper objectMapper) {
        return new Retrofit.Builder()
                .baseUrl("https://api.vk.com/")
                .client(httpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create(objectMapper)).build()
                .create(VkApiService.class);
    }
}
