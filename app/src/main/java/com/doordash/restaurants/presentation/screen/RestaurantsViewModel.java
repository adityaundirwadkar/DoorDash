package com.doordash.restaurants.presentation.screen;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.doordash.base.data.LatLong;
import com.doordash.location.domain.GetLocationUseCase;
import com.doordash.restaurants.data.Restaurant;
import com.doordash.restaurants.domain.GetRefreshingStateUseCase;
import com.doordash.restaurants.domain.GetReloadingRestaurantListUseCase;
import com.doordash.restaurants.domain.RefreshRestaurantListPageUseCase;
import com.doordash.restaurants.domain.RefreshRestaurantListUseCase;
import com.doordash.restaurants.presentation.rowitem.RestaurantViewModel;
import com.doordash.restaurants.presentation.rowitem.RestaurantViewModelFactory;
import com.doordash.restaurants.presentation.screen.restaurant.RestaurantDetailsNavigator;
import com.doordash.utils.ListUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.observers.DisposableObserver;

/**
 *
 */
public class RestaurantsViewModel extends ViewModel {

    // Use cases
    private final RefreshRestaurantListUseCase mRefreshRestaurantListUseCase;
    private final RefreshRestaurantListPageUseCase mRefreshRestaurantListPageUseCase;
    private final GetRefreshingStateUseCase mGetRefreshingStateUseCase;
    private final GetReloadingRestaurantListUseCase mGetReloadingRestaurantListUseCase;
    private final GetLocationUseCase mGetLocationUseCase;

    // Live Data
    private final MutableLiveData<Boolean> mRestaurantsAvailableLiveData;
    private final MutableLiveData<Boolean> mReloadingLiveData;

    // Auto Factories
    private final RestaurantViewModelFactory mRestaurantViewModelFactory;

    // Navigators
    private final RestaurantDetailsNavigator mRestaurantDetailsNavigator;

    // State containers.
    private boolean mIsRefreshingStatus;
    private boolean mIsReloadingStatus;
    private LatLong mLatLong;

    private final Map<Long, RestaurantViewModel> mRestaurantViewModels;

    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int mCurrentPage = PAGE_START;
    private int mTotalPages = TOTAL_PAGES;           // Update the value when repo cant provide anymore data.
    private static final int PAGE_START = 1;
    private static final int TOTAL_PAGES = Integer.MAX_VALUE;
    private static final int REFRESH_ATTEMPTS = 5;
    private int mRefreshAttempts = 0;


    @Inject
    public RestaurantsViewModel(RefreshRestaurantListUseCase refreshRestaurantListUseCase,
                                RefreshRestaurantListPageUseCase refreshRestaurantListPageUseCase,
                                GetRefreshingStateUseCase getRefreshingStateUseCase,
                                GetReloadingRestaurantListUseCase getReloadingRestaurantListUseCase,
                                GetLocationUseCase getLocationUseCase,
                                RestaurantViewModelFactory restaurantViewModelFactory,
                                RestaurantDetailsNavigator restaurantDetailsNavigator) {

        // Use cases.
        mRefreshRestaurantListUseCase = refreshRestaurantListUseCase;
        mRefreshRestaurantListPageUseCase = refreshRestaurantListPageUseCase;
        mGetRefreshingStateUseCase = getRefreshingStateUseCase;
        mGetReloadingRestaurantListUseCase = getReloadingRestaurantListUseCase;
        mGetLocationUseCase = getLocationUseCase;

        mRestaurantViewModelFactory = restaurantViewModelFactory;

        mRestaurantDetailsNavigator = restaurantDetailsNavigator;

        // Live Data
        mRestaurantsAvailableLiveData = new MutableLiveData<>();
        mReloadingLiveData = new MutableLiveData<>();

        // Data Structures
        mRestaurantViewModels = new HashMap<>();

        getRefreshStatusUpdates();
        getReloadingRestaurantListUpdates();
        getLocationUpdates();
    }

    public boolean getLocationUpdates() {
        if (!mIsRefreshingStatus) {
            mGetLocationUseCase.execute(null, new DisposableObserver<LatLong>() {
                @Override
                public void onNext(LatLong latLong) {
                    if (latLong!= null) {
                        setLatLong(latLong);
                        reloadRestaurantList();
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

    private void reloadRestaurantList() {
        setLoading(true);

        mRefreshRestaurantListUseCase.execute(getLatLong(), new DisposableObserver<List<Restaurant>>() {
            @Override
            public void onNext(List<Restaurant> restaurants) {
                if (!isReloadingStatus()) {
                    return;
                }
                resetRefreshAttempts();
                setTotalPages(TOTAL_PAGES);
                setCurrentPage(PAGE_START);

                // Convert to ViewModels.
                mRestaurantViewModels.clear();
                for (Restaurant restaurant : restaurants) {
                    RestaurantViewModel viewModel = mRestaurantViewModelFactory.create(restaurant);
                    mRestaurantViewModels.put(restaurant.id, viewModel);
                }
                setLoading(false);
                mRestaurantsAvailableLiveData.setValue(true);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void updateRestaurantList() {
        if (mIsRefreshingStatus) {
            return;
        }
        // mRefreshRestaurantListUseCase.clear();
        mRefreshRestaurantListPageUseCase.clear();
        RefreshRestaurantListPageUseCase.Data data = RefreshRestaurantListPageUseCase.Data.create(getLatLong(), mRestaurantViewModels.size());
        mRefreshRestaurantListPageUseCase.execute(data, new DisposableObserver<List<Restaurant>>() {
            @Override
            public void onNext(List<Restaurant> restaurants) {

                final List<RestaurantViewModel> list = getRestaurantViewModelsList();
                if (ListUtils.equalSize(restaurants, list)) {
                    if (getRefreshAttempts() >= REFRESH_ATTEMPTS) {
                        setTotalPages(getCurrentPage());
                        setLastPage(true);
                    } else {
                        setRefreshAttempts(getRefreshAttempts() + 1);
                    }
                }
                for (Restaurant restaurant : restaurants) {
                    if (!mRestaurantViewModels.containsKey(restaurant.id)) {
                        RestaurantViewModel viewModel = mRestaurantViewModelFactory.create(restaurant);
                        mRestaurantViewModels.put(restaurant.id, viewModel);
                    }
                }
                setLoading(false);
                mRestaurantsAvailableLiveData.setValue(true);
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

    private void getReloadingRestaurantListUpdates() {
        mGetReloadingRestaurantListUseCase.execute(null, new DisposableObserver<Boolean>() {
            @Override
            public void onNext(Boolean aBoolean) {
                if (aBoolean != null) {
                    setReloadingStatus(aBoolean);
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

    public void navigateRestaurantDetails(Restaurant restaurant) {
        mRestaurantDetailsNavigator.navigate(RestaurantDetailsNavigator.Data.create(restaurant.id));
    }

    public void loadMoreItems() {

        if (getTotalPages() <= getCurrentPage()) {
            return;
        }
        setLoading(true);
        setCurrentPage(getCurrentPage() + 1);
        updateRestaurantList();
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    public boolean isLastPage() {
        return isLastPage;
    }

    public void setLastPage(boolean lastPage) {
        isLastPage = lastPage;
    }

    public int getCurrentPage() {
        return mCurrentPage;
    }

    public void setCurrentPage(int mCurrentPage) {
        this.mCurrentPage = mCurrentPage;
    }

    public int getTotalPages() {
        return mTotalPages;
    }

    public void setTotalPages(int mTotalPages) {
        this.mTotalPages = mTotalPages;
    }

    public int getRefreshAttempts() {
        return mRefreshAttempts;
    }

    public void setRefreshAttempts(int mRefreshAttempts) {
        this.mRefreshAttempts = mRefreshAttempts;
    }

    public void resetRefreshAttempts() {
        setRefreshAttempts(0);
    }

    public boolean isReloadingStatus() {
        return mIsReloadingStatus;
    }

    public void setReloadingStatus(boolean reloadingStatus) {
        mIsReloadingStatus = reloadingStatus;
        mReloadingLiveData.setValue(reloadingStatus);
    }

    public Map<Long, RestaurantViewModel> getRestaurantViewModels() {
        return mRestaurantViewModels;
    }

    public List<RestaurantViewModel> getRestaurantViewModelsList() {
        return new ArrayList<>(getRestaurantViewModels().values());
    }

    public LatLong getLatLong() {
        return mLatLong;
    }

    public void setLatLong(LatLong mLatLong) {
        this.mLatLong = mLatLong;
    }

    // Getters for live data.
    public MutableLiveData<Boolean> getRestaurantsAvailable() {
        return mRestaurantsAvailableLiveData;
    }

    public MutableLiveData<Boolean> getReloadingLiveData() {
        return mReloadingLiveData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        mRefreshRestaurantListUseCase.dispose();
        mRefreshRestaurantListPageUseCase.dispose();
        mGetRefreshingStateUseCase.dispose();
        mGetReloadingRestaurantListUseCase.dispose();
        mGetLocationUseCase.dispose();
    }
}
