package net.nutrima.nutrimaprotogui;

/**
 * Created by melsisi on 9/9/2016.
 */
import android.os.AsyncTask;

import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;

/**
 * Created by melsisi on 4/27/2016.
 */
class YelpAsyncTask extends
        AsyncTask<OAuthRequest, Void, String> {

    protected String doInBackground(
            OAuthRequest... requests) {

        Response toReturn = requests[0].send();

        return toReturn.getBody();
    }

    /*protected void onPostExecute(DynamoDBManagerTaskResult result) {

        if (result.getTableStatus().equalsIgnoreCase("ACTIVE")
                && result.getTaskType() == DynamoDBManagerType.INSERT_ITEM) {
            Toast.makeText(UserPreferenceDemoActivity.this,
                    "Users inserted successfully!", Toast.LENGTH_SHORT).show();
        }
    }*/
}