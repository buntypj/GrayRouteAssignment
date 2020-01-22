package com.example.assignment;

/**
 ******************************************************************************
 * C O P Y R I G H T  A N D  C O N F I D E N T I A L I T Y  N O T I C E
 * <p>
 * Copyright © 2008-2009 Mobicule Technologies Pvt. Ltd. All rights reserved.
 * This is proprietary information of Mobicule Technologies Pvt. Ltd.and is
 * subject to applicable licensing agreements. Unauthorized reproduction,
 * transmission or distribution of this file and its contents is a
 * violation of applicable laws.
 ******************************************************************************
 *
 * @project PvAutomation
 */


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


/**
 *
 * <enter description here>
 *
 * @author lalita <enter lastname>
 * @see
 *
 * @createdOn 15-May-2013
 * @modifiedOn
 *
 * @copyright © 2008-2009 Mobicule Technologies Pvt. Ltd. All rights reserved.
 */
public class GpsTracker implements LocationListener {


    public final Context mContext;

    // flag for GPS status
    public boolean isGPSEnabled = false;

    // flag for network status
    public boolean isNetworkEnabled = false;

    // flag for GPS status
    public boolean canGetLocation = false;

    Location location; // location

    public double latitude; // latitude

    public double longitude; // longitude

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

    // Declaring a Location Manager
    protected LocationManager locationManager;

    public GpsTracker(Context context) {
        this.mContext = context;
        getLocation();
    }

    /**
     * Function to get the user's current location
     * @return
     */
    public Location getLocation() {
        try {
            locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);


            // getting network status
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);


            if (isGPSEnabled == false && isNetworkEnabled == false) {
                // no network provider is enabled
            } else {
                this.canGetLocation = true;
                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    ActivityCompat.requestPermissions((AppCompatActivity) mContext,
                            new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                                    Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS}, 7);

                }
                if (isNetworkEnabled) {

                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    if (locationManager != null)
                    {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null)
                        {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled)
                {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    if (locationManager != null)
                    {
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (location != null)
                        {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return location;
    }

    /**
     * Stop using GPS listener Calling this function will stop using GPS in your
     * app
     * */
    public void stopUsingGPS()
    {
        if (locationManager != null)
        {
            locationManager.removeUpdates(GpsTracker.this);
        }
    }

    /**
     * Function to get latitude
     * */
    public double getLatitude()
    {
        if (location != null)
        {
            latitude = location.getLatitude();
        }

        // return latitude
        return latitude;
    }

    /**
     * Function to get longitude
     * */
    public double getLongitude()
    {
        if (location != null)
        {
            longitude = location.getLongitude();
        }

        // return longitude
        return longitude;
    }

    /**
     * Function to check GPS/wifi enabled
     *
     * @return boolean
     * */
    public boolean canGetLocation()
    {
        return this.canGetLocation;
    }

    @Override
    public void onLocationChanged(Location location)
    {
    }

    @Override
    public void onProviderDisabled(String provider)
    {
    }

    @Override
    public void onProviderEnabled(String provider)
    {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras)
    {
    }

    public boolean getGPSEnabled()
    {
        return isGPSEnabled;
    }



}

