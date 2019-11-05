package com.doordash;

import com.alexfacciorusso.daggerviewmodel.DaggerViewModelInjectionModule;
import com.doordash.image.ImageLoader;
import com.doordash.presentation.MainActivity;
import com.doordash.restaurants.presentation.screen.RestaurantComponent;
import com.doordash.restaurants.presentation.screen.RestaurantModule;
import com.doordash.restaurants.presentation.screen.restaurant.RestaurantDetailsComponent;
import com.doordash.restaurants.presentation.screen.restaurant.RestaurantDetailsModule;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.support.AndroidSupportInjectionModule;

/**
 *
 */

@Singleton
@Component(modules = {
        DoorDashModule.class,
        SchedulersModule.class,
        AndroidInjectionModule.class,
        DaggerViewModelInjectionModule.class,
        ViewModelModule.class,
        AndroidSupportInjectionModule.class,
})

public interface DoorDashComponent {
    // for inverse injection.
    // All the dependencies mentioned here will become injectable to DaggerApplication.
    void inject(DoorDashApplication daggerApplication);

    // Inject Activities or Fragments
    void inject(MainActivity mainActivity);

    //Inject sub components providing dependencies.
    RestaurantComponent newRestaurantComponent(RestaurantModule module);

    RestaurantDetailsComponent newRestaurantDetailsComponent(RestaurantDetailsModule module);

    // Independent Managers/Repositories.
    ImageLoader provideImageLoader();
}
