package com.doordash.restaurants.data;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.doordash.base.data.BaseModel;
import com.google.gson.annotations.SerializedName;

/**
 * Data base entity to store a single row of Restaurant.
 */

@Entity(tableName = Restaurant.TABLE_NAME)
public class Restaurant extends BaseModel {

    public static final String TABLE_NAME = "restaurants";

    public static final String STATUS_OPEN = "open";
    public static final String STATUS_CLOSED = "pre-order";
    public static final String STATUS_CLOSED_RESPONSE = "Closed";

    @ColumnInfo(name = "id")
    @PrimaryKey
    @NonNull
    public Long id = Long.MIN_VALUE;

    @ColumnInfo(name = "name")
    @SerializedName("name")
    public String name;

    @ColumnInfo(name = "description")
    @SerializedName("description")
    public String description;

    @ColumnInfo(name = "cover_img_url")
    @SerializedName("cover_img_url")
    public String coverImgUrl;

    @ColumnInfo(name = "header_img_url")
    @SerializedName("header_img_url")
    public String headerImgUrl;

    @ColumnInfo(name = "status_type")
    @SerializedName("status_type")
    public String statusType;

    @ColumnInfo(name = "status")
    @SerializedName("status")
    public String status;

    @ColumnInfo(name = "average_rating")
    @SerializedName("average_rating")
    public Double avgRating;

    @ColumnInfo(name = "apiIndex")
    @NonNull
    public Long apiIndex;

    public Restaurant() {

    }
}
