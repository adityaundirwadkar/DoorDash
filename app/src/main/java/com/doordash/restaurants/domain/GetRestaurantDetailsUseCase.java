package com.doordash.restaurants.domain;

import com.doordash.SchedulersModule;
import com.doordash.base.data.UseCase;
import com.doordash.restaurants.data.RestaurantDetails;
import com.doordash.restaurants.data.RestaurantRepository;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

/**
 *
 */
public class GetRestaurantDetailsUseCase extends UseCase<RestaurantDetails, Long> {

    private final RestaurantRepository mRestaurantRepository;

    @Inject
    public GetRestaurantDetailsUseCase(@Named(SchedulersModule.IO) Scheduler subscribeOn,
                                       @Named(SchedulersModule.UI_THREAD) Scheduler observeOn,
                                       RestaurantRepository restaurantRepository) {
        super(subscribeOn, observeOn);
        mRestaurantRepository = restaurantRepository;
    }

    @Override
    public Observable<RestaurantDetails> buildUseCaseObservable(Long data) {
        return Observable.defer(() -> mRestaurantRepository.getRestaurantDetails(data)
                // Filter out request.
                .flatMap(restaurantDetails -> {
                    if (data.equals(restaurantDetails.id)) {
                        return Observable.just(restaurantDetails);
                    }
                    return Observable.just(new RestaurantDetails());
                }));
    }
}
