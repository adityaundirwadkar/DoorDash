package com.doordash.location;

import com.doordash.base.TestDisposableObserver;
import com.doordash.base.data.LatLong;
import com.doordash.location.domain.GetLocationUseCase;
import com.doordash.location.domain.LocationManager;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.TestScheduler;

import static org.mockito.Mockito.when;

/**
 *
 */

@RunWith(MockitoJUnitRunner.class)
public class GetLocationUseCaseTest {

    @Mock
    private LocationManager mLocationManager;

    public static final double RESTAURANT_LATITUDE = 37.422740;
    public static final double RESTAURANT_LONGITUDE = -122.139956;

    private LatLong getValidResponse() {
        return new LatLong(RESTAURANT_LATITUDE, RESTAURANT_LONGITUDE);
    }

    private LatLong getInvalidResponse() {
        return new LatLong();
    }

    @Test
    public void Location_Valid_Response() {
        when(mLocationManager.getLocationSubject()).thenReturn(Observable.just(getValidResponse()));

        // Set up variables for test
        TestScheduler testScheduler = new TestScheduler();
        TestDisposableObserver<LatLong> testObserver = new TestDisposableObserver<>();
        GetLocationUseCase getRestaurantUseCase = new GetLocationUseCase(testScheduler, testScheduler, mLocationManager);

        // Execute Test
        getRestaurantUseCase.execute(null, testObserver);
        testScheduler.triggerActions();

        // Verify Results
        Assert.assertFalse(testObserver.isDisposed());
        Assert.assertEquals(testObserver.getStarts(), 1);
        Assert.assertEquals(testObserver.getCompletions(), 1);
        List<Throwable> errors = testObserver.getErrors();
        Assert.assertNotNull(errors);
        Assert.assertEquals(errors.size(), 0);
        List<LatLong> results = testObserver.getValues();
        Assert.assertNotNull(results);
        Assert.assertEquals(results.size(), 1);

        // verify each field.
        LatLong response = results.get(0);
        LatLong validResponse = getValidResponse();

        Assert.assertEquals(response.getLatitude(), validResponse.getLatitude());
        Assert.assertEquals(response.getLongitude(), validResponse.getLongitude());

        // Cleanup
        getRestaurantUseCase.dispose();
        Assert.assertTrue(testObserver.isDisposed());
    }

    @Test
    public void Location_Invalid_Response() {
        when(mLocationManager.getLocationSubject()).thenReturn(Observable.just(getInvalidResponse()));

        // Set up variables for test
        TestScheduler testScheduler = new TestScheduler();
        TestDisposableObserver<LatLong> testObserver = new TestDisposableObserver<>();
        GetLocationUseCase getRestaurantUseCase = new GetLocationUseCase(testScheduler, testScheduler, mLocationManager);

        // Execute Test
        getRestaurantUseCase.execute(null, testObserver);
        testScheduler.triggerActions();

        // Verify Results
        Assert.assertFalse(testObserver.isDisposed());
        Assert.assertEquals(testObserver.getStarts(), 1);
        Assert.assertEquals(testObserver.getCompletions(), 1);
        List<Throwable> errors = testObserver.getErrors();
        Assert.assertNotNull(errors);
        Assert.assertEquals(errors.size(), 0);
        List<LatLong> results = testObserver.getValues();
        Assert.assertNotNull(results);
        Assert.assertEquals(results.size(), 1);

        // verify each field.
        LatLong response = results.get(0);
        LatLong validResponse = getInvalidResponse();

        Assert.assertEquals(response.getLatitude(), validResponse.getLatitude());
        Assert.assertEquals(response.getLongitude(), validResponse.getLongitude());

        // Cleanup
        getRestaurantUseCase.dispose();
        Assert.assertTrue(testObserver.isDisposed());
    }
}
