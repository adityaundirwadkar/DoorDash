package com.doordash.restaurants.domain;

import com.doordash.SchedulersModule;
import com.doordash.base.data.LatLong;
import com.doordash.base.data.UseCase;
import com.doordash.restaurants.data.Restaurant;
import com.doordash.restaurants.data.RestaurantRepository;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

/**
 * Refresh the entire list.
 */
public class RefreshRestaurantListUseCase extends UseCase<List<Restaurant>, LatLong> {

    private final RestaurantRepository mRestaurantRepository;

    @Inject
    public RefreshRestaurantListUseCase(@Named(SchedulersModule.IO) Scheduler subscribeOn,
                                        @Named(SchedulersModule.UI_THREAD) Scheduler observeOn,
                                        RestaurantRepository restaurantRepository) {
        super(subscribeOn, observeOn);
        mRestaurantRepository = restaurantRepository;
    }

    @Override
    public Observable<List<Restaurant>> buildUseCaseObservable(LatLong data) {
        return Observable.defer(() -> mRestaurantRepository.refreshRestaurants(data, RestaurantRepository.RESTAURANT_LIST_PAGESIZE));
    }
}
