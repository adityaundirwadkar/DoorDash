package com.doordash.restaurants.data;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

/**
 *
 */
public class RestaurantDetails {

    @SerializedName("id")
    @NonNull
    public Long id = Long.MIN_VALUE;

    @SerializedName("name")
    public String name;

    @SerializedName("description")
    public String description;

    @SerializedName("cover_img_url")
    public String coverImgUrl;

    @SerializedName("status_type")
    public String statusType;

    @SerializedName("status")
    public String status;

    @SerializedName("average_rating")
    public Float avgRating;

    @SerializedName("delivery_fee")
    public Long deliveryFee;

    @SerializedName("should_show_store_logo")
    public Boolean shouldShowStoreLogo;

    @SerializedName("number_of_ratings")
    public Long numberOfRatings;
}
