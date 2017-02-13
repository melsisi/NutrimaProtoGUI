package net.nutrima.nutrimaprotogui;

/**
 * Created by melsisi on 9/9/2016.
 */
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by melsisi on 4/27/2016.
 */
public class UrlAsyncTask extends
        AsyncTask<String, Void, Drawable> {

    protected Drawable doInBackground(
            String... urls) {

        return drawableFromUrl(urls[0]);
    }

    /*protected void onPostExecute(DynamoDBManagerTaskResult result) {

        if (result.getTableStatus().equalsIgnoreCase("ACTIVE")
                && result.getTaskType() == DynamoDBManagerType.INSERT_ITEM) {
            Toast.makeText(UserPreferenceDemoActivity.this,
                    "Users inserted successfully!", Toast.LENGTH_SHORT).show();
        }
    }*/

    public static Drawable drawableFromUrl(String url) {
        Bitmap x = null;

        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();

            connection.connect();
            InputStream input = connection.getInputStream();

            x = BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new BitmapDrawable(x);
    }
}