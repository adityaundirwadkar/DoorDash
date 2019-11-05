package com.doordash.restaurants.data;

import com.doordash.base.data.DoorDashDatabase;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Provides data from local Database.
 */
public class RestaurantLocalProvider {

    private final RestaurantDAO mLocalDAO;

    @Inject
    public RestaurantLocalProvider() {
        mLocalDAO = DoorDashDatabase.getDatabase().getRestaurantDAO();
    }

    public void insertRestaurant(Restaurant restaurant) {
        mLocalDAO.insert(restaurant);
    }

    public void insertRestaurants(List<Restaurant> restaurants) {
        mLocalDAO.insertAll(restaurants);
    }

    public Observable<Restaurant> getRestaurant(long id) {
        return mLocalDAO.getRestaurant(id).toObservable();
    }

    public Restaurant getRestaurantSynchronous(long id) {
        return mLocalDAO.getRestaurantSynchronous(id);
    }

    public Observable<List<Restaurant>> getRestaurants() {
        return mLocalDAO.getRestaurants().toObservable();
    }

    public List<Restaurant> getRestaurantsSynchronous() {
        return mLocalDAO.getRestaurantsSynchronous();
    }

    public void updateRestaurant(Restaurant restaurant) {
        mLocalDAO.update(restaurant);
    }

    public void deleteRestaurant(Restaurant restaurant) {
        mLocalDAO.delete(restaurant);
    }

    public void deleteAllRestaurant() {
        mLocalDAO.deleteAll();
    }
}
