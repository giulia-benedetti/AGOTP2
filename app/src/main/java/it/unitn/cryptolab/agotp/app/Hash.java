package it.unitn.cryptolab.agotp.app;

import android.util.Base64;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * This class computes the hash of the message using SHA-256.
 * @author Erickson
 * @version 1.0
 */

public class Hash {

    public static String generate(String input) {
        byte[] bytes = getHash(input);
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    /**
     * This method computes the digest using Sha-256.
     * @param password the message to be hashed.
     * @return An array of bytes.
     */

    private static byte[] getHash(String password) {


        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        }

        catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        }
        digest.reset();
        byte[] pwBytes = {};

        try {
            pwBytes = password.getBytes("UTF-8");
        }

        catch (Exception e) {
            e.printStackTrace();
        }

        return digest.digest(pwBytes);
    }

}
