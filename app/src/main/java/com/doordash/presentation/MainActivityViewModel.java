package com.doordash.presentation;

import androidx.lifecycle.ViewModel;

import com.doordash.base.data.LatLong;
import com.doordash.location.domain.GetLocationUseCase;
import com.doordash.restaurants.data.Restaurant;
import com.doordash.restaurants.domain.GetRefreshingStateUseCase;
import com.doordash.restaurants.domain.RefreshRestaurantListUseCase;
import com.doordash.restaurants.presentation.screen.RestaurantsNavigator;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.observers.DisposableObserver;

/**
 * ViewModel for MainActivity.
 */
public class MainActivityViewModel extends ViewModel {


    private final RefreshRestaurantListUseCase mRefreshRestaurantListUseCase;
    private final GetRefreshingStateUseCase mGetRefreshingStateUseCase;
    private final GetLocationUseCase mGetLocationUseCase;

    private final RestaurantsNavigator mRestaurantsNavigator;

    private boolean mIsRefreshingStatus;

    @Inject
    public MainActivityViewModel(RefreshRestaurantListUseCase refreshRestaurantListUseCase,
                                 GetRefreshingStateUseCase getRefreshingStateUseCase,
                                 GetLocationUseCase getLocationUseCase,
                                 RestaurantsNavigator restaurantsNavigator) {

        // Use cases.
        mRefreshRestaurantListUseCase = refreshRestaurantListUseCase;
        mGetRefreshingStateUseCase = getRefreshingStateUseCase;
        mGetLocationUseCase = getLocationUseCase;

        // Navigators.
        mRestaurantsNavigator = restaurantsNavigator;

        // Refresh the list on launch.
        getRefreshStatusUpdates();
    }

    public void navigateRestaurantsFragment() {
       mRestaurantsNavigator.navigate(null);
    }

    public boolean getLocationUpdates() {
        if (!mIsRefreshingStatus) {
            mGetLocationUseCase.execute(null, new DisposableObserver<LatLong>() {
                @Override
                public void onNext(LatLong latLong) {
                    if (latLong!= null) {
                        reloadRestaurantList(latLong);
                    }
                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onComplete() {

                }
            });
        }
        return !mIsRefreshingStatus;
    }

    public void reloadRestaurantList(LatLong latLong) {
        mRefreshRestaurantListUseCase.execute(latLong, new DisposableObserver<List<Restaurant>>() {
            @Override
            public void onNext(List<Restaurant> restaurants) {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    public void getRefreshStatusUpdates() {
        mGetRefreshingStateUseCase.execute(null, new DisposableObserver<Boolean>() {
            @Override
            public void onNext(Boolean aBoolean) {
                if (aBoolean != null) {
                    mIsRefreshingStatus = aBoolean;
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        mRefreshRestaurantListUseCase.dispose();
        mGetRefreshingStateUseCase.dispose();
        mGetLocationUseCase.dispose();
    }
}
