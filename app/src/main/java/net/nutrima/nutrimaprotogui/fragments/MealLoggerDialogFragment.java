package net.nutrima.nutrimaprotogui.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import java.util.List;

public class MealLoggerDialogFragment extends DialogFragment {

    List<String> potentialsList;
    private String selected;
    private String spokenItem;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("What kind of \"" + spokenItem + "\"?")
                .setItems(potentialsList.toArray(new String[0]), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        selected = potentialsList.get(which);
                        LogFragment.refreshList(selected);
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    public void setPotentialsList(List<String> potentialsList) {
        this.potentialsList = potentialsList;
    }

    public void setSpokenItem(String spokenItem) {
        this.spokenItem = spokenItem;
    }
}