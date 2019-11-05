package com.doordash;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.doordash.base.data.DoorDashDatabase;
import com.doordash.base.presentation.DoorDashActivity;
import com.doordash.image.ImageLoader;
import com.facebook.stetho.Stetho;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasAndroidInjector;
import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;


/**
 *
 */
public class DoorDashApplication extends Application implements HasAndroidInjector, LifecycleObserver {

    private static final String TAG = DoorDashApplication.class.getSimpleName();

    private static DoorDashApplication sApplication;

    private DoorDashComponent mComponent;

    private WeakReference<DoorDashActivity> mCurrentActivity;

    @Inject
    volatile DispatchingAndroidInjector<Object> mAndroidInjector;

    @Inject
    ImageLoader mImageLoader;

    @Override
    public void onCreate() {
        super.onCreate();

        initComponent();
        // Init database.
        DoorDashDatabase.initDatabase(this);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this);
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();

        // Cleanup some memory
        mImageLoader.onLowMemory();
    }

    private void initComponent() {
        sApplication = this;
        mComponent = DaggerDoorDashComponent.builder()
                .doorDashModule(new DoorDashModule(this))
                .build();
        mComponent.inject(this);
        ComponentProvider.set(mComponent);
    }

    public void setCurrentActivity(@NonNull DoorDashActivity activity) {
        mCurrentActivity = new WeakReference<>(activity);
    }

    @Nullable
    public DoorDashActivity getCurrentActivity() {
        return mCurrentActivity != null ? mCurrentActivity.get() : null;
    }

    public static DoorDashApplication getApplication() {
        return sApplication;
    }

    // Lifecycle related events to manager app state.
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onAppBackgrounded() {
        Log.d(TAG, "App in background");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onAppDestroyed() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onAppForegrounded() {
        Log.d(TAG, "App in foreground");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onActivityForegrounded() {
        Log.d(TAG, "Activity resumed.");
    }

    @Override
    public AndroidInjector<Object> androidInjector() {
        return mAndroidInjector;
    }
}
