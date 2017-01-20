package net.nutrima.nutrimaprotogui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import net.nutrima.engine.NutrimaMetrics;
import net.nutrima.engine.UserProfile;
import net.nutrima.nutrimaprotogui.fragments.ProfileAllergiesFragment;
import net.nutrima.nutrimaprotogui.fragments.ProfileHabitsFragment;
import net.nutrima.nutrimaprotogui.fragments.ProfileIllnessesFragment;
import net.nutrima.nutrimaprotogui.fragments.ProfileMeasurementsDetailsFragment;
import net.nutrima.nutrimaprotogui.fragments.ProfileMeasurementsFragment;
import net.nutrima.nutrimaprotogui.fragments.ProfileThankyouFragment;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class ProfileCreatorActivity extends FragmentActivity
        implements ProfileMeasurementsFragment.OnNextClickedListener,
        ProfileMeasurementsDetailsFragment.OnNextClickedListener,
        ProfileHabitsFragment.OnNextClickedListener,
        ProfileIllnessesFragment.OnNextClickedListener,
        ProfileAllergiesFragment.OnNextClickedListener,
        ProfileThankyouFragment.OnNextClickedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_creator);

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create a new Fragment to be placed in the activity layout
            ProfileMeasurementsFragment firstFragment = new ProfileMeasurementsFragment();

            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            //firstFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_right,
                            R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                    .add(R.id.fragment_container, firstFragment).commit();
        }
    }

    public void onNextClicked(int currentPosition){
        switchFragments(currentPosition);
    }

    private void switchFragments(int currentPosition) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                R.anim.enter_from_left, R.anim.exit_to_right);
        Fragment newFragment = null;

        if(currentPosition == 0){
            newFragment = new ProfileMeasurementsDetailsFragment();
            //Bundle args = new Bundle();
            //args.putInt(ArticleFragment.ARG_POSITION, position);
            //newFragment.setArguments(args);
        }
        else if(currentPosition == 1) {
            newFragment = new ProfileHabitsFragment();
        }
        else if(currentPosition == 2) {
            newFragment = new ProfileIllnessesFragment();

        }
        else if(currentPosition == 3) {
            newFragment = new ProfileAllergiesFragment();

        }
        else if(currentPosition == 4) {
            newFragment = new ProfileThankyouFragment();

        }
        else if(currentPosition == 5) {
            NutrimaMetrics nutrimaMetrics = new NutrimaMetrics();
            nutrimaMetrics.calcNutrima(Globals.getInstance().getUserProfile());
            Globals.getInstance().setNutrimaMetrics(nutrimaMetrics);
            saveDataToStorage(Globals.getInstance().getUserProfile());
            new Handler().postDelayed(new Runnable(){
                @Override
                public void run() {
                    Intent activityChangeIntent = new Intent(ProfileCreatorActivity.this,
                            SimpleMainActivity.class);
                    activityChangeIntent.putExtra("FROM", "PROFILE");
                    startActivity(activityChangeIntent);
                }
            }, 3000);
        }
        if(currentPosition != 5) {
            transaction.replace(R.id.fragment_container, newFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    // TODO: Save to cloud
    private void saveDataToStorage(UserProfile dataToSave) {
        FileOutputStream fos = null;
        try {
            fos = openFileOutput(getString(R.string.profile_data_file_name), Context.MODE_PRIVATE);

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

}
