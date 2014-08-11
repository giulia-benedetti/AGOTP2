package it.unitn.cryptolab.agotp.app;

import android.app.Application;
import android.content.Context;

/**This class provides a singleton object that is used by the other activities to get
 * data that, after being initialized, remain always the same.
 * @author Ambra, Francesco, Giulia
 * @version 1.0
 */

public class AGOTPApplication extends Application {

 private static AGOTPApplication singleton;
 private static Context appContext;


    @Override
    public final void onCreate(){
        super.onCreate();
        singleton = this;
        appContext = getApplicationContext();
    }

    public static AGOTPApplication getInstance(){
        return singleton;
    }

    public static Context getAppContext() {
        return appContext;
    }

    /**
     * This class creates a list of reference String that contains the values of the keys
     * that are used in the other activities.
     */
    public static class Keys {

        public static final String KEYS_REPOSITORY = "it.unitn.cryptolab.agotp.keyrepo";

        public static final String KEY_SEED_EXISTS = "KEY_SEED_EXISTS";

        public static final String KEY_PIN_EXISTS = "KEY_PIN_EXISTS";

        public static final String KEY_PIN_HASH = "KEY_PIN_HASH";

        public static final String KEY_SEED = "KEY_SEED";

        public static final String KEY_IV = "KEY_IV";

    }

}
