package com.doordash.base.data;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Update;

import java.util.List;

interface BaseDaoInterface<T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(T obj);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<T> items);

    @Update
    void update(T obj);

    @Delete
    void delete(T obj);
}

public abstract class BaseDao<T> implements BaseDaoInterface<T> {

}
