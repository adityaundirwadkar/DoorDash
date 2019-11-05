package com.doordash.location.domain;

import com.doordash.base.data.LatLong;
import com.doordash.base.domain.DoorDashManager;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 *
 */

@Singleton
public class LocationManager extends DoorDashManager {


    private static final double RESTAURANT_LATITUDE = 37.422740;
    private static final double RESTAURANT_LONGITUDE = -122.139956;

    private final PublishSubject<LatLong> mLocationSubject;

    @Inject
    public LocationManager() {
        mLocationSubject = PublishSubject.create();
    }

    public LatLong getLocation() {
        return new LatLong(RESTAURANT_LATITUDE, RESTAURANT_LONGITUDE);
    }

    public Observable<LatLong> getLocationSubject() {
        return mLocationSubject.startWith(getLocation());
    }
}
