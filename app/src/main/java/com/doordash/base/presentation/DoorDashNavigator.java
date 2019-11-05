package com.doordash.base.presentation;

import androidx.annotation.Nullable;

import com.doordash.DoorDashApplication;

import java.lang.ref.WeakReference;

/**
 * Base class used to show a piece of UI, generally an activity or fragment. Navigators
 * will generally be injected, so the activity is generally provided through the
 * OwlApplication.
 *
 * @param <Data> Any initial data needed to complete the navigation
 */

public abstract class DoorDashNavigator<Data> {
    Data data;

    public abstract void navigate(Data data);

    private WeakReference<DoorDashActivity> mActivity;

    public DoorDashNavigator(DoorDashApplication application) {
        mActivity = new WeakReference<>(application.getCurrentActivity());
    }

    @Nullable
    protected DoorDashActivity getActivity() {
        return mActivity.get();
    }
}
