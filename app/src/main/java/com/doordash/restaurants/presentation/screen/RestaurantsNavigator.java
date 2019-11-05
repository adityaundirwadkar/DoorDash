package com.doordash.restaurants.presentation.screen;

import com.doordash.DoorDashApplication;
import com.doordash.R;
import com.doordash.base.presentation.DoorDashFragmentNavigator;
import com.doordash.base.presentation.DoorDashNavigator;

import javax.inject.Inject;

/**
 *
 */
public class RestaurantsNavigator extends DoorDashNavigator<Void> {

    private final DoorDashFragmentNavigator mDoorDashFragmentNavigator;

    @Inject
    protected RestaurantsNavigator(DoorDashApplication application,
                                   DoorDashFragmentNavigator fragmentNavigator) {
        super(application);
        mDoorDashFragmentNavigator = fragmentNavigator;
    }

    @Override
    public void navigate(Void aVoid) {
        final DoorDashFragmentNavigator.Data data = new DoorDashFragmentNavigator.Data(R.id.fragment_container, RestaurantsFragment.create(), RestaurantsFragment.TAG);
        mDoorDashFragmentNavigator.navigate(data);
    }
}
