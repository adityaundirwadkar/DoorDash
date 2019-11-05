package com.doordash;

import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.doordash.presentation.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

/**
 *
 */

@RunWith(AndroidJUnit4.class)
public class RestaurantFragmentTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testRestaurantFragmentVisibility() {
        onView(withId(R.id.fragment_container)).check(matches(isDisplayed()));
    }

    @Test
    public void testProgressBarVisibility() {
        onView(withId(R.id.rl_status_container)).check(matches(isDisplayed()));
        onView(withId(R.id.pb_status)).check(matches(isDisplayed()));
        onView(withId(R.id.tv_status)).check(matches(isDisplayed()));

        onView(withId(R.id.rl_status_container)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.pb_status)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.tv_status)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }


    @Test
    public void testProgressBarHidden() throws InterruptedException {
        Thread.sleep(TimeUnit.SECONDS.toMillis(10));
        onView(withId(R.id.rl_status_container)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
    }
}
