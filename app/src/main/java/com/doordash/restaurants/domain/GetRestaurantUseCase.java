package com.doordash.restaurants.domain;

import com.doordash.SchedulersModule;
import com.doordash.base.data.UseCase;
import com.doordash.restaurants.data.Restaurant;
import com.doordash.restaurants.data.RestaurantRepository;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

/**
 * Retrieves individual restaurant by id.
 */
public class GetRestaurantUseCase extends UseCase<Restaurant, Long> {

    private final RestaurantRepository mRestaurantRepository;

    @Inject
    public GetRestaurantUseCase(@Named(SchedulersModule.IO) Scheduler subscribeOn,
                                @Named(SchedulersModule.UI_THREAD) Scheduler observeOn,
                                RestaurantRepository restaurantRepository) {

        super(subscribeOn, observeOn);
        mRestaurantRepository = restaurantRepository;
    }

    @Override
    public Observable<Restaurant> buildUseCaseObservable(Long data) {
        return Observable.defer(() -> mRestaurantRepository.getRestaurant(data));
    }
}
