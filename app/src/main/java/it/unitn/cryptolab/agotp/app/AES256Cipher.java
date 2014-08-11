package it.unitn.cryptolab.agotp.app;

/**
 * Taken from https://gist.github.com/dealforest/1949873
 */



import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.InvalidAlgorithmParameterException;
import java.security.spec.AlgorithmParameterSpec;

/**
 * This class contains static methods that are used to encrypt and decrypt messages using AES-256.
 * @author Toshihiro Morimoto
 * @version 1.0
 */

public class AES256Cipher {

    /**
     * This method encrypts the plaintext using AES-256 in CBC mode.
     * @param ivBytes the Initialitation Vector, its lenght is 16 bytes.
     * @param keyBytes the key, its lenght is 32 bytes.
     * @param textBytes the plaintext.
     * @return The ciphertext.
     * @throws java.io.UnsupportedEncodingException The Character Encoding is not supported.
     * @throws NoSuchAlgorithmException This exception is thrown when a particular cryptographic
     *                                  algorithm is requested but is not available in the enviroment.
     * @throws NoSuchPaddingException This exception is thrown when a particular padding mechanism
     *                                is requested but is not available in the environment.
     * @throws InvalidKeyException This is the exception for invalid keys(invalid encoding,
     *                             wrong lenght, uninitialized,etc).
     * @throws InvalidAlgorithmParameterException This is the exception for invalid or
     *                                            inappropriate parameters.
     * @throws IllegalBlockSizeException This expection is thrown when the lenght of data
     *                                   provided to a block cipher is incorrect.
     * @throws BadPaddingException This expection is thrown when a particular padding mechanism is
     *                             expected for the input data but the data is not padded properly.
     */

    //the key is the pin padded with a fixed String.
    public static byte[] encrypt(byte[] ivBytes, byte[] keyBytes, byte[] textBytes)
            throws java.io.UnsupportedEncodingException,
            NoSuchAlgorithmException,
            NoSuchPaddingException,
            InvalidKeyException,
            InvalidAlgorithmParameterException,
            IllegalBlockSizeException,
            BadPaddingException {

        AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
        SecretKeySpec newKey = new SecretKeySpec(keyBytes, "AES");
        Cipher cipher = null;
        cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, newKey, ivSpec);
        return cipher.doFinal(textBytes);
    }

    /**
     *This method decrypts the ciphertext using AES-256 in CBC mode.
     * @param ivBytes the Initialitation Vector, its lenght is 16 bytes.
     * @param keyBytes the key, its lenght is 32 bytes.
     * @param textBytes The ciphertext.
     * @return the plaintext.
     * @throws java.io.UnsupportedEncodingException The Character Encoding is not supported.
     * @throws NoSuchAlgorithmException This exception is thrown when a particular cryptographic
     *                                  algorithm is requested but is not available in the enviroment.
     * @throws NoSuchPaddingException This exception is thrown when a particular padding mechanism
     *                                is requested but is not available in the environment.
     * @throws InvalidKeyException This is the exception for invalid keys(invalid encoding,
     *                             wrong lenght, uninitialized,etc).
     * @throws InvalidAlgorithmParameterException This is the exception for invalid or
     *                                            inappropriate parameters.
     * @throws IllegalBlockSizeException This expection is thrown when the lenght of data
     *                                   provided to a block cipher is incorrect.
     * @throws BadPaddingException This expection is thrown when a particular padding mechanism is
     *                             expected for the input data but the data is not padded properly.
     */

    public static byte[] decrypt(byte[] ivBytes, byte[] keyBytes, byte[] textBytes)
            throws java.io.UnsupportedEncodingException,
            NoSuchAlgorithmException,
            NoSuchPaddingException,
            InvalidKeyException,
            InvalidAlgorithmParameterException,
            IllegalBlockSizeException,
            BadPaddingException {

        AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
        SecretKeySpec newKey = new SecretKeySpec(keyBytes, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec);
        return cipher.doFinal(textBytes);
    }

}
