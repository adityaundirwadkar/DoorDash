package com.doordash.restaurants.presentation.screen;

import androidx.lifecycle.LifecycleOwner;

import com.doordash.base.presentation.PerFragment;

import dagger.Module;
import dagger.Provides;

/**
 *
 */

@Module
public class RestaurantModule {

    private LifecycleOwner mFragment;

    public RestaurantModule(LifecycleOwner lifecycleOwner) {
        mFragment = lifecycleOwner;
    }

    @Provides
    @PerFragment
    LifecycleOwner getLifecycleOwner() {
        return mFragment;
    }
}
