package net.nutrima.nutrimaprotogui.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import net.nutrima.engine.ActivityLevel;
import net.nutrima.engine.UserProfile;
import net.nutrima.nutrimaprotogui.Globals;
import net.nutrima.nutrimaprotogui.R;


public class ProfileHabitsFragment extends Fragment {

    public ProfileHabitsFragment() { }

    public static ProfileHabitsFragment newInstance() {
        ProfileHabitsFragment fragment = new ProfileHabitsFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_profile_habits, container, false);
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
        Spinner numMealsSpinner = (Spinner) rootView.findViewById(R.id.num_meals_spinner);
        Spinner activitySpinner = (Spinner) rootView.findViewById(R.id.activity_level_spinner);
        Spinner typicalDaySpinner = (Spinner) rootView.findViewById(R.id.typical_day_spinner);

        if(numMealsSpinner.getSelectedItem().toString().equals("Select one")) {
            Snackbar.make(rootView, "Please select number of meals", Snackbar.LENGTH_LONG).show();
            return;
        }
        if(activitySpinner.getSelectedItem().toString().equals("Select one")) {
            Snackbar.make(rootView, "Please select exercise frequency", Snackbar.LENGTH_LONG).show();
            return;
        }
        if(typicalDaySpinner.getSelectedItem().toString().equals("Select one")) {
            Snackbar.make(rootView, "Please select typical day", Snackbar.LENGTH_LONG).show();
            return;
        }

        UserProfile tempUserProfile = Globals.getInstance().getUserProfile();
        switch (activitySpinner.getSelectedItem().toString()) {
            case "Sedentary":
                tempUserProfile.setActLevel(ActivityLevel.SEDENTARY);
                break;
            case "Light activity":
                tempUserProfile.setActLevel(ActivityLevel.LIGHT_ACTIVE);
                break;
            case "Moderate activity":
                tempUserProfile.setActLevel(ActivityLevel.MODERATE_ACTIVE);
                break;
            case "High activity":
                tempUserProfile.setActLevel(ActivityLevel.HIGH_ACTIVE);
                break;
            case "Very high activity":
                tempUserProfile.setActLevel(ActivityLevel.VERY_HIGH_ACTIVE);
                break;
        }
        tempUserProfile.setNumOfMeals(Byte.valueOf(numMealsSpinner.getSelectedItem().toString()));

        //TODO: Handle Typical day later.

        mCallback.onNextClicked(2);
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
