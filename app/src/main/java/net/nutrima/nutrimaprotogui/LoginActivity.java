package net.nutrima.nutrimaprotogui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amazon.identity.auth.device.authorization.api.AmazonAuthorizationManager;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import net.nutrima.aws.*;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class LoginActivity extends Activity {
    private static final String TAG = "LoginActivity";

    private static final String[] APP_SCOPES = {
            "profile"
    };

    private Button btnLoginFacebook;
    private CallbackManager callbackManager;

    private AmazonAuthorizationManager mAuthManager;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    EditText email ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //--- FACEBOOK ---
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_login);
        CognitoSyncClientManager.init(this);

        // Populate USDA data from xls ///////////////////////////////////
        List<NutritionUSDAEntry> USDAList = populateList();
        Globals.getInstance().setUSDATable(USDAList);
        //////////////////////////////////////////////////////////////////

        // Start location service ////////////////////////////////////////
        //Intent intent = new Intent(this, LocationService.class);
        //startService(intent);
        //////////////////////////////////////////////////////////////////

        // Instantiate AWS client manager ////////////////////////////////
        Globals.getInstance().setClientManager(new AmazonClientManager(this));
        String restaurantName = "Red Robin";
        List<RestaurantMenuItem> tempRawMenuItem;
        try {
            tempRawMenuItem = new DynamoDBManagerTask().
                    execute(restaurantName).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        //////////////////////////////////////////////////////////////////

        //If access token is already here, set fb session and proceed to application
        final AccessToken fbAccessToken = AccessToken.getCurrentAccessToken();
        if (fbAccessToken != null) {
            Log.i("Main Activity", "======= FB Button: Inside tokenn======= ");
            //--x--setFacebookSession(fbAccessToken);
            new CognitoInternetAccess(fbAccessToken).execute();
            //btnLoginFacebook.setVisibility(View.GONE);
            Intent activityChangeIntent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(activityChangeIntent);
            finish();
        }


        btnLoginFacebook = (Button) findViewById(R.id.fb_button);
        btnLoginFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // start Facebook Login
                Log.i("Main Activity", "======= FB Button: Inside listen======= ");
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile", "user_friends", "user_birthday", "user_about_me", "email"));
                LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.i("Main Activity", "======= FB Button: Inside onSuccess======= ");
                        CognitoSyncClientManager.clear(); //Clear an unauthorized access
                        btnLoginFacebook.setVisibility(View.INVISIBLE);
                        new GetFbName(loginResult).execute();
                        //setFacebookSession(loginResult.getAccessToken());
                        Intent activityChangeIntent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(activityChangeIntent);
                        finish();
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(LoginActivity.this, "Facebook login cancelled",
                                Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Toast.makeText(LoginActivity.this, "Error in Facebook login " +
                                error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        btnLoginFacebook.setEnabled(getString(R.string.facebook_app_id) != "facebook_app_id");

        //--- GOOGLE + ---
        //TODO: Handle Google + login


        final Button button = (Button) findViewById(R.id.sneak_peak_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new CognitoInternetAccess(null).execute();
                Intent activityChangeIntent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(activityChangeIntent);
            }
        });
        email = (EditText) findViewById(R.id.editText);
        email.addTextChangedListener(new TextWatcher() {
            int textB4Change;
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textB4Change = email.getText().length();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (email.getText().length() > textB4Change) {
                    Intent activityChangeIntent = new Intent(LoginActivity.this, LoginDetailsActivity.class);
                    activityChangeIntent.putExtra("content", email.getText().toString());
                    startActivity(activityChangeIntent);
                }
            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private ArrayList<NutritionUSDAEntry> populateList() {
        ArrayList<NutritionUSDAEntry> list = new ArrayList<>();

        InputStream file = null;
        try {
            file = getResources().openRawResource(getResources().getIdentifier("nutrition",
                    "raw", getPackageName()));

            BufferedReader br = new BufferedReader(new InputStreamReader(file));
            //Create Workbook instance holding reference to .xlsx file
            HSSFWorkbook workbook = new HSSFWorkbook(file);

            //Get first/desired sheet from the workbook
            HSSFSheet sheet = workbook.getSheetAt(0);

            //Iterate through each rows one by one
            Iterator<Row> rowIterator = sheet.iterator();

            // Skip first two rows
            rowIterator.next();
            rowIterator.next();

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                //For each row, iterate through all the columns
                Iterator<Cell> cellIterator = row.cellIterator();

                Cell cell;

                // i is to index the sheet columns, format dependent
                int i = 0;
                NutritionUSDAEntry newEntry = new NutritionUSDAEntry();
                while (cellIterator.hasNext()) {
                    cell = cellIterator.next();
                    if (i == 0)
                        newEntry.setFood(cell.getStringCellValue().toLowerCase());
                    else if (i == 1)
                        newEntry.setServing(cell.getStringCellValue());
                    else if (i == 2)
                        newEntry.setWeight(cell.getNumericCellValue());
                    else if (i == 3)
                        newEntry.setCalories(cell.getNumericCellValue());
                    else if (i == 4)
                        newEntry.setTotalFat(cell.getNumericCellValue());
                    else if (i == 5)
                        newEntry.setCarbohydrates(cell.getNumericCellValue());
                    else if (i == 6)
                        newEntry.setProtein(cell.getNumericCellValue());
                    i++;
                }
                list.add(newEntry);
            }
            file.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    private void setFacebookSession(AccessToken accessToken) {
        Log.i("Main Activity", "======= FB Button: Inside Token, ======= ");
        Log.i("Main Activity", accessToken.getToken());
        CognitoSyncClientManager.addLogins("graph.facebook.com",
                accessToken.getToken());
        //btnLoginFacebook.setVisibility(View.GONE);

    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://www.nutrima.net"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onResume() {
        super.onResume();
        EditText email = (EditText) findViewById(R.id.editText);
        email.setText("");
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();

    }

    private class CognitoInternetAccess extends AsyncTask<Void, Void, Bundle> {
        private AccessToken accessToken;
        String ID;

        public CognitoInternetAccess (AccessToken accessToken) {
            this.accessToken = accessToken;
        }

        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Bundle doInBackground(Void... params) {
            if (accessToken == null) {
                ID = CognitoSyncClientManager.getUnAuthenticatedID();
                //CognitoSyncClientManager.refresh();
            } else {
                GraphRequest request = GraphRequest.newMeRequest(
                        accessToken,
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                // Application code
                                //Log.v("LoginActivity", response.toString());
                                Bundle bFacebookData = getFacebookData(object);
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, name, first_name, last_name, email, gender, birthday, location"); // Parámetros que pedimos a facebook
                request.setParameters(parameters);
                GraphResponse graphResponse = request.executeAndWait();
                setFacebookSession(accessToken);
                return getFacebookData(graphResponse.getJSONObject());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bundle response) {
            //super.onPostExecute(response);
            if (accessToken == null) {
                Toast.makeText(LoginActivity.this, "Please register to enjoy Nutrima full capability!", Toast.LENGTH_LONG).show();
            } else {
                if (response != null) {
                    Toast.makeText(LoginActivity.this, "Hello " + response.getString("name"), Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private class GetFbName extends AsyncTask<Void, Void, Bundle> {
        private final LoginResult loginResult;
        private ProgressDialog dialog;

        public GetFbName(LoginResult loginResult) {
            this.loginResult = loginResult;
        }

        @Override
        protected void onPreExecute() {
            //dialog = ProgressDialog.show(MainActivity.this, "Wait", "Getting user name");
        }

        @Override
        protected Bundle doInBackground(Void... params) {
            GraphRequest request = GraphRequest.newMeRequest(
                    loginResult.getAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(
                                JSONObject object,
                                GraphResponse response) {
                            // Application code
                            Log.v("LoginActivity", response.toString());
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id, name, first_name, last_name, email, gender, birthday, location"); // Parámetros que pedimos a facebook
            request.setParameters(parameters);
            GraphResponse graphResponse = request.executeAndWait();
            setFacebookSession(loginResult.getAccessToken());
            //TODO: Make a singleton class/bundle to hold account information from facebook or any other logins
            return getFacebookData(graphResponse.getJSONObject());
        }

        @Override
        protected void onPostExecute(Bundle response) {
            //dialog.dismiss();
            if (response != null) {
                Toast.makeText(LoginActivity.this, "Hello " + response.getString("name"), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(LoginActivity.this, "Unable to get user name from Facebook",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    private Bundle getFacebookData(JSONObject object) {
        try {
            Bundle bundle = new Bundle();
            String id = object.getString("id");

            try {
                URL profile_pic = new URL("https://graph.facebook.com/" + id + "/picture?width=200&height=150");
                Log.i("profile_pic", profile_pic + "");
                bundle.putString("profile_pic", profile_pic.toString());

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }

            bundle.putString("idFacebook", id);
            if (object.has("name"))
                bundle.putString("name", object.getString("name"));
            if (object.has("first_name"))
                bundle.putString("first_name", object.getString("first_name"));
            if (object.has("last_name"))
                bundle.putString("last_name", object.getString("last_name"));
            if (object.has("email"))
                bundle.putString("email", object.getString("email"));
            if (object.has("gender"))
                bundle.putString("gender", object.getString("gender"));
            if (object.has("birthday"))
                bundle.putString("birthday", object.getString("birthday"));
            if (object.has("location"))
                bundle.putString("location", object.getJSONObject("location").getString("name"));
            return bundle;

        } catch (JSONException j) {
            j.printStackTrace();
            return null;
        }
    }
}
