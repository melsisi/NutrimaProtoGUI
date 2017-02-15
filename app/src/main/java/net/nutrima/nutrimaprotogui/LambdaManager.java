package net.nutrima.nutrimaprotogui;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunctionException;
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaInvokerFactory;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.internal.RestUtils;

import net.nutrima.aws.RestaurantMenuItem;
import net.nutrima.nutrimaprotogui.fragments.MapFragment;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by melsisi on 2/8/2017.
 */
public class LambdaManager {

    // Create an instance of CognitoCachingCredentialsProvider
    private CognitoCachingCredentialsProvider cognitoProvider;

    // Create LambdaInvokerFactory, to be used to instantiate the Lambda proxy.
    private LambdaInvokerFactory factory;

    // Create the Lambda proxy object with a default Json data binder.
    // You can provide your own data binder by implementing
    // LambdaDataBinder.
    private LambdaInterface myInterface;

    private static LambdaManager ourInstance = new LambdaManager();

    public static LambdaManager getInstance() {
        return ourInstance;
    }

    private LambdaManager() {
    }

    public void initObjects(Context context) {
        cognitoProvider = new CognitoCachingCredentialsProvider(
                context, "us-west-2:d8906892-52a9-45c2-aa66-10fc5e19af64", Regions.US_WEST_2);
        factory = new LambdaInvokerFactory(context, Regions.US_WEST_2, cognitoProvider);
        myInterface = factory.build(LambdaInterface.class);
    }

    public void getTopThreeMenuItemsAroundMeAsync(LambdaRequest input) {
        new AsyncTask<LambdaRequest, Void, RestaurantMenuItem[]>() {
            @Override
            protected RestaurantMenuItem[] doInBackground(LambdaRequest... params) {
                try {
                    return myInterface.HelloFunction(params[0]);
                } catch (LambdaFunctionException lfe) {
                    Log.e("Tag", "Failed to invoke lambda fn", lfe);
                    return null;
                }
            }

            @Override
            protected void onPostExecute(RestaurantMenuItem[] result) {
                if (result == null) {
                    return;
                }
                Globals.setRestaurantFullMenuMapFiltered(reconstructMapFromList(result));
                MapFragment.awsReadyCallback();
            }
        }.execute(input);
    }

    public interface MyCallbackInterface {

        void onDownloadFinished(LambdaRespMenues result);
    }

    public List<List<RestaurantMenuItem>> getFullAndFilteredMenuForRestaurant(String restaurantName,
                                                                              final MyCallbackInterface callbackIf) {
        try {
            LambdaRespMenues tempResults = new AsyncTask<String, Void, LambdaRespMenues>() {
                @Override
                protected LambdaRespMenues doInBackground(String... params) {
                    try {
                        return myInterface.GetFullAndFilteredMenuForRestaurant(params[0]);
                    } catch (LambdaFunctionException lfe) {
                        Log.e("Tag", "Failed to invoke lambda fn", lfe);
                        return null;
                    }
                }
                @Override
                protected void onPostExecute(LambdaRespMenues result) {
                    if (result == null) {
                        return;
                    }
                    callbackIf.onDownloadFinished(result);
                }
            }.execute(restaurantName).get();

            ArrayList<RestaurantMenuItem> tempFullList =
                    new ArrayList<>(Arrays.asList(tempResults.getFullMenuList()));
            ArrayList<RestaurantMenuItem> tempFilteredList =
                    new ArrayList<>(Arrays.asList(tempResults.getFilteredMenuList()));

            List<List<RestaurantMenuItem>> toReturn = new ArrayList<>();
            toReturn.add(tempFullList);
            toReturn.add(tempFilteredList);

            return toReturn;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Map<Business, List<RestaurantMenuItem>> reconstructMapFromList(
            RestaurantMenuItem[] menuList) {

        Map<Business, List<RestaurantMenuItem>> toReturn = new HashMap<>();

        for (RestaurantMenuItem menuItem : menuList) {
            List<RestaurantMenuItem> tempList = new ArrayList<>();
            if(toReturn.containsKey(menuItem.getBusiness()))
                tempList = toReturn.get(menuItem.getBusiness());
            tempList.add(menuItem);
            toReturn.put(menuItem.getBusiness(), tempList);
        }

        return toReturn;
    }
}
