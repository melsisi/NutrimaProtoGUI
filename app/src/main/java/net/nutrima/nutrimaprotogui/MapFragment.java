package net.nutrima.nutrimaprotogui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.location.Location;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import net.nutrima.aws.DynamoDBManagerTask;
import net.nutrima.aws.RestaurantMenuItem;
import net.nutrima.engine.CurrentMetrics;
import net.nutrima.engine.MealNutrients;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class MapFragment extends SupportMapFragment implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    private String city;
    private final int MY_PERMISSIONS_REQUEST = 0;
    private ArrayList<Business> businessesToMap;
    private final String QUERY = "restaurant";
    private FloatingActionButton businessOkFab;
    private boolean markerClicked;
    private  View rootView;
    public static final String TAG = MapFragment.class.getSimpleName();
    private Marker marker;

    private static GoogleApiClient mGoogleApiClient;
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        FindHeavyOperations.getInstance().populateLocations(getActivity());

        // Animate camera to current position
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(FindHeavyOperations.getInstance().getmLastLocation().getLatitude(),
                        FindHeavyOperations.getInstance().getmLastLocation().getLongitude()), 13.0f));
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

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity(), 0, this)
                .addApi(LocationServices.API)
                // Not used currently .addApi(Places.GEO_DATA_API)
                // Not used currently .addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        rootView = inflater.inflate(R.layout.activity_map, container, false);

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

        new Thread(new Runnable() {
            public void run() {
                // Wait until Yelp retrieval has finished
                while (!FindHeavyOperations.getInstance().isYelpBusinessesReady()) {}

                businessesToMap = FindHeavyOperations.getInstance().getBusinessesToMap();

                // TODO: Filter for presence in AWS (local)

                // Get menus from AWS
                for(final Business business : businessesToMap) {
                        //new Thread(new Runnable() {
                        //    public void run() {
                                try {
                                    if(businessNameAbscent(business.getName()))
                                        continue;
                                    List<RestaurantMenuItem> tempRawMenuItem = new DynamoDBManagerTask().
                                            execute(business.getName()).get();
                                    Globals.getInstance().getRestaurantFullMenuMap().
                                            put(business, tempRawMenuItem);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                }
                         //   }
                        //}).start();
                }
                Globals.getInstance().setMenusReady(true);

                MealNutrients mn = new MealNutrients(Globals.getInstance().getNutrimaMetrics(),
                        new CurrentMetrics(),
                        Globals.getInstance().getUserProfile());

                filterFromEngine(mn);

                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable(){
                    @Override
                    public void run() {
                        for (Map.Entry<Business, List<RestaurantMenuItem>> entry :
                                Globals.getInstance().getRestaurantFullMenuMap().entrySet())
                                //Globals.getInstance().getRestaurantPersonalizedMenuMap().entrySet())
                        {
                            mMap.addMarker(new MarkerOptions()
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_logo))
                                    .title(entry.getKey().getName())
                                    //.snippet(b.getName())
                                    .position(entry.getKey().getCoordinates()));
                        }

                        //FindHeavyOperations.getInstance().getActivity().getWindow().
                        getActivity().getWindow().
                                clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

                        rootView.findViewById(R.id.progressBar).setVisibility(View.GONE);
                    }
                });
            }
        }).start();
    }

    private void filterFromEngine(MealNutrients mn) {
        Map<Business,List<RestaurantMenuItem>> personalizedMenus = new HashMap<>();

        for (Map.Entry<Business, List<RestaurantMenuItem>> entry :
                Globals.getInstance().getRestaurantFullMenuMap().entrySet()) {
            List<RestaurantMenuItem> personalizedMenu = new ArrayList<>();
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
                if((Integer.parseInt(menuItem.getCalories()) >= mn.calories.min &&
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
                }
            }
            if(personalizedMenu.size() > 0)
                personalizedMenus.put(entry.getKey(), personalizedMenu);
        }

        Globals.getInstance().setRestaurantPersonalizedMenuMap(personalizedMenus);
    }

    private boolean businessNameAbscent(String name) {
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
        if (FindHeavyOperations.getInstance().getmGoogleApiClient()!=null &&
                FindHeavyOperations.getInstance().getmGoogleApiClient().isConnected()) {
            FindHeavyOperations.getInstance().getmGoogleApiClient().disconnect();
        }
    }

    @Override
    public void onStop() {
        if( FindHeavyOperations.getInstance().getmGoogleApiClient() != null &&
                FindHeavyOperations.getInstance().getmGoogleApiClient().isConnected() ) {
            FindHeavyOperations.getInstance().getmGoogleApiClient().disconnect();
        }
        super.onStop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.


                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
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
        markerClicked = true;
        businessOkFab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.nutrimaOrange)));
        this.marker = marker;
        LinearLayout l = (LinearLayout)this.getActivity().findViewById(R.id.bottom_bar_linearLayout);
        //l.setVisibility(View.VISIBLE);
        Animation bottomLayoutSlideIn;
        if(l.getLayoutParams().height == 0){
            bottomLayoutSlideIn = AnimationUtils.loadAnimation(this.getContext(), R.anim.enter_from_bottom);
        }
        else {
            bottomLayoutSlideIn = AnimationUtils.loadAnimation(this.getContext(), android.R.anim.fade_in);
        }
        l.getLayoutParams().height=700;

        final TextView businessName = (TextView)this.getActivity().findViewById(R.id.business_name_textview);
        TextView businessPhone = (TextView)this.getActivity().findViewById(R.id.business_phone_textview);
        TextView businessAddress = (TextView)this.getActivity().findViewById(R.id.business_address_textview);

        businessName.setText(marker.getTitle());
        //Animation bottomLayoutSlideIn = AnimationUtils.loadAnimation(this.getContext(), R.anim.enter_from_bottom
        //        /*android.R.anim.fade_in*/);
        for (Map.Entry<Business, List<RestaurantMenuItem>> entry :
                Globals.getInstance().getRestaurantFullMenuMap().entrySet())
        {
            if(entry.getKey().getName().equals(marker.getTitle())){
                businessPhone.setText(entry.getKey().getPhone());
                businessAddress.setText(entry.getKey().getAddress());
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
                Globals.getInstance().getRestaurantFullMenuMap().entrySet()) {
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

    boolean tryParseInt(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}