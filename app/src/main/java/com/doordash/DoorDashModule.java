package com.doordash;

import android.content.Context;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 *
 */

@Module
public class DoorDashModule {

    public static final String APPLICATION_CONTEXT = "application_context";

    private DoorDashApplication mApplication;

    public DoorDashModule(DoorDashApplication application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    DoorDashApplication getDoorDashApplication() {
        return mApplication;
    }

    @Provides
    @Singleton
    @Named(APPLICATION_CONTEXT)
    Context getAppContext() {
        return mApplication.getApplicationContext();
    }
}
