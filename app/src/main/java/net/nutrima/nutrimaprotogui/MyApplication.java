package net.nutrima.nutrimaprotogui;

import android.app.Application;
import android.content.Context;

import net.nutrima.nutrimaprotogui.R;

import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

/**
 * Created by melsisi on 3/2/2017.
 */

@ReportsCrashes(mailTo = "moh.amr88@gmail.com",
        mode = ReportingInteractionMode.DIALOG,
        resDialogText = R.string.crash_toast_text)
public class MyApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        // The following line triggers the initialization of ACRA
        ACRA.init(this);
    }
}
