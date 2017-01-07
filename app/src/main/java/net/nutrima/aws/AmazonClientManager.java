package net.nutrima.aws;

import android.content.Context;
import android.util.Log;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

import net.nutrima.nutrimaprotogui.R;

/**
 * Created by melsisi on 4/27/2016.
 */
public class AmazonClientManager {

    private static final String LOG_TAG = "AmazonClientManager";

    private AmazonDynamoDBClient ddb = null;

    private Context context;

    public AmazonClientManager(Context context) {
        this.context = context;
    }

    public AmazonDynamoDBClient ddb() {
        validateCredentials();
        return ddb;
    }

    public void validateCredentials() {

        if (ddb == null) {
            initClients();
        }
    }

    private void initClients() {
        CognitoCachingCredentialsProvider credentials = new CognitoCachingCredentialsProvider(
                context,
                context.getString(R.string.identity_pool),
                Regions.US_EAST_1);

        ddb = new AmazonDynamoDBClient(credentials);
        ddb.setRegion(Region.getRegion(Regions.US_EAST_1));
    }

    public boolean wipeCredentialsOnAuthError(AmazonServiceException ex) {
        Log.e(LOG_TAG, "Error, wipeCredentialsOnAuthError called" + ex);
        if (
            // STS
            // http://docs.amazonwebservices.com/STS/latest/APIReference/CommonErrors.html
                ex.getErrorCode().equals("IncompleteSignature")
                        || ex.getErrorCode().equals("InternalFailure")
                        || ex.getErrorCode().equals("InvalidClientTokenId")
                        || ex.getErrorCode().equals("OptInRequired")
                        || ex.getErrorCode().equals("RequestExpired")
                        || ex.getErrorCode().equals("ServiceUnavailable")

                        // DynamoDB
                        // http://docs.amazonwebservices.com/amazondynamodb/latest/developerguide/ErrorHandling.html#APIErrorTypes
                        || ex.getErrorCode().equals("AccessDeniedException")
                        || ex.getErrorCode().equals("IncompleteSignatureException")
                        || ex.getErrorCode().equals(
                        "MissingAuthenticationTokenException")
                        || ex.getErrorCode().equals("ValidationException")
                        || ex.getErrorCode().equals("InternalFailure")
                        || ex.getErrorCode().equals("InternalServerError")) {

            return true;
        }

        return false;
    }

    public Context getContext() {
        return context;
    }
}
