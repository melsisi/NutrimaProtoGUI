package net.nutrima.aws;

import android.os.AsyncTask;
import android.util.Log;

import net.nutrima.nutrimaprotogui.Business;
import net.nutrima.nutrimaprotogui.Globals;
import net.nutrima.nutrimaprotogui.ListContentFragment;
import net.nutrima.nutrimaprotogui.MapFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by melsisi on 4/27/2016.
 */
public class DynamoDBManagerTask extends
        AsyncTask<Business, Void, List<RestaurantMenuItem>> {

    @Override
    protected List<RestaurantMenuItem> doInBackground(
            Business... business) {

        Log.d("DynamoDBManagerTask", "Start business name: " + business[0].getName());
        List<RestaurantMenuItem> toReturn = new ArrayList<>();

        //String tableStatus = DynamoDBManager.getTestTableStatus();

        //if (tableStatus.equals("ACTIVE")) {
                toReturn = DynamoDBManager.getMenuForRestaurant(business[0].getName());
        //}

        Globals.getInstance().setNumRunningAWSThreads(
                Globals.getInstance().getNumRunningAWSThreads() - 1);

        Log.d("DynamoDBManagerTask", "End business name: " + business[0].getName());

        if(toReturn == null || toReturn.size() == 0)
            return null;

        Globals.getInstance().getRestaurantFullMenuMap().
                put(business[0], toReturn);

        return toReturn;
    }

    @Override
    protected void onPostExecute(List<RestaurantMenuItem> result) {
        if(result == null)
            return;
        Log.d("DynamoDBManagerTask", "onPostExecute called for business name: " + result.get(0).getRestaurant());
        if (Globals.getInstance().getNumRunningAWSThreads() < 1) {
            MapFragment.awsReadyCallback();
            ListContentFragment.refreshListAfterAWS();
        }
    }
}
