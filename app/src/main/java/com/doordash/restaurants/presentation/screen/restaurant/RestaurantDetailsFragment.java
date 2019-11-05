package com.doordash.restaurants.presentation.screen.restaurant;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.doordash.ComponentProvider;
import com.doordash.R;
import com.doordash.base.presentation.DoorDashFragment;
import com.doordash.restaurants.data.Restaurant;
import com.doordash.restaurants.data.RestaurantDetails;

import java.text.NumberFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 */
public class RestaurantDetailsFragment extends DoorDashFragment<RestaurantDetailsViewModel> {

    public static final String TAG = RestaurantDetailsFragment.class.getSimpleName();

    private long mRestaurantId;

    // View Bindings
    @BindView(R.id.rl_status_container)
    RelativeLayout mRlLoading;

    @BindView(R.id.rl_restaurant_details)
    RelativeLayout mRlRestaurantDetails;

    @BindView(R.id.iv_restaurant_details_logo)
    ImageView mIvLogo;

    @BindView(R.id.tv_restaurant_details_name)
    TextView mTvName;

    @BindView(R.id.rb_restaurant_details_ratings)
    RatingBar mRbRatings;

    @BindView(R.id.tv_restaurant_details_ratings)
    TextView mTvRatingsDetails;

    @BindView(R.id.tv_restaurant_details_delivery_charges)
    TextView mTvCharges;

    @BindView(R.id.tv_restaurant_details_delivery_time)
    TextView mTvTime;

    public static RestaurantDetailsFragment create(long restaurantId) {
        RestaurantDetailsFragment fragment = new RestaurantDetailsFragment();
        fragment.mRestaurantId = restaurantId;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        ComponentProvider.get().newRestaurantDetailsComponent(new RestaurantDetailsModule(this)).inject(this);
        View root = inflater.inflate(R.layout.fragment_restaurant_details, container, false);
        ButterKnife.bind(this, root);
        mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(RestaurantDetailsViewModel.class);
        // Init observers.
        showProgressUI();
        initRestaurantObserver();
        initRestaurantDetailsObserver();
        initRestaurantLogoObserver();
        mViewModel.getRestaurantUpdates(mRestaurantId);
        return root;
    }

    private void initRestaurantObserver() {
        mViewModel.getRestaurantLiveData().observe(this, restaurant -> {
            // TODO: Can change the progress status to let user know that we are doing something in background.
            if (restaurant != null && restaurant.id > Long.MIN_VALUE) {
                mViewModel.getRestaurantDetailsUpdates();
            }
        });
    }

    private void initRestaurantDetailsObserver() {
        mViewModel.getRestaurantDetailsLiveData().observe(this, restaurantDetails -> {
            // Now we have everything. Update the UI.
            updateRestaurantDetails();
            hideProgressUI();
        });
    }

    private void initRestaurantLogoObserver() {
        mViewModel.getRestaurantLogoLiveData().observe(this, logo_states -> {
            switch (logo_states) {
                case UNKNOWN:
                    mViewModel.getRestaurantLogo(getContext());
                    mIvLogo.setColorFilter(null);
                    break;
                case LOADING:
                    // Add Error Image.
                    mIvLogo.setImageDrawable(getContext().getDrawable(R.drawable.ic_cloud_download));
                    mIvLogo.setColorFilter(getContext().getColor(R.color.darkblue), PorterDuff.Mode.SRC_ATOP);
                    break;
                case LOADED:
                    mIvLogo.setImageBitmap(mViewModel.getBitmap());
                    mIvLogo.setColorFilter(null);
                    break;
                case FAILED:
                    // Add Error Image.
                    mIvLogo.setImageDrawable(getContext().getDrawable(R.drawable.ic_error));
                    mIvLogo.setColorFilter(getContext().getColor(R.color.darkred), PorterDuff.Mode.SRC_ATOP);
                    break;
                default:
                    break;
            }
        });
    }

    private void showProgressUI() {
        mRlLoading.setVisibility(View.VISIBLE);
        mRlRestaurantDetails.setVisibility(View.GONE);
    }

    private void hideProgressUI() {
        mRlLoading.setVisibility(View.GONE);
        mRlRestaurantDetails.setVisibility(View.VISIBLE);
    }


    private void updateRestaurantDetails() {

        RestaurantDetails restaurantDetails = mViewModel.getRestaurantDetails();

        if (restaurantDetails.shouldShowStoreLogo) {
            mViewModel.getRestaurantLogo(getContext());
        }

        // Set the name.
        mTvName.setText(restaurantDetails.name);

        // Set ratings.
        mRbRatings.setRating(restaurantDetails.avgRating);

        mTvRatingsDetails.setText(
                getString(R.string.restaurant_details_ratings,
                        String.valueOf(restaurantDetails.avgRating),
                        String.valueOf(restaurantDetails.numberOfRatings)));

        // Set delivery charges.
        mTvCharges.setText(NumberFormat.getCurrencyInstance(Locale.US).format(restaurantDetails.deliveryFee / 100.0));

        // Set delivery time.
        if (TextUtils.equals(Restaurant.STATUS_OPEN, restaurantDetails.statusType)) {
            mTvTime.setText(restaurantDetails.status.substring(0, restaurantDetails.status.indexOf(" mins")));
        } else {
            mTvTime.setText("-");
        }
    }
}
