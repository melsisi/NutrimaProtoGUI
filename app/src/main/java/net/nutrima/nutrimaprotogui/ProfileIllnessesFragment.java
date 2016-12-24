package net.nutrima.nutrimaprotogui;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import net.nutrima.engine.UserProfile;


public class ProfileIllnessesFragment extends Fragment {

    public ProfileIllnessesFragment() { }

    public static ProfileIllnessesFragment newInstance() {
        ProfileIllnessesFragment fragment = new ProfileIllnessesFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_profile_illnesses, container, false);
        handleNextButton(rootView);
        handleBackButton(rootView);
        return rootView;
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
        UserProfile tempUserProfile = Globals.getInstance().getUserProfile();

        if(((CheckBox) rootView.findViewById(R.id.heart_disease_checkBox)).isChecked())
            tempUserProfile.setHeartDisease(true);
        if(((CheckBox) rootView.findViewById(R.id.diabetes_checkBox)).isChecked())
            tempUserProfile.setDiabetic(true);
        if(((CheckBox) rootView.findViewById(R.id.kidney_disease_checkBox)).isChecked())
            tempUserProfile.setKidneyDisease(true);
        if(((CheckBox) rootView.findViewById(R.id.liver_disease_checkBox)).isChecked())
            tempUserProfile.setLiverDisease(true);
        if(((CheckBox) rootView.findViewById(R.id.cancer_disease_checkBox)).isChecked())
            tempUserProfile.setCancerDisease(true);
        if(((CheckBox) rootView.findViewById(R.id.hbp_disease_checkBox)).isChecked())
            tempUserProfile.setHighBloodPressure(true);
        if(((CheckBox) rootView.findViewById(R.id.celiac_disease_checkBox)).isChecked())
            tempUserProfile.setCeliacDisease(true);
        if(((CheckBox) rootView.findViewById(R.id.other_illness_checkBox)).isChecked())
            if(!isEmpty(((EditText) rootView.findViewById(R.id.other_illness_editText))))
                tempUserProfile.setOtherDiseases(
                        ((EditText) rootView.findViewById(R.id.other_illness_editText)).getText().toString());
        mCallback.onNextClicked(3);
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
