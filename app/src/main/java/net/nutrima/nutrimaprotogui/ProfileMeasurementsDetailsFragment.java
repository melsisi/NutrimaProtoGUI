package net.nutrima.nutrimaprotogui;


import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import net.nutrima.engine.BodyType;
import net.nutrima.engine.MetricStandard;
import net.nutrima.engine.UserProfile;


public class ProfileMeasurementsDetailsFragment extends Fragment {

    public ProfileMeasurementsDetailsFragment() { }

    public static ProfileMeasurementsDetailsFragment newInstance() {
        ProfileMeasurementsDetailsFragment fragment = new ProfileMeasurementsDetailsFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_profile_measurments_details, container, false);
        handleNextButton(rootView);
        handleBackButton(rootView);
        handleEditTexts(rootView);
        return rootView;
    }

    private void handleEditTexts(View rootView) {
        MetricStandard metricStandard= Globals.getInstance().getUserProfile().getMetricImperial();
        if(metricStandard == MetricStandard.METRIC) {
            ((EditText)rootView.findViewById(R.id.waist_editText)).setHint("waist (cm)");
            ((EditText)rootView.findViewById(R.id.hips_editText)).setHint("hips (cm)");
            ((EditText)rootView.findViewById(R.id.wrist_editText)).setHint("wrist (cm)");
            ((EditText)rootView.findViewById(R.id.thigh_editText)).setHint("thigh (cm)");
            ((EditText)rootView.findViewById(R.id.forearm_editText)).setHint("forearm (cm)");
            ((EditText)rootView.findViewById(R.id.neck_editText)).setHint("neck (cm)");
            ((EditText)rootView.findViewById(R.id.calf_editText)).setHint("calf (cm)");
        }
    }

    private void handleNextButton(final View rootView) {
        Button button= (Button) rootView.findViewById(R.id.next_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Add handlers
                collectDataFromUI(rootView);
            }
        });
    }

    private void handleBackButton(final View rootView) {
        Button button= (Button) rootView.findViewById(R.id.back_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK));
                getActivity().dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK));
            }
        });
    }

    private void collectDataFromUI(View rootView) {
        EditText waistEditText = (EditText)rootView.findViewById(R.id.waist_editText);
        EditText hipsEditText = (EditText)rootView.findViewById(R.id.hips_editText);
        EditText wristEditText = (EditText)rootView.findViewById(R.id.wrist_editText);
        EditText thighEditText = (EditText)rootView.findViewById(R.id.thigh_editText);
        EditText forearmEditText = (EditText)rootView.findViewById(R.id.forearm_editText);
        EditText neckEditText = (EditText)rootView.findViewById(R.id.neck_editText);
        EditText calfEditText = (EditText)rootView.findViewById(R.id.calf_editText);
        RadioButton endoRadioButton = (RadioButton)rootView.findViewById(R.id.endo_radioButton);
        RadioButton mesoRadioButton = (RadioButton)rootView.findViewById(R.id.meso_radioButton);
        RadioButton ectoRadioButton = (RadioButton)rootView.findViewById(R.id.ecto_radioButton);

        UserProfile tempUserProfile = Globals.getInstance().getUserProfile();

        if(!isEmpty(waistEditText))
            tempUserProfile.setWaist(Float.parseFloat(waistEditText.getText().toString()));
        if(!isEmpty(hipsEditText))
            tempUserProfile.setHips(Float.parseFloat(hipsEditText.getText().toString()));
        if(!isEmpty(wristEditText))
            tempUserProfile.setWrist(Float.parseFloat(wristEditText.getText().toString()));
        if(!isEmpty(thighEditText))
            tempUserProfile.setThigh(Float.parseFloat(thighEditText.getText().toString()));
        if(!isEmpty(forearmEditText))
            tempUserProfile.setForearm(Float.parseFloat(forearmEditText.getText().toString()));
        if(!isEmpty(neckEditText))
            tempUserProfile.setNeck(Float.parseFloat(neckEditText.getText().toString()));
        if(!isEmpty(calfEditText))
            tempUserProfile.setCalf(Float.parseFloat(calfEditText.getText().toString()));
        if(endoRadioButton.isChecked())
            tempUserProfile.setbType(BodyType.ENDOMORPH);
        else if(mesoRadioButton.isChecked())
            tempUserProfile.setbType(BodyType.MESOMORPH);
        else if(ectoRadioButton.isChecked())
            tempUserProfile.setbType(BodyType.ECTOMORPH);
        mCallback.onNextClicked(1);
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
