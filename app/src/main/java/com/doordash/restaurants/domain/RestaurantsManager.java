package com.doordash.restaurants.domain;

import com.doordash.base.domain.DoorDashManager;
import com.doordash.restaurants.data.RestaurantRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

/**
 *
 */

@Singleton
public class RestaurantsManager extends DoorDashManager {

    private final RestaurantRepository mRepository;

    @Inject
    public RestaurantsManager(RestaurantRepository restaurantRepository) {
        mRepository = restaurantRepository;
    }

    public boolean isFetchingRecords() {
        return mRepository.isFetchingRecords();
    }

    private void setFetchingRecords(boolean flag) {
        mRepository.setFetchingRecords(flag);
    }

    public Observable<Boolean> getRefreshingSubject() {
        return mRepository.getRefreshingSubject();
    }

    public Observable<Boolean> getReloadingSubject() {
        return mRepository.getReloadingSubject();
    }
}
