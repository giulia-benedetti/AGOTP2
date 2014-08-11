package it.unitn.cryptolab.agotp.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Base64;
import android.util.Log;
import java.security.SecureRandom;

/**
 * This class manages the seed. It contains static methods that save the encrypted seed and
 * the Initialitation Vector in the Shared Preferences. Moreover they get those values from
 * the Shared Preferences and use them to retrieve the original seed.
 * @author Ambra, Francesco, Giulia
 * @version 1.0
 */
public class SeedManager {

    //initialize the variables
    private final static String pinPad = "7e56311679f12b6fc91aa77a5eb";

    private static SharedPreferences prefs =
            AGOTPApplication.getAppContext().getSharedPreferences(
                AGOTPApplication.Keys.KEYS_REPOSITORY, Context.MODE_PRIVATE
            );

    /**
     * This method allows to save the encrypted seed in the Shared Preferences.
     * @param pin the pin inserted by the user.
     * @param seed the seed inserted by the user.
     */

    public static void saveSeed(String pin, String seed) {

        Editor ed = prefs.edit();

        //initialize the Random IV (lenght:16 byte)
        byte[] ivBytes = new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(ivBytes);
        String ivString = Base64.encodeToString(ivBytes, Base64.DEFAULT);

        //initialize the variables
        byte[] cipherData;
        String cipherString = "";

        //pin that is inserted, is padded with the variable pinPad. In this way the lenght of the pin is 32 bytes.
        pin += pinPad;

        try {
            //check
            Log.d("AGOTP", "Siamo nel SeedManager, controllo lunghezza pin: " + Integer.toString(pin.getBytes("UTF-8").length));

            cipherData = AES256Cipher.encrypt(ivBytes, pin.getBytes("UTF-8"), seed.getBytes("UTF-8"));
            cipherString = Base64.encodeToString(cipherData, Base64.DEFAULT);
        }

        catch (Exception e) {
                e.printStackTrace();
        }

        // save the IV and the encrypted seed in the Shared Preferences
        ed.putString(AGOTPApplication.Keys.KEY_IV, ivString);
        ed.putString(AGOTPApplication.Keys.KEY_SEED, cipherString);
        ed.putBoolean(AGOTPApplication.Keys.KEY_SEED_EXISTS, true);

        ed.commit();

    }

    /**
     * This method takes the IV and encrypted seed from the Shared Preferences and
     * returns the initial seed inserted by the user.
     * @param pin The pin inserted by the user.
     * @return The initial seed inserted by the user.
     */

    public static String loadSeed(String pin) {

        String ivString = prefs.getString(AGOTPApplication.Keys.KEY_IV, "");
        String cipherString = prefs.getString(AGOTPApplication.Keys.KEY_SEED, "");
        byte[] ivBytes = Base64.decode(ivString, Base64.DEFAULT);
        byte[] cipherBytes = Base64.decode(cipherString, Base64.DEFAULT);

        byte[] seedBytes = {};
        String seed = "";

        pin += pinPad;


        try {
            //check
            Log.d("AGOTP", "Siamo nella SeedManager, load seed: " + Integer.toString(pin.getBytes("UTF-8").length));

            seedBytes = AES256Cipher.decrypt(ivBytes, pin.getBytes("UTF-8"), cipherBytes);
            seed = new String(seedBytes, "UTF-8");
        }

        catch (Exception e) {
            e.printStackTrace();
        }

        return seed;

    }
}
