package com.doordash.base.presentation;

import androidx.annotation.AnimRes;
import androidx.annotation.AnimatorRes;

import com.doordash.DoorDashApplication;

import javax.inject.Inject;

/**
 *
 */
public class DoorDashFragmentNavigator extends DoorDashNavigator<DoorDashFragmentNavigator.Data> {

    @Inject
    protected DoorDashFragmentNavigator(DoorDashApplication application) {
        super(application);
    }

    @Override
    public void navigate(Data data) {
        DoorDashActivity activity = getActivity();
        if (activity != null && !activity.getSupportFragmentManager().isStateSaved()) {
            activity.getSupportFragmentManager()
                    .beginTransaction()
                    .add(data.mContainer, data.mFragment)
                    .addToBackStack(data.mTag)
                    .commit();
        }
    }

    // Navigate to a fragment with in/out pop in/out transition animation.
    public void execute(Data data,
                        @AnimatorRes @AnimRes int enter,
                        @AnimatorRes @AnimRes int exit,
                        @AnimatorRes @AnimRes int popEnter,
                        @AnimatorRes @AnimRes int popExit) {
        DoorDashActivity activity = getActivity();
        if (activity != null && !activity.getSupportFragmentManager().isStateSaved()) {
            activity.getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(
                            enter,
                            exit,
                            popEnter,
                            popExit)
                    .replace(data.mContainer, data.mFragment)
                    .addToBackStack(data.mTag)
                    .commit();
        }
    }

    public static class Data {
        private int mContainer;
        private DoorDashFragment mFragment;
        private String mTag;

        public Data(int container, DoorDashFragment fragment, String tag) {
            mContainer = container;
            mFragment = fragment;
            mTag = tag;
        }
    }
}
