package net.nutrima.engine;

/**
 * Created by ayehia on 9/30/2016.
 */

public class MealNutrients {

    //Macro Nutrients
    public MaxMinNutrients calories;
    public MaxMinNutrients protein;
    public MaxMinNutrients carbs;
    public MaxMinNutrients fat;
    public MaxMinNutrients satFat;
    public MaxMinNutrients fiber;
    public MaxMinNutrients addedSugar;

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

   public MaxMinNutrients computeRange (int l, int r) {
       MaxMinNutrients out = new MaxMinNutrients();
        if (l > (int) r * 1.1)  {
            out.max = (int) (r * 1.1);
            out.min = (int) (r * 0.9);
        } else {
            out.max = (int) l;
            out.min = (int) (l * 0.9);
        }
       return out;
    }

    public MealNutrients(NutrimaMetrics nutrimaMetrics, CurrentMetrics currentMetrics, UserProfile userProfile) {

        //Init
        calories    = new MaxMinNutrients();
        protein     = new MaxMinNutrients();
        carbs       = new MaxMinNutrients();
        fat         = new MaxMinNutrients();
        satFat      = new MaxMinNutrients();
        fiber       = new MaxMinNutrients();
        addedSugar  = new MaxMinNutrients();

        int calMax      = (int) (nutrimaMetrics.getCalNutrima() - currentMetrics.calories);
        int proteinMax  = (int) (nutrimaMetrics.getProteinNutrima() - currentMetrics.protein);
        int carbsMax    = (int) (nutrimaMetrics.getCarbsNutrima() - currentMetrics.carbs);
        int fatMax      = (int) (nutrimaMetrics.getFatNutrima() - currentMetrics.fat);
        int satFatMax   = (int) (nutrimaMetrics.getSatFatNutrima() - currentMetrics.satFat);
        int fiberMin    = (int) (nutrimaMetrics.getDietaryFiberNutrima() - currentMetrics.fibers);
        int addedSugarMax = (int) (nutrimaMetrics.getAddedSugarNutrima() - currentMetrics.addedsugar);

        int calMeal      = (int) (nutrimaMetrics.getCalNutrima() / userProfile.getNumOfMeals());
        int proteinMeal  = (int) (nutrimaMetrics.getProteinNutrima() / userProfile.getNumOfMeals());
        int carbsMeal    = (int) (nutrimaMetrics.getCarbsNutrima() / userProfile.getNumOfMeals());
        int fatMeal      = (int) (nutrimaMetrics.getFatNutrima() / userProfile.getNumOfMeals());
        int satFatMeal   = (int) (nutrimaMetrics.getSatFatNutrima() / userProfile.getNumOfMeals());
        int fiberMeal    = (int) (nutrimaMetrics.getDietaryFiberNutrima() / userProfile.getNumOfMeals());

        //Calories
        calories = computeRange(calMax, calMeal);

        //Protein
        protein = computeRange(proteinMax, proteinMeal);

        //Carbs
        carbs = computeRange(carbsMax, carbsMeal);

        //Fat
        fat = computeRange(fatMax, fatMeal);

        //SatFat
        satFat.max = computeMaxMin(MaxMin.MIN, satFatMax, (int) (satFatMeal*1.2));
        satFat.min = 0;

        //Fibers
        if (currentMetrics.partOfDay == PartOfDay.END) {
            fiber.min = fiberMin;
        } else {
            fiber.min = (int) (fiberMeal * 0.8);
        }
        fiber.max = (int) (fiber.min * 1.2);

        //Sugar
        addedSugar.max = addedSugarMax;
        addedSugar.min = 0;
    }
}
