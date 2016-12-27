package net.nutrima.nutrimaprotogui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import com.facebook.login.LoginManager;

import net.nutrima.engine.ActivityLevel;
import net.nutrima.engine.BodyType;
import net.nutrima.engine.Gender;
import net.nutrima.engine.MetricStandard;
import net.nutrima.engine.NutrimaMetrics;
import net.nutrima.engine.UserProfile;
import net.nutrima.engine.WeightGoal;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class PersonalInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);

        // Populate age spinner //////////////////////////////////////////
        String[] age_array = new String[91];
        age_array[0] = "Select age";
        for(int i = 1; i < 91; i++)
            age_array[i] = Integer.toString(i + 10);
        Spinner spinner = (Spinner) findViewById(R.id.age_spinner);
        ArrayAdapter<String> ageSpinnerAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, age_array);
        ageSpinnerAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        spinner.setAdapter(ageSpinnerAdapter);
        //////////////////////////////////////////////////////////////////

        // Populate info from file ///////////////////////////////////////
        UserProfile savedUserProfile = readDataFromFile();
        if(savedUserProfile != null)
            populateData(savedUserProfile);
        //////////////////////////////////////////////////////////////////

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.profile_save_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collectDataFromUI();

                Intent activityChangeIntent = new Intent(PersonalInfoActivity.this,
                        MainActivity.class);
                activityChangeIntent.putExtra("FROM", "PROFILE");
                startActivity(activityChangeIntent);
            }
        });
    }

    private void collectDataFromUI() {
        UserProfile userProfile = new UserProfile();

        userProfile.setAtheletic(false);

        if(((Switch)findViewById(R.id.imp_metric_switch)).isChecked())
            userProfile.setMetricImperial(MetricStandard.METRIC);
        else
            userProfile.setMetricImperial(MetricStandard.IMPERIAL);

        // section 1 //////////////////
        switch (((Spinner) findViewById(R.id.gender_spinner)).getSelectedItem().toString()) {
            case "Male":
                userProfile.setGender(Gender.MALE);
                break;
            case "Female":
                userProfile.setGender(Gender.FEMALE);
                break;
            case "Breastfeeding Female":
                userProfile.setGender(Gender.BREAST_FEEDING_FEMALE);
                break;
            case "Pregnant Female":
                userProfile.setGender(Gender.PREGNANT_FEMALE);
                break;
        }
        if(!((Spinner) findViewById(R.id.age_spinner))
                .getSelectedItem().toString().equals("Select age"))
            userProfile.setAge(Integer.parseInt(
                    ((Spinner) findViewById(R.id.age_spinner)).getSelectedItem().toString()));
        if(!isEmpty(((EditText) findViewById(R.id.height_editText))))
            userProfile.setHeight(Float.parseFloat(
                    ((EditText) findViewById(R.id.height_editText)).getText().toString()));
        if(!isEmpty(((EditText) findViewById(R.id.weight_editText))))
            userProfile.setWeight(Float.parseFloat(
                    ((EditText) findViewById(R.id.weight_editText)).getText().toString()));
        ///////////////////////////////

        // section 2 //////////////////
        if(!isEmpty(((EditText) findViewById(R.id.waist_editText))))
            userProfile.setWaist(Float.parseFloat(
                    ((EditText) findViewById(R.id.waist_editText)).getText().toString()));
        if(!isEmpty(((EditText) findViewById(R.id.hips_editText))))
            userProfile.setHips(Float.parseFloat(
                    ((EditText) findViewById(R.id.hips_editText)).getText().toString()));
        if(!isEmpty(((EditText) findViewById(R.id.wrist_editText))))
            userProfile.setWrist(Float.parseFloat(
                    ((EditText) findViewById(R.id.wrist_editText)).getText().toString()));
        if(!isEmpty(((EditText) findViewById(R.id.forearm_editText))))
            userProfile.setForearm(Float.parseFloat(
                    ((EditText) findViewById(R.id.forearm_editText)).getText().toString()));
        if(!isEmpty(((EditText) findViewById(R.id.neck_editText))))
            userProfile.setNeck(Float.parseFloat(
                    ((EditText) findViewById(R.id.neck_editText)).getText().toString()));
        if(!isEmpty(((EditText) findViewById(R.id.thigh_editText))))
            userProfile.setThigh(Float.parseFloat(
                    ((EditText) findViewById(R.id.thigh_editText)).getText().toString()));
        if(!isEmpty(((EditText) findViewById(R.id.calf_editText))))
            userProfile.setCalf(Float.parseFloat(
                    ((EditText) findViewById(R.id.calf_editText)).getText().toString()));
        ///////////////////////////////

        // section 3 //////////////////
        switch (((Spinner) findViewById(R.id.body_type_spinner)).getSelectedItem().toString()) {
            case "Ectomorph":
                userProfile.setbType(BodyType.ECTOMORPH);
                break;
            case "Mesomorph":
                userProfile.setbType(BodyType.MESOMORPH);
                break;
            case "Endomorph":
                userProfile.setbType(BodyType.ENDOMORPH);
                break;
        }
        if(!((Spinner) findViewById(R.id.num_meals_spinner)).
                getSelectedItem().toString().equals("Select one"))
            userProfile.setNumOfMeals(Byte.valueOf(
                    ((Spinner) findViewById(R.id.num_meals_spinner)).getSelectedItem().toString()));
        switch (((Spinner) findViewById(R.id.activity_level_spinner)).getSelectedItem().toString()) {
            case "Sedentary":
                userProfile.setActLevel(ActivityLevel.SEDENTARY);
                break;
            case "Light activity":
                userProfile.setActLevel(ActivityLevel.LIGHT_ACTIVE);
                break;
            case "Moderate activity":
                userProfile.setActLevel(ActivityLevel.MODERATE_ACTIVE);
                break;
            case "High activity":
                userProfile.setActLevel(ActivityLevel.HIGH_ACTIVE);
                break;
            case "Very high activity":
                userProfile.setActLevel(ActivityLevel.VERY_HIGH_ACTIVE);
                break;
        }
        switch (((Spinner) findViewById(R.id.weight_goal_spinner)).getSelectedItem().toString()) {
            case "Lose weight":
                userProfile.setWeightGoal(WeightGoal.LOSE);
                break;
            case "Bulk up":
                userProfile.setWeightGoal(WeightGoal.BULKUP);
                break;
            case "Maintain":
                userProfile.setWeightGoal(WeightGoal.MAINTAIN);
                break;
        }
        if(!isEmpty(((EditText) findViewById(R.id.calorie_offset_editText))))
            userProfile.setCalorieOffset(Integer.parseInt(
                    ((EditText) findViewById(R.id.calorie_offset_editText)).getText().toString()));
        ///////////////////////////////

        // section 4 //////////////////
        if(((CheckBox) findViewById(R.id.heart_disease_checkBox)).isChecked())
            userProfile.setHeartDisease(true);
        if(((CheckBox) findViewById(R.id.diabetes_checkBox)).isChecked())
            userProfile.setDiabetic(true);
        if(((CheckBox) findViewById(R.id.kidney_disease_checkBox)).isChecked())
            userProfile.setKidneyDisease(true);
        if(((CheckBox) findViewById(R.id.liver_disease_checkBox)).isChecked())
            userProfile.setLiverDisease(true);
        if(((CheckBox) findViewById(R.id.cancer_disease_checkBox)).isChecked())
            userProfile.setCancerDisease(true);
        if(((CheckBox) findViewById(R.id.hbp_disease_checkBox)).isChecked())
            userProfile.setHighBloodPressure(true);
        if(((CheckBox) findViewById(R.id.celiac_disease_checkBox)).isChecked())
            userProfile.setCeliacDisease(true);
        if(((CheckBox) findViewById(R.id.other_illness_checkBox)).isChecked())
            if(!isEmpty(((EditText) findViewById(R.id.other_illness_editText))))
                userProfile.setOtherDiseases(
                        ((EditText) findViewById(R.id.other_illness_editText)).getText().toString());
        ///////////////////////////////

        // section 5 //////////////////
        if(((CheckBox) findViewById(R.id.celiac_gluten_allergy_checkBox)).isChecked())
            userProfile.setGlutenIntorlerance(true);
        if(((CheckBox) findViewById(R.id.dairy_allergy_checkBox)).isChecked())
            userProfile.setDiaryAllergy(true);
        if(((CheckBox) findViewById(R.id.egg_allergy_checkBox)).isChecked())
            userProfile.setEggAllergy(true);
        if(((CheckBox) findViewById(R.id.fish_allergy_checkBox)).isChecked())
            userProfile.setFishAllergy(true);
        if(((CheckBox) findViewById(R.id.shellfish_allergy_checkBox)).isChecked())
            userProfile.setShellfishAllergy(true);
        if(((CheckBox) findViewById(R.id.soybeans_allergy_checkBox)).isChecked())
            userProfile.setSoyAllergy(true);
        if(((CheckBox) findViewById(R.id.nuts_allergy_checkBox)).isChecked())
            userProfile.setNutsAllergy(true);
        if(((CheckBox) findViewById(R.id.peanuts_allergy_checkBox)).isChecked())
            userProfile.setPeanutsAllergy(true);
        if(((CheckBox) findViewById(R.id.other_allergy_checkBox)).isChecked())
            if(!isEmpty(((EditText) findViewById(R.id.other_allergy_editText))))
                userProfile.setOtherAllergies(
                        ((EditText) findViewById(R.id.other_allergy_editText)).getText().toString());
        ///////////////////////////////

        NutrimaMetrics nutrimaMetrics = new NutrimaMetrics();
        nutrimaMetrics.calcNutrima(userProfile);
        Globals.getInstance().setNutrimaMetrics(nutrimaMetrics);
        saveDataToStorage(userProfile);
    }

    private boolean isEmpty(EditText myeditText) {
        return myeditText.getText().toString().trim().length() == 0;
    }

    // TODO: Write to and read from cloud

    @Nullable
    public UserProfile readDataFromFile() {
        UserProfile userProfile = null;

        try {
            FileInputStream fis = openFileInput("TEST1");
            ObjectInputStream is = null;

            is = new ObjectInputStream(fis);

            userProfile = (UserProfile) is.readObject();
            is.close();
            fis.close();
        } catch (IOException e) {
            return null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        NutrimaMetrics nutrimaMetrics = new NutrimaMetrics();
        nutrimaMetrics.calcNutrima(userProfile);
        Globals.getInstance().setNutrimaMetrics(nutrimaMetrics);
        Globals.getInstance().setUserProfile(userProfile);
        return userProfile;
    }

    private void saveDataToStorage(UserProfile dataToSave) {
        FileOutputStream fos = null;
        try {
            fos = openFileOutput("TEST1", Context.MODE_PRIVATE);

            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(dataToSave);
            os.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void populateData(UserProfile savedUserProfile) {

        // section 1 //////////////////
        switch (savedUserProfile.getGender()) {
            case MALE:
                ((Spinner) findViewById(R.id.gender_spinner)).setSelection(1, true);
                break;
            case FEMALE:
                ((Spinner) findViewById(R.id.gender_spinner)).setSelection(2, true);
                break;
            case BREAST_FEEDING_FEMALE:
                ((Spinner) findViewById(R.id.gender_spinner)).setSelection(3, true);
                break;
            case PREGNANT_FEMALE:
                ((Spinner) findViewById(R.id.gender_spinner)).setSelection(4, true);
                break;
        }
        ((Spinner) findViewById(R.id.age_spinner)).setSelection(savedUserProfile.getAge() - 10 + 1);
        ((EditText) findViewById(R.id.height_editText)).
                setText(Float.toString(savedUserProfile.getHeight()));
        ((EditText) findViewById(R.id.weight_editText)).
                setText(Float.toString(savedUserProfile.getWeight()));
        ///////////////////////////////

        // section 2 //////////////////
        ((EditText) findViewById(R.id.waist_editText)).
                setText(Float.toString(savedUserProfile.getWaist()));
        ((EditText) findViewById(R.id.hips_editText)).
                setText(Float.toString(savedUserProfile.getHips()));
        ((EditText) findViewById(R.id.wrist_editText)).
                setText(Float.toString(savedUserProfile.getWrist()));
        ((EditText) findViewById(R.id.forearm_editText)).
                setText(Float.toString(savedUserProfile.getForearm()));
        ((EditText) findViewById(R.id.neck_editText)).
                setText(Float.toString(savedUserProfile.getNeck()));
        ((EditText) findViewById(R.id.thigh_editText)).
                setText(Float.toString(savedUserProfile.getThigh()));
        ((EditText) findViewById(R.id.calf_editText)).
                setText(Float.toString(savedUserProfile.getCalf()));
        ///////////////////////////////

        // section 3 //////////////////
        switch (savedUserProfile.getbType()) {
            case ECTOMORPH:
                ((Spinner) findViewById(R.id.body_type_spinner)).setSelection(1, true);
                break;
            case MESOMORPH:
                ((Spinner) findViewById(R.id.body_type_spinner)).setSelection(2, true);
                break;
            case ENDOMORPH:
                ((Spinner) findViewById(R.id.body_type_spinner)).setSelection(3, true);
                break;
        }
        ((Spinner) findViewById(R.id.num_meals_spinner)).
                setSelection(savedUserProfile.getNumOfMeals() - 1 + 1, true);
        switch (savedUserProfile.getActLevel()) {
            case SEDENTARY:
                ((Spinner) findViewById(R.id.activity_level_spinner)).setSelection(1, true);
                break;
            case LIGHT_ACTIVE:
                ((Spinner) findViewById(R.id.activity_level_spinner)).setSelection(2, true);
                break;
            case MODERATE_ACTIVE:
                ((Spinner) findViewById(R.id.activity_level_spinner)).setSelection(3, true);
                break;
            case HIGH_ACTIVE:
                ((Spinner) findViewById(R.id.activity_level_spinner)).setSelection(4, true);
                break;
            case VERY_HIGH_ACTIVE:
                ((Spinner) findViewById(R.id.activity_level_spinner)).setSelection(5, true);
                break;
        }
        switch (savedUserProfile.getWeightGoal()) {
            case LOSE:
                ((Spinner) findViewById(R.id.weight_goal_spinner)).setSelection(1, true);
                break;
            case BULKUP:
                ((Spinner) findViewById(R.id.weight_goal_spinner)).setSelection(2, true);
                break;
            case MAINTAIN:
                ((Spinner) findViewById(R.id.weight_goal_spinner)).setSelection(3, true);
                break;
        }
        ((EditText) findViewById(R.id.calorie_offset_editText)).
                setText(Integer.toString(savedUserProfile.getCalorieOffset()));
        ///////////////////////////////

        // section 4 //////////////////
        if(savedUserProfile.isHeartDisease())
            ((CheckBox) findViewById(R.id.heart_disease_checkBox)).setChecked(true);
        if(savedUserProfile.isDiabetic())
            ((CheckBox) findViewById(R.id.diabetes_checkBox)).setChecked(true);
        if(savedUserProfile.isKidneyDisease())
            ((CheckBox) findViewById(R.id.kidney_disease_checkBox)).setChecked(true);
        if(savedUserProfile.isLiverDisease())
            ((CheckBox) findViewById(R.id.liver_disease_checkBox)).setChecked(true);
        if(savedUserProfile.isCancerDisease())
            ((CheckBox) findViewById(R.id.cancer_disease_checkBox)).setChecked(true);
        if(savedUserProfile.isHighBloodPressure())
            ((CheckBox) findViewById(R.id.hbp_disease_checkBox)).setChecked(true);
        if(savedUserProfile.isCeliacDisease())
            ((CheckBox) findViewById(R.id.celiac_disease_checkBox)).setChecked(true);
        if(savedUserProfile.getOtherDiseases().length() > 0) {
            ((CheckBox) findViewById(R.id.other_illness_checkBox)).setChecked(true);
            ((EditText) findViewById(R.id.other_illness_editText)).
                    setText(savedUserProfile.getOtherDiseases());
        }
        ///////////////////////////////

        // section 5 //////////////////
        if(savedUserProfile.isGlutenIntorlerance())
            ((CheckBox) findViewById(R.id.celiac_gluten_allergy_checkBox)).setChecked(true);
        if(savedUserProfile.isDiaryAllergy())
            ((CheckBox) findViewById(R.id.dairy_allergy_checkBox)).setChecked(true);
        if(savedUserProfile.isEggAllergy())
            ((CheckBox) findViewById(R.id.egg_allergy_checkBox)).setChecked(true);
        if(savedUserProfile.isFishAllergy())
            ((CheckBox) findViewById(R.id.fish_allergy_checkBox)).setChecked(true);
        if(savedUserProfile.isShellfishAllergy())
            ((CheckBox) findViewById(R.id.shellfish_allergy_checkBox)).setChecked(true);
        if(savedUserProfile.isSoyAllergy())
            ((CheckBox) findViewById(R.id.soybeans_allergy_checkBox)).setChecked(true);
        if(savedUserProfile.isNutsAllergy())
            ((CheckBox) findViewById(R.id.nuts_allergy_checkBox)).setChecked(true);
        if(savedUserProfile.isPeanutsAllergy())
            ((CheckBox) findViewById(R.id.peanuts_allergy_checkBox)).setChecked(true);
        if(savedUserProfile.getOtherAllergies().length() > 0) {
            ((CheckBox) findViewById(R.id.other_allergy_checkBox)).setChecked(true);
            ((EditText) findViewById(R.id.other_allergy_editText)).
                    setText(savedUserProfile.getOtherAllergies());
        }
        ///////////////////////////////
    }
}
