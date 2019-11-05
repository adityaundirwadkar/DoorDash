package com.doordash.restaurants.data;

import android.content.Context;

import com.doordash.BuildConfig;
import com.doordash.DoorDashModule;
import com.doordash.base.data.LatLong;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.readystatesoftware.chuck.ChuckInterceptor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Named;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Provides data from remote.
 * Network provider can further be abstracted to provide generic functionality of creating a
 * retrofit object and initing the service.
 */

public class RestaurantNetworkProvider {

    private final static String TAG = RestaurantNetworkProvider.class.getSimpleName();

    private static final String BASE_URL = "https://api.doordash.com/v2/";
    private static final int CONNECT_TIMEOUT = 30 * 1000; /* milliseconds */

    private final Retrofit mRetrofit;
    private final RestaurantService mService;

    @Inject
    public RestaurantNetworkProvider(@Named(DoorDashModule.APPLICATION_CONTEXT) Context appContext) {

        mRetrofit = new retrofit2.Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(getOkHttpClient(appContext))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mService = mRetrofit.create(RestaurantService.class);
    }

    private OkHttpClient getOkHttpClient(Context context) {
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        // Add network interceptor.
        if (BuildConfig.DEBUG) {
            ChuckInterceptor chuckInterceptor = new ChuckInterceptor(context);
            chuckInterceptor.showNotification(true);
            builder.addNetworkInterceptor(chuckInterceptor);
            builder.addNetworkInterceptor(new StethoInterceptor());
        }

        // Timeouts for read and write.
        builder.connectTimeout(CONNECT_TIMEOUT, TimeUnit.MILLISECONDS);
        builder.readTimeout(CONNECT_TIMEOUT, TimeUnit.MILLISECONDS);
        builder.writeTimeout(CONNECT_TIMEOUT, TimeUnit.MILLISECONDS);
        return builder.build();
    }


    public void getRestaurants(LatLong latLong, int offset, int pageSize, Callback<List<Restaurant>> callback) {
        final Map<String, String> body = new HashMap<>();
        body.put("lat", String.valueOf(latLong.getLatitude()));
        body.put("lng", String.valueOf(latLong.getLongitude()));
        body.put("offset", String.valueOf(offset));
        body.put("limit", String.valueOf(pageSize));
        Call<List<Restaurant>> call = mService.getRestaurants(body);
        call.enqueue(callback);
    }


    public void getRestaurantDetails(long restaurantId, Callback<RestaurantDetails> callback) {
        Call<RestaurantDetails> call = mService.getRestaurantDetails(restaurantId);
        call.enqueue(callback);
    }
}
