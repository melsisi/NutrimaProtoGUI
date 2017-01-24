package net.nutrima.engine;

/**
 * Created by ayehia on 9/30/2016.
 */

public class CurrentMetrics {

    //Calories
    int calories=-1;

    //Macro Nutrients
    int protein=-1;
    int carbs=-1;
    int fat=-1;
    int satFat=-1;
    int addedsugar=-1;
    int fibers=-1;

    //Minerals
    int calcium=0; //mg
    int Iron=0; //mg
    int Magnesium=0; //mg
    int phosphorus=0; //mg
    int potassium=0; //mg
    int sodium=0; //mg
    int zinc=0; //mg
    int copper=0; //mcg
    int manganese=0; //mg
    int selenium=0; //mcg

    //Vitamins
    int vitaminA=0; //mg
    int vitaminE=0; //mg
    int vitaminD=0; //IU
    int vitaminC=0;//mg
    int thiamin=0;
    int riboflavin=0;//mg
    int niacin=0;//mg
    int vitaminB6=0;//mg
    int vitaminB12=0; // mcg
    int choline=0; //mg
    int vitaminK=0; //mcg
    int folate=0; //mcg

    //Time in day
    PartOfDay partOfDay= PartOfDay.BEGIN;

}