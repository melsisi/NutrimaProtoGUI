package net.nutrima.nutrimaprotogui;

import android.content.Context;
import android.util.Log;

import com.amazonaws.auth.AWSAbstractCognitoIdentityProvider;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.auth.CognitoCredentialsProvider;
import com.amazonaws.mobileconnectors.cognito.CognitoSyncManager;
import com.amazonaws.regions.Regions;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ayehia on 9/26/2016.
 */

public class CognitoSyncClientManager {
    private static final String TAG = "CognitoClientManager";

    /**
     * Enter here the Identity Pool associated with your app and the AWS
     * region where it belongs. Get this information from the AWS console.
     */

    private static final String IDENTITY_POOL_ID = "us-east-1:6ee03d7d-72c9-40e5-bf69-282319705efe";
    private static final Regions REGION = Regions.US_EAST_1;

    private static CognitoSyncManager syncClient;
    protected static CognitoCachingCredentialsProvider credentialsProvider = null;
    protected static AWSAbstractCognitoIdentityProvider developerIdentityProvider;

    /**
     * Set this flag to true for using developer authenticated identities
     * Make sure you configured it in DeveloperAuthenticationProvider.java.
     */
    private static boolean useDeveloperAuthenticatedIdentities = false;


    /**
     * Initializes the Cognito Identity and Sync clients. This must be called before getInstance().
     *
     * @param context a context of the app
     */
    public static void init(Context context) {

        if (syncClient != null) return;

        if (useDeveloperAuthenticatedIdentities) {
            developerIdentityProvider = new DeveloperAuthenticationProvider(
                    null, IDENTITY_POOL_ID, context, REGION);
            credentialsProvider = new CognitoCachingCredentialsProvider(context, developerIdentityProvider,
                    REGION);
            Log.i(TAG, "Using developer authenticated identities");
        } else {
            credentialsProvider = new CognitoCachingCredentialsProvider(context, IDENTITY_POOL_ID,
                    REGION);
            Log.i(TAG, "Developer authenticated identities is not configured");
        }

        syncClient = new CognitoSyncManager(context, REGION, credentialsProvider);
    }

    /**
     * Sets the login so that you can use authorized identity. This requires a
     * network request, so you should call it in a background thread.
     *
     * @param providerName the name of the external identity provider
     * @param token openId token
     */
    public static void addLogins(String providerName, String token) {
        if (syncClient == null) {
            throw new IllegalStateException("CognitoSyncClientManager not initialized yet");
        }

        Map<String, String> logins = credentialsProvider.getLogins();
        if (logins == null) {
            logins = new HashMap<String, String>();
        }
        logins.put(providerName, token);
        credentialsProvider.setLogins(logins);
        credentialsProvider.refresh(); //Causes APP to crash but it updates AWS analytics with unAuthorised identity!!!
    }

    public static String getUnAuthenticatedID () {
        return credentialsProvider.getIdentityId();
    }
    public static void refresh () {
        credentialsProvider.refresh();
    }
    /**
     * Gets the singleton instance of the CognitoClient. init() must be called
     * prior to this.
     *
     * @return an instance of CognitoClient
     */
    public static CognitoSyncManager getInstance() {
        if (syncClient == null) {
            throw new IllegalStateException("CognitoSyncClientManager not initialized yet");
        }
        return syncClient;
    }

    /**
     * Returns a credentials provider object
     *
     * @return
     */
    public CognitoCredentialsProvider getCredentialsProvider() {
        return credentialsProvider;
    }

    public static void clear () {
        credentialsProvider.clearCredentials();
        credentialsProvider.clear();
    }
}
