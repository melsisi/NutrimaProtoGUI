package net.nutrima.engine;

import net.nutrima.aws.RestaurantMenuItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by ayehia on 9/30/2016.
 */

public class MealNutrients {

    //Compute max or minumum of two integers
    public int computeMaxMin (MaxMin m, int l, int r) {
        if (m == MaxMin.MIN) {
            if (l < r )
                return l;
            else
                return r;
        } else {
            if (l > r )
                return l;
            else
                return r;
        }
    }

    //TODO: Connect it to BuisnessDetailsActivity.plateNamesPM
    ArrayList<RestaurantMenuItem> plateNamesPM;

    public void filterMenuItemsUserPref (ArrayList<RestaurantMenuItem> plateNamesPM, NutritionFilters nutritionFilters){
        MaxMinNutrients calories = new MaxMinNutrients(-1,Integer.MAX_VALUE, nutritionFilters.calories.selected);
        MaxMinNutrients protein  = new MaxMinNutrients(-1,Integer.MAX_VALUE, nutritionFilters.protein.selected);
        MaxMinNutrients carbs  = new MaxMinNutrients(-1,Integer.MAX_VALUE, nutritionFilters.carbs.selected);
        MaxMinNutrients fat  = new MaxMinNutrients(-1,Integer.MAX_VALUE, nutritionFilters.fat.selected);
        MaxMinNutrients satFat  = new MaxMinNutrients(-1,Integer.MAX_VALUE, nutritionFilters.satFat.selected);
        MaxMinNutrients fiber  = new MaxMinNutrients(-1,Integer.MAX_VALUE, nutritionFilters.fiber.selected);
        MaxMinNutrients addedSugar  = new MaxMinNutrients(-1,Integer.MAX_VALUE, nutritionFilters.addedSugar.selected);

        ArrayList<RestaurantMenuItem> top =
                new ArrayList<RestaurantMenuItem>(plateNamesPM.size());
       // ArrayList<RestaurantMenuItem> bottom =
       //         new ArrayList<RestaurantMenuItem>(plateNamesPM.size());
        //TODO: Handle additional filters by user on the fly
        for (RestaurantMenuItem mi : plateNamesPM) {
            //Hard filters; remove from the list
            if (nutritionFilters.isHalal() && (mi.getHalal() == "N" )) {
                continue;
            }
            if ((nutritionFilters.isGlutenIntorlerance() || nutritionFilters.isCeliacDisease()) && mi.getGlutenFree()=="N")  {
                continue;
            }
            if ((nutritionFilters.isEggAllergy()) && mi.getEggsFree()=="N")  {
                continue;
            }
            if ((nutritionFilters.isNutsAllergy()) && mi.getNutsFree()=="N")  {
                continue;
            }
            if ((nutritionFilters.isSoyAllergy()) && mi.getSoyFree()=="N")  {
                continue;
            }
            if ((nutritionFilters.isDiaryAllergy()) && mi.getDiaryFree()=="N")  {
                continue;
            }
            if ((nutritionFilters.isFishAllergy()) && mi.getFishFree()=="N")  {
                continue;
            }
            if ((nutritionFilters.isShellfishAllergy()) && mi.getShellFishFree()=="N")  {
                continue;
            }
            if ((nutritionFilters.isPeanutsAllergy()) && mi.getPeanutsFree()=="N")  {
                continue;
            }
            //TODO : Handle userprofile.otherAllergies
            //TODO : Handle userprofile diseases

            //Soft filters; put them at the end of the list
            if ( (! nutritionFilters.getKitchens().contains(Kitchen.ALL)) &&
                    nutritionFilters.getKitchens().contains(mi.getKitchen())) {
                //bottom.add(mi);
                continue;
            }
            //Not favurable Food Category
            if ( (! nutritionFilters.getFoodTypes().contains(FoodType.ALL)) &&
                    (! nutritionFilters.getFoodTypes().contains(mi.getFoodCategory_e()))) {
                //bottom.add(mi);
                continue;
            }
            //Not favurable Kitchen
            if ( (! nutritionFilters.getKitchens().contains(Kitchen.ALL)) &&
                    (! nutritionFilters.getKitchens().contains(mi.getKitchen_e()))) {
                //bottom.add(mi);
                continue;
            }
            top.add(mi);

            calories.max = computeMaxMin(MaxMin.MAX, calories.max, Integer.parseInt(mi.getCalories()));
            calories.min = computeMaxMin(MaxMin.MIN, calories.min, Integer.parseInt(mi.getCalories()));

            protein.max = computeMaxMin(MaxMin.MAX, protein.max, Integer.parseInt(mi.getProtein()));
            protein.min = computeMaxMin(MaxMin.MIN, protein.min, Integer.parseInt(mi.getProtein()));

            carbs.max = computeMaxMin(MaxMin.MAX, carbs.max, Integer.parseInt(mi.getCarbohydrates()));
            carbs.min = computeMaxMin(MaxMin.MIN, carbs.min, Integer.parseInt(mi.getCarbohydrates()));

            fat.max = computeMaxMin(MaxMin.MAX, fat.max, Integer.parseInt(mi.getTotalFat()));
            fat.min = computeMaxMin(MaxMin.MIN, fat.min, Integer.parseInt(mi.getTotalFat()));

            satFat.max = computeMaxMin(MaxMin.MAX, satFat.max, Integer.parseInt(mi.getSaturatedFat()));
            satFat.min = computeMaxMin(MaxMin.MIN, satFat.min, Integer.parseInt(mi.getSaturatedFat()));

            fiber.max = computeMaxMin(MaxMin.MAX, fiber.max, Integer.parseInt(mi.getFiber()));
            fiber.min = computeMaxMin(MaxMin.MIN, fiber.min, Integer.parseInt(mi.getFiber()));

            addedSugar.max = computeMaxMin(MaxMin.MAX, addedSugar.max, Integer.parseInt(mi.getSugar()));
            addedSugar.min = computeMaxMin(MaxMin.MIN, addedSugar.min, Integer.parseInt(mi.getSugar()));
        }
        plateNamesPM.clear();
        plateNamesPM.addAll(top);
        //plateNamesPM.addAll(bottom);
        nutritionFilters.setCalories(calories);
        nutritionFilters.setProtein(protein);
        nutritionFilters.setCarbs(carbs);
        nutritionFilters.setFat(fat);
        nutritionFilters.setSatFat(satFat);
        nutritionFilters.setFiber(fiber);
        nutritionFilters.setAddedSugar(addedSugar);
    }

    //Constructor: Takes user profile, and current macro nutrients data, and the Nutrima recommended metrics
    public void extractMeals(NutrimaMetrics nutrimaMetrics, CurrentMetrics currentMetrics,
                             NutritionFilters nutritionFilters) {
        //TODO: handling nutritionFilters is NULL

        //Maximum daily allowance remaining
        int calMax        = (int) (nutrimaMetrics.getCalNutrima() - currentMetrics.calories);
        int proteinMax    = (int) (nutrimaMetrics.getProteinNutrima() - currentMetrics.protein);
        int carbsMax      = (int) (nutrimaMetrics.getCarbsNutrima() - currentMetrics.carbs);
        int fatMax        = (int) (nutrimaMetrics.getFatNutrima() - currentMetrics.fat);
        int satFatMax     = (int) (nutrimaMetrics.getSatFatNutrima() - currentMetrics.satFat);
        int fiberMin      = (int) (nutrimaMetrics.getDietaryFiberNutrima() - currentMetrics.fibers);
        int addedSugarMax = (int) (nutrimaMetrics.getAddedSugarNutrima() - currentMetrics.addedsugar);

        //Ideal recommendations
        //TODO: Use filters to tweak those
        int calMeal      = (int) (nutrimaMetrics.getCalNutrima() / nutritionFilters.getNumOfMeals());
        int proteinMeal  = (int) (nutrimaMetrics.getProteinNutrima() / nutritionFilters.getNumOfMeals());
        int carbsMeal    = (int) (nutrimaMetrics.getCarbsNutrima() / nutritionFilters.getNumOfMeals());
        int fatMeal      = (int) (nutrimaMetrics.getFatNutrima() / nutritionFilters.getNumOfMeals());
        int satFatMeal   = (int) (nutrimaMetrics.getSatFatNutrima() / nutritionFilters.getNumOfMeals());
        int fiberMeal    = (int) (nutrimaMetrics.getDietaryFiberNutrima() / nutritionFilters.getNumOfMeals());
        int addedSugarMeal = (int) (nutrimaMetrics.getAddedSugarNutrima() / nutritionFilters.getNumOfMeals());

        //Methodology:
        //1: Sort items according to user preferences
        filterMenuItemsUserPref(plateNamesPM, nutritionFilters);

        if (nutritionFilters.calories.selected == -1) {
            nutritionFilters.calories.selected = calMeal;
        }
        if (nutritionFilters.protein.selected == -1) {
            nutritionFilters.protein.selected = proteinMeal;
        }
        if (nutritionFilters.carbs.selected == -1) {
            nutritionFilters.carbs.selected = carbsMeal;
        }
        if (nutritionFilters.fat.selected == -1) {
            nutritionFilters.fat.selected = fatMeal;
        }
        if (nutritionFilters.satFat.selected == -1) {
            nutritionFilters.satFat.selected = satFatMeal;
        }
        if (nutritionFilters.fiber.selected == -1) {
            nutritionFilters.fiber.selected = fiberMeal;
        }
        if (nutritionFilters.addedSugar.selected == -1) {
            nutritionFilters.addedSugar.selected = addedSugarMeal;
        }


        // 1. First search for any items that fall into the ideal recommendations as is.
        //  a. We should check for calories and for those items check the macro nutrients that fit
        //  b. Provide recommendations
        //  c. For items that will not fit sort them and provide soft recommendations if in the middle or early day
        //  Or hard recommendations if the end of the day or diseases

        //Sort the menu items in ascending order according to the calories
        Collections.sort(plateNamesPM, new Comparator<RestaurantMenuItem>() {
            @Override public int compare(RestaurantMenuItem p1, RestaurantMenuItem p2) {
                return (Integer.parseInt(p1.getCalories()) - Integer.parseInt(p2.getCalories()));
            }
        });

        int tempCalories = computeMaxMin (MaxMin.MIN, calMax, (int) (nutritionFilters.calories.selected));
        for (RestaurantMenuItem mi : plateNamesPM) {
            if (Integer.parseInt(mi.getCalories()) <= tempCalories) {
                //Calories match

            } else if (Integer.parseInt(mi.getCalories()) <= tempCalories * 1.333) {
                //Calories recommendation
                mi.addRecommendations("Eat 3/4 the item!");
            }

            else if (Integer.parseInt(mi.getCalories()) <= tempCalories * 1.5) {
                //Calories recommendation
                mi.addRecommendations("Eat 2/3 the item!");
            }
            else if (Integer.parseInt(mi.getCalories()) <= tempCalories * 2) {
                //Calories recommendation
                mi.addRecommendations("Eat 1/2 the item!");
            }
        }

    }
}
