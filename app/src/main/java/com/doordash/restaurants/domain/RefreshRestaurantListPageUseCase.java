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
 * Request a specific page for List.
 */
public class RefreshRestaurantListPageUseCase extends UseCase<List<Restaurant>, RefreshRestaurantListPageUseCase.Data> {

    private final RestaurantRepository mRestaurantRepository;

    @Inject
    public RefreshRestaurantListPageUseCase(@Named(SchedulersModule.IO) Scheduler subscribeOn,
                                            @Named(SchedulersModule.UI_THREAD) Scheduler observeOn,
                                            RestaurantRepository restaurantRepository) {
        super(subscribeOn, observeOn);
        mRestaurantRepository = restaurantRepository;
    }

    @Override
    public Observable<List<Restaurant>> buildUseCaseObservable(Data data) {
        return Observable.defer(() -> mRestaurantRepository.getRestaurants(data.latLong, data.offset, RestaurantRepository.RESTAURANT_LIST_PAGESIZE));
    }

    public static class Data {
        LatLong latLong;
        int offset;
        // page limit can be added here as another arg.
        // int pageLimit;

        public static Data create(LatLong latLong, int offset) {
            Data data = new Data();
            data.latLong = latLong;
            data.offset = offset;
            return data;
        }
    }
}
