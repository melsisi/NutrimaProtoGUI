package net.nutrima.engine;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by ayehia on 1/21/2017.
 */

public class NutritionFilters {

    //--- Calories and Macro nutrients ranges ---
    public MaxMinNutrients calories;
    public MaxMinNutrients protein;
    public MaxMinNutrients carbs;
    public MaxMinNutrients fat;
    public MaxMinNutrients satFat;
    public MaxMinNutrients fiber;
    public MaxMinNutrients addedSugar;

    //--- Diseases ---
    private boolean heartDisease       = false;
    private boolean diabetic           = false;
    private boolean highBloodPressure  = false;
    private boolean liverDisease       = false;
    private boolean kidneyDisease      = false;
    private boolean celiacDisease      = false;
    private boolean cancerDisease      = false;
    private String otherDiseases       = "";

    //--- Allergies ---
    private boolean glutenIntorlerance = false;
    private boolean eggAllergy         = false;
    private boolean diaryAllergy       = false;
    private boolean fishAllergy        = false;
    private boolean shellfishAllergy   = false;
    private boolean soyAllergy         = false;
    private boolean nutsAllergy        = false;
    private boolean peanutsAllergy     = false;
    private String otherAllergies      = "";

    private int numOfMeals;

    private FilterSortOn filtersSorton = FilterSortOn.CALORIES;
    private boolean ascendingSorting = true;

    //--- Kitchen types ---
    private ArrayList<Kitchen> kitchens   = new ArrayList<Kitchen>(Arrays.asList(Kitchen.ALL));

    //--- Food types ---
    private ArrayList<FoodType> foodTypes = new ArrayList<FoodType>(Arrays.asList(FoodType.ALL));

    //--- Halal ---
    private boolean halal = false;

    public void updateFilters (NutritionFilters nutritionFilters){
        //--- Calories ---
        this.calories = nutritionFilters.calories;

        this.numOfMeals = nutritionFilters.numOfMeals;

        //--- Macro Nutrients
        this.protein     = nutritionFilters.protein;
        this.carbs       = nutritionFilters.carbs;
        this.fat         = nutritionFilters.fat;
        this.satFat      = nutritionFilters.satFat;
        this.addedSugar  = nutritionFilters.addedSugar;
        this.fiber       = nutritionFilters.fiber;

        //--- Diseases ---
        this.heartDisease       = nutritionFilters.heartDisease;
        this.diabetic           = nutritionFilters.diabetic;
        this.highBloodPressure  = nutritionFilters.highBloodPressure;
        this.liverDisease       = nutritionFilters.liverDisease;
        this.kidneyDisease      = nutritionFilters.kidneyDisease;
        this.celiacDisease      = nutritionFilters.celiacDisease;
        this.cancerDisease      = nutritionFilters.cancerDisease;
        this.otherDiseases      = nutritionFilters.otherDiseases;

        //--- Allergies ---
        this.glutenIntorlerance = nutritionFilters.glutenIntorlerance;
        this.eggAllergy         = nutritionFilters.eggAllergy;
        this.diaryAllergy       = nutritionFilters.diaryAllergy;
        this.fishAllergy        = nutritionFilters.fishAllergy;
        this.shellfishAllergy   = nutritionFilters.shellfishAllergy;
        this.soyAllergy         = nutritionFilters.soyAllergy;
        this.nutsAllergy        = nutritionFilters.nutsAllergy;
        this.peanutsAllergy     = nutritionFilters.peanutsAllergy;
        this.otherAllergies     = nutritionFilters.otherAllergies;

        //--- Kitchen types ---
        this.kitchens   = nutritionFilters.kitchens;

        //--- Food types ---
        this.foodTypes = nutritionFilters.foodTypes;

        //--- Halal ---
        this.halal = nutritionFilters.halal;
    }

    public NutritionFilters (UserProfile userProfile) {

        calories = new MaxMinNutrients(-1,Integer.MAX_VALUE,-1);
        protein = new MaxMinNutrients(-1,Integer.MAX_VALUE,-1);
        carbs = new MaxMinNutrients(-1,Integer.MAX_VALUE,-1);
        fat = new MaxMinNutrients(-1,Integer.MAX_VALUE,-1);
        satFat = new MaxMinNutrients(-1,Integer.MAX_VALUE,-1);
        fiber = new MaxMinNutrients(-1,Integer.MAX_VALUE,-1);
        addedSugar = new MaxMinNutrients(-1,Integer.MAX_VALUE,-1);

        //--- Diseases ---
        this.heartDisease       = userProfile.isHeartDisease();
        this.diabetic           = userProfile.isDiabetic();
        this.highBloodPressure  = userProfile.isHighBloodPressure();
        this.liverDisease       = userProfile.isLiverDisease();
        this.kidneyDisease      = userProfile.isKidneyDisease();
        this.celiacDisease      = userProfile.isCeliacDisease();
        this.cancerDisease      = userProfile.isCancerDisease();
        this.otherDiseases      = userProfile.getOtherDiseases();

        this.numOfMeals = userProfile.getNumOfMeals();

        //--- Allergies ---
        this.glutenIntorlerance = userProfile.isGlutenIntorlerance();
        this.eggAllergy         = userProfile.isEggAllergy();
        this.diaryAllergy       = userProfile.isDiaryAllergy();
        this.fishAllergy        = userProfile.isFishAllergy();
        this.shellfishAllergy   = userProfile.isShellfishAllergy();
        this.soyAllergy         = userProfile.isSoyAllergy();
        this.nutsAllergy        = userProfile.isNutsAllergy();
        this.peanutsAllergy     = userProfile.isPeanutsAllergy();
        this.otherAllergies     = userProfile.getOtherAllergies();

        //--- Kitchen types ---
        this.kitchens   = userProfile.getKitchens();

        //--- Food types ---
        this.foodTypes = userProfile.getFoodTypes();

        //--- Halal ---
        this.halal = userProfile.isHalal();
    }

    //--- Setters / Getters ---

    public MaxMinNutrients getCalories() {
        return calories;
    }

    public void setCalories(MaxMinNutrients calories) {
        this.calories = calories;
    }

    public MaxMinNutrients getProtein() {
        return protein;
    }

    public void setProtein(MaxMinNutrients protein) {
        this.protein = protein;
    }

    public MaxMinNutrients getCarbs() {
        return carbs;
    }

    public void setCarbs(MaxMinNutrients carbs) {
        this.carbs = carbs;
    }

    public MaxMinNutrients getFat() {
        return fat;
    }

    public void setFat(MaxMinNutrients fat) {
        this.fat = fat;
    }

    public MaxMinNutrients getSatFat() {
        return satFat;
    }

    public void setSatFat(MaxMinNutrients satFat) {
        this.satFat = satFat;
    }

    public MaxMinNutrients getFiber() {
        return fiber;
    }

    public void setFiber(MaxMinNutrients fiber) {
        this.fiber = fiber;
    }

    public MaxMinNutrients getAddedSugar() {
        return addedSugar;
    }

    public void setAddedSugar(MaxMinNutrients addedSugar) {
        this.addedSugar = addedSugar;
    }

    public boolean isHeartDisease() {
        return heartDisease;
    }

    public void setHeartDisease(boolean heartDisease) {
        this.heartDisease = heartDisease;
    }

    public boolean isDiabetic() {
        return diabetic;
    }

    public void setDiabetic(boolean diabetic) {
        this.diabetic = diabetic;
    }

    public boolean isHighBloodPressure() {
        return highBloodPressure;
    }

    public void setHighBloodPressure(boolean highBloodPressure) {
        this.highBloodPressure = highBloodPressure;
    }

    public boolean isLiverDisease() {
        return liverDisease;
    }

    public void setLiverDisease(boolean liverDisease) {
        this.liverDisease = liverDisease;
    }

    public boolean isKidneyDisease() {
        return kidneyDisease;
    }

    public void setKidneyDisease(boolean kidneyDisease) {
        this.kidneyDisease = kidneyDisease;
    }

    public boolean isCeliacDisease() {
        return celiacDisease;
    }

    public void setCeliacDisease(boolean celiacDisease) {
        this.celiacDisease = celiacDisease;
    }

    public boolean isCancerDisease() {
        return cancerDisease;
    }

    public void setCancerDisease(boolean cancerDisease) {
        this.cancerDisease = cancerDisease;
    }

    public String getOtherDiseases() {
        return otherDiseases;
    }

    public void setOtherDiseases(String otherDiseases) {
        this.otherDiseases = otherDiseases;
    }

    public boolean isGlutenIntorlerance() {
        return glutenIntorlerance;
    }

    public void setGlutenIntorlerance(boolean glutenIntorlerance) {
        this.glutenIntorlerance = glutenIntorlerance;
    }

    public boolean isEggAllergy() {
        return eggAllergy;
    }

    public void setEggAllergy(boolean eggAllergy) {
        this.eggAllergy = eggAllergy;
    }

    public boolean isDiaryAllergy() {
        return diaryAllergy;
    }

    public void setDiaryAllergy(boolean diaryAllergy) {
        this.diaryAllergy = diaryAllergy;
    }

    public boolean isFishAllergy() {
        return fishAllergy;
    }

    public void setFishAllergy(boolean fishAllergy) {
        this.fishAllergy = fishAllergy;
    }

    public boolean isShellfishAllergy() {
        return shellfishAllergy;
    }

    public void setShellfishAllergy(boolean shellfishAllergy) {
        this.shellfishAllergy = shellfishAllergy;
    }

    public boolean isSoyAllergy() {
        return soyAllergy;
    }

    public void setSoyAllergy(boolean soyAllergy) {
        this.soyAllergy = soyAllergy;
    }

    public boolean isNutsAllergy() {
        return nutsAllergy;
    }

    public void setNutsAllergy(boolean nutsAllergy) {
        this.nutsAllergy = nutsAllergy;
    }

    public boolean isPeanutsAllergy() {
        return peanutsAllergy;
    }

    public void setPeanutsAllergy(boolean peanutsAllergy) {
        this.peanutsAllergy = peanutsAllergy;
    }

    public String getOtherAllergies() {
        return otherAllergies;
    }

    public void setOtherAllergies(String otherAllergies) {
        this.otherAllergies = otherAllergies;
    }

    public ArrayList<Kitchen> getKitchens() {
        return kitchens;
    }

    public void setKitchens(ArrayList<Kitchen> kitchens) {
        this.kitchens = kitchens;
    }

    public ArrayList<FoodType> getFoodTypes() {
        return foodTypes;
    }

    public void setFoodTypes(ArrayList<FoodType> foodTypes) {
        this.foodTypes = foodTypes;
    }

    public boolean isHalal() {
        return halal;
    }

    public void setHalal(boolean halal) {
        this.halal = halal;
    }

    public int getNumOfMeals() {
        return numOfMeals;
    }

    public void setNumOfMeals(int numOfMeals) {
        this.numOfMeals = numOfMeals;
    }

    public FilterSortOn getFiltersSorton() {
        return filtersSorton;
    }

    public void setFiltersSorton(FilterSortOn filtersSorton) {
        this.filtersSorton = filtersSorton;
    }

    public boolean isAscendingSorting() {
        return ascendingSorting;
    }

    public void setAscendingSorting(boolean ascendingSorting) {
        this.ascendingSorting = ascendingSorting;
    }
}