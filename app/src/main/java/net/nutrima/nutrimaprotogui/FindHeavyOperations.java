package net.nutrima.nutrimaprotogui;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;

import net.nutrima.aws.AmazonClientManager;
import net.nutrima.engine.NutrimaMetrics;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import static com.facebook.FacebookSdk.getApplicationContext;
import static net.nutrima.nutrimaprotogui.MapFragment.TAG;

/**
 * Created by melsisi on 9/26/2016.
 */
public class FindHeavyOperations implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static FindHeavyOperations ourInstance = new FindHeavyOperations();
    private static ArrayList<Business> businessesToMap;
    private static Activity activity;
    private final int MY_PERMISSIONS_REQUEST = 0;
    private static Location mLastLocation;
    private static GoogleApiClient mGoogleApiClient;
    private static String city;

    private static boolean yelpBusinessesReady = false;

    public static FindHeavyOperations getInstance() {
        if(ourInstance == null) {
            ourInstance = new FindHeavyOperations();
        }
        return ourInstance;
    }

    protected FindHeavyOperations() {
        businessesToMap = new ArrayList<Business>();
    }

    public void getGoogleClient(){
        if(mGoogleApiClient != null)
            return;

        // Create an instance of GoogleAPIClient.
        mGoogleApiClient = new GoogleApiClient.Builder(activity.getApplicationContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();
        mGoogleApiClient.connect();
    }

    public void populateBusinessesAsync(){
        Yelp.getYelp(activity.getBaseContext());
        new Thread(new Runnable() {
            public void run() {
                businessesToMap = Yelp.getYelp(activity.getBaseContext()).search("food",
                        mLastLocation.getLatitude(),
                        mLastLocation.getLongitude(),
                        city);
                yelpBusinessesReady = true;
            }
        }).start();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        // 1- Get my current location ////////////////////////////////////
        if (ActivityCompat.checkSelfPermission(activity.getBaseContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST);
            return;
        }


        /*
        When running in emulator, use home location by default
         */

        if(Globals.getInstance().amIRunningInEmulator()) {
            mLastLocation = new Location("HOME_ADDRESS");
            mLastLocation.setLatitude(33.7131339d);//your coords of course
            mLastLocation.setLongitude(-117.7853251d);
        }
        else
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        Geocoder gcd = new Geocoder(activity, Locale.getDefault());
        try {
            List<Address> addresses = gcd.getFromLocation(mLastLocation.getLatitude(),
                    mLastLocation.getLongitude(),
                    1);
            if(addresses.size() > 0)
                city = addresses.get(0).getAddressLine(0) + " " + addresses.get(0).getAddressLine(1);
            //addresses.get(0).getLocality() + ", " + addresses.get(0).getAdminArea();
        } catch (IOException e) {
            e.printStackTrace();
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setMessage("Unable to detect your location.")
                    .setTitle("Oops");

            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    activity.startActivity(intent);
                }
            });
            AlertDialog dialog = builder.create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            return;
        }
        Log.d(TAG, mLastLocation.toString());
        //////////////////////////////////////////////////////////////////

        populateBusinessesAsync();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public static Location getmLastLocation() {
        return mLastLocation;
    }

    public static void setmLastLocation(Location mLastLocation) {
        FindHeavyOperations.mLastLocation = mLastLocation;
    }

    public static String getCity() {
        return city;
    }

    public static void setCity(String city) {
        FindHeavyOperations.city = city;
    }

    public static GoogleApiClient getmGoogleApiClient() {
        return mGoogleApiClient;
    }

    public static void setmGoogleApiClient(GoogleApiClient mGoogleApiClient) {
        FindHeavyOperations.mGoogleApiClient = mGoogleApiClient;
    }

    public static ArrayList<Business> getBusinessesToMap() {
        return businessesToMap;
    }

    public static void setBusinessesToMap(ArrayList<Business> businessesToMap) {
        FindHeavyOperations.businessesToMap = businessesToMap;
    }
    public static Activity getActivity() {
        return activity;
    }

    public static void setActivity(Activity activity) {
        FindHeavyOperations.activity = activity;
    }

    public static boolean isYelpBusinessesReady() {
        return yelpBusinessesReady;
    }

    public static void setYelpBusinessesReady(boolean yelpBusinessesReady) {
        FindHeavyOperations.yelpBusinessesReady = yelpBusinessesReady;
    }
}
