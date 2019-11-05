package com.doordash.restaurants.presentation.screen.restaurant;

import androidx.lifecycle.LifecycleOwner;

import com.doordash.base.presentation.PerFragment;

import dagger.Module;
import dagger.Provides;

/**
 *
 */
@Module
public class RestaurantDetailsModule {

    private LifecycleOwner mFragment;

    public RestaurantDetailsModule(LifecycleOwner lifecycleOwner) {
        mFragment = lifecycleOwner;
    }

    @Provides
    @PerFragment
    LifecycleOwner getLifecycleOwner() {
        return mFragment;
    }
}
