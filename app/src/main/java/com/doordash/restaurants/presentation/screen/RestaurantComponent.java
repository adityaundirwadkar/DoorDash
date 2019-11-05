package com.doordash.restaurants.presentation.screen;

import com.doordash.base.presentation.PerFragment;

import dagger.Subcomponent;

/**
 *
 */

@PerFragment
@Subcomponent(modules = {
        RestaurantModule.class
})
public interface RestaurantComponent {
    void inject(RestaurantsFragment fragment);
}
