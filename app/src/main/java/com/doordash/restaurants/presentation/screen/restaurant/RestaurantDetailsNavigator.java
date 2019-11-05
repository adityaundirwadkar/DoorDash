package com.doordash.restaurants.presentation.screen.restaurant;

import com.doordash.DoorDashApplication;
import com.doordash.R;
import com.doordash.base.presentation.DoorDashFragmentNavigator;
import com.doordash.base.presentation.DoorDashNavigator;

import javax.inject.Inject;

/**
 *
 */
public class RestaurantDetailsNavigator extends DoorDashNavigator<RestaurantDetailsNavigator.Data> {

    private final DoorDashFragmentNavigator mDoorDashFragmentNavigator;

    @Inject
    public RestaurantDetailsNavigator(DoorDashApplication application,
                                      DoorDashFragmentNavigator fragmentNavigator) {
        super(application);
        mDoorDashFragmentNavigator = fragmentNavigator;
    }

    @Override
    public void navigate(Data data) {
        final RestaurantDetailsFragment fragment = RestaurantDetailsFragment.create(data.restaurantId);
        mDoorDashFragmentNavigator.execute(new DoorDashFragmentNavigator.Data(R.id.fragment_container, fragment, RestaurantDetailsFragment.TAG),
                R.anim.slide_in_right,/* enter */
                R.anim.slide_out_right,/* exit */
                R.anim.slide_in_right,/* pop enter */
                R.anim.slide_out_right /* pop exit */);
    }

    public static class Data {
        long restaurantId;

        public static Data create(long restaurantId) {
            Data data = new Data();
            data.restaurantId = restaurantId;
            return data;
        }
    }
}
