package net.nutrima.nutrimaprotogui;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;

import net.nutrima.aws.AmazonClientManager;
import net.nutrima.aws.RestaurantMenuItem;
import net.nutrima.engine.ActivityLevel;
import net.nutrima.engine.NutrimaMetrics;
import net.nutrima.engine.UserProfile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by melsisi on 9/26/2016.
 */
public class Globals {
    private static List<NutritionUSDAEntry> USDATable;
    private static Globals ourInstance = new Globals();
    private static NutrimaMetrics nutrimaMetrics;
    private static UserProfile userProfile;
    private static AmazonClientManager clientManager;
    private static Map<Business, List<RestaurantMenuItem>> restaurantFullMenuMap;
    private static Map<Business, List<RestaurantMenuItem>> restaurantPersonalizedMenuMap;
    private static int numRunningAWSThreads;
    private static List<String> AWSRestaurants;
    private static Bundle fbResponse;

    public static Globals getInstance() {
        if(ourInstance == null) {
            ourInstance = new Globals();
        }
        return ourInstance;
    }

    protected Globals() {
        USDATable = new ArrayList<NutritionUSDAEntry>();
        nutrimaMetrics = new NutrimaMetrics();
        restaurantFullMenuMap = new HashMap<>();
        numRunningAWSThreads = 0;
        AWSRestaurants = new ArrayList<>();
        fbResponse = null;
    }

    public List<NutritionUSDAEntry> getUSDATable() {
        return USDATable;
    }

    public void setUSDATable(List<NutritionUSDAEntry> USDATable) {
        Globals.USDATable = USDATable;
    }

    public NutrimaMetrics getNutrimaMetrics() {
        return nutrimaMetrics;
    }

    public void setNutrimaMetrics(NutrimaMetrics nutrimaMetrics) {
        Globals.nutrimaMetrics = nutrimaMetrics;
    }

    public AmazonClientManager getClientManager() {
        return clientManager;
    }

    public void setClientManager(AmazonClientManager clientManager) {
        Globals.clientManager = clientManager;
    }

    public Map<Business, List<RestaurantMenuItem>> getRestaurantFullMenuMap() {
        return restaurantFullMenuMap;
    }

    public void setRestaurantFullMenuMap(Map<Business, List<RestaurantMenuItem>> restaurantMenuMap) {
        this.restaurantFullMenuMap = restaurantMenuMap;
    }

    public int getNumRunningAWSThreads() {
        return numRunningAWSThreads;
    }

    public void setNumRunningAWSThreads(int numRunningAWSThreads) {
        Globals.numRunningAWSThreads = numRunningAWSThreads;
    }

    public List<String> getAWSRestaurants() {
        return AWSRestaurants;
    }

    public void setAWSRestaurants(List<String> AWSRestaurants) {
        Globals.AWSRestaurants = AWSRestaurants;
    }

    public Map<Business, List<RestaurantMenuItem>> getRestaurantPersonalizedMenuMap() {
        return restaurantPersonalizedMenuMap;
    }

    public void setRestaurantPersonalizedMenuMap(Map<Business, List<RestaurantMenuItem>> restaurantPersonalizedMenuMap) {
        Globals.restaurantPersonalizedMenuMap = restaurantPersonalizedMenuMap;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        Globals.userProfile = userProfile;
    }

    public Bundle getFbResponse() {
        return fbResponse;
    }

    public void setFbResponse(Bundle fbResponse) {
        Globals.fbResponse = fbResponse;
    }

    public boolean amIRunningInEmulator() {
        return Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || "google_sdk".equals(Build.PRODUCT);
    }
}
