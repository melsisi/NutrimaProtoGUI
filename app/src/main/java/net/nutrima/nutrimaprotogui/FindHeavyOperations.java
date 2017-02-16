package net.nutrima.nutrimaprotogui;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import net.nutrima.engine.CurrentMetrics;
import net.nutrima.nutrimaprotogui.fragments.MapFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.facebook.FacebookSdk.getApplicationContext;
import static net.nutrima.nutrimaprotogui.fragments.MapFragment.TAG;

/**
 * Created by melsisi on 9/26/2016.
 */
public class FindHeavyOperations implements LocationListener {
    private static FindHeavyOperations ourInstance;
    private static ArrayList<Business> businessesToMap;
    private final int MY_PERMISSIONS_REQUEST = 0;
    private static Location mLastLocation;
    private static String city;
    private static boolean yelpBusinessesReady = false;

    public static FindHeavyOperations getInstance() {
        if(ourInstance == null) {
            ourInstance = new FindHeavyOperations();
        }
        return ourInstance;
    }

    protected FindHeavyOperations() {
        mLastLocation = null;
        businessesToMap = new ArrayList<Business>();
    }

    public void populateBusinessesAsync(final Activity activity){
        if(Globals.getInstance().isRunningInLambda()) {
            LambdaManager lambdaManager = LambdaManager.getInstance();
            lambdaManager.initObjects(getApplicationContext());
            lambdaManager.getTopThreeMenuItemsAroundMeAsync("food",
                    mLastLocation.getLongitude(),
                    mLastLocation.getLatitude(),
                    city);
        }
        else {
            Yelp.getYelp(activity.getBaseContext());
            new Thread(new Runnable() {
                public void run() {
                    businessesToMap = Yelp.getYelp(activity).search("food",
                            mLastLocation.getLatitude(),
                            mLastLocation.getLongitude(),
                            city);
                    MapFragment.yelpReadyCallback();
                }
            }).start();
        }
    }

    public void populateLocations(final Activity activity, GoogleApiClient mGoogleApiClient) {

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(0);
        locationRequest.setFastestInterval(1000);
        locationRequest.setNumUpdates(1);
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);

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
            while(mLastLocation == null) {
                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                        mGoogleApiClient);
            }

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

    @Override
    public void onLocationChanged(Location location) {

    }
}
