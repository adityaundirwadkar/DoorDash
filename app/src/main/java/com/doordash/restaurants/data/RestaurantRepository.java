package com.doordash.restaurants.data;

import com.doordash.base.data.DoorDashRepository;
import com.doordash.base.data.LatLong;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *
 */

@Singleton
public class RestaurantRepository extends DoorDashRepository {

    private final static String TAG = RestaurantRepository.class.getSimpleName();

    public static final int RESTAURANT_LIST_PAGESIZE = 50;

    private final RestaurantNetworkProvider mNetworkProvider;
    private final RestaurantLocalProvider mLocalProvider;

    private final AtomicBoolean mIsFetchingRecords = new AtomicBoolean(false);

    private final PublishSubject<Boolean> mRefreshingSubject;
    private final PublishSubject<Boolean> mReloadingSubject;
    private final PublishSubject<List<Restaurant>> mRestaurantsSubject;
    private final PublishSubject<RestaurantDetails> mRestaurantDetailsSubject;

    private long mResponseIndex = 1;

    @Inject
    public RestaurantRepository(RestaurantNetworkProvider restaurantNetworkProvider,
                                RestaurantLocalProvider restaurantLocalProvider) {

        mNetworkProvider = restaurantNetworkProvider;
        mLocalProvider = restaurantLocalProvider;

        mRefreshingSubject = PublishSubject.create();
        mReloadingSubject = PublishSubject.create();
        mRestaurantsSubject = PublishSubject.create();
        mRestaurantDetailsSubject = PublishSubject.create();
    }

    /**
     * Local DB related methods.
     * */
    public Observable<Restaurant> getRestaurant(long id) {
        return mLocalProvider.getRestaurant(id);
    }

    public Restaurant getRestaurantSynchronous(long id) {
        return mLocalProvider.getRestaurantSynchronous(id);
    }

    /**
     * Getters and Setters.
     * */
    public boolean isFetchingRecords() {
        return mIsFetchingRecords.get();
    }

    public void setFetchingRecords(boolean flag) {
        mIsFetchingRecords.set(flag);
        mRefreshingSubject.onNext(flag);
    }

    public long getResponseIndex() {
        return mResponseIndex;
    }

    public void setResponseIndex(long mResponseIndex) {
        this.mResponseIndex = mResponseIndex;
    }

    public Observable<Boolean> getRefreshingSubject() {
        return mRefreshingSubject.startWith(false);
    }

    public Observable<Boolean> getReloadingSubject() {
        return mReloadingSubject.startWith(false);
    }

    public Observable<RestaurantDetails> getRestaurantDetailsSubject() {
        return mRestaurantDetailsSubject;
    }

    /**
     * Network related methods.
     * */
    // Fetch records from the beginning.
    public Observable<List<Restaurant>> refreshRestaurants(LatLong latLong, int pageSize) {
        mLocalProvider.deleteAllRestaurant();
        setResponseIndex(1);
        return getRestaurants(latLong,0, pageSize, true);
    }

    // Fetch records from the offset.
    public Observable<List<Restaurant>> getRestaurants(LatLong latLong, int offset, int pageSize) {
        return getRestaurants(latLong, offset, pageSize, false);
    }

    public Observable<List<Restaurant>> getRestaurants(LatLong latLong, int offset, int pageSize, boolean isReloading) {
        if (isFetchingRecords()) {
            return Observable.empty();
        }
        setFetchingRecords(true);
        mReloadingSubject.onNext(isReloading);
        mNetworkProvider.getRestaurants(latLong, offset, pageSize, new Callback<List<Restaurant>>() {
            @Override
            public void onResponse(Call<List<Restaurant>> call, Response<List<Restaurant>> response) {
                setFetchingRecords(false);
                //Store the records in the db and then update the UI.
                // int index = mLocalProvider.getRestaurantsSynchronous().size() + 1;
                for (Restaurant restaurant : response.body()) {
                    restaurant.apiIndex = getResponseIndex();
                    setResponseIndex(getResponseIndex() + 1);
                }
                mLocalProvider.insertRestaurants(response.body());
                mRestaurantsSubject.onNext(mLocalProvider.getRestaurantsSynchronous());
                // Done reloading the list.
                mReloadingSubject.onNext(false);
            }

            @Override
            public void onFailure(Call<List<Restaurant>> call, Throwable t) {
                setFetchingRecords(false);
            }
        });
        return mRestaurantsSubject;
    }


    public Observable<RestaurantDetails> getRestaurantDetails(long restaurantId) {

        mNetworkProvider.getRestaurantDetails(restaurantId, new Callback<RestaurantDetails>() {
            @Override
            public void onResponse(Call<RestaurantDetails> call, Response<RestaurantDetails> response) {
                mRestaurantDetailsSubject.onNext(response.body());
            }

            @Override
            public void onFailure(Call<RestaurantDetails> call, Throwable t) {
                // TODO : handle error responses.
            }
        });

        return getRestaurantDetailsSubject();
    }
}
