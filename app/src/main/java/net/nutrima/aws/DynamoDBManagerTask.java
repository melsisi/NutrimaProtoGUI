package net.nutrima.aws;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by melsisi on 4/27/2016.
 */
public class DynamoDBManagerTask extends
        AsyncTask<String, Void, List<RestaurantMenuItem>> {

    protected List<RestaurantMenuItem> doInBackground(
            String... restaurantName) {

        List<RestaurantMenuItem> toReturn = new ArrayList<>();

        String tableStatus = DynamoDBManager.getTestTableStatus();

        if (tableStatus.equals("ACTIVE")) {
                toReturn = DynamoDBManager.getMenuForRestaurant(restaurantName[0]);
        }

        return toReturn;
    }

}
