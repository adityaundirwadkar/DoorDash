package com.doordash.base.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.doordash.restaurants.data.Restaurant;
import com.doordash.restaurants.data.RestaurantDAO;

/**
 * Local Data storage for the records retrieved from remote.
 */

@Database(entities = {
        Restaurant.class
}, version = 1, exportSchema = false)

public abstract class DoorDashDatabase extends RoomDatabase {

    private static DoorDashDatabase sDatabase;

    private static final String DATABASE_TYPE = "owl.sqlite";

    public static DoorDashDatabase getDatabase() {
        return sDatabase;
    }

    public static void initDatabase(final Context context) {
        synchronized (DoorDashDatabase.class) {
            if (sDatabase == null) {
                sDatabase = Room
                        .databaseBuilder(context.getApplicationContext(), DoorDashDatabase.class, DATABASE_TYPE)
                        .fallbackToDestructiveMigration()
                        .allowMainThreadQueries()
                        .build();
            }
        }
    }

    public abstract RestaurantDAO getRestaurantDAO();
}
