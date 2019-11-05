package com.doordash.restaurants.domain;

import com.doordash.SchedulersModule;
import com.doordash.base.data.UseCase;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

/**
 *
 */
public class GetReloadingRestaurantListUseCase extends UseCase<Boolean, Void> {

    private final RestaurantsManager mRestaurantsManager;

    @Inject
    public GetReloadingRestaurantListUseCase(@Named(SchedulersModule.IO) Scheduler subscribeOn,
                                             @Named(SchedulersModule.UI_THREAD) Scheduler observeOn,
                                             RestaurantsManager manager) {
        super(subscribeOn, observeOn);
        mRestaurantsManager = manager;
    }

    @Override
    public Observable<Boolean> buildUseCaseObservable(Void data) {
        return Observable.defer(mRestaurantsManager::getReloadingSubject);
    }
}
