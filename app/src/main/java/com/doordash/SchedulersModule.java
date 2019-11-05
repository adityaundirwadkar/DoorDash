package com.doordash;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@Module
public class SchedulersModule {
    public static final String IO = "io";
    public static final String COMPUTE = "compute";
    public static final String UI_THREAD = "ui";
    public static final String NEW_THREAD = "new_thread";

    @Provides
    @Named(COMPUTE)
    public Scheduler provideComputeScheduler() {
        return Schedulers.computation();
    }

    @Provides
    @Named(IO)
    public Scheduler provideIOScheduler() {
        return Schedulers.io();
    }

    @Provides
    @Named(UI_THREAD)
    public Scheduler provideUIObserver() {
        return AndroidSchedulers.mainThread();
    }

    @Provides
    @Named(NEW_THREAD)
    public Scheduler provideNewThreadScheduler() {
        return Schedulers.newThread();
    }
}
