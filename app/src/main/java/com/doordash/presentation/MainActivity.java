package com.doordash.presentation;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.doordash.ComponentProvider;
import com.doordash.R;
import com.doordash.base.presentation.DoorDashActivity;
import com.doordash.base.presentation.DoorDashFragment;
import com.readystatesoftware.chuck.Chuck;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends DoorDashActivity {

    @Inject
    ViewModelProvider.Factory mViewModelFactory;

    private MainActivityViewModel mViewModel;

    // View Bindings.
    @BindView(R.id.iv_refresh_list)
    ImageView mIvRefreshList;

    @BindView(R.id.iv_debug_traffic)
    ImageView mIvChuck;

    @BindView(R.id.tb_doordash)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Inject MainActivity.
        ComponentProvider.get().inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(MainActivityViewModel.class);

        // Launch the Restaurant Fragment.
        navigateRestaurantFragment();
    }

    private void navigateRestaurantFragment() {
        mViewModel.navigateRestaurantsFragment();
    }

    @OnClick(R.id.iv_refresh_list)
    public void onClickedRefresh() {
        // Refresh the list via manager/repo.
        boolean result = mViewModel.getLocationUpdates();
        Toast.makeText(this,
                getText(result ? R.string.toast_refresh_in_progress : R.string.toast_refresh_failed),
                Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.iv_debug_traffic)
    public void onClickedChuck() {
        // Launch chuck.
        startActivity(Chuck.getLaunchIntent(this));
    }

    @Override
    public void onBackPressed() {

        // Imitate backpress to quit the app instead of popping the last fragment.
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        // Provide "onBackPressed" to the topmost fragment.
        final int top = fragments.size() - 1;
        boolean shouldDestroyActivity = false;
        if (fragments.get(top) instanceof DoorDashFragment) {
            shouldDestroyActivity = ((DoorDashFragment)fragments.get(top)).shouldDestroyActivity();
        }
        if (shouldDestroyActivity) {
            finish();
        } else {
            super.onBackPressed();
        }
    }
}
