package it.unitn.cryptolab.agotp.app;

/**
 * Taken from https://github.com/dynalogin/dynalogin-android/blob/master/src/org/dynalogin/android/HOTP.java
 */

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * This class contains static methods that are used to calculate the One-Time Password (OTP) using HMAC-SHA-1.
 *
 * @author Daniel Pocock
 * @version 1.0
 */

public class HOTPAlgorithm {

    /**
     * This method  manages the exceptions thrown by the generateOTP method.
     *
     * @param seed the secret seed.
     * @param count the counter, time, that changes on a per use basis.
     * @param digits the number of digits of OTP.
     * @return A numeric String in base 10.
     */

    public static String gen(String seed, int count, int digits)
    {
        try
        {
            return generateOTP(seed.getBytes(), count, digits);
        } catch (InvalidKeyException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }

    /**
     * This method provides the HMAC-SHA-1 algorithm.
     * HMAC computes a Hashed Message Authentication Code
     * and in this case SHA-1 is the hash algorithm used.
     *
     * @param keyBytes the bytes to use for the HMAC-SHA-1 key.
     * @param text the message or text to be authenticated.
     * @throws NoSuchAlgorithmException if no provider makes either HmacSHA1 or HMAC-SHA-1
     *        digest algorithms available.
     * @throws InvalidKeyException
     *        The secret provided was not a valid HMAC-SHA-1 key.
     */
    public static byte[] hmac_sha1(byte[] keyBytes, byte[] text)
            throws NoSuchAlgorithmException, InvalidKeyException
    {
        // try {
        Mac hmacSha1;
        try
        {
            hmacSha1 = Mac.getInstance("HmacSHA1");
        } catch (NoSuchAlgorithmException nsae)
        {
            hmacSha1 = Mac.getInstance("HMAC-SHA-1");
        }
        SecretKeySpec macKey = new SecretKeySpec(keyBytes, "RAW");
        hmacSha1.init(macKey);
        return hmacSha1.doFinal(text);
    }

    /**
     * This method generates an OTP values for the given set of parameters.
     *
     * @param secret the shared secret.
     * @param movingFactor the counter, time, that changes on a per use basis.
     * @param codeDigits the number of digits of OTP.
     * @return A numeric String in base 10.
     * @throws NoSuchAlgorithmException if no provider makes either HmacSHA1 or HMAC-SHA-1
     *        digest algorithms available.
     * @throws InvalidKeyException
     *         The secret provided was not a valid HMAC-SHA-1 key.
     */
    static private String generateOTP(byte[] secret, long movingFactor,
                                      int codeDigits) throws NoSuchAlgorithmException,
            InvalidKeyException
    {
        // put movingFactor value into text byte array
        String result = null;
        byte[] text = new byte[8];
        for (int i = text.length - 1; i >= 0; i--)
        {
            text[i] = (byte) (movingFactor & 0xff);
            movingFactor >>= 8;
        }

        // compute hmac hash
        byte[] hash = hmac_sha1(secret, text);

        // put selected bytes into result int
        int offset = hash[hash.length - 1] & 0xf;
        int binary = ((hash[offset] & 0x7f) << 24)
                | ((hash[offset + 1] & 0xff) << 16)
                | ((hash[offset + 2] & 0xff) << 8) | (hash[offset + 3] & 0xff);

        int otp = (int) (binary % Math.pow(10, codeDigits));
//        int otp = binary % DIGITS_POWER[codeDigits];
        result = Integer.toString(otp);
        while (result.length() < codeDigits)
        {
            result = "0" + result;
        }
        return result;
    }



}