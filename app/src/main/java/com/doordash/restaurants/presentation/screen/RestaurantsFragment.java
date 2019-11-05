package com.doordash.restaurants.presentation.screen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.doordash.ComponentProvider;
import com.doordash.R;
import com.doordash.base.presentation.DoorDashFragment;
import com.doordash.base.presentation.recyclerview.PaginationScrollListener;
import com.doordash.base.presentation.recyclerview.RecyclerViewAdapter;
import com.doordash.restaurants.data.Restaurant;
import com.doordash.restaurants.presentation.rowitem.RestaurantViewModel;
import com.doordash.utils.MapUtils;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 */

public class RestaurantsFragment extends DoorDashFragment<RestaurantsViewModel> implements RestaurantViewModel.Interactor {

    public static final String TAG = RestaurantsFragment.class.getSimpleName();

    @BindView(R.id.rl_status_container)
    RelativeLayout mRlStatusContainer;

    @BindView(R.id.pb_status)
    ProgressBar mPbStatus;

    @BindView(R.id.tv_status)
    TextView mTvStatus;

    @BindView(R.id.rv_restaurants)
    RecyclerView mRvRestaurantsRecyclerView;

    @Inject
    RecyclerViewAdapter mRestaurantsAdapter;

    public static RestaurantsFragment create() {
        return new RestaurantsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        ComponentProvider.get().newRestaurantComponent(new RestaurantModule(this)).inject(this);
        View root = inflater.inflate(R.layout.fragment_restaurants, container, false);
        ButterKnife.bind(this, root);
        mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(RestaurantsViewModel.class);
        initRestaurantScreen();
        refreshRestaurantList();
        // Init observers.
        initRestaurantListObserver();
        initReloadingListObserver();
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onRestaurantSelected(Restaurant restaurant) {
        mViewModel.navigateRestaurantDetails(restaurant);
    }

    @Override
    public boolean shouldDestroyActivity() {
        return true;
    }

    private void initRestaurantScreen() {
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRvRestaurantsRecyclerView.setLayoutManager(linearLayoutManager);

        ((SimpleItemAnimator) mRvRestaurantsRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        mRvRestaurantsRecyclerView.getItemAnimator().setChangeDuration(0);
        mRvRestaurantsRecyclerView.setItemAnimator(null);

        mRvRestaurantsRecyclerView.addOnScrollListener(setRecyclerViewScrollListener(linearLayoutManager));
        mRvRestaurantsRecyclerView.setAdapter(mRestaurantsAdapter);
    }

    private RecyclerView.OnScrollListener setRecyclerViewScrollListener(LinearLayoutManager layoutManager) {
        return new PaginationScrollListener(layoutManager) {
            @Override
            protected void loadMoreItems() {
                mViewModel.loadMoreItems();
            }

            @Override
            public int getTotalPageCount() {
                return mViewModel.getTotalPages();
            }

            @Override
            public boolean isLastPage() {
                return mViewModel.isLastPage();
            }

            @Override
            public boolean isLoading() {
                return mViewModel.isLoading();
            }
        };
    }

    private void refreshRestaurantList() {
        mRlStatusContainer.setVisibility(View.VISIBLE);
        mPbStatus.setVisibility(View.VISIBLE);
        mTvStatus.setText(getString(R.string.status_loading_restaurants));
    }

    private void updateRestaurantList() {
        mRlStatusContainer.setVisibility(View.GONE);
    }

    private void updateErrorRestaurantList() {
        mRlStatusContainer.setVisibility(View.VISIBLE);
        mPbStatus.setVisibility(View.GONE);
        mTvStatus.setText(getString(R.string.status_error));
    }

    private void initRestaurantListObserver() {
        mViewModel.getRestaurantsAvailable().observe(this, isAvailable -> {
            if (MapUtils.isEmpty(mViewModel.getRestaurantViewModels())) {
                // DO something about empty list.
            }
            updateRestaurantList();
            // Set onClick Listener for each view holder.
            List<RestaurantViewModel> viewModels = mViewModel.getRestaurantViewModelsList();
            viewModels.sort((v1, v2) -> Long.compare(v1.getApiRestaurantIndex(), v2.getApiRestaurantIndex()));

            for (RestaurantViewModel viewModel : viewModels) {
                viewModel.setInteractor(this);
            }
            mRestaurantsAdapter.setViewModels(viewModels);
        });
    }

    private void initReloadingListObserver() {
        mViewModel.getReloadingLiveData().observe(this, isReloading -> {
            if (isReloading) {
                refreshRestaurantList();
            }
        });
    }
}
