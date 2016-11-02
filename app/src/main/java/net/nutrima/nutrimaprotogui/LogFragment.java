package net.nutrima.nutrimaprotogui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class LogFragment extends Fragment {

    private static final int VOICE_RECOGNITION_REQUEST_CODE = 1001;

    private static LogMealItemAdapter customAdapter;

    private static ArrayList<ArrayList<String>> mealLogArray;

    private static ArrayList<ArrayList<String>> mealLogDisplayArray;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.activity_meal_logger, container, false);

        // Creating menu listview ////////////////////////////////////////
        final ListView logMealsListView = (ListView) rootView.findViewById(R.id.logged_listview);

        mealLogArray = new ArrayList<>();
        ArrayList<String> row = new ArrayList<>();
        row.add("Amount");
        row.add("Measure");
        row.add("Item");
        row.add("Brand");
        mealLogArray.add(row);

        mealLogDisplayArray = new ArrayList<>();
        mealLogDisplayArray.addAll(mealLogArray);
        customAdapter = new LogMealItemAdapter(this.getContext(),
                R.layout.log_list_item,
                R.id.logged_listview,
                mealLogDisplayArray);

        logMealsListView.setAdapter(customAdapter);
        //////////////////////////////////////////////////////////////////
        return rootView;
    }

    private long textToInt(String input) {
        boolean isValidInput = true;
        long result = 0;
        long finalResult = 0;
        List<String> allowedStrings = Arrays.asList
                (
                        "zero", "one", "two", "three", "four", "five", "six", "seven",
                        "eight", "nine", "ten", "eleven", "twelve", "thirteen", "fourteen",
                        "fifteen", "sixteen", "seventeen", "eighteen", "nineteen", "twenty",
                        "thirty", "forty", "fifty", "sixty", "seventy", "eighty", "ninety",
                        "hundred", "thousand", "million", "billion", "trillion"
                );

        if (input != null && input.length() > 0) {
            input = input.replaceAll("-", " ");
            input = input.toLowerCase().replaceAll(" and", " ");
            String[] splittedParts = input.trim().split("\\s+");

            for (String str : splittedParts) {
                if (!allowedStrings.contains(str)) {
                    isValidInput = false;
                    System.out.println("Invalid word found : " + str);
                    break;
                }
            }
            if (isValidInput) {
                for (String str : splittedParts) {
                    if (str.equalsIgnoreCase("zero")) {
                        result += 0;
                    } else if (str.equalsIgnoreCase("one")) {
                        result += 1;
                    } else if (str.equalsIgnoreCase("two")) {
                        result += 2;
                    } else if (str.equalsIgnoreCase("three")) {
                        result += 3;
                    } else if (str.equalsIgnoreCase("four")) {
                        result += 4;
                    } else if (str.equalsIgnoreCase("five")) {
                        result += 5;
                    } else if (str.equalsIgnoreCase("six")) {
                        result += 6;
                    } else if (str.equalsIgnoreCase("seven")) {
                        result += 7;
                    } else if (str.equalsIgnoreCase("eight")) {
                        result += 8;
                    } else if (str.equalsIgnoreCase("nine")) {
                        result += 9;
                    } else if (str.equalsIgnoreCase("ten")) {
                        result += 10;
                    } else if (str.equalsIgnoreCase("eleven")) {
                        result += 11;
                    } else if (str.equalsIgnoreCase("twelve")) {
                        result += 12;
                    } else if (str.equalsIgnoreCase("thirteen")) {
                        result += 13;
                    } else if (str.equalsIgnoreCase("fourteen")) {
                        result += 14;
                    } else if (str.equalsIgnoreCase("fifteen")) {
                        result += 15;
                    } else if (str.equalsIgnoreCase("sixteen")) {
                        result += 16;
                    } else if (str.equalsIgnoreCase("seventeen")) {
                        result += 17;
                    } else if (str.equalsIgnoreCase("eighteen")) {
                        result += 18;
                    } else if (str.equalsIgnoreCase("nineteen")) {
                        result += 19;
                    } else if (str.equalsIgnoreCase("twenty")) {
                        result += 20;
                    } else if (str.equalsIgnoreCase("thirty")) {
                        result += 30;
                    } else if (str.equalsIgnoreCase("forty")) {
                        result += 40;
                    } else if (str.equalsIgnoreCase("fifty")) {
                        result += 50;
                    } else if (str.equalsIgnoreCase("sixty")) {
                        result += 60;
                    } else if (str.equalsIgnoreCase("seventy")) {
                        result += 70;
                    } else if (str.equalsIgnoreCase("eighty")) {
                        result += 80;
                    } else if (str.equalsIgnoreCase("ninety")) {
                        result += 90;
                    } else if (str.equalsIgnoreCase("hundred")) {
                        result *= 100;
                    } else if (str.equalsIgnoreCase("thousand")) {
                        result *= 1000;
                        finalResult += result;
                        result = 0;
                    } else if (str.equalsIgnoreCase("million")) {
                        result *= 1000000;
                        finalResult += result;
                        result = 0;
                    } else if (str.equalsIgnoreCase("billion")) {
                        result *= 1000000000;
                        finalResult += result;
                        result = 0;
                    } else if (str.equalsIgnoreCase("trillion")) {
                        result *= 1000000000000L;
                        finalResult += result;
                        result = 0;
                    }
                }

                finalResult += result;
                result = 0;
            }
        }
        return finalResult;
    }

    public void speak(Activity activity){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,activity.getPackageName());
        // Format: <amount> <measure> of <item>
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say something like: \"one cup of greek yogurt\"");
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);

        int maxNoOfMatches = 1;
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, maxNoOfMatches);
        startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode == RESULT_OK){
            ArrayList<String> textMatchList = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (!textMatchList.isEmpty()){
                String spokenText = textMatchList.get(0);
                String[] split = spokenText.split("\\s+");
                ArrayList<String> newRow;

                if (split.length > 3) {
                    // Create row from input text ////////////////////////////
                    newRow = createRowFromText(split);
                    mealLogArray.add(newRow);
                    //////////////////////////////////////////////////////////

                    List<String> potentialStrings = new ArrayList<>();
                    for(NutritionUSDAEntry e : Globals.getInstance().getUSDATable()) {
                        if(e.getFood().contains(newRow.get(newRow.size()-1)))
                            potentialStrings.add(e.getFood());
                    }

                    MealLoggerDialogFragment dialog = new MealLoggerDialogFragment();
                    dialog.setSpokenItem(newRow.get(newRow.size()-1));
                    dialog.setPotentialsList(potentialStrings);
                    dialog.show(getFragmentManager(), "Dialog");
                }
                else
                    showToastMessage("Captured sentence too short. Needs to be in the form" +
                            " \" <amount/number> <measure unit> of <item>\"");
            }
            else
                showToastMessage("Nothing was captured!");
        }
        else if (resultCode == RecognizerIntent.RESULT_AUDIO_ERROR){
            showToastMessage("Audio Error");

        }
        else if ((resultCode == RecognizerIntent.RESULT_CLIENT_ERROR)){
            showToastMessage("Client Error");

        }
        else if (resultCode == RecognizerIntent.RESULT_NETWORK_ERROR){
            showToastMessage("Network Error");
        }
        else if (resultCode == RecognizerIntent.RESULT_NO_MATCH){
            showToastMessage("No Match");
        }
        else if (resultCode == RecognizerIntent.RESULT_SERVER_ERROR){
            showToastMessage("Server Error");
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @TargetApi(Build.VERSION_CODES.M)
    void  showToastMessage(String message) {
        Toast.makeText(this.getContext(), message, Toast.LENGTH_LONG).show();
    }

    private ArrayList<String> createRowFromText (String[] split) {

        int indexOfOf = 0;
        for(int i = 0; i < split.length; i++){
            if(split[i].equals("of")){
                indexOfOf = i;
                break;
            }
        }

        // Get amount ////////////////////////////////////////////////////
        String amount = "";
        if(tryParseInt(split[0]))
            amount = Integer.toString(Integer.parseInt(split[0]));
        else {
            String temp = "";
            for(int i = 0; i < indexOfOf - 1; i++){
                temp += split[i] + " ";
            }
            amount = Long.toString(textToInt(temp));
        }
        //////////////////////////////////////////////////////////////////

        // Get measure ///////////////////////////////////////////////////
        String measure = split[indexOfOf - 1];
        //////////////////////////////////////////////////////////////////

        // Get item //////////////////////////////////////////////////////
        String item = "";
        for(int i = indexOfOf + 1; i < split.length; i++){
            item += split[i] + " ";
        }
        //////////////////////////////////////////////////////////////////
        ArrayList<String> row = new ArrayList<>();

        row.add(amount);
        row.add(measure);
        // Trimming extra space at end
        row.add(item.substring(0, item.length()-1));
        return row;
    }

    boolean tryParseInt(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static void refreshList(String correctedItem) {
        mealLogArray.get(mealLogArray.size()-1).set(2, correctedItem);
        mealLogDisplayArray.clear();
        mealLogDisplayArray.addAll(mealLogArray);
        customAdapter.notifyDataSetChanged();
    }
}
