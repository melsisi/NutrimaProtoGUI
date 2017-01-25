package net.nutrima.engine;

import net.nutrima.aws.RestaurantMenuItem;
import net.nutrima.nutrimaprotogui.Business;
import net.nutrima.nutrimaprotogui.Globals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

    private ArrayList<RestaurantMenuItem> plateNamesPM = new ArrayList<> ();
    Map<Business, List<RestaurantMenuItem>> restaurantFullMenuMapFiltered =   new  HashMap<Business, List<RestaurantMenuItem>>();

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
            if ((mi.getCalories() == null) || (! mi.getCalories().matches("(\\d+(?:\\.\\d+)?)")) ||
                    (mi.getProtein() == null) ||    (! mi.getProtein().matches("(\\d+(?:\\.\\d+)?)")) ||
                    (mi.getCarbohydrates() == null) ||(! mi.getCarbohydrates().matches("(\\d+(?:\\.\\d+)?)"))   ||
                    (mi.getTotalFat() == null) ||(! mi.getTotalFat().matches("(\\d+(?:\\.\\d+)?)"))   ||
                    (mi.getSaturatedFat() == null) ||(! mi.getSaturatedFat().matches("(\\d+(?:\\.\\d+)?)"))   ||
                    (mi.getFiber() == null) ||(! mi.getFiber().matches("(\\d+(?:\\.\\d+)?)"))   ||
                    (mi.getSugar() == null) ||(! mi.getSugar().matches("(\\d+(?:\\.\\d+)?)"))) {
                continue;
            }

            top.add(mi);

            calories.max = computeMaxMin(MaxMin.MAX, calories.max, (int) (Float.isNaN(Float.parseFloat(mi.getCalories())) ? 0 : Float.parseFloat(mi.getCalories())));
            calories.min = computeMaxMin(MaxMin.MIN, calories.min, (int) (Float.isNaN(Float.parseFloat(mi.getCalories())) ? 0 : Float.parseFloat(mi.getCalories())));

            protein.max = computeMaxMin(MaxMin.MAX, protein.max, (int) Float.parseFloat(mi.getProtein()));
            protein.min = computeMaxMin(MaxMin.MIN, protein.min, (int) Float.parseFloat(mi.getProtein()));

            carbs.max = computeMaxMin(MaxMin.MAX, carbs.max, (int) Float.parseFloat(mi.getCarbohydrates()));
            carbs.min = computeMaxMin(MaxMin.MIN, carbs.min, (int) Float.parseFloat(mi.getCarbohydrates()));

            fat.max = computeMaxMin(MaxMin.MAX, fat.max, (int) Float.parseFloat(mi.getTotalFat()));
            fat.min = computeMaxMin(MaxMin.MIN, fat.min, (int) Float.parseFloat(mi.getTotalFat()));

            satFat.max = computeMaxMin(MaxMin.MAX, satFat.max, (int) Float.parseFloat(mi.getSaturatedFat()));
            satFat.min = computeMaxMin(MaxMin.MIN, satFat.min, (int) Float.parseFloat(mi.getSaturatedFat()));

            fiber.max = computeMaxMin(MaxMin.MAX, fiber.max, (int) Float.parseFloat(mi.getFiber()));
            fiber.min = computeMaxMin(MaxMin.MIN, fiber.min, (int) Float.parseFloat(mi.getFiber()));

            addedSugar.max = computeMaxMin(MaxMin.MAX, addedSugar.max, (int) Float.parseFloat(mi.getSugar()));
            addedSugar.min = computeMaxMin(MaxMin.MIN, addedSugar.min, (int) Float.parseFloat(mi.getSugar()));
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
    public void extractMeals(NutrimaMetrics nutrimaMetrics,
                             CurrentMetrics currentMetrics, NutritionFilters nutritionFilters)
    {
        //TODO: handling nutritionFilters when NULL

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

        //Construct the plateNamesPM
        Map<Business, List<RestaurantMenuItem>> map = Globals.getInstance().getRestaurantFullMenuMap();
        Iterator<Map.Entry<Business, List<RestaurantMenuItem>>> it = map.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<Business, List<RestaurantMenuItem>> pair = it.next();
            ArrayList<RestaurantMenuItem> temp = (ArrayList<RestaurantMenuItem>) pair.getValue();
            plateNamesPM.addAll(temp);
        }


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

        //Sort the menu items in ascending order according to the user selection (default sort on calories)
        switch (nutritionFilters.getFiltersSorton()) {
            case CALORIES:
                if (nutritionFilters.isAscendingSorting()) {
                    Collections.sort(plateNamesPM, new Comparator<RestaurantMenuItem>() {
                        @Override
                        public int compare(RestaurantMenuItem p1, RestaurantMenuItem p2) {
                            return (((int) Float.parseFloat(p1.getCalories())) - ((int) Float.parseFloat(p2.getCalories())));
                        }
                    });
                } else {
                    Collections.sort(plateNamesPM, new Comparator<RestaurantMenuItem>() {
                        @Override
                        public int compare(RestaurantMenuItem p1, RestaurantMenuItem p2) {
                            return (((int) Float.parseFloat(p2.getCalories())) - ((int) Float.parseFloat(p1.getCalories())));
                        }
                    });
                }
                break;

            case PROTEIN:
                if (nutritionFilters.isAscendingSorting()) {
                    Collections.sort(plateNamesPM, new Comparator<RestaurantMenuItem>() {
                        @Override
                        public int compare(RestaurantMenuItem p1, RestaurantMenuItem p2) {
                            return (((int) Float.parseFloat(p1.getProtein())) - ((int) Float.parseFloat(p2.getProtein())));
                        }
                    });
                } else {
                    Collections.sort(plateNamesPM, new Comparator<RestaurantMenuItem>() {
                        @Override
                        public int compare(RestaurantMenuItem p1, RestaurantMenuItem p2) {
                            return (((int) Float.parseFloat(p2.getProtein())) - ((int) Float.parseFloat(p1.getProtein())));
                        }
                    });
                }
                break;

            case CARBS:
                if (nutritionFilters.isAscendingSorting()) {
                    Collections.sort(plateNamesPM, new Comparator<RestaurantMenuItem>() {
                        @Override
                        public int compare(RestaurantMenuItem p1, RestaurantMenuItem p2) {
                            return (((int) Float.parseFloat(p1.getCarbohydrates())) - ((int) Float.parseFloat(p2.getCarbohydrates())));
                        }
                    });
                } else {
                    Collections.sort(plateNamesPM, new Comparator<RestaurantMenuItem>() {
                        @Override
                        public int compare(RestaurantMenuItem p1, RestaurantMenuItem p2) {
                            return (((int) Float.parseFloat(p2.getCarbohydrates())) - ((int) Float.parseFloat(p1.getCarbohydrates())));
                        }
                    });
                }
                break;

            case FAT:
                if (nutritionFilters.isAscendingSorting()) {
                    Collections.sort(plateNamesPM, new Comparator<RestaurantMenuItem>() {
                        @Override
                        public int compare(RestaurantMenuItem p1, RestaurantMenuItem p2) {
                            return (((int) Float.parseFloat(p1.getTotalFat())) - ((int) Float.parseFloat(p2.getTotalFat())));
                        }
                    });
                } else {
                    Collections.sort(plateNamesPM, new Comparator<RestaurantMenuItem>() {
                        @Override
                        public int compare(RestaurantMenuItem p1, RestaurantMenuItem p2) {
                            return (((int) Float.parseFloat(p2.getTotalFat())) - ((int) Float.parseFloat(p1.getTotalFat())));
                        }
                    });
                }
                break;

            case SATFAT:
                if (nutritionFilters.isAscendingSorting()) {
                    Collections.sort(plateNamesPM, new Comparator<RestaurantMenuItem>() {
                        @Override
                        public int compare(RestaurantMenuItem p1, RestaurantMenuItem p2) {
                            return (((int) Float.parseFloat(p1.getSaturatedFat())) - ((int) Float.parseFloat(p2.getSaturatedFat())));
                        }
                    });
                } else {
                    Collections.sort(plateNamesPM, new Comparator<RestaurantMenuItem>() {
                        @Override
                        public int compare(RestaurantMenuItem p1, RestaurantMenuItem p2) {
                            return (((int) Float.parseFloat(p2.getSaturatedFat())) - ((int) Float.parseFloat(p1.getSaturatedFat())));
                        }
                    });
                }
                break;

            case ADDEDSUGAR:
                if (nutritionFilters.isAscendingSorting()) {
                    Collections.sort(plateNamesPM, new Comparator<RestaurantMenuItem>() {
                        @Override
                        public int compare(RestaurantMenuItem p1, RestaurantMenuItem p2) {
                            return (((int) Float.parseFloat(p1.getSugar())) - ((int) Float.parseFloat(p2.getSugar())));
                        }
                    });
                } else {
                    Collections.sort(plateNamesPM, new Comparator<RestaurantMenuItem>() {
                        @Override
                        public int compare(RestaurantMenuItem p1, RestaurantMenuItem p2) {
                            return (((int) Float.parseFloat(p2.getSugar())) - ((int) Float.parseFloat(p1.getSugar())));
                        }
                    });
                }
                break;

            case FIBER:
                if (nutritionFilters.isAscendingSorting()) {
                    Collections.sort(plateNamesPM, new Comparator<RestaurantMenuItem>() {
                        @Override
                        public int compare(RestaurantMenuItem p1, RestaurantMenuItem p2) {
                            return (((int) Float.parseFloat(p1.getFiber())) - ((int) Float.parseFloat(p2.getFiber())));
                        }
                    });
                } else {
                    Collections.sort(plateNamesPM, new Comparator<RestaurantMenuItem>() {
                        @Override
                        public int compare(RestaurantMenuItem p1, RestaurantMenuItem p2) {
                            return (((int) Float.parseFloat(p2.getFiber())) - ((int) Float.parseFloat(p1.getFiber())));
                        }
                    });
                }
                break;

            default:
                if (nutritionFilters.isAscendingSorting()) {
                    Collections.sort(plateNamesPM, new Comparator<RestaurantMenuItem>() {
                        @Override
                        public int compare(RestaurantMenuItem p1, RestaurantMenuItem p2) {
                            return (((int) Float.parseFloat(p1.getCalories())) - ((int) Float.parseFloat(p2.getCalories())));
                        }
                    });
                } else {
                    Collections.sort(plateNamesPM, new Comparator<RestaurantMenuItem>() {
                        @Override
                        public int compare(RestaurantMenuItem p1, RestaurantMenuItem p2) {
                            return (((int) Float.parseFloat(p2.getCalories())) - ((int) Float.parseFloat(p1.getCalories())));
                        }
                    });
                }
                break;
        }

        int tempCalories = computeMaxMin (MaxMin.MIN, calMax, (int) (nutritionFilters.calories.selected));
        for (RestaurantMenuItem mi : plateNamesPM) {
            if ((int) Float.parseFloat(mi.getCalories()) <= tempCalories) {
                //Calories match

            } else if ((int) Float.parseFloat(mi.getCalories()) <= tempCalories * 1.333) {
                //Calories recommendation
                mi.addRecommendations("Eat 3/4 the item!");
            }

            else if ((int) Float.parseFloat(mi.getCalories()) <= tempCalories * 1.5) {
                //Calories recommendation
                mi.addRecommendations("Eat 2/3 the item!");
            }
            else if ((int) Float.parseFloat(mi.getCalories()) <= tempCalories * 2) {
                //Calories recommendation
                mi.addRecommendations("Eat 1/2 the item!");
            }
        }

        Globals.getInstance().setPlateNamesPMFiltered(plateNamesPM);

        for (RestaurantMenuItem mi : plateNamesPM) {
            if (restaurantFullMenuMapFiltered.containsKey(mi.getBusiness())) {
                //ArrayList<RestaurantMenuItem> temp = (ArrayList<RestaurantMenuItem>) restaurantFullMenuMapFiltered.get(mi.getBusiness());
                //temp.add(mi);
                restaurantFullMenuMapFiltered.get(mi.getBusiness()).add(mi);
            } else {
                ArrayList<RestaurantMenuItem> temp = new ArrayList<>();
                temp.add(mi);
                restaurantFullMenuMapFiltered.put(mi.getBusiness(), temp);
            }
        }
        Globals.getInstance().setRestaurantFullMenuMapFiltered(restaurantFullMenuMapFiltered);
    }
}
