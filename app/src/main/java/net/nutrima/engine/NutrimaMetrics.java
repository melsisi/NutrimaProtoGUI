package net.nutrima.engine;

/**
 * Created by ayehia on 9/22/2016.
 */
public class NutrimaMetrics {
    // ===============================================
    // === CALCULATED SECTION; ALL PRIVATE MEMBERS ===
    // ===============================================

    //Calculated Body Mass Index
    private double bmi = 25;

    //Calculated Base Metabolic Rate calories
    private double bmr = 1833;

    //Calculated activity level calories
    private double actLevelCal = 367;

    //Calculated dialy total calories for weight maintenance
    private double weightMainCal = 2200;

    //Nutrima dialy recommended calories
    private double calNutrima = 2200;

    //User daily recommended calories
    private double calUser = 2200;

    //Body Fat %
    private double bfDod =100;
    private double bfYmca=100;
    private double bfYmcaModified=100;
    private double bfCovert=100;
    private double bfNutrima=100;

    //USDA daily recommended ranges
    private int usdaProteinMin;
    private int usdaProteinMax;
    private int usdaCarbsMin;
    private int usdaCarbsMax;
    private int usdaFatMin;
    private int usdaFatMax;
    private int usdaSatFatMin;
    private int usdaSatFatMax;
    private int usdaDietaryFiber;
    private int usdaAddedSugarMin;
    private int usdaAddedSugarMax;
    private int usdaSodiumMin = 500;
    private int usdaSodiumMax = 2300;

    //Nutrima Recommendation percentage
    private final byte bulkupProteinMin = 25;
    private final byte bulkupProteinMax = 35 ;
    private final byte bulkupCarbsMin   = 40;
    private final byte bulkupCarbsMax   = 60;
    private final byte bulkupFatMin     = 15;
    private final byte bulkupFatMax     = 25;

    private final byte maintProteinMin  = 25;
    private final byte maintProteinMax  = 35;
    private final byte maintCarbsMin    = 30;
    private final byte maintCarbsMax    = 50;
    private final byte maintFatMin      = 25;
    private final byte maintFatMax      = 35;

    private final byte trimProteinMin   = 40;
    private final byte trimProteinMax   = 50;
    private final byte trimCarbsMin     = 10;
    private final byte trimCarbsMax     = 30;
    private final byte trimFatMin       = 30;
    private final byte trimFatMax       = 40;

    //Nutrima recommended daily intake
    //MacroNutrients
    private int proteinNutrima=150;
    private int carbsNutrima=250;
    private int fatNutrima=44;
    private int satFatNutrima=22;
    private int dietaryFiberNutrima=28;
    private int addedSugarNutrima=0;
    private int sodiumNutrima=2300;

    private int proteinPercNutrima=30;
    private int carbsPercNutrima=50;
    private int fatPercNutrima=20;

    //Water
    private double waterNutrima=69;


    //TODO: Add USDA vitamins and minerals recommended intake





//--- Calculation ---
    //BMI
    public void calcBmi(UserProfile userProfile){
        try {
            if (userProfile.getMetricImperial() == MetricStandard.IMPERIAL)
                this.bmi = (userProfile.getWeight() * 0.453592) / Math.pow((userProfile.getHeight()*0.0254), 2);
            else
                this.bmi = (userProfile.getWeight()) / Math.pow((userProfile.getHeight() / 100), 2);
        } catch (ArithmeticException ae) {
            ae.printStackTrace();
        }
    }


    //CALORIES
    private void calcBmr(UserProfile userProfile){
        try {
            if (userProfile.getMetricImperial() == MetricStandard.METRIC) {
                if (userProfile.getGender() == Gender.MALE)
                    this.bmr =  66.47 +
                                13.75*userProfile.getWeight() +
                                5*userProfile.getHeight() -
                                6.75*userProfile.getAge();
                else
                    this.bmr =  655.09 +
                                9.56*userProfile.getWeight() +
                                1.84*userProfile.getHeight() -
                                4.67*userProfile.getAge();
            } else {
                if (userProfile.getGender() == Gender.MALE)
                    this.bmr =  66.47 +
                            13.75*userProfile.getWeight()*0.453592 +
                            5*userProfile.getHeight()*2.54 -
                            6.75*userProfile.getAge();
                else
                    this.bmr =  655.09 +
                            9.56*userProfile.getWeight()*0.453592 +
                            1.84*userProfile.getHeight()*2.54 -
                            4.67*userProfile.getAge();
            }
        } catch (ArithmeticException ae) {
            ae.printStackTrace();
        }
    }

    private void calcActLevelCal(UserProfile userProfile){
        try {
            switch (userProfile.getActLevel()) {
                case SEDENTARY:
                    this.actLevelCal = 0.2 * this.bmr;
                    break;
                case LIGHT_ACTIVE:
                    this.actLevelCal = 0.375 * this.bmr;
                    break;
                case MODERATE_ACTIVE:
                    this.actLevelCal = 0.55 * this.bmr;
                    break;
                case HIGH_ACTIVE:
                    this.actLevelCal = 0.725 * this.bmr;
                    break;
                case VERY_HIGH_ACTIVE:
                    this.actLevelCal = 0.9 * this.bmr;
                    break;
                default:
                    this.actLevelCal = 0.2 * this.bmr;
                    break;
            }
        } catch (ArithmeticException ae) {
            ae.printStackTrace();
        }
    }

    private void calcWeightMainCal(UserProfile userProfile){
        calcBmr(userProfile);
        calcActLevelCal(userProfile);
        try {
            this.weightMainCal = this.actLevelCal + this.bmr;
        } catch (ArithmeticException ae) {
            ae.printStackTrace();
        }
    }

    private void calcCalUser(UserProfile userProfile) {
        try {
            switch (userProfile.getWeightGoal()) {
                case LOSE:
                    this.calUser = (int) (this.weightMainCal - userProfile.getCalorieOffset());
                    break;
                case BULKUP:
                    this.calUser = (int) (this.weightMainCal + userProfile.getCalorieOffset());
                    break;
                case MAINTAIN:
                    this.calUser = (int) (this.weightMainCal);
                    break;
                default:
                    this.calUser = (int) (this.weightMainCal);
                    break;
            }
        } catch (ArithmeticException ae) {
            ae.printStackTrace();
        }
    }

    public void calcCalNutrima(UserProfile userProfile) {
        calcWeightMainCal(userProfile);
        calcCalUser(userProfile);
        /*
        TODO: Handle the case when calories from user prespecitve
        Is different from Nutrima calculation. We need the user to
        decide in this case. Below when overweight is being
        identified by Nutrima, we decide directly that user needs to lose weight
        We should the user decide according to his prespective (userprofile.weightGoal)
        */
        try {
            if (userProfile.getGender() == Gender.MALE) {
                if (bmi >= 30 || bfNutrima > 24) { //Obese
                    calNutrima = (int) (weightMainCal - userProfile.getCalorieOffset());
                } else {
                    calNutrima = calUser;
                }
            } else {
                if (bmi >= 30 || bfNutrima > 31) { //Obese
                    calNutrima = (int) (weightMainCal - userProfile.getCalorieOffset());
                } else {
                    calNutrima = calUser;
                }
            }
            if (calNutrima != calUser) {
                //TODO: defer this to calling context
                /*Toast.makeText(MainActivity,
                        "My calorie recommendation customized for your health is different than your goal! " +
                                "Revisit Goals section if needed.",
                        Toast.LENGTH_LONG).show();*/
            }
        } catch (ArithmeticException ae) {
            ae.printStackTrace();
        }
    }


    // BODY FAT
    public boolean checkBfInitValues (UserProfile userProfile) {
        if  ((userProfile.getWaist()==-1) || (userProfile.getWrist()==-1) ||
            (userProfile.getForearm()==-1) || (userProfile.getHips()==-1) ||
            (userProfile.getNeck()==-1) || (userProfile.getThigh()==-1) ||
                (userProfile.getCalf()==-1)) {
            return false;
        } else
            return true;
    }
    private void calcBfDod(UserProfile userProfile) {
        if (! checkBfInitValues(userProfile)) {
            return;
        }
        try {
            if (userProfile.getMetricImperial() == MetricStandard.IMPERIAL) {
                if (userProfile.getGender() == Gender.MALE)
                    this.bfDod = 86.01 *
                            Math.log10(userProfile.getWaist()-userProfile.getNeck()) -
                            70.041*Math.log10(userProfile.getHeight()) +
                            36.76;
                else
                    this.bfDod = 163.205 *
                            Math.log10(userProfile.getWaist()+userProfile.getHips()-userProfile.getNeck()) -
                            97.684*Math.log10(userProfile.getHeight()) -
                            78.387;
            } else {
                if (userProfile.getGender() == Gender.MALE)
                    this.bfDod = 86.01 *
                            Math.log10((userProfile.getWaist()-userProfile.getNeck())*0.393701) -
                            70.041*Math.log10(userProfile.getHeight()*0.393701) +
                            36.76;
                else
                    this.bfDod = 163.205 *
                            Math.log10((userProfile.getWaist()+userProfile.getHips()-userProfile.getNeck())*0.393701) -
                            97.684*Math.log10(userProfile.getHeight()*0.393701) -
                            78.387;
            }
        } catch (ArithmeticException ae) {
            ae.printStackTrace();
        }
    }

    private void calcBfYmca(UserProfile userProfile) {
        if (! checkBfInitValues(userProfile))
            return;
        try {
            if (userProfile.getMetricImperial() == MetricStandard.IMPERIAL) {
                if (userProfile.getGender() == Gender.MALE)
                    this.bfYmca = (100 *
                            (4.15*userProfile.getWaist()-0.082*userProfile.getWeight()-98.42)) /
                            userProfile.getWeight();
                else
                    this.bfYmca = (100 *
                            (4.15*userProfile.getWaist()-0.082*userProfile.getWeight()-76.76)) /
                            userProfile.getWeight();
            } else {
                if (userProfile.getGender() == Gender.MALE)
                    this.bfYmca = (100 *
                            (4.15*userProfile.getWaist()*0.393701-0.082*userProfile.getWeight()*0.393701-98.42)) /
                            (userProfile.getWeight()*0.453592);
                else
                    this.bfYmca = (100 *
                            (4.15*userProfile.getWaist()*0.393701-0.082*userProfile.getWeight()*0.393701-76.76)) /
                            (userProfile.getWeight()*0.453592);
            }
        } catch (ArithmeticException ae) {
            ae.printStackTrace();
        }
    }

    private void calcBfYmcaModified(UserProfile userProfile) {
        if (! checkBfInitValues(userProfile))
            return;
        try {
            if (userProfile.getMetricImperial() == MetricStandard.IMPERIAL) {
                if (userProfile.getGender() == Gender.MALE)
                    this.bfYmca = (100 *
                            (4.15*userProfile.getWaist()-0.082*userProfile.getWeight()-94.42)) /
                            userProfile.getWeight();
                else //TODO: Get formula for females
                    this.bfYmca = (100 *
                            (0.268*userProfile.getWeight()-0.318 * userProfile.getWrist() + 0.157*userProfile.getWaist()+0.245*userProfile.getHips()-0.434*userProfile.getForearm()-8.987)) /
                            userProfile.getWeight();
            } else {
                if (userProfile.getGender() == Gender.MALE)
                    this.bfYmca = (100 *
                            (4.15*userProfile.getWaist()*0.393701-0.082*userProfile.getWeight()*0.393701-94.42)) /
                            (userProfile.getWeight()*0.453592);
                else //TODO: Get formula for females
                    this.bfYmca = (100 *
                            (0.268*userProfile.getWeight()*0.453592-0.393701*0.318 * userProfile.getWrist() + 0.393701*0.157*userProfile.getWaist()+0.393701*0.245*userProfile.getHips()-0.393701*0.434*userProfile.getForearm()-8.987)) /
                            (userProfile.getWeight()*0.453592);
            }
        } catch (ArithmeticException ae) {
            ae.printStackTrace();
        }
    }

    private void calcBfCovert(UserProfile userProfile) {
        if (! checkBfInitValues(userProfile))
            return;
        try {
            if (userProfile.getMetricImperial() == MetricStandard.IMPERIAL) {
                if (userProfile.getGender() == Gender.MALE) {
                    if (userProfile.getAge() < 31) {
                     this.bfCovert = userProfile.getWaist() +
                                    0.5*userProfile.getHips() -
                                    3*userProfile.getForearm() -
                                    userProfile.getWrist();
                    } else {
                        this.bfCovert = userProfile.getWaist() +
                                0.5*userProfile.getHips() -
                                2.7*userProfile.getForearm() -
                                userProfile.getWrist();
                    }
                } else { //Female
                    if (userProfile.getAge() < 31) {
                        this.bfCovert = userProfile.getHips() +
                                0.8*userProfile.getThigh() -
                                2*userProfile.getCalf() -
                                userProfile.getWrist();
                    } else {
                        this.bfCovert = userProfile.getHips() +
                                userProfile.getThigh() -
                                2*userProfile.getCalf() -
                                userProfile.getWrist();
                    }
                }
            } else {
                if (userProfile.getGender() == Gender.MALE) {
                    if (userProfile.getAge() < 31) {
                        this.bfCovert = 0.393701*(userProfile.getWaist() +
                                0.5*userProfile.getHips() -
                                3*userProfile.getForearm() -
                                userProfile.getWrist());
                    } else {
                        this.bfCovert = 0.393701*(userProfile.getWaist() +
                                0.5*userProfile.getHips() -
                                2.7*userProfile.getForearm() -
                                userProfile.getWrist());
                    }
                } else { //Female
                    if (userProfile.getAge() < 31) {
                        this.bfCovert = 0.393701*(userProfile.getHips() +
                                0.8*userProfile.getThigh() -
                                2*userProfile.getCalf() -
                                userProfile.getWrist());
                    } else {
                        this.bfCovert = 0.393701*(userProfile.getHips() +
                                userProfile.getThigh() -
                                2*userProfile.getCalf() -
                                userProfile.getWrist());
                    }
                }
            }
        } catch (ArithmeticException ae) {
            ae.printStackTrace();
        }
    }

    public void calcBfNutrima(UserProfile userProfile) {
        if (! checkBfInitValues(userProfile))
            return;

        try {
            if (userProfile.isAtheletic()) {
                calcBfYmca(userProfile);
                calcBfCovert(userProfile);
                this.bfNutrima = (this.bfYmca + this.bfCovert) / 2; //TODO: Need corner cases to test
            } else {
                calcBfYmcaModified(userProfile);
                calcBfDod(userProfile);
                this.bfNutrima = (this.bfYmcaModified + this.bfDod) / 2; //TODO: Need corner cases to test
            }
        } catch (ArithmeticException ae) {
            ae.printStackTrace();
        }
    }


    //Macro Nutrients
    public void calcUsdaMacroNutrients (UserProfile userProfile) {
        int age = userProfile.getAge();

        usdaCarbsMin = (int) ((45 * calNutrima) / (4 * 100));
        usdaCarbsMax = (int) ((65 * calNutrima) / (4 * 100));

        usdaProteinMin = (int) ((10 * calNutrima) / (4 * 100));
        usdaProteinMax = (int) ((35 * calNutrima) / (4 * 100));

        usdaFatMin = (int) ((20 * calNutrima) / (9 * 100));
        usdaFatMax = (int) ((35 * calNutrima) / (9 * 100));

        usdaAddedSugarMin = 0;
        usdaAddedSugarMax = (int) ((10 * calNutrima)  / (4 * 100));

        usdaSatFatMin = 0;
        usdaSatFatMax = (int) ((10 * calNutrima) / (9 * 100));

        usdaDietaryFiber = (int) (14 * calNutrima/1000);

        if (age <4) {
            usdaProteinMin = (int) ((5 * calNutrima)  / (4 * 100));
            usdaProteinMax = (int) ((20 * calNutrima) / (4 * 100));
            usdaFatMin = (int) ((30 * calNutrima)  / (9 * 100));
            usdaFatMax = (int) ((40 * calNutrima) / (9 * 100));

        } else if (age < 9){
            usdaProteinMax   = (int) ((30 * calNutrima) / (4 * 100));
            usdaFatMin       = (int) ((25 * calNutrima)  / (9 * 100));

        } else if (age < 14){
            usdaProteinMax   = (int) ((30 * calNutrima) / (4 * 100));
            usdaFatMin       = (int) ((25 * calNutrima)  / (9 * 100));

        } else if (age < 19) {
            usdaProteinMax = (int) ((30 * calNutrima) / (4 * 100));
            usdaFatMin = (int) ((25 * calNutrima) / (9 * 100));
        }
    }

    public void calcNutrimaMacroNutrients (UserProfile userProfile) {

        WeightGoal weightGoal = userProfile.getWeightGoal();
        calcUsdaMacroNutrients(userProfile);

        //TODO: Handle diseases
        satFatNutrima     = usdaSatFatMax;
        sodiumNutrima     = usdaSodiumMax;
        addedSugarNutrima = 0;
        dietaryFiberNutrima = usdaDietaryFiber;

        switch (userProfile.getbType()) {
            case ECTOMORPH:
                if (weightGoal == WeightGoal.BULKUP) {
                    proteinNutrima = (int) (bulkupProteinMin * calNutrima /(100*4));
                    carbsNutrima   = (int) (bulkupCarbsMax   * calNutrima /(100*4));
                } else if (weightGoal == WeightGoal.MAINTAIN) {
                    proteinNutrima = (int) (((maintProteinMin+maintProteinMax)/2) * calNutrima /(100*4));
                    carbsNutrima   = (int) (maintCarbsMax   * calNutrima /(100*4));
                } else if (weightGoal == WeightGoal.LOSE) {
                    proteinNutrima = (int) (trimProteinMin * calNutrima /(100*4));
                    carbsNutrima   = (int) (trimCarbsMax   * calNutrima /(100*4));
                } else { //TODO: Should NOT be here!
                }
                break;
            case MESOMORPH:
                if (weightGoal == WeightGoal.BULKUP) {
                    proteinNutrima = (int) (((bulkupProteinMax+bulkupProteinMin)/2) * calNutrima /(100*4));
                    carbsNutrima   = (int) (((bulkupCarbsMax+bulkupCarbsMin)/2)   * calNutrima /(100*4));
                } else if (weightGoal == WeightGoal.MAINTAIN) {
                    proteinNutrima = (int) (((maintProteinMin+maintProteinMax)/2) * calNutrima /(100*4));
                    carbsNutrima   = (int) (((maintCarbsMax+maintCarbsMin)/2)   * calNutrima /(100*4));
                } else if (weightGoal == WeightGoal.LOSE) {
                    proteinNutrima = (int) (((trimProteinMax+trimProteinMin)/2) * calNutrima /(100*4));
                    carbsNutrima   = (int) (((trimCarbsMax+trimCarbsMin)/2)   * calNutrima /(100*4));
                } else { //TODO: Should NOT be here!
                }
                break;
            case ENDOMORPH:
                if (weightGoal == WeightGoal.BULKUP) {
                    proteinNutrima = (int) (bulkupProteinMax * calNutrima /(100*4));
                    carbsNutrima   = (int) (bulkupCarbsMin   * calNutrima /(100*4));
                } else if (weightGoal == WeightGoal.MAINTAIN) {
                    proteinNutrima = (int) (maintProteinMax * calNutrima /(100*4));
                    carbsNutrima   = (int) (maintCarbsMin   * calNutrima /(100*4));
                } else if (weightGoal == WeightGoal.LOSE) {
                    proteinNutrima = (int) (trimProteinMax * calNutrima /(100*4));
                    carbsNutrima   = (int) (trimCarbsMin   * calNutrima /(100*4));
                } else { //TODO: Should NOT be here!
                }
                break;
            default: //TODO: Should NOT get here!!
                break;
        }
        fatNutrima     = (int) ((calNutrima - 4*(proteinNutrima+carbsNutrima))/9);
    }


    //Water
    public void calcNutrimaWater (UserProfile userProfile){
        Gender gender = userProfile.getGender();
        if (userProfile.getMetricImperial() == MetricStandard.IMPERIAL) {
            waterNutrima = (int) ( 2 * userProfile.getWeight() / 3);
            if (gender == Gender.PREGNANT_FEMALE || gender == Gender.BREAST_FEEDING_FEMALE) {
                waterNutrima += 32;
            }
        } else {
            waterNutrima = 0.0295735 * ( 2 * userProfile.getWeight()*2.20462 / 3);
            if (gender == Gender.PREGNANT_FEMALE || gender == Gender.BREAST_FEEDING_FEMALE) {
                waterNutrima += 32 * 0.0295735;
            }
        }
    }


    //Calculation Main method
    public void calcNutrima (UserProfile userProfile){
        calcBmi (userProfile);
        calcBfNutrima (userProfile);
        calcCalNutrima (userProfile);
        calcNutrimaMacroNutrients(userProfile);
        calcNutrimaWater (userProfile);
    }


    //--- Setter ---
    //TODO: Check for false settings from user

    public void setActLevelCal(int actLevelCal, UserProfile userProfile) {
        this.actLevelCal = actLevelCal;
        calcCalNutrima(userProfile); //Re-calculate the calories
        calcNutrimaMacroNutrients (userProfile); //Re-calculate the macro nutrients
    }

    public void setBfNutrima(double bfNutrima, UserProfile userProfile) {
        this.bfNutrima = bfNutrima;
        calcCalNutrima(userProfile); //Re-calculate the calories
        calcNutrimaMacroNutrients (userProfile); //Re-calculate the macro nutrients
    }

    public void setCalNutrima(int calNutrima, UserProfile userProfile) {
        this.calNutrima = calNutrima;
        calcNutrimaMacroNutrients (userProfile); //Re-calculate the macro nutrients
    }
    //TODO: Infrastructure does not support user to play with MAcro nutrients yet
    /*public void setProteinNutrima(int proteinNutrima) {
        this.proteinNutrima = proteinNutrima;
    }

    public void setCarbsNutrima(int carbsNutrima) {
        this.carbsNutrima = carbsNutrima;
    }

    public void setFatNutrima(int fatNutrima) {
        this.fatNutrima = fatNutrima;
    }*/

    public void setSatFatNutrima(int satFatNutrima) {
        this.satFatNutrima = satFatNutrima;
    }

    public void setDietaryFiberNutrima(int dietaryFiberNutrima) {
        this.dietaryFiberNutrima = dietaryFiberNutrima;
    }

    public void setAddedSugarNutrima(int addedSugarNutrima) {
        this.addedSugarNutrima = addedSugarNutrima;
    }

    public void setSodiumNutrima(int sodiumNutrima) {
        this.sodiumNutrima = sodiumNutrima;
    }

    public void setWaterNutrima(int waterNutrima) {
        this.waterNutrima = waterNutrima;
    }


    //--- getters ---
    public double getBmi() {
        return bmi;
    }

    public double getBmr() {
        return bmr;
    }

    public double getActLevelCal() {
        return actLevelCal;
    }

    public double getWeightMainCal() {
        return weightMainCal;
    }

    public double getCalNutrima() {
        return calNutrima;
    }

    public double getBfDod() {
        return bfDod;
    }

    public double getBfYmca() {
        return bfYmca;
    }

    public double getBfCovert() {
        return bfCovert;
    }

    public double getBfNutrima() {
        return bfNutrima;
    }

    public int getUsdaProteinMin() {
        return usdaProteinMin;
    }

    public int getUsdaProteinMax() {
        return usdaProteinMax;
    }

    public int getUsdaCarbsMin() {
        return usdaCarbsMin;
    }

    public int getUsdaCarbsMax() {
        return usdaCarbsMax;
    }

    public int getUsdaFatMin() {
        return usdaFatMin;
    }

    public int getUsdaFatMax() {
        return usdaFatMax;
    }

    public int getUsdaSatFatMin() {
        return usdaSatFatMin;
    }

    public int getUsdaSatFatMax() {
        return usdaSatFatMax;
    }

    public int getUsdaDietaryFiber() {
        return usdaDietaryFiber;
    }

    public int getUsdaAddedSugarMin() {
        return usdaAddedSugarMin;
    }

    public int getUsdaAddedSugarMax() {
        return usdaAddedSugarMax;
    }

    public int getUsdaSodiumMin() {
        return usdaSodiumMin;
    }

    public int getUsdaSodiumMax() {
        return usdaSodiumMax;
    }

    public byte getBulkupProteinMin() {
        return bulkupProteinMin;
    }

    public byte getBulkupProteinMax() {
        return bulkupProteinMax;
    }

    public byte getBulkupCarbsMin() {
        return bulkupCarbsMin;
    }

    public byte getBulkupCarbsMax() {
        return bulkupCarbsMax;
    }

    public byte getBulkupFatMin() {
        return bulkupFatMin;
    }

    public byte getBulkupFatMax() {
        return bulkupFatMax;
    }

    public byte getMaintProteinMin() {
        return maintProteinMin;
    }

    public byte getMaintProteinMax() {
        return maintProteinMax;
    }

    public byte getMaintCarbsMin() {
        return maintCarbsMin;
    }

    public byte getMaintCarbsMax() {
        return maintCarbsMax;
    }

    public byte getMaintFatMin() {
        return maintFatMin;
    }

    public byte getMaintFatMax() {
        return maintFatMax;
    }

    public byte getTrimProteinMin() {
        return trimProteinMin;
    }

    public byte getTrimProteinMax() {
        return trimProteinMax;
    }

    public byte getTrimCarbsMin() {
        return trimCarbsMin;
    }

    public byte getTrimCarbsMax() {
        return trimCarbsMax;
    }

    public byte getTrimFatMin() {
        return trimFatMin;
    }

    public byte getTrimFatMax() {
        return trimFatMax;
    }

    public int getProteinNutrima() {
        return proteinNutrima;
    }

    public int getCarbsNutrima() {
        return carbsNutrima;
    }

    public int getFatNutrima() {
        return fatNutrima;
    }

    public int getSatFatNutrima() {
        return satFatNutrima;
    }

    public int getDietaryFiberNutrima() {
        return dietaryFiberNutrima;
    }

    public int getAddedSugarNutrima() {
        return addedSugarNutrima;
    }

    public int getSodiumNutrima() {
        return sodiumNutrima;
    }

    public double getWaterNutrima() {
        return waterNutrima;
    }

    public double getBfYmcaModified() {
        return bfYmcaModified;
    }

    public double getCalUser() {
        return calUser;
    }


}

