package net.nutrima.nutrimaprotogui;


import android.app.Activity;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import net.nutrima.engine.MetricStandard;
import net.nutrima.engine.UserProfile;
import net.nutrima.engine.WeightGoal;


public class ProfileMeasurementsFragment extends Fragment {

    MetricStandard metricStandard = MetricStandard.IMPERIAL;
    Boolean isUserMale = null;

    public ProfileMeasurementsFragment() { }

    public static ProfileMeasurementsFragment newInstance() {
        ProfileMeasurementsFragment fragment = new ProfileMeasurementsFragment();
        //Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        //fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile_measurments, container, false);
        handleUnitsRadioButtons(rootView);
        populateAgeSpinner(rootView);
        handleGenderImageViews(rootView);
        handleNextButton(rootView);
        return rootView;
    }

    private void handleUnitsRadioButtons(View rootView) {
        final EditText height1EditText = (EditText) rootView.findViewById(R.id.height1_editText);
        final EditText height2EditText = (EditText) rootView.findViewById(R.id.height2_editText);
        final EditText weightEditText = (EditText) rootView.findViewById(R.id.weight_editText);

        RadioGroup categoryGroup = (RadioGroup) rootView.findViewById(R.id.units_radioButton);
        categoryGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId) {
                    case R.id.imperial_radioButton:
                        metricStandard = MetricStandard.IMPERIAL;
                        height1EditText.setHint("height (ft)");
                        height2EditText.setVisibility(View.VISIBLE);
                        height2EditText.setHint("height (in)");
                        weightEditText.setHint("weight (lb)");
                        break;
                    case R.id.metric_radioButton:
                        metricStandard = MetricStandard.METRIC;
                        height1EditText.setHint("height (cm)");
                        height2EditText.setVisibility(View.INVISIBLE);
                        weightEditText.setHint("weight (kg)");
                        break;
                }
            }
        });
    }

    private void populateAgeSpinner(View rootView) {
        String[] age_array = new String[91];
        age_array[0] = "Select age";
        for(int i = 1; i < 91; i++)
            age_array[i] = Integer.toString(i + 10);
        Spinner spinner = (Spinner) rootView.findViewById(R.id.age_spinner);
        ArrayAdapter<String> ageSpinnerAdapter = new ArrayAdapter<>(
                this.getActivity(), android.R.layout.simple_spinner_item, age_array);
        ageSpinnerAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        spinner.setAdapter(ageSpinnerAdapter);
    }

    private void handleGenderImageViews(View rootView) {
        final ImageView maleImageView = (ImageView)rootView.findViewById(R.id.gender_male_imageView);
        final ImageView femaleImageView = (ImageView)rootView.findViewById(R.id.gender_female_imageView);

        View.OnClickListener clickListener = new View.OnClickListener() {
            public void onClick(View v) {
                if (v.equals(maleImageView)) {
                    maleImageView.setColorFilter(Color.argb(0, 0, 0, 0));
                    femaleImageView.setColorFilter(Color.argb(200, 255, 255, 255));
                    isUserMale = true;
                }
                else if (v.equals(femaleImageView)) {
                    femaleImageView.setColorFilter(Color.argb(0, 0, 0, 0));
                    maleImageView.setColorFilter(Color.argb(200, 255, 255, 255));
                    isUserMale = false;
                }
            }
        };
        maleImageView.setOnClickListener(clickListener);
        femaleImageView.setOnClickListener(clickListener);
    }

    private void handleNextButton(final View rootView) {
        Button button= (Button) rootView.findViewById(R.id.next_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collectDataFromUI(rootView);
            }
        });
    }

    private void collectDataFromUI(View rootView) {
        EditText height1EditText = ((EditText) rootView.findViewById(R.id.height1_editText));
        EditText height2EditText = ((EditText) rootView.findViewById(R.id.height2_editText));
        EditText weightEditText = ((EditText) rootView.findViewById(R.id.weight_editText));
        Spinner ageSpinner = ((Spinner) rootView.findViewById(R.id.age_spinner));
        Spinner weightGoalSpinner = ((Spinner) rootView.findViewById(R.id.weight_goal_spinner));

        if(isEmpty(height1EditText)) {
            Snackbar.make(rootView, "Height can't be empty", Snackbar.LENGTH_LONG).show();
            return;
        }

        if(isEmpty(weightEditText)) {
            Snackbar.make(rootView, "Weight can't be empty", Snackbar.LENGTH_LONG).show();
            return;
        }

        if(isUserMale == null) {
            Snackbar.make(rootView, "Please select a gender", Snackbar.LENGTH_LONG).show();
            return;
        }

        if(ageSpinner.getSelectedItem().toString().equals("Select age")) {
            Snackbar.make(rootView, "Please select age", Snackbar.LENGTH_LONG).show();
            return;
        }

        if(weightGoalSpinner.getSelectedItem().toString().equals("Select one")) {
            Snackbar.make(rootView, "Please select weight goal", Snackbar.LENGTH_LONG).show();
            return;
        }

        if(isEmpty(height2EditText))
            height2EditText.setText("0");

        UserProfile tempProfile = Globals.getInstance().getUserProfile();
        if(tempProfile == null)
            tempProfile = new UserProfile();

        tempProfile.setMetricImperial(this.metricStandard);

        tempProfile.setAge(Integer.parseInt(ageSpinner.getSelectedItem().toString()));

        switch (weightGoalSpinner.getSelectedItem().toString()) {
            case "Lose weight":
                tempProfile.setWeightGoal(WeightGoal.LOSE);
                break;
            case "Bulk up":
                tempProfile.setWeightGoal(WeightGoal.BULKUP);
                break;
            case "Maintain":
                tempProfile.setWeightGoal(WeightGoal.MAINTAIN);
                break;
        }

        float heightInCm = 0.0f;

        if(this.metricStandard == MetricStandard.METRIC)
            heightInCm = Float.parseFloat(
                    height1EditText.getText().toString());
        else
            heightInCm = ftInToCm(Integer.parseInt(height1EditText.getText().toString()),
                    Integer.parseInt(height2EditText.getText().toString()));

        tempProfile.setHeight(heightInCm);

        float weightInKg = 0.0f;
        if(this.metricStandard == MetricStandard.METRIC)
            weightInKg = Float.parseFloat(weightEditText.getText().toString());
        else
            weightInKg = lbToKg(Float.parseFloat(weightEditText.getText().toString()));

        tempProfile.setWeight(weightInKg);

        Globals.getInstance().setUserProfile(tempProfile);

        mCallback.onNextClicked(0);
    }

    private float lbToKg(float lb) {
        return (lb*0.453592f);
    }

    private float ftInToCm(int ft, int in) {
        return ((ft*30.48f) + (in*2.54f));
    }

    private boolean isEmpty(EditText myEditText) {
        return myEditText.getText().toString().trim().length() == 0;
    }

    OnNextClickedListener mCallback;

    // Container Activity must implement this interface
    public interface OnNextClickedListener {
        public void onNextClicked(int myOrder);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnNextClickedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnNextClickedListener");
        }
    }
}
