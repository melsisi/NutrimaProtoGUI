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
import android.widget.CompoundButton;
import android.widget.EditText;

import net.nutrima.engine.UserProfile;


public class ProfileAllergiesFragment extends Fragment {

    public ProfileAllergiesFragment() { }

    public static ProfileAllergiesFragment newInstance() {
        ProfileAllergiesFragment fragment = new ProfileAllergiesFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_profile_allergies, container, false);
        handleNextButton(rootView);
        handleBackButton(rootView);
        handleOtherTextBox(rootView);
        return rootView;
    }

    private void handleOtherTextBox(View rootView) {
        final EditText otherAllergiesEditText = (EditText) rootView.findViewById(R.id.other_allergy_editText);
        CheckBox otherAllergiesCheckBox = (CheckBox) rootView.findViewById(R.id.other_allergy_checkBox);
        otherAllergiesCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                otherAllergiesEditText.setEnabled(isChecked);
            }
        });
    }

    private void handleNextButton(final View rootView) {
        Button button= (Button) rootView.findViewById(R.id.done_button);
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

        if(((CheckBox) rootView.findViewById(R.id.celiac_gluten_allergy_checkBox)).isChecked())
            tempUserProfile.setGlutenIntorlerance(true);
        if(((CheckBox) rootView.findViewById(R.id.dairy_allergy_checkBox)).isChecked())
            tempUserProfile.setDiaryAllergy(true);
        if(((CheckBox) rootView.findViewById(R.id.egg_allergy_checkBox)).isChecked())
            tempUserProfile.setEggAllergy(true);
        if(((CheckBox) rootView.findViewById(R.id.fish_allergy_checkBox)).isChecked())
            tempUserProfile.setFishAllergy(true);
        if(((CheckBox) rootView.findViewById(R.id.shellfish_allergy_checkBox)).isChecked())
            tempUserProfile.setShellfishAllergy(true);
        if(((CheckBox) rootView.findViewById(R.id.soybeans_allergy_checkBox)).isChecked())
            tempUserProfile.setSoyAllergy(true);
        if(((CheckBox) rootView.findViewById(R.id.nuts_allergy_checkBox)).isChecked())
            tempUserProfile.setNutsAllergy(true);
        if(((CheckBox) rootView.findViewById(R.id.peanuts_allergy_checkBox)).isChecked())
            tempUserProfile.setPeanutsAllergy(true);
        if(((CheckBox) rootView.findViewById(R.id.other_allergy_checkBox)).isChecked())
            if(!isEmpty(((EditText) rootView.findViewById(R.id.other_allergy_editText))))
                tempUserProfile.setOtherAllergies(
                        ((EditText) rootView.findViewById(R.id.other_allergy_editText)).getText().toString());

        mCallback.onNextClicked(4);
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
