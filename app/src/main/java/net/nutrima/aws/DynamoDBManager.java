package net.nutrima.aws;

import android.util.Log;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.BatchWriteItemRequest;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.ConditionalOperator;
import com.amazonaws.services.dynamodbv2.model.DescribeTableRequest;
import com.amazonaws.services.dynamodbv2.model.DescribeTableResult;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.dynamodbv2.model.WriteRequest;

import net.nutrima.nutrimaprotogui.Business;
import net.nutrima.nutrimaprotogui.Globals;
import net.nutrima.nutrimaprotogui.R;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
    public static ArrayList<RestaurantMenuItem> getMenuForRestaurant(Business restaurant) {
        AmazonDynamoDBClient ddb = Globals.getInstance().getClientManager().ddb();

        //DynamoDBMapper mapper = new DynamoDBMapper(ddb);
        Map<String, AttributeValue> attValue =  new HashMap<>();
        attValue.put(":v_id", new AttributeValue(restaurant.getName()));

        QueryRequest queryRequest = new QueryRequest().
                withTableName(Globals.getInstance().getClientManager().
                        getContext().getString(R.string.restaurant_us_table))
                .withKeyConditionExpression("Restaurant = :v_id")
                .withExpressionAttributeValues(attValue);

        //RestaurantMenuItem replyKey = new RestaurantMenuItem();
        //replyKey.setRestaurant(restaurant.getName());

        //DynamoDBQueryExpression<RestaurantMenuItem> queryExpression =
        //        new DynamoDBQueryExpression<RestaurantMenuItem>()
        //        .withHashKeyValues(replyKey);

        try {
            long startTime = System.nanoTime();
            //ArrayList<RestaurantMenuItem> latestReplies = paginatedQueryListToArrayList(
            //        mapper.query(RestaurantMenuItem.class, queryExpression), restaurant);
            QueryResult queryResult = ddb.query(queryRequest);
            long endTime = System.nanoTime();

            long duration = (endTime - startTime);

            Log.d("DYNAMODB_MANAGER", "Time taken in single AWS xaction: " + (duration / 1000000) + " milliseconds.");
            return processResult(queryResult, restaurant);
        } catch (AmazonServiceException ex) {
            Globals.getInstance().getClientManager().wipeCredentialsOnAuthError(ex);
        } catch (AmazonClientException ex) {
            Log.e("DynamoDBManager", ex.toString() + " for business: " + restaurant);
        }

        return null;
    }

    private static ArrayList<RestaurantMenuItem> processResult(QueryResult queryResult,
                                                               Business restaurant) {
        ArrayList<RestaurantMenuItem> toReturn = new ArrayList<>();
        for(Map<String, AttributeValue> item : queryResult.getItems()) {
            RestaurantMenuItem toAdd = new RestaurantMenuItem();
            if (item.get("Restaurant") != null)
                toAdd.setRestaurant(item.get("Restaurant").getS());
            if (item.get("ItemName") != null)
                toAdd.setItemName(item.get("ItemName").getS());
            if (item.get("ItemDescription") != null)
                toAdd.setItemDescription(item.get("ItemDescription").getS());
            if (item.get("ServingsPerItem") != null)
                toAdd.setServingsPerItem(item.get("ServingsPerItem").getS());
            if (item.get("ServingSizeMetric") != null)
                toAdd.setServingSizeMetric(item.get("ServingSizeMetric").getS());
            if (item.get("ServingSizeUnit") != null)
                toAdd.setServingSizeUnit(item.get("ServingSizeUnit").getS());
            if (item.get("ServingsSizePieces") != null)
                toAdd.setServingsSizePieces(item.get("ServingsSizePieces").getS());
            if (item.get("Calories") != null)
                toAdd.setCalories(item.get("Calories").getS());
            if (item.get("TotalFat") != null)
                toAdd.setTotalFat(item.get("TotalFat").getS());
            if (item.get("SaturatedFat") != null)
                toAdd.setSaturatedFat(item.get("SaturatedFat").getS());
            if (item.get("TransFat") != null)
                toAdd.setTransFat(item.get("TransFat").getS());
            if (item.get("Cholesterol") != null)
                toAdd.setCholesterol(item.get("Cholesterol").getS());
            if (item.get("Sodium") != null)
                toAdd.setSodium(item.get("Sodium").getS());
            if (item.get("Potassium") != null)
                toAdd.setPotassium(item.get("Potassium").getS());
            if (item.get("Carbohydrates") != null)
                toAdd.setCarbohydrates(item.get("Carbohydrates").getS());
            if (item.get("Fiber") != null)
                toAdd.setFiber(item.get("Fiber").getS());
            if (item.get("Sugar") != null)
                toAdd.setSugar(item.get("Sugar").getS());
            if (item.get("Protein") != null)
                toAdd.setProtein(item.get("Protein").getS());
            if (item.get("GlutenFree") != null)
                toAdd.setGlutenFree(item.get("GlutenFree").getS());
            if (item.get("SoyFree") != null)
                toAdd.setSoyFree(item.get("SoyFree").getS());
            if (item.get("DiaryFree") != null)
                toAdd.setDairyFree(item.get("DiaryFree").getS());
            if (item.get("FishFree") != null)
                toAdd.setFishFree(item.get("FishFree").getS());
            if (item.get("ShellFishFree") != null)
                toAdd.setShellFishFree(item.get("ShellFishFree").getS());
            if (item.get("NutsFree") != null)
                toAdd.setNutsFree(item.get("NutsFree").getS());
            if (item.get("PeanutsFree") != null)
                toAdd.setPeanutsFree(item.get("PeanutsFree").getS());
            if (item.get("EggsFree") != null)
                toAdd.setEggsFree(item.get("EggsFree").getS());
            if (item.get("Halal") != null)
                toAdd.setHalal(item.get("Halal").getS());
            if (item.get("FoodCategory") != null)
                toAdd.setFoodCategory(item.get("FoodCategory").getS());
            if (restaurant != null)
                toAdd.setBusiness(restaurant);

            toReturn.add(toAdd);
        }
        return toReturn;
    }

    private static ArrayList<RestaurantMenuItem> paginatedQueryListToArrayList(
            PaginatedQueryList<RestaurantMenuItem> menuItemPaginatedQueryList, Business restaurant) {
        ArrayList<RestaurantMenuItem> toReturn = new ArrayList<>();
        for(RestaurantMenuItem mi : menuItemPaginatedQueryList) {
            mi.setBusiness(restaurant);
            toReturn.add(mi);
        }
        return toReturn;
    }

    private static void writeMenuesToAWSNewsFormat_test() {
        AmazonDynamoDBClient ddb = Globals.getInstance().getClientManager().ddb();

        String restName = "";
        StringBuilder categoryString = new StringBuilder("");
        StringBuilder itemString = new StringBuilder("");
        StringBuilder descString = new StringBuilder("");
        StringBuilder servPerItemString = new StringBuilder("");
        StringBuilder servSizeString = new StringBuilder("");
        StringBuilder servSizeUnitString = new StringBuilder("");
        StringBuilder servSizeTextString = new StringBuilder("");
        StringBuilder calsString = new StringBuilder("");
        StringBuilder totFatString = new StringBuilder("");
        StringBuilder satFatString = new StringBuilder("");
        StringBuilder transFatString = new StringBuilder("");
        StringBuilder cholestString = new StringBuilder("");
        StringBuilder sodiumString = new StringBuilder("");
        StringBuilder potassiumString = new StringBuilder("");
        StringBuilder carbString = new StringBuilder("");
        StringBuilder fiberString = new StringBuilder("");
        StringBuilder sugarString = new StringBuilder("");
        StringBuilder protString = new StringBuilder("");
        for (RestaurantMenuItem item : Globals.getTempMenuesList()) {
            if (restName.equals(""))
                restName = item.getRestaurant();
            if (!item.getRestaurant().equals(restName)) {
                ArrayList<String> newEntry = new ArrayList();
                newEntry.add(restName);
                newEntry.add(categoryString.toString());
                newEntry.add(itemString.toString());
                newEntry.add(descString.toString());
                newEntry.add(servPerItemString.toString());
                newEntry.add(servSizeString.toString());
                newEntry.add(servSizeUnitString.toString());
                newEntry.add(servSizeTextString.toString());
                newEntry.add(calsString.toString());
                newEntry.add(totFatString.toString());
                newEntry.add(satFatString.toString());
                newEntry.add(transFatString.toString());
                newEntry.add(cholestString.toString());
                newEntry.add(sodiumString.toString());
                newEntry.add(potassiumString.toString());
                newEntry.add(carbString.toString());
                newEntry.add(fiberString.toString());
                newEntry.add(sugarString.toString());
                newEntry.add(protString.toString());
                Map<String, AttributeValue> rowToWrite = newItem(newEntry);
                PutItemRequest putItemRequest = new PutItemRequest("RestaurantUS_comp", rowToWrite);
                try {
                    PutItemResult putItemResult = ddb.putItem(putItemRequest);
                    System.out.println("Result: " + putItemResult);
                } catch (AmazonServiceException ex) {
                    Globals.getInstance().getClientManager().wipeCredentialsOnAuthError(ex);
                } catch (AmazonClientException ex) {
                    Log.e("DynamoDBManager", ex.toString() + " for business: ");
                }
                restName = item.getRestaurant();
            } else {
                categoryString.append(item.getFoodCategory() + "#");
                itemString.append(item.getItemName() + "#");
                descString.append(item.getItemDescription() + "#");
                servPerItemString.append(item.getServingsPerItem() + "#");
                servSizeString.append(item.getServingSizeMetric() + "#");
                servSizeUnitString.append(item.getServingSizeUnit() + "#");
                servSizeTextString.append(item.getServingsSizePieces() + "#");
                calsString.append(item.getCalories() + "#");
                totFatString.append(item.getTotalFat() + "#");
                satFatString.append(item.getSaturatedFat() + "#");
                transFatString.append(item.getTransFat() + "#");
                cholestString.append(item.getCholesterol() + "#");
                sodiumString.append(item.getSodium() + "#");
                potassiumString.append(item.getPotassium() + "#");
                carbString.append(item.getCarbohydrates() + "#");
                fiberString.append(item.getFiber() + "#");
                sugarString.append(item.getSugar() + "#");
                protString.append(item.getProtein() + "#");
            }
        }
    }

    private static Map<String, AttributeValue> newItem(ArrayList<String> row) {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("Restaurant", new AttributeValue(row.get(0)));
        item.put("FoodCategory", new AttributeValue(row.get(1)));
        item.put("ItemName", new AttributeValue(row.get(2)));
        item.put("ItemDescription", new AttributeValue(row.get(3)));
        item.put("ServingsPerItem", new AttributeValue(row.get(4)));
        item.put("ServingSizeMetric", new AttributeValue(row.get(5)));
        item.put("ServingSizeUnit", new AttributeValue(row.get(6)));
        item.put("ServingsSizePieces", new AttributeValue(row.get(7)));
        item.put("Calories", new AttributeValue(row.get(8)));
        item.put("TotalFat", new AttributeValue(row.get(9)));
        item.put("SaturatedFat", new AttributeValue(row.get(10)));
        item.put("TransFat", new AttributeValue(row.get(11)));
        item.put("Cholesterol", new AttributeValue(row.get(12)));
        item.put("Sodium", new AttributeValue(row.get(13)));
        item.put("Potassium", new AttributeValue(row.get(14)));
        item.put("Carbohydrates", new AttributeValue(row.get(15)));
        item.put("Fiber", new AttributeValue(row.get(16)));
        item.put("Sugar", new AttributeValue(row.get(17)));
        item.put("Protein", new AttributeValue(row.get(18)));

        return item;
    }
}