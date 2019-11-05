package com.doordash.restaurant;

import com.doordash.base.TestDisposableObserver;
import com.doordash.restaurants.data.RestaurantDetails;
import com.doordash.restaurants.data.RestaurantRepository;
import com.doordash.restaurants.domain.GetRestaurantDetailsUseCase;

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
public class GetRestaurantDetailsUseCaseTest {

    @Mock
    private RestaurantRepository mRestaurantRepository;


    private final long mRestaurantId = 666;

    private RestaurantDetails getValidResponse() {
        RestaurantDetails validResponse = new RestaurantDetails();
        validResponse.id = mRestaurantId;
        validResponse.name = "name";
        validResponse.description = "description";
        validResponse.coverImgUrl = "coverImgUrl";
        validResponse.statusType = "statusType";
        validResponse.status = "status";
        validResponse.avgRating = 5.0f;
        validResponse.deliveryFee = 0l;
        validResponse.shouldShowStoreLogo = true;
        validResponse.numberOfRatings = 1000l;
        return validResponse;
    }

    private RestaurantDetails getInvalidResponse() {
        return new RestaurantDetails();
    }

    @Test
    public void RestaurantDetails_Valid_Response() {
        when(mRestaurantRepository.getRestaurantDetails(mRestaurantId))
                .thenReturn(Observable.just(getValidResponse()));

        // Set up variables for test
        TestScheduler testScheduler = new TestScheduler();
        TestDisposableObserver<RestaurantDetails> testObserver = new TestDisposableObserver<>();
        GetRestaurantDetailsUseCase getRestaurantDetailsUseCase = new GetRestaurantDetailsUseCase(testScheduler, testScheduler, mRestaurantRepository);

        // Execute Test
        getRestaurantDetailsUseCase.execute(mRestaurantId, testObserver);
        testScheduler.triggerActions();

        // Verify Results
        Assert.assertFalse(testObserver.isDisposed());
        Assert.assertEquals(testObserver.getStarts(), 1);
        Assert.assertEquals(testObserver.getCompletions(), 1);
        List<Throwable> errors = testObserver.getErrors();
        Assert.assertNotNull(errors);
        Assert.assertEquals(errors.size(), 0);
        List<RestaurantDetails> results = testObserver.getValues();
        Assert.assertNotNull(results);
        Assert.assertEquals(results.size(), 1);

        // verify each field.
        RestaurantDetails response = results.get(0);
        RestaurantDetails validResponse = getValidResponse();

        Assert.assertEquals(response.id, validResponse.id);
        Assert.assertEquals(response.name, validResponse.name);
        Assert.assertEquals(response.description, validResponse.description);
        Assert.assertEquals(response.coverImgUrl, validResponse.coverImgUrl);
        Assert.assertEquals(response.statusType, validResponse.statusType);
        Assert.assertEquals(response.status, validResponse.status);

        Assert.assertEquals(response.avgRating, validResponse.avgRating);
        Assert.assertNotNull(response.avgRating);

        Assert.assertEquals(response.deliveryFee, validResponse.deliveryFee);
        Assert.assertNotNull(response.deliveryFee);

        Assert.assertEquals(response.shouldShowStoreLogo, validResponse.shouldShowStoreLogo);
        Assert.assertNotNull(response.shouldShowStoreLogo);

        Assert.assertEquals(response.numberOfRatings, validResponse.numberOfRatings);
        Assert.assertNotNull(response.numberOfRatings);

        // Cleanup
        getRestaurantDetailsUseCase.dispose();
        Assert.assertTrue(testObserver.isDisposed());

    }

    @Test
    public void RestaurantDetails_Invalid_Response() {
        when(mRestaurantRepository.getRestaurantDetails(mRestaurantId))
                .thenReturn(Observable.just(getInvalidResponse()));

        // Set up variables for test
        TestScheduler testScheduler = new TestScheduler();
        TestDisposableObserver<RestaurantDetails> testObserver = new TestDisposableObserver<>();
        GetRestaurantDetailsUseCase getRestaurantDetailsUseCase = new GetRestaurantDetailsUseCase(testScheduler, testScheduler, mRestaurantRepository);

        // Execute Test
        getRestaurantDetailsUseCase.execute(mRestaurantId, testObserver);
        testScheduler.triggerActions();

        // Verify Results
        Assert.assertFalse(testObserver.isDisposed());
        Assert.assertEquals(testObserver.getStarts(), 1);
        Assert.assertEquals(testObserver.getCompletions(), 1);
        List<Throwable> errors = testObserver.getErrors();
        Assert.assertNotNull(errors);
        Assert.assertEquals(errors.size(), 0);
        List<RestaurantDetails> results = testObserver.getValues();
        Assert.assertNotNull(results);
        Assert.assertEquals(results.size(), 1);

        // verify each field.
        RestaurantDetails response = results.get(0);
        RestaurantDetails invalidResponse = getInvalidResponse();

        Assert.assertEquals(response.id, invalidResponse.id);
        Assert.assertEquals(response.name, invalidResponse.name);
        Assert.assertEquals(response.description, invalidResponse.description);
        Assert.assertEquals(response.coverImgUrl, invalidResponse.coverImgUrl);
        Assert.assertEquals(response.statusType, invalidResponse.statusType);
        Assert.assertEquals(response.status, invalidResponse.status);
        Assert.assertEquals(response.avgRating, invalidResponse.avgRating);
        Assert.assertNull(response.avgRating);

        Assert.assertEquals(response.deliveryFee, invalidResponse.deliveryFee);
        Assert.assertNull(response.deliveryFee);

        Assert.assertEquals(response.shouldShowStoreLogo, invalidResponse.shouldShowStoreLogo);
        Assert.assertNull(response.shouldShowStoreLogo);

        Assert.assertEquals(response.numberOfRatings, invalidResponse.numberOfRatings);
        Assert.assertNull(response.numberOfRatings);

        // Cleanup
        getRestaurantDetailsUseCase.dispose();
        Assert.assertTrue(testObserver.isDisposed());
    }
}
