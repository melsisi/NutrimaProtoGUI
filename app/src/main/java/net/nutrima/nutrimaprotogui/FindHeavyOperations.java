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
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
public class FindHeavyOperations {
    private static FindHeavyOperations ourInstance;
    private static ArrayList<Business> businessesToMap;
    private final int MY_PERMISSIONS_REQUEST = 0;
    private static Location mLastLocation;
    private static String city;
    private static GoogleApiClient mGoogleApiClient;
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

    public void populateBusinessesAsync(final Activity activity){
        Yelp.getYelp(activity.getBaseContext());
        new Thread(new Runnable() {
            public void run() {
                businessesToMap = Yelp.getYelp(activity).search("food",
                        mLastLocation.getLatitude(),
                        mLastLocation.getLongitude(),
                        city);
                yelpBusinessesReady = true;
            }
        }).start();
    }

    public void populateLocations(final Activity activity) {

        // 1- Get my current location ////////////////////////////////////
        if (ActivityCompat.checkSelfPermission(activity.getBaseContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST);
            return;
        }


        // When running in emulator, use home location by default


        if(Globals.getInstance().amIRunningInEmulator()) {
            mLastLocation = new Location("HOME_ADDRESS");
            mLastLocation.setLatitude(33.7131339d);
            mLastLocation.setLongitude(-117.7853251d);
        }
        else
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);

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
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);

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

        populateBusinessesAsync(activity);
    }

    public Location getmLastLocation() {
        return mLastLocation;
    }

    public void setmLastLocation(Location mLastLocation) {
        FindHeavyOperations.mLastLocation = mLastLocation;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        FindHeavyOperations.city = city;
    }

    public ArrayList<Business> getBusinessesToMap() {
        return businessesToMap;
    }

    public void setBusinessesToMap(ArrayList<Business> businessesToMap) {
        FindHeavyOperations.businessesToMap = businessesToMap;
    }

    public boolean isYelpBusinessesReady() {
        return yelpBusinessesReady;
    }

    public void setYelpBusinessesReady(boolean yelpBusinessesReady) {
        FindHeavyOperations.yelpBusinessesReady = yelpBusinessesReady;
    }

    public GoogleApiClient getmGoogleApiClient() {
        return mGoogleApiClient;
    }

}
