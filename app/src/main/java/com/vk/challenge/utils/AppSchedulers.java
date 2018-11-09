package com.vk.challenge.utils;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class AppSchedulers {

    public static <T> SchedulerTransformer<T> ioToMainTransformer() {
        return new SchedulerTransformer<>(io(), main());
    }

    public static Scheduler main() {
        return AndroidSchedulers.mainThread();
    }

    public static Scheduler io() {
        return Schedulers.io();
    }
}
