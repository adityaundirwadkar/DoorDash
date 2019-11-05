package com.doordash.restaurant;

import com.doordash.base.TestDisposableObserver;
import com.doordash.restaurants.data.Restaurant;
import com.doordash.restaurants.data.RestaurantRepository;
import com.doordash.restaurants.domain.GetRestaurantUseCase;

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
public class GetRestaurantUseCaseTest {

    @Mock
    private RestaurantRepository mRestaurantRepository;

    private final long mRestaurantId = 666;

    private Restaurant getValidResponse() {
        Restaurant validResponse = new Restaurant();
        validResponse.id = mRestaurantId;
        validResponse.name = "name";
        validResponse.description = "description";
        validResponse.coverImgUrl = "coverImgUrl";
        validResponse.headerImgUrl = "headerImgUrl";
        validResponse.statusType = "statusType";
        validResponse.status = "status";
        validResponse.avgRating = 5.0;
        validResponse.apiIndex = 1000l;
        return validResponse;
    }

    private Restaurant getInvalidResponse() {
        return new Restaurant();
    }

    @Test
    public void Restaurant_Valid_Response() {
        when(mRestaurantRepository.getRestaurant(mRestaurantId))
                .thenReturn(Observable.just(getValidResponse()));

        // Set up variables for test
        TestScheduler testScheduler = new TestScheduler();
        TestDisposableObserver<Restaurant> testObserver = new TestDisposableObserver<>();
        GetRestaurantUseCase getRestaurantUseCase = new GetRestaurantUseCase(testScheduler, testScheduler, mRestaurantRepository);

        // Execute Test
        getRestaurantUseCase.execute(mRestaurantId, testObserver);
        testScheduler.triggerActions();

        // Verify Results
        Assert.assertFalse(testObserver.isDisposed());
        Assert.assertEquals(testObserver.getStarts(), 1);
        Assert.assertEquals(testObserver.getCompletions(), 1);
        List<Throwable> errors = testObserver.getErrors();
        Assert.assertNotNull(errors);
        Assert.assertEquals(errors.size(), 0);
        List<Restaurant> results = testObserver.getValues();
        Assert.assertNotNull(results);
        Assert.assertEquals(results.size(), 1);

        // verify each field.
        Restaurant response = results.get(0);
        Restaurant validResponse = getValidResponse();

        Assert.assertEquals(response.id, validResponse.id);
        Assert.assertEquals(response.name, validResponse.name);
        Assert.assertEquals(response.description, validResponse.description);
        Assert.assertEquals(response.coverImgUrl, validResponse.coverImgUrl);
        Assert.assertEquals(response.headerImgUrl, validResponse.headerImgUrl);
        Assert.assertEquals(response.statusType, validResponse.statusType);
        Assert.assertEquals(response.status, validResponse.status);

        Assert.assertEquals(response.avgRating, validResponse.avgRating);
        Assert.assertNotNull(response.avgRating);

        Assert.assertEquals(response.apiIndex, validResponse.apiIndex);
        Assert.assertNotNull(response.apiIndex);

        // Cleanup
        getRestaurantUseCase.dispose();
        Assert.assertTrue(testObserver.isDisposed());
    }

    @Test
    public void Restaurant_Invalid_Response() {
        when(mRestaurantRepository.getRestaurant(mRestaurantId))
                .thenReturn(Observable.just(getInvalidResponse()));

        // Set up variables for test
        TestScheduler testScheduler = new TestScheduler();
        TestDisposableObserver<Restaurant> testObserver = new TestDisposableObserver<>();
        GetRestaurantUseCase getRestaurantUseCase = new GetRestaurantUseCase(testScheduler, testScheduler, mRestaurantRepository);

        // Execute Test
        getRestaurantUseCase.execute(mRestaurantId, testObserver);
        testScheduler.triggerActions();

        // Verify Results
        Assert.assertFalse(testObserver.isDisposed());
        Assert.assertEquals(testObserver.getStarts(), 1);
        Assert.assertEquals(testObserver.getCompletions(), 1);
        List<Throwable> errors = testObserver.getErrors();
        Assert.assertNotNull(errors);
        Assert.assertEquals(errors.size(), 0);
        List<Restaurant> results = testObserver.getValues();
        Assert.assertNotNull(results);
        Assert.assertEquals(results.size(), 1);

        // verify each field.
        Restaurant response = results.get(0);
        Restaurant validResponse = getInvalidResponse();

        Assert.assertEquals(response.id, validResponse.id);
        Assert.assertEquals(response.name, validResponse.name);
        Assert.assertEquals(response.description, validResponse.description);
        Assert.assertEquals(response.coverImgUrl, validResponse.coverImgUrl);
        Assert.assertEquals(response.headerImgUrl, validResponse.headerImgUrl);
        Assert.assertEquals(response.statusType, validResponse.statusType);
        Assert.assertEquals(response.status, validResponse.status);

        Assert.assertEquals(response.avgRating, validResponse.avgRating);
        Assert.assertNull(response.avgRating);

        Assert.assertEquals(response.apiIndex, validResponse.apiIndex);
        Assert.assertNull(response.apiIndex);

        // Cleanup
        getRestaurantUseCase.dispose();
        Assert.assertTrue(testObserver.isDisposed());
    }
}
