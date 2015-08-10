package com.jonkussmann.ribbit;

import android.app.Application;

import com.jonkussmann.ribbit.utils.ParseConstants;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseUser;

/**
 * Created by Jon Kussmann on 3/26/2015.
 */
public class RibbitApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "mrL9pQRPiYmBdEl4ei0sv0EXDPfX8nhEh8eEB4Zq", "k9fPuPUnN7uDmaedt2kgiLrHZurX1AONIHfCD4ze");

        ParseInstallation.getCurrentInstallation().saveInBackground();
    }

    public static void updateParseInstallation(ParseUser user) {
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put(ParseConstants.KEY_USER_ID, user.getObjectId());
        installation.saveInBackground();
    }
}
