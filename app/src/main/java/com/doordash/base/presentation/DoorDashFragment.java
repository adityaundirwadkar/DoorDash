package com.doordash.base.presentation;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import javax.inject.Inject;

/**
 *
 */
public abstract class DoorDashFragment<T extends ViewModel> extends Fragment {

    public static final String TAG = DoorDashFragment.class.getSimpleName();

    @Inject
    protected ViewModelProvider.Factory mViewModelFactory;

    protected T mViewModel;

    protected T getViewModel() {
        return mViewModel;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mViewModel = getViewModel();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public boolean shouldDestroyActivity() {
        return false;
    }
}