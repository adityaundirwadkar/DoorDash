package com.doordash;

import androidx.lifecycle.ViewModel;

import com.alexfacciorusso.daggerviewmodel.ViewModelKey;
import com.doordash.presentation.MainActivityViewModel;
import com.doordash.restaurants.presentation.screen.RestaurantsViewModel;
import com.doordash.restaurants.presentation.screen.restaurant.RestaurantDetailsViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

/**
 *
 */
@Module
public abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainActivityViewModel.class)
    abstract ViewModel bindMainActivityViewModel(MainActivityViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(RestaurantsViewModel.class)
    abstract ViewModel bindRestaurantsViewModel(RestaurantsViewModel restaurantsViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(RestaurantDetailsViewModel.class)
    abstract ViewModel bindRestaurantDetailsViewModel(RestaurantDetailsViewModel restaurantDetailsViewModel);
}
