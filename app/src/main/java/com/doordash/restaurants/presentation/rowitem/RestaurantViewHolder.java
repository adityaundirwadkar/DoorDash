package com.doordash.restaurants.presentation.rowitem;

import android.graphics.PorterDuff;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.lifecycle.Observer;

import com.doordash.R;
import com.doordash.base.presentation.recyclerview.ViewHolder;

import butterknife.BindView;

/**
 *
 */
public class RestaurantViewHolder extends ViewHolder<RestaurantViewModel> {

    @BindView(R.id.ll_restaurant_data)
    LinearLayout mLlRestaurant;

    @BindView(R.id.iv_restaurant_logo)
    ImageView mIvLogo;

    @BindView(R.id.tv_restaurant_name)
    TextView mTvName;

    @BindView(R.id.tv_restaurant_cuisine)
    TextView mTvCuisine;

    @BindView(R.id.tv_restaurant_delivery)
    TextView mTvDeliveryTime;

    public RestaurantViewHolder(ViewGroup parent) {
        super(parent, R.layout.row_restaurant);
    }

    @Override
    public void bindViewModel(RestaurantViewModel viewModel) {
        super.bindViewModel(viewModel);

        // Observers.
        initRestaurantLogoObserver();
        //Init views.
        setupImages();
        setupText();
    }

    @Override
    public void onRecycled() {
        super.onRecycled();
        resetViews();
        // Remove live data updates.
        if (mViewModel != null) {
            mViewModel.getRestaurantLogoLiveData().removeObserver(mLogoObserver);
        }
    }

    private void resetViews() {
        mIvLogo.setImageBitmap(null);
        mTvName.setText(null);
        mTvCuisine.setText(null);
        mTvDeliveryTime.setText(null);
    }

    private void setupImages() {
        mViewModel.loadRestaurantLogo(getContext());
    }

    private void setupText() {
        mTvName.setText(mViewModel.getRestaurantName());
        mTvCuisine.setText(mViewModel.getRestaurantDescription());
        mTvDeliveryTime.setText(mViewModel.getRestaurantDeliveryTime());
    }

    private void initRestaurantLogoObserver() {
        mViewModel.getRestaurantLogoLiveData().observeForever(mLogoObserver);
    }

    // Observer for image download.
    private Observer<RestaurantViewModel.LOGO_STATES> mLogoObserver = logoState -> {
        if (mViewModel == null) {
            return;
        }
        switch (logoState) {
            case UNKNOWN:
                mViewModel.loadRestaurantLogo(getContext());
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
    };
}
