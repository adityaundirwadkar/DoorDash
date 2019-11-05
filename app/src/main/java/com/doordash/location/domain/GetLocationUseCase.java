package com.doordash.location.domain;

import com.doordash.SchedulersModule;
import com.doordash.base.data.LatLong;
import com.doordash.base.data.UseCase;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

/**
 *
 */
public class GetLocationUseCase extends UseCase<LatLong, Void> {

    private final LocationManager mLocationManager;

    @Inject
    public GetLocationUseCase(@Named(SchedulersModule.IO) Scheduler subscribeOn,
                              @Named(SchedulersModule.UI_THREAD) Scheduler observeOn,
                              LocationManager manager) {
        super(subscribeOn, observeOn);
        mLocationManager = manager;
    }

    @Override
    public Observable<LatLong> buildUseCaseObservable(Void data) {
        return mLocationManager.getLocationSubject();
    }
}
