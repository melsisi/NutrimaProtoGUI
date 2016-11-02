package net.nutrima.aws;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

/**
 * Created by melsisi on 4/26/2016.
 */
@DynamoDBTable(tableName = "RestaurantsUS2")
public class RestaurantMenuItem {

    private String Restaurant;
    private String ItemName;
    private String FoodCategory;
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
    private String DiaryFree;
    private String FishFree;
    private String ShellFishFree;
    private String NutsFree;
    private String PeanutsFree;
    private String EggsFree;
    private String Halal;

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

    @DynamoDBAttribute(attributeName = "FoodCategory")
    public String getFoodCategory() {
        return FoodCategory;
    }

    public void setFoodCategory(String foodCategory) {
        FoodCategory = foodCategory;
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

    @DynamoDBAttribute(attributeName = "DiaryFree")
    public String getDiaryFree() {
        return DiaryFree;
    }

    public void setDiaryFree(String diaryFree) {
        DiaryFree = diaryFree;
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

}
