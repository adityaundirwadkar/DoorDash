package com.doordash.base.data;

/**
 *
 */
public class LatLong {

    private double mLatitude;
    private double mLongitude;

    public LatLong() {

    }

    public LatLong(double latitude, double longitude) {
        mLatitude = latitude;
        mLongitude = longitude;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }
}
