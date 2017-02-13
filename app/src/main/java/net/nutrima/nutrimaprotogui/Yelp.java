package net.nutrima.nutrimaprotogui;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class Yelp {

    OAuthService service;
    Token accessToken;

    public static Yelp getYelp(Context context) {
        return new Yelp("xZl4tVchOhvVfn6LEJCYcQ", "lbftgJ7Am2aCIF7blZ52VD-Jc-0",
                "LrpXd1ubnJlAV-mQUrKPDN-63YtfeoPl", "A8N7BrVd6zQfpEuxDimzr6ibNvU");
    }

    /**
     * Setup the Yelp API OAuth credentials.
     * <p/>
     * OAuth credentials are available from the developer site, under Manage API access (version 2 API).
     *
     * @param consumerKey    Consumer key
     * @param consumerSecret Consumer secret
     * @param token          Token
     * @param tokenSecret    Token secret
     */
    private Yelp(String consumerKey, String consumerSecret, String token, String tokenSecret) {
        this.service = new ServiceBuilder().provider(YelpApi2.class).apiKey(consumerKey).apiSecret(consumerSecret).build();
        this.accessToken = new Token(token, tokenSecret);
    }

    /**
     * Search with term and location.
     *
     * @param term      Search term
     * @param latitude  Latitude
     * @param longitude Longitude
     * @return JSON string response
     */
    public ArrayList<Business> search(String term, double latitude, double longitude, String city) {
        ArrayList<Business> toReturn;
        int totalResults = 0;
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://api.yelp.com/v2/search/");
        request.addQuerystringParameter("term", term);
        request.addQuerystringParameter("location", city);
        request.addQuerystringParameter("cll", latitude + "," + longitude);
        request.addQuerystringParameter("radius_filter", "2000");
        this.service.signRequest(this.accessToken, request);

        String responseBody = "";
        try {
            responseBody = new YelpAsyncTask().execute(request).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        toReturn = getBusinessesArray(responseBody);
        totalResults = getTotalResults(responseBody);

        int offset = 20;
        while(totalResults - offset > 20) {
            request = new OAuthRequest(Verb.GET, "http://api.yelp.com/v2/search/");
            request.addQuerystringParameter("term", term);
            request.addQuerystringParameter("location", city);
            request.addQuerystringParameter("cll", latitude + "," + longitude);
            request.addQuerystringParameter("offset", Integer.toString(offset));
            request.addQuerystringParameter("radius_filter", "2000");
            this.service.signRequest(this.accessToken, request);

            responseBody = "";
            try {
                responseBody = new YelpAsyncTask().execute(request).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            toReturn.addAll(getBusinessesArray(responseBody));
            offset += 20;
        }
        return toReturn;
    }

    /**
     * Search with term string location.
     *
     * @param term      Search term
     * @param location  Location
     * @return JSON string response
     */
    public String search(String term, String location) {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://api.yelp.com/v2/search");
        request.addQuerystringParameter("term", term);
        request.addQuerystringParameter("location", location);
        this.service.signRequest(this.accessToken, request);
        Response response = request.send();
        return response.getBody();
    }

    // TODO: Handle missing entries.

    private ArrayList<Business> getBusinessesArray(String JSONBody){
        JSONObject root = null;
        ArrayList<Business> businesses = new ArrayList<Business>();
        try {
            root = new JSONObject(JSONBody);

            JSONArray businessesArray = root.getJSONArray("businesses");
            for(int i = 0; i< businessesArray.length(); i++) {
                // get business
                try {
                    JSONObject business = businessesArray.getJSONObject(i);

                    // get name and phone
                    String name = business.getString("name"); // basketball
                    String phone = business.getString("display_phone");

                    // get image and rating mage urls
                    String imageUrl = business.getString("image_url");
                    String ratingImageUrl = business.getString("rating_img_url");

                    // get location and coordinates
                    JSONObject location = business.getJSONObject("location");
                    JSONObject coordinate = location.getJSONObject("coordinate");
                    JSONArray address = location.getJSONArray("display_address");

                    Double longitude = coordinate.getDouble("longitude"); // 40
                    Double latitude = coordinate.getDouble("latitude");

                    Business toAdd = new Business();
                    toAdd.setName(name);
                    toAdd.setPhone(phone);
                    toAdd.setCoordinates(new LatLng(longitude, latitude));
                    toAdd.setImageUrl(imageUrl);
                    toAdd.setRatingImageUrl(ratingImageUrl);

                    String fullAddress = "";
                    for (int j = 0; j < address.length(); j++)
                        fullAddress += address.getString(j) + " ";
                    toAdd.setAddress(fullAddress);
                    businesses.add(toAdd);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return businesses;
    }

    private int getTotalResults(String JSONBody){
        JSONObject root = null;
        ArrayList<Business> businesses = new ArrayList<Business>();
        int total = 0;
        try {
            root = new JSONObject(JSONBody);
            total = root.getInt("total");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return total;
    }
}