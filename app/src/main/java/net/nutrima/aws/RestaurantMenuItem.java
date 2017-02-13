package net.nutrima.aws;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

import net.nutrima.engine.FoodType;
import net.nutrima.nutrimaprotogui.Business;
import net.nutrima.engine.Kitchen;

import java.util.ArrayList;

/**
 * Created by melsisi on 4/26/2016.
 */
@DynamoDBTable(tableName = "RestaurantsUS2")
public class RestaurantMenuItem {

    private String Restaurant;
    private String FoodCategory;
    private String ItemName;
    private String ItemDescription;
    private String ServingsPerItem;
    private String ServingSizeMetric;
    private String ServingSizeUnit;
    private String ServingsSizePieces;
    private String Calories;
    private String TotalFat;
    private String SaturatedFat;
    private String TransFat;
    private String Cholesterol;
    private String Sodium;
    private String Potassium;
    private String Carbohydrates;
    private String Fiber;
    private String Sugar;
    private String Protein;

    private String GlutenFree;
    private String SoyFree;
    private String DairyFree;
    private String FishFree;
    private String ShellFishFree;
    private String NutsFree;
    private String PeanutsFree;
    private String EggsFree;
    private String Halal;

    private Business business;

    @DynamoDBHashKey(attributeName = "Restaurant")
    public String getRestaurant() {
        return Restaurant;
    }

    public void setRestaurant(String restaurant) {
        this.Restaurant = restaurant;
    }

    @DynamoDBRangeKey(attributeName = "ItemName")
    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    @DynamoDBAttribute(attributeName = "ItemDescription")
    public String getItemDescription() {
        return ItemDescription;
    }

    public void setItemDescription(String itemDescription) {
        ItemDescription = itemDescription;
    }

    @DynamoDBAttribute(attributeName = "ServingsPerItem")
    public String getServingsPerItem() {
        return ServingsPerItem;
    }

    public void setServingsPerItem(String servingsPerItem) {
        ServingsPerItem = servingsPerItem;
    }

    @DynamoDBAttribute(attributeName = "ServingSizeMetric")
    public String getServingSizeMetric() {
        return ServingSizeMetric;
    }

    public void setServingSizeMetric(String servingSizeMetric) {
        ServingSizeMetric = servingSizeMetric;
    }

    @DynamoDBAttribute(attributeName = "ServingSizeUnit")
    public String getServingSizeUnit() {
        return ServingSizeUnit;
    }

    public void setServingSizeUnit(String servingSizeUnit) {
        ServingSizeUnit = servingSizeUnit;
    }

    @DynamoDBAttribute(attributeName = "ServingsSizePieces")
    public String getServingsSizePieces() {
        return ServingsSizePieces;
    }

    public void setServingsSizePieces(String servingsSizePieces) {
        ServingsSizePieces = servingsSizePieces;
    }

    @DynamoDBAttribute(attributeName = "Calories")
    public String getCalories() {
        return Calories;
    }

    public void setCalories(String calories) {
        Calories = calories;
    }

    @DynamoDBAttribute(attributeName = "TotalFat")
    public String getTotalFat() {
        return TotalFat;
    }

    public void setTotalFat(String totalFat) {
        TotalFat = totalFat;
    }

    @DynamoDBAttribute(attributeName = "SaturatedFat")
    public String getSaturatedFat() {
        return SaturatedFat;
    }

    public void setSaturatedFat(String saturatedFat) {
        SaturatedFat = saturatedFat;
    }

    @DynamoDBAttribute(attributeName = "TransFat")
    public String getTransFat() {
        return TransFat;
    }

    public void setTransFat(String transFat) {
        TransFat = transFat;
    }

    @DynamoDBAttribute(attributeName = "Cholesterol")
    public String getCholesterol() {
        return Cholesterol;
    }

    public void setCholesterol(String cholesterol) {
        Cholesterol = cholesterol;
    }

    @DynamoDBAttribute(attributeName = "Sodium")
    public String getSodium() {
        return Sodium;
    }

    public void setSodium(String sodium) {
        Sodium = sodium;
    }

    @DynamoDBAttribute(attributeName = "Potassium")
    public String getPotassium() {
        return Potassium;
    }

    public void setPotassium(String potassium) {
        Potassium = potassium;
    }

    @DynamoDBAttribute(attributeName = "Carbohydrates")
    public String getCarbohydrates() {
        return Carbohydrates;
    }

    public void setCarbohydrates(String carbohydrates) {
        Carbohydrates = carbohydrates;
    }

    @DynamoDBAttribute(attributeName = "Fiber")
    public String getFiber() {
        return Fiber;
    }

    public void setFiber(String fiber) {
        Fiber = fiber;
    }

    @DynamoDBAttribute(attributeName = "Sugar")
    public String getSugar() {
        return Sugar;
    }

    public void setSugar(String sugar) {
        Sugar = sugar;
    }

    @DynamoDBAttribute(attributeName = "Protein")
    public String getProtein() {
        return Protein;
    }

    public void setProtein(String protein) {
        Protein = protein;
    }

    @DynamoDBAttribute(attributeName = "GlutenFree")
    public String getGlutenFree() {
        return GlutenFree;
    }

    public void setGlutenFree(String glutenFree) {
        GlutenFree = glutenFree;
    }

    @DynamoDBAttribute(attributeName = "SoyFree")
    public String getSoyFree() {
        return SoyFree;
    }

    public void setSoyFree(String soyFree) {
        SoyFree = soyFree;
    }

    @DynamoDBAttribute(attributeName = "DairyFree")
    public String getDairyFree() {
        return DairyFree;
    }

    public void setDairyFree(String dairyFree) {
        DairyFree = dairyFree;
    }

    @DynamoDBAttribute(attributeName = "FishFree")
    public String getFishFree() {
        return FishFree;
    }

    public void setFishFree(String fishFree) {
        FishFree = fishFree;
    }

    @DynamoDBAttribute(attributeName = "ShellFishFree")
    public String getShellFishFree() {
        return ShellFishFree;
    }

    public void setShellFishFree(String shellFishFree) {
        ShellFishFree = shellFishFree;
    }

    @DynamoDBAttribute(attributeName = "NutsFree")
    public String getNutsFree() {
        return NutsFree;
    }

    public void setNutsFree(String nutsFree) {
        NutsFree = nutsFree;
    }

    @DynamoDBAttribute(attributeName = "PeanutsFree")
    public String getPeanutsFree() {
        return PeanutsFree;
    }

    public void setPeanutsFree(String peanutsFree) {
        PeanutsFree = peanutsFree;
    }

    @DynamoDBAttribute(attributeName = "EggsFree")
    public String getEggsFree() {
        return EggsFree;
    }

    public void setEggsFree(String eggsFree) {
        EggsFree = eggsFree;
    }

    @DynamoDBAttribute(attributeName = "Halal")
    public String getHalal() {
        return Halal;
    }

    public void setHalal(String halal) {
        Halal = halal;
    }

    //--x-- Added by Ahmed
    private FoodType foodCategory_e;
    private String Kitchen;
    private Kitchen kitchen_e;
    private ArrayList <String> recommendations = new ArrayList<> ();


    // TODO: Sync with AWS table

    @DynamoDBAttribute(attributeName = "FoodCategory")
    public String getFoodCategory() {return FoodCategory; }

    public void setFoodCategory(String foodCategory) {
        FoodCategory = foodCategory;
        switch (foodCategory) {
            case "Appetizers & Sides":foodCategory_e = FoodType.APPETIZER; break;
            case "Baked Goods":foodCategory_e = FoodType.BAKED_GOODS; break;
            case "Beverages":foodCategory_e = FoodType.BEVERAGE; break;
            case "Burgers":foodCategory_e = FoodType.FAST_FOOD; break;
            case "Entrees":foodCategory_e = FoodType.ENTREE; break;
            case "Fried Potatoes":foodCategory_e = FoodType.FAST_FOOD; break;
            case "Salads":foodCategory_e = FoodType.SALAD; break;
            case "Sandwiches":foodCategory_e = FoodType.SANDWICH; break;
            case "Pizza":foodCategory_e = FoodType.PIZZA; break;
            case "Desserts":foodCategory_e = FoodType.DESSERT; break;
            case "Soup":foodCategory_e = FoodType.SOUP; break;
            case "Toppings & Ingredients":foodCategory_e = FoodType.TOPPING; break;
            case "Pasta":foodCategory_e = FoodType.PASTA; break;
            default:foodCategory_e = FoodType.ALL; break;
        }
    }

    public FoodType getFoodCategory_e() {
        return foodCategory_e;
    }

    //@DynamoDBAttribute(attributeName = "Kitchen")
    public String getKitchen() {
        return Kitchen;
    }

    public void setKitchen(String kitchen) {
        this.Kitchen = kitchen;
        switch (kitchen) {
            case "ALL":
                kitchen_e = net.nutrima.engine.Kitchen.ALL;
                break;
            case "EGYPTIAN":
                kitchen_e = net.nutrima.engine.Kitchen.EGYPTIAN;
                break;
            case "ARABIC":
                kitchen_e = net.nutrima.engine.Kitchen.ARABIC;
                break;
            case "MIDDLE_EASTERN":
                kitchen_e = net.nutrima.engine.Kitchen.MIDDLE_EASTERN;
                break;
            case "INDIAN":
                kitchen_e = net.nutrima.engine.Kitchen.INDIAN;
                break;
            case "CHINESE":
                kitchen_e = net.nutrima.engine.Kitchen.CHINESE;
                break;
            case "KOREAN":
                kitchen_e = net.nutrima.engine.Kitchen.KOREAN;
                break;
            case "JAPANESE":
                kitchen_e = net.nutrima.engine.Kitchen.JAPANESE;
                break;
            case "FAST_FOOD":
                kitchen_e = net.nutrima.engine.Kitchen.FAST_FOOD;
                break;
            case "ITALIAN":
                kitchen_e = net.nutrima.engine.Kitchen.ITALIAN;
                break;
            case "FRENCH":
                kitchen_e = net.nutrima.engine.Kitchen.FRENCH;
                break;
            case "ASIAN":
                kitchen_e = net.nutrima.engine.Kitchen.ASIAN;
                break;
            default:
                kitchen_e = net.nutrima.engine.Kitchen.ALL;
                break;
        }
    }

    public Kitchen getKitchen_e() {
        return kitchen_e;
    }

    public void addRecommendations(String s) {
        this.recommendations.add(s);
    }
    public ArrayList<String> getRecommendations() {
        return recommendations;
    }
    //--x--

    public Business getBusiness() {
        return business;
    }

    public void setBusiness(Business business) {
        this.business = business;
    }
}
