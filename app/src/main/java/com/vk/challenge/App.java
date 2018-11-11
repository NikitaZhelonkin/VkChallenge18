package com.vk.challenge;

import android.app.Application;
import android.support.annotation.NonNull;

import com.vk.challenge.di.AppComponent;
import com.vk.challenge.di.AppModule;
import com.vk.challenge.di.DaggerAppComponent;
import com.vk.sdk.VKSdk;

public class App extends Application {

    private static AppComponent sAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        VKSdk.initialize(this);

        sAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(getApplicationContext()))
                .build();
    }

    @NonNull
    public static AppComponent getAppComponent() {
        return sAppComponent;
    }
}
