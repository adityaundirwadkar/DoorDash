package com.doordash.restaurants.data;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import com.doordash.base.data.BaseDao;

import java.util.List;

import io.reactivex.Flowable;

/**
 *
 */

@Dao
public abstract class RestaurantDAO extends BaseDao<Restaurant> {

    @Query("DELETE FROM " + Restaurant.TABLE_NAME)
    public abstract void deleteAll();

    @Query("SELECT * FROM " + Restaurant.TABLE_NAME + " WHERE id = :id")
    public abstract Flowable<Restaurant> getRestaurant(long id);

    @Query("SELECT * FROM " + Restaurant.TABLE_NAME + " WHERE id = :id")
    public abstract Restaurant getRestaurantSynchronous(long id);

    @Query("SELECT * FROM " + Restaurant.TABLE_NAME)
    public abstract Flowable<List<Restaurant>> getRestaurants();

    @Query("SELECT * FROM " + Restaurant.TABLE_NAME)
    public abstract List<Restaurant> getRestaurantsSynchronous();

    @Transaction
    public void clearAndInsertAll(List<Restaurant> profileModels) {
        deleteAll();
        insertAll(profileModels);
    }
}
