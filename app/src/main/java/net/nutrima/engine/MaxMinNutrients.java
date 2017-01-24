package net.nutrima.engine;

/**
 * Created by ayehia on 9/30/2016.
 */

public class MaxMinNutrients {
    public int max=-1;
    public int min=Integer.MAX_VALUE;
    public int selected=-1;

    public MaxMinNutrients (int max, int min, int selected) {
        this.max = max;
        this.min = min;
        this.selected = selected;
    }
    public MaxMinNutrients (int max, int min) {
        this.max = max;
        this.min = min;
    }
    public MaxMinNutrients (int selected) {
        this.selected = selected;
    }


}