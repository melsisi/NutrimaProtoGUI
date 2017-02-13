package net.nutrima.nutrimaprotogui.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import android.content.IntentSender;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import net.nutrima.aws.DynamoDBManagerTask;
import net.nutrima.aws.RestaurantMenuItem;
import net.nutrima.engine.CurrentMetrics;
import net.nutrima.engine.FoodType;
import net.nutrima.engine.MealNutrients;
import net.nutrima.engine.NutrimaMetrics;
import net.nutrima.engine.NutritionFilters;
import net.nutrima.engine.UserProfile;
import net.nutrima.nutrimaprotogui.Business;
import net.nutrima.nutrimaprotogui.BusinessDetailsActivity;
import net.nutrima.nutrimaprotogui.FindHeavyOperations;
import net.nutrima.nutrimaprotogui.Globals;
import net.nutrima.nutrimaprotogui.ListMenuItemAdapter;
import net.nutrima.nutrimaprotogui.ProfileCreatorActivity;
import net.nutrima.nutrimaprotogui.R;
import net.nutrima.nutrimaprotogui.SimpleMainActivity;
import net.nutrima.nutrimaprotogui.UrlAsyncTask;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class MapFragment extends SupportMapFragment implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private Marker previousMarker;
    private static GoogleMap mMap;
    private String city;
    private final int MY_PERMISSIONS_REQUEST = 0;
    private static ArrayList<Business> businessesToMap;
    private final String QUERY = "restaurant";
    private FloatingActionButton businessOkFab;
    private boolean markerClicked;
    private static View rootView;
    public static final String TAG = MapFragment.class.getSimpleName();
    private Marker marker;
    private static GoogleApiClient mGoogleApiClient;
    private static long startTime;

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        checkIfLocationServicesEnabled();
    }

    LocationRequest mCoarseLocationRequest;
    private LocationRequest createLocationRequest() {
        if (mCoarseLocationRequest == null) {
            mCoarseLocationRequest = new LocationRequest();
            mCoarseLocationRequest.setInterval(5000);
            mCoarseLocationRequest.setFastestInterval(1000);
            mCoarseLocationRequest.setNumUpdates(1);
            mCoarseLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        }
        return mCoarseLocationRequest;
    }

    /**
     * Constant used in the location settings dialog.
     */
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    public final static int REQUEST_LOCATION = 199;

    private void checkIfLocationServicesEnabled() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(createLocationRequest());

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        startTime = System.nanoTime();
                        FindHeavyOperations.getInstance().populateLocations(getActivity(), mGoogleApiClient);

                        // Animate camera to current position
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                new LatLng(FindHeavyOperations.getInstance().getmLastLocation().getLatitude(),
                                        FindHeavyOperations.getInstance().getmLastLocation().getLongitude()), 13.0f));
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(getActivity(), REQUEST_LOCATION);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        break;
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
        switch (requestCode) {
            case REQUEST_LOCATION:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        // All required changes were successfully made
                        startTime = System.nanoTime();
                        FindHeavyOperations.getInstance().populateLocations(getActivity(), mGoogleApiClient);

                        // Animate camera to current position
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                new LatLng(FindHeavyOperations.getInstance().getmLastLocation().getLatitude(),
                                        FindHeavyOperations.getInstance().getmLastLocation().getLongitude()), 13.0f));
                        break;
                    case Activity.RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to

                        break;
                    default:

                        break;
                }
                break;
        }
    }

    static public void yelpReadyCallback() {
        if(Globals.getInstance().isRunningInLambda()) {

        }
        else {
            businessesToMap = FindHeavyOperations.getInstance().getBusinessesToMap();

            for (final Business business : businessesToMap) {
                if (businessNameAbscent(business.getName()))
                    continue;
                Globals.getInstance().setNumRunningAWSThreads(
                        Globals.getInstance().getNumRunningAWSThreads() + 1);
            }
            int i = 0;
            for (final Business business : businessesToMap) {
                if (businessNameAbscent(business.getName()))
                    continue;
                i++;
                new DynamoDBManagerTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, business);
            }

            Log.d("MAP_FRAGMENT", "Dispatched " + i + " AWS threads.");

            long endTime = System.nanoTime();

            long duration = (endTime - startTime);

            Log.d("MAP_FRAGMENT", "Time taken in Yelp execution: " + (duration / 1000000) + " milliseconds.");
        }
    }

    static public void awsReadyCallback() {
        if(Globals.getInstance().isRunningInLambda()) {
            for (Map.Entry<Business, List<RestaurantMenuItem>> entry :
                    Globals.getRestaurantFullMenuMapFiltered().entrySet())
            {
                mMap.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_logo))
                        .title(entry.getKey().getName())
                        .position(new LatLng(entry.getKey().getCoordinates().getLongitude(),
                                entry.getKey().getCoordinates().getLatitude())));
            }
        }
        else {
            long tempTime = System.nanoTime();

            long duration = (tempTime - startTime);

            Log.d("MAP_FRAGMENT", "awsReadyCallback called after: " + (duration / 1000000) + " milliseconds.");

            NutritionFilters nutritionFilters = new NutritionFilters(Globals.getInstance().getUserProfile());
            ArrayList<FoodType> allButBeveragesFilter = new ArrayList<>();
            allButBeveragesFilter.add(FoodType.APPETIZER);
            allButBeveragesFilter.add(FoodType.BAKED_GOODS);
            allButBeveragesFilter.add(FoodType.ENTREE);
            allButBeveragesFilter.add(FoodType.FAST_FOOD);
            allButBeveragesFilter.add(FoodType.PASTA);
            allButBeveragesFilter.add(FoodType.PIZZA);
            allButBeveragesFilter.add(FoodType.SALAD);
            allButBeveragesFilter.add(FoodType.SANDWICH);
            allButBeveragesFilter.add(FoodType.SOUP);
            nutritionFilters.setFoodTypes(allButBeveragesFilter);
            MealNutrients mn = new MealNutrients();
            mn.extractMeals(Globals.getInstance().getNutrimaMetrics(),
                    // TODO: Fill from log
                    new CurrentMetrics(),
                    nutritionFilters);

            filterFromEngine(mn);

            for (Map.Entry<Business, List<RestaurantMenuItem>> entry :
                    Globals.getRestaurantFullMenuMapFiltered().entrySet())
            //        Globals.getInstance().getRestaurantFullMenuMap().entrySet())
            //Globals.getInstance().getRestaurantPersonalizedMenuMap().entrySet())
            {
                mMap.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_logo))
                        .title(entry.getKey().getName())
                        .position(new LatLng(entry.getKey().getCoordinates().getLatitude(),
                                entry.getKey().getCoordinates().getLongitude())));
            }

            //getActivity().getWindow().
            //        clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            long endTime = System.nanoTime();

            duration = (endTime - startTime);

            Log.d("MAP_FRAGMENT", "Time taken in execution: " + (duration / 1000000) + " milliseconds.");
        }
        rootView.findViewById(R.id.progressBar).setVisibility(View.GONE);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e("GOOGLE CLIENT SISI", "SUSPENDED");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("GOOGLE CLIENT SISI", "FAILED");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        rootView = inflater.inflate(R.layout.activity_map, container, false);

        // Populate user info from file ///////////////////////////////////////
        UserProfile savedUserProfile = readDataFromFile();
        if(savedUserProfile == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setMessage("You need to provide us some info to get started.")
                    .setTitle("Oops");

            builder.setPositiveButton("Add now", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Intent activityChangeIntent = new Intent(getActivity(),
                            ProfileCreatorActivity.class);
                    //PersonalInfoActivity.class);
                    startActivity(activityChangeIntent);
                }
            });
            AlertDialog dialog = builder.create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            return rootView;
        }
        //////////////////////////////////////////////////////////////////

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity(), 0, this)
                .addApi(LocationServices.API)
                // Not used currently .addApi(Places.GEO_DATA_API)
                // Not used currently .addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        FloatingActionButton myLocationFab = (FloatingActionButton) rootView.findViewById(R.id.my_location_fab);
        myLocationFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Animate camera to current position
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(FindHeavyOperations.getInstance().getmLastLocation().getLatitude(),
                                FindHeavyOperations.getInstance().getmLastLocation().getLongitude()), 13.0f));
            }
        });

        businessOkFab = (FloatingActionButton) rootView.findViewById(R.id.business_ok_fab);
        businessOkFab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(!markerClicked)
                    return;
                Intent intent = new Intent(getContext(), BusinessDetailsActivity.class);
                String message = marker.getTitle();
                intent.putExtra("BUSINESS_NAME", message);
                startActivity(intent);
            }
        });
        markerClicked = false;
        businessOkFab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.medium_grey)));

        return rootView;
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);

        if (ActivityCompat.checkSelfPermission(this.getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.getActivity(),
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST);
            return;
        }

        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);

        // 2- Use location to get surrounding businesses /////////////////
        //LatLng northEast = move(new LatLng(mLastLocation.getLatitude(),
        //        mLastLocation.getLongitude()), 709, 709);
        //LatLng southWest = move(new LatLng(mLastLocation.getLatitude(),
        //        mLastLocation.getLongitude()), -709, -709);

        //LatLngBounds tmpBounds = new LatLngBounds(southWest, northEast);

        //AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
        //        .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ESTABLISHMENT)
        //        .build();

        //PendingResult<AutocompletePredictionBuffer> result =
        //        Places.GeoDataApi.getAutocompletePredictions(mGoogleApiClient, QUERY,
        //                tmpBounds, typeFilter);
        //////////////////////////////////////////////////////////////////

        // 3- Filter surroundings

        // 3-a- Filter for "FOOD" ////////////////////////////////////////
/*
        new Thread(new Runnable() {
            public void run() {
                // Wait until Yelp retrieval has finished
                while (!FindHeavyOperations.getInstance().isYelpBusinessesReady()) {
                    //Log.d("MAP_FRAGMENT", "Waiting for Yelp Thread!");
                }

                businessesToMap = FindHeavyOperations.getInstance().getBusinessesToMap();

                // TODO: Filter for presence in AWS (local)

                // Get menus from AWS
                for(final Business business : businessesToMap) {
                        //new Thread(new Runnable() {
                        //    public void run() {
                                //try {
                                    if(businessNameAbscent(business.getName()))
                                        continue;
                                    //List<RestaurantMenuItem> tempRawMenuItem = new DynamoDBManagerTask().
                                    //        execute(business.getName()).get();
                                    new DynamoDBManagerTask().execute(business);
                                    Globals.getInstance().setNumRunningAWSThreads(
                                            Globals.getInstance().getNumRunningAWSThreads() + 1);
                                    //if(tempRawMenuItem == null || tempRawMenuItem.size() == 0)
                                    //    continue;
                                    //Globals.getInstance().getRestaurantFullMenuMap().
                                    //        put(business, tempRawMenuItem);
                                //} catch (InterruptedException e) {
                                //    e.printStackTrace();
                                //} catch (ExecutionException e) {
                                //    e.printStackTrace();
                                //}
                         //   }
                        //}).start();
                }

                //Globals.getInstance().setMenusReady(true);

                while(Globals.getInstance().getNumRunningAWSThreads() != 0) {
                    //Log.d("MAP_FRAGMENT", "Waiting for AWS Thread! (" +
                    //        Globals.getInstance().getNumRunningAWSThreads()  + ")");
                }

                MealNutrients mn = new MealNutrients(Globals.getInstance().getNutrimaMetrics(),
                        new CurrentMetrics(),
                        Globals.getInstance().getUserProfile());

                filterFromEngine(mn);

                //Handler handler = new Handler(Looper.getMainLooper());
                //handler.post(new Runnable(){
                //    @Override
                //    public void run() {
                        for (Map.Entry<Business, List<RestaurantMenuItem>> entry :
                                Globals.getInstance().getRestaurantFullMenuMap().entrySet())
                                //Globals.getInstance().getRestaurantPersonalizedMenuMap().entrySet())
                        {
                            mMap.addMarker(new MarkerOptions()
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_logo))
                                    .title(entry.getKey().getName())
                                    .position(entry.getKey().getCoordinates()));
                        }

                        //FindHeavyOperations.getInstance().getActivity().getWindow().
                        getActivity().getWindow().
                                clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

                        rootView.findViewById(R.id.progressBar).setVisibility(View.GONE);
                        long endTime = System.nanoTime();

                        long duration = (endTime - startTime);

                        Log.d("MAP_FRAGMENT", "Time taken in execution: " + (duration/1000000) + " milliseconds.");
                //    }
                //});
            }
        }).start();
*/
    }

    private static void filterFromEngine(MealNutrients mn) {
        Map<Business,List<RestaurantMenuItem>> personalizedMenus = new HashMap<>();

        for (Map.Entry<Business, List<RestaurantMenuItem>> entry :
                Globals.getInstance().getRestaurantFullMenuMap().entrySet()) {
            List<RestaurantMenuItem> personalizedMenu = new ArrayList<>();
            if(entry.getValue() == null) {
                Log.w("FILTER", "Menu is NULL for: " + entry.getKey().getName());
                continue;
            }
            for(RestaurantMenuItem menuItem : entry.getValue()) {
                if(menuItem.getCalories() == null || menuItem.getCarbohydrates() == null ||
                        menuItem.getTotalFat() == null || menuItem.getProtein() == null ||
                        menuItem.getSaturatedFat() == null || menuItem.getFiber() == null ||
                        menuItem.getSugar() == null)
                    continue;
                if(!tryParseInt(menuItem.getCalories()) || !tryParseInt(menuItem.getCarbohydrates()) ||
                        !tryParseInt(menuItem.getTotalFat()) || !tryParseInt(menuItem.getProtein()) ||
                        !tryParseInt(menuItem.getSaturatedFat()) || !tryParseInt(menuItem.getFiber()) ||
                        !tryParseInt(menuItem.getSugar()))
                    continue;
                /*if((Integer.parseInt(menuItem.getCalories()) >= mn.calories.min &&
                        Integer.parseInt(menuItem.getCalories()) <= mn.calories.max) &&
                        (Integer.parseInt(menuItem.getCarbohydrates()) >= mn.carbs.min &&
                                Integer.parseInt(menuItem.getCarbohydrates()) <= mn.carbs.max) &&
                        (Integer.parseInt(menuItem.getTotalFat()) >= mn.fat.min &&
                                Integer.parseInt(menuItem.getTotalFat()) <= mn.fat.max) &&
                        (Integer.parseInt(menuItem.getProtein()) >= mn.protein.min &&
                                Integer.parseInt(menuItem.getProtein()) <= mn.protein.max) &&
                        (Integer.parseInt(menuItem.getSaturatedFat()) >= mn.satFat.min &&
                                Integer.parseInt(menuItem.getSaturatedFat()) <= mn.satFat.max) &&
                        (Integer.parseInt(menuItem.getFiber()) >= mn.fiber.min &&
                                Integer.parseInt(menuItem.getFiber()) <= mn.fiber.max) &&
                        (Integer.parseInt(menuItem.getSugar()) >= mn.addedSugar.min &&
                                Integer.parseInt(menuItem.getSugar()) <= mn.addedSugar.max)) {
                    personalizedMenu.add(menuItem);
                }*/
            }
            if(personalizedMenu.size() > 0)
                personalizedMenus.put(entry.getKey(), personalizedMenu);
        }

        Globals.getInstance().setRestaurantPersonalizedMenuMap(personalizedMenus);
    }

    private static boolean businessNameAbscent(String name) {
        boolean absent = true;
        for(String s : Globals.getInstance().getAWSRestaurants()) {
            if(s.toLowerCase().contains(name.toLowerCase()) ||
                    name.toLowerCase().contains(s.toLowerCase())) {
                absent = false;
                break;
            }
        }
        return absent;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mGoogleApiClient!=null &&
                mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onStop() {
        if(mGoogleApiClient != null &&
                mGoogleApiClient.isConnected() ) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    private static final double EARTHRADIUS = 6366198;

    private static LatLng move(LatLng startLL, double toNorth, double toEast) {
        double lonDiff = meterToLongitude(toEast, startLL.latitude);
        double latDiff = meterToLatitude(toNorth);
        return new LatLng(startLL.latitude + latDiff, startLL.longitude
                + lonDiff);
    }

    private static double meterToLongitude(double meterToEast, double latitude) {
        double latArc = Math.toRadians(latitude);
        double radius = Math.cos(latArc) * EARTHRADIUS;
        double rad = meterToEast / radius;
        return Math.toDegrees(rad);
    }


    private static double meterToLatitude(double meterToNorth) {
        double rad = meterToNorth / EARTHRADIUS;
        return Math.toDegrees(rad);
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        if(previousMarker != null) {
            previousMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker_logo));
        }

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 18.0f));

        previousMarker = marker;
        marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker_logo_clicked));
        markerClicked = true;
        businessOkFab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.nutrimaOrange)));
        this.marker = marker;
        LinearLayout l = (LinearLayout)this.getActivity().findViewById(R.id.bottom_bar_linearLayout);

        Animation bottomLayoutSlideIn;
        if(l.getLayoutParams().height == 0){
            bottomLayoutSlideIn = AnimationUtils.loadAnimation(this.getContext(), R.anim.enter_from_bottom);
        }
        else {
            bottomLayoutSlideIn = AnimationUtils.loadAnimation(this.getContext(), android.R.anim.fade_in);
        }
        l.getLayoutParams().height=800;

        final TextView businessName = (TextView)this.getActivity().findViewById(R.id.business_name_textview);
        TextView businessPhone = (TextView)this.getActivity().findViewById(R.id.business_phone_textview);
        TextView businessAddress = (TextView)this.getActivity().findViewById(R.id.business_address_textview);
        TextView numOptionsTextView = (TextView)this.getActivity().findViewById(R.id.num_options_text_view);

        businessName.setText(marker.getTitle());
        //Animation bottomLayoutSlideIn = AnimationUtils.loadAnimation(this.getContext(), R.anim.enter_from_bottom
        //        /*android.R.anim.fade_in*/);
        for (Map.Entry<Business, List<RestaurantMenuItem>> entry :
                Globals.getInstance().getRestaurantFullMenuMapFiltered().entrySet())
        {
            if(entry.getKey().getName().equals(marker.getTitle())){
                businessPhone.setText(entry.getKey().getPhone());
                businessAddress.setText(entry.getKey().getAddress());
                numOptionsTextView.setText("Top 3 options: (from " + entry.getValue().size() + " total)");
            }
        }
        l.startAnimation(bottomLayoutSlideIn);

        getActivity().findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
        fillTopThreeRelevantOptions(marker.getTitle());
        getActivity().findViewById(R.id.progressBar).setVisibility(View.GONE);
        return true;
    }

    void fillTopThreeRelevantOptions(String businessName) {
        final ListView sneakPeakListView = (ListView) getActivity().findViewById(R.id.sneak_peak_list_view);
        ArrayList<RestaurantMenuItem> sneakPeakFM= new ArrayList<>();

        // TODO: Convert to partial menu
        // Get full menu ///////////////////////////////////
        for (Map.Entry<Business, List<RestaurantMenuItem>> entry :
                Globals.getInstance().getRestaurantFullMenuMapFiltered().entrySet()) {
            if(entry.getKey().getName().toLowerCase().equals(businessName.toLowerCase())) {
                int i = 0;
                for(RestaurantMenuItem item : entry.getValue()) {
                    sneakPeakFM.add(item);
                    i++;
                    if(i == 3)
                        break;
                }
                break;
            }
        }

        final ListMenuItemAdapter customAdapter = new ListMenuItemAdapter(getActivity(),
                R.layout.item_list,
                R.id.sneak_peak_list_view,
                sneakPeakFM);

        sneakPeakListView.setAdapter(customAdapter);

        ///////////////////////////////////////////////
    }

    static boolean tryParseInt(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // TODO: Move this somewhere else
    @Nullable
    public UserProfile readDataFromFile() {
        UserProfile userProfile = null;

        try {
            FileInputStream fis = getActivity().openFileInput(getString(R.string.profile_data_file_name));
            ObjectInputStream is = null;

            is = new ObjectInputStream(fis);

            userProfile = (UserProfile) is.readObject();
            is.close();
            fis.close();
        } catch (IOException e) {
            return null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        //NutrimaMetrics nutrimaMetrics = new NutrimaMetrics();
        //nutrimaMetrics.calcNutrima(userProfile);
        //Globals.getInstance().setNutrimaMetrics(nutrimaMetrics);
        Globals.getInstance().setUserProfile(userProfile);
        return userProfile;
    }
}