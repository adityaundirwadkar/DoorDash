package com.doordash.restaurants.presentation.screen.restaurant;

import com.doordash.base.presentation.PerFragment;

import dagger.Subcomponent;

/**
 *
 */
@PerFragment
@Subcomponent(modules = {
        RestaurantDetailsModule.class
})
public interface RestaurantDetailsComponent {
    void inject(RestaurantDetailsFragment fragment);
}
