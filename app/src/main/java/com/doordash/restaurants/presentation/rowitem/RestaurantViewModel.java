package com.doordash.restaurants.presentation.rowitem;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.doordash.base.presentation.recyclerview.RecyclerViewViewModel;
import com.doordash.image.ImageLoader;
import com.doordash.restaurants.data.Restaurant;
import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;

/**
 *
 */

@AutoFactory
public class RestaurantViewModel extends RecyclerViewViewModel<RestaurantViewModel.Interactor> {

    private static final String TAG = RestaurantViewModel.class.getSimpleName();

    private final Restaurant mRestaurant;
    private final ImageLoader mImageLoader;
    private Bitmap mBitmap;

    private final MutableLiveData<LOGO_STATES> mRestaurantLogo = new MutableLiveData<>();

    private static final int MAX_ALLOWED_LENGTH = 35;
    private static final int ALLOWED_CHARACTERS = 32;

    public enum LOGO_STATES {
        LOADING,
        LOADED,
        FAILED,
        UNKNOWN
    }

    public RestaurantViewModel(@NonNull final Restaurant restaurant,
                               @Provided ImageLoader imageLoader) {

        super(RestaurantViewHolder.class);
        mRestaurant = restaurant;
        mImageLoader = imageLoader;
        mRestaurantLogo.setValue(LOGO_STATES.UNKNOWN);
    }

    public void loadRestaurantLogo(Context context) {
        mImageLoader.load(context, mRestaurant.coverImgUrl, new ImageLoader.Callback() {
            @Override
            public void loadingStarted(String uri) {
                mRestaurantLogo.setValue(LOGO_STATES.LOADING);
            }

            @Override
            public void loadingCompleted(String uri, Bitmap bitmap) {
                mBitmap = bitmap;
                mRestaurantLogo.setValue(LOGO_STATES.LOADED);
            }

            @Override
            public void loadingError(String uri) {
                // display error.
                mRestaurantLogo.setValue(LOGO_STATES.FAILED);
            }
        });
    }

    @Override
    public void onRecycled() {
        super.onRecycled();

        mImageLoader.cancelRequest(mRestaurant.coverImgUrl);
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public String getRestaurantName() {
        // Trim out the address part from the name of the restaurant.
        int index = mRestaurant.name.indexOf("(");
        String name = mRestaurant.name;
        if (index > 0) {
            name = mRestaurant.name.substring(0, mRestaurant.name.indexOf("(")).trim();
        }
        if (name.length() > MAX_ALLOWED_LENGTH) {
            return name.substring(0, ALLOWED_CHARACTERS) + "...";
        }
        return name;
    }

    public String getRestaurantDescription() {
        if (mRestaurant.description.length() > MAX_ALLOWED_LENGTH) {
            return mRestaurant.description.substring(0, ALLOWED_CHARACTERS) + "...";
        }
        return mRestaurant.description;
    }

    public String getRestaurantDeliveryTime() {
        if (TextUtils.equals(Restaurant.STATUS_OPEN, mRestaurant.statusType)) {
            return mRestaurant.status;
        }
        return Restaurant.STATUS_CLOSED_RESPONSE;

    }

    public long getApiRestaurantIndex() {
        return mRestaurant.apiIndex;
    }

    public MutableLiveData<LOGO_STATES> getRestaurantLogoLiveData() {
        return mRestaurantLogo;
    }

    // Callback to higher level
    public void onViewHolderClicked() {
        if (mInteractor!= null && mInteractor.get() != null) {
            mInteractor.get().onRestaurantSelected(mRestaurant);
        }
    }

    public interface Interactor {
        public void onRestaurantSelected(Restaurant restaurant);
    }
}
