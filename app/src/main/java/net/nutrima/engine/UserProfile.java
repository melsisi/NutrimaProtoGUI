package net.nutrima.engine;

import java.io.Serializable;

/**
 * Created by ayehia on 9/22/2016.
 */

public class UserProfile implements Serializable{

    //Parameter to define if we are working with metric or imperial settings
    private MetricStandard metricImperial = MetricStandard.IMPERIAL;

    // Gender
    private Gender gender = Gender.MALE;

    //Age in Years
    private int age = 30;

    //Weight in kgs or pounds
    private float weight = (float) (179.5);

    //Height in cms or inches
    private float height = (float) (66.9);

    //If a person describes himself as Atheletic
    private boolean atheletic = false;

    //Waist (cm/inch)
    private float waist = -1;

    //Hips (cm/inch)
    private float hips = -1;

    //Wrist (cm/inch)
    private float wrist = -1;

    //Forarm (cm/inch)
    private float forearm = -1;

    //Neck (cm/inch)
    private float neck = -1;

    //Thigh (cm/inch)
    private float thigh = -1;

    //Calf (cm/inch)
    private float calf = -1;

    //BodyType
    private BodyType bType = BodyType.MESOMORPH;

    //Number of Meals per day
    private byte numOfMeals = 3;

    //Activity level
    private ActivityLevel actLevel = ActivityLevel.SEDENTARY;

    //Weight goal
    private WeightGoal weightGoal = WeightGoal.LOSE;

    //calorie offset
    private int calorieOffset = 600;

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

    //--- Setters ---

    public void setMetricImperial(MetricStandard metricImperial) {
        this.metricImperial = metricImperial;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void setWaist(float waist) {
        this.waist = waist;
    }

    public void setHips(float hips) {
        this.hips = hips;
    }

    public void setWrist(float wrist) {
        this.wrist = wrist;
    }

    public void setForearm(float forearm) {
        this.forearm = forearm;
    }

    public void setNeck(float neck) {
        this.neck = neck;
    }

    public void setThigh(float thigh) {
        this.thigh = thigh;
    }

    public void setCalf(float calf) {
        this.calf = calf;
    }

    public void setbType(BodyType bType) {
        this.bType = bType;
    }

    public void setNumOfMeals(byte numOfMeals) {
        this.numOfMeals = numOfMeals;
    }

    public void setActLevel(ActivityLevel actLevel) {
        this.actLevel = actLevel;
    }

    public void setWeightGoal(WeightGoal weightGoal) {
        this.weightGoal = weightGoal;
    }

    public void setCalorieOffset(int calorieOffset) {
        this.calorieOffset = calorieOffset;
    }

    public void setHeartDisease(boolean heartDisease) {
        this.heartDisease = heartDisease;
    }

    public void setDiabetic(boolean diabetic) {
        this.diabetic = diabetic;
    }

    public void setHighBloodPressure(boolean highBloodPressure) {
        this.highBloodPressure = highBloodPressure;
    }

    public void setLiverDisease(boolean liverDisease) {
        this.liverDisease = liverDisease;
    }

    public void setKidneyDisease(boolean kidneyDisease) {
        this.kidneyDisease = kidneyDisease;
    }

    public void setCeliacDisease(boolean celiacDisease) {
        this.celiacDisease = celiacDisease;
    }

    public void setCancerDisease(boolean cancerDisease) {
        this.cancerDisease = cancerDisease;
    }

    public void setOtherDiseases(String otherDiseases) {
        this.otherDiseases = otherDiseases;
    }

    public void setGlutenIntorlerance(boolean glutenIntorlerance) {
        this.glutenIntorlerance = glutenIntorlerance;
    }

    public void setEggAllergy(boolean eggAllergy) {
        this.eggAllergy = eggAllergy;
    }

    public void setDiaryAllergy(boolean diaryAllergy) {
        this.diaryAllergy = diaryAllergy;
    }

    public void setFishAllergy(boolean fishAllergy) {
        this.fishAllergy = fishAllergy;
    }

    public void setShellfishAllergy(boolean shellfishAllergy) {
        this.shellfishAllergy = shellfishAllergy;
    }

    public void setSoyAllergy(boolean soyAllergy) {
        this.soyAllergy = soyAllergy;
    }

    public void setNutsAllergy(boolean nutsAllergy) {
        this.nutsAllergy = nutsAllergy;
    }

    public void setPeanutsAllergy(boolean peanutsAllergy) {
        this.peanutsAllergy = peanutsAllergy;
    }

    public void setOtherAllergies(String otherAllergies) {
        this.otherAllergies = otherAllergies;
    }

    //--- Getters ---

    public MetricStandard getMetricImperial() {
        return metricImperial;
    }

    public Gender getGender() {
        return gender;
    }

    public int getAge() {
        return age;
    }

    public float getWeight() {
        return weight;
    }

    public float getHeight() {
        return height;
    }

    public float getWaist() {
        return waist;
    }

    public float getHips() {
        return hips;
    }

    public float getWrist() {
        return wrist;
    }

    public float getForearm() {
        return forearm;
    }

    public float getNeck() {
        return neck;
    }

    public float getThigh() {
        return thigh;
    }

    public float getCalf() {
        return calf;
    }

    public BodyType getbType() {
        return bType;
    }

    public byte getNumOfMeals() {
        return numOfMeals;
    }

    public ActivityLevel getActLevel() {
        return actLevel;
    }

    public WeightGoal getWeightGoal() {
        return weightGoal;
    }

    public int getCalorieOffset() {
        return calorieOffset;
    }

    public boolean isHeartDisease() {
        return heartDisease;
    }

    public boolean isDiabetic() {
        return diabetic;
    }

    public boolean isHighBloodPressure() {
        return highBloodPressure;
    }

    public boolean isLiverDisease() {
        return liverDisease;
    }

    public boolean isKidneyDisease() {
        return kidneyDisease;
    }

    public boolean isCeliacDisease() {
        return celiacDisease;
    }

    public boolean isCancerDisease() {
        return cancerDisease;
    }

    public String getOtherDiseases() {
        return otherDiseases;
    }

    public boolean isGlutenIntorlerance() {
        return glutenIntorlerance;
    }

    public boolean isEggAllergy() {
        return eggAllergy;
    }

    public boolean isDiaryAllergy() {
        return diaryAllergy;
    }

    public boolean isFishAllergy() {
        return fishAllergy;
    }

    public boolean isShellfishAllergy() {
        return shellfishAllergy;
    }

    public boolean isSoyAllergy() {
        return soyAllergy;
    }

    public boolean isNutsAllergy() {
        return nutsAllergy;
    }

    public boolean isPeanutsAllergy() {
        return peanutsAllergy;
    }

    public String getOtherAllergies() {
        return otherAllergies;
    }

    public boolean isAtheletic() {
        return atheletic;
    }

    public void setAtheletic(boolean atheletic) {
        this.atheletic = atheletic;
    }
}
