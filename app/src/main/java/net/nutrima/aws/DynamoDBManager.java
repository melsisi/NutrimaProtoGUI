package net.nutrima.aws;

import android.util.Log;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBQueryExpression;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.DescribeTableRequest;
import com.amazonaws.services.dynamodbv2.model.DescribeTableResult;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;

import net.nutrima.nutrimaprotogui.Globals;
import net.nutrima.nutrimaprotogui.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by melsisi on 4/27/2016.
 */
public class DynamoDBManager {

    private static final String TAG = "DynamoDBManager";

    /*
     * Retrieves the table description and returns the table status as a string.
     */
    public static String getTestTableStatus() {

        try {
            AmazonDynamoDBClient ddb = Globals.getInstance().getClientManager().ddb();

            DescribeTableRequest request = new DescribeTableRequest()
                    .withTableName(Globals.getInstance().getClientManager().
                            getContext().getString(R.string.restaurant_us_table));
            DescribeTableResult result = ddb.describeTable(request);

            String status = result.getTable().getTableStatus();
            return status == null ? "" : status;

        } catch (ResourceNotFoundException e) {
        } catch (AmazonServiceException ex) {
            Globals.getInstance().getClientManager()
                    .wipeCredentialsOnAuthError(ex);
        }

        return "";
    }

    /*
     * Retrieves all of the attribute/value pairs for the specified restaurant.
     */
    public static List<RestaurantMenuItem> getMenuForRestaurant(String restaurant) {

        AmazonDynamoDBClient ddb = Globals.getInstance().getClientManager().ddb();
        DynamoDBMapper mapper = new DynamoDBMapper(ddb);

        RestaurantMenuItem replyKey = new RestaurantMenuItem();
        replyKey.setRestaurant(restaurant);

        DynamoDBQueryExpression<RestaurantMenuItem> queryExpression =
                new DynamoDBQueryExpression<RestaurantMenuItem>()
                .withHashKeyValues(replyKey);
        ArrayList<Object> testList = new ArrayList<>();
        RestaurantMenuItem testItem = new RestaurantMenuItem();
        testItem.setRestaurant("Subway");
        testList.add(testItem);
        try {
            List<RestaurantMenuItem> latestReplies = mapper.query(RestaurantMenuItem.class, queryExpression);
            return latestReplies;
        } catch (AmazonServiceException ex) {
            Globals.getInstance().getClientManager().wipeCredentialsOnAuthError(ex);
        } catch (AmazonClientException ex) {
            Log.e("DynamoDBManager", ex.toString() + " for business: " + restaurant);
        }

        return null;
    }

}