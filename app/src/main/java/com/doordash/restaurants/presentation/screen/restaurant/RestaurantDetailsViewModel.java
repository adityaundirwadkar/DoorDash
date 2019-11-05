package com.doordash.restaurants.presentation.screen.restaurant;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.doordash.image.ImageLoader;
import com.doordash.restaurants.data.Restaurant;
import com.doordash.restaurants.data.RestaurantDetails;
import com.doordash.restaurants.domain.GetRestaurantDetailsUseCase;
import com.doordash.restaurants.domain.GetRestaurantUseCase;

import javax.inject.Inject;

import io.reactivex.observers.DisposableObserver;

/**
 *
 */
public class RestaurantDetailsViewModel extends ViewModel {

    //Use cases
    private final GetRestaurantUseCase mGetRestaurantUseCase;
    private final GetRestaurantDetailsUseCase mGetRestaurantDetailsUseCase;

    //Live data
    private final MutableLiveData<Restaurant> mRestaurantLiveData;
    private final MutableLiveData<RestaurantDetails> mRestaurantDetailsLiveData;
    private final MutableLiveData<LOGO_STATES> mRestaurantLogoLiveData;

    private final ImageLoader mImageLoader;

    // members
    private Restaurant mRestaurant;
    private RestaurantDetails mRestaurantDetails;
    private Bitmap mBitmap;


    public enum LOGO_STATES {
        LOADING,
        LOADED,
        FAILED,
        UNKNOWN
    }

    @Inject
    public RestaurantDetailsViewModel(GetRestaurantUseCase getRestaurantUseCase,
                                      GetRestaurantDetailsUseCase getRestaurantDetailsUseCase,
                                      ImageLoader imageLoader) {

        mImageLoader = imageLoader;

        mGetRestaurantUseCase = getRestaurantUseCase;
        mGetRestaurantDetailsUseCase = getRestaurantDetailsUseCase;


        mRestaurantLiveData = new MutableLiveData<>();
        mRestaurantDetailsLiveData = new MutableLiveData<>();
        mRestaurantLogoLiveData = new MutableLiveData<>();
    }

    public void getRestaurantUpdates(long id) {
        mGetRestaurantUseCase.execute(id, new DisposableObserver<Restaurant>() {
            @Override
            public void onNext(Restaurant restaurant) {
                if (restaurant != null) {
                    setRestaurant(restaurant);
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

    public void getRestaurantDetailsUpdates() {
        mGetRestaurantDetailsUseCase.execute(mRestaurant.id, new DisposableObserver<RestaurantDetails>() {
            @Override
            public void onNext(RestaurantDetails restaurantDetails) {
                if (restaurantDetails != null && restaurantDetails.id > Long.MIN_VALUE) {
                    setRestaurantDetails(restaurantDetails);
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

    public void getRestaurantLogo(Context context) {
        mImageLoader.load(context, mRestaurant.coverImgUrl, new ImageLoader.Callback() {
            @Override
            public void loadingStarted(String uri) {
                mRestaurantLogoLiveData.setValue(LOGO_STATES.LOADING);
            }

            @Override
            public void loadingCompleted(String uri, Bitmap bitmap) {
                setBitmap(bitmap);
                mRestaurantLogoLiveData.setValue(LOGO_STATES.LOADED);
            }

            @Override
            public void loadingError(String uri) {
                // display error.
                mRestaurantLogoLiveData.setValue(LOGO_STATES.FAILED);
            }
        });
    }


    public MutableLiveData<Restaurant> getRestaurantLiveData() {
        return mRestaurantLiveData;
    }

    public MutableLiveData<RestaurantDetails> getRestaurantDetailsLiveData() {
        return mRestaurantDetailsLiveData;
    }

    public MutableLiveData<LOGO_STATES> getRestaurantLogoLiveData() {
        return mRestaurantLogoLiveData;
    }

    public Restaurant getRestaurant() {
        return mRestaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        mRestaurant = restaurant;
        mRestaurantLiveData.setValue(mRestaurant);
    }

    public RestaurantDetails getRestaurantDetails() {
        return mRestaurantDetails;
    }

    public void setRestaurantDetails(RestaurantDetails mRestaurantDetails) {
        this.mRestaurantDetails = mRestaurantDetails;
        mRestaurantDetailsLiveData.setValue(mRestaurantDetails);
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public void setBitmap(Bitmap mBitmap) {
        this.mBitmap = mBitmap;
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        mGetRestaurantUseCase.dispose();
    }
}
