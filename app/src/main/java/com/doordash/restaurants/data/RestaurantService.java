package com.doordash.restaurants.data;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * Retrofit service.
 */
public interface RestaurantService {

    @GET("restaurant/")
    Call<List<Restaurant>> getRestaurants(@QueryMap Map<String, String> params);

    @GET("restaurant/{id}")
    Call<RestaurantDetails> getRestaurantDetails(@Path("id") long restaurantId);
}
