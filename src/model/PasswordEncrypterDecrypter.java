package model;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

/**
 * Class that allows to encrypt and decrypt a password using the AES algorithm (and PKCS5 Padding)
 * the password is then a cipher, with a key and an initial vector
 * they are all stored as Strings into the DB
 * The class also contains method to reverse the operation and generate iv and key from strings,
 * To then decrypt a cipher
 * @author Baptiste Petiot
 */
public class PasswordEncrypterDecrypter {
    private static final String algorithm = "AES/CBC/PKCS5Padding";
    private static final int n = 128;

    /**
     * generate a key for the AES algorithm
     * @return key : SecretKey
     * @throws NoSuchAlgorithmException : no such thing algorithm exception
     */
    public static SecretKey generateKey() throws NoSuchAlgorithmException {
        KeyGenerator kg = KeyGenerator.getInstance("AES");
        kg.init(n);
        SecretKey key = kg.generateKey();
        return key;
    }

    /**
     * transforms a secret key into a string
     * @param key : String
     * @return String
     */
    public static String secretKey2String(SecretKey key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    /**
     * transforms a stringified secret key into the original secret key
     * @param key : String
     * @return SecretKey
     */
    public static SecretKey string2SecretKey(String key) {
        byte[] decodedKey = Base64.getDecoder().decode(key);
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
    }

    /**
     * transforms an iv byte array into a String
     * @param array : byte[]
     * @return String
     */
    public static String array2String(byte[] array) {
        return Arrays.toString(array);
    }

    /**
     * transforms an iv byte array stringified back into the original byte array
     * @param s : String
     * @return byte[]
     */
    public static byte[] string2Array(String s) {
        String[] strings = s.replace("[", "").replace("]", "").split(", ");
        byte[] result = new byte[strings.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = Byte.parseByte(strings[i]);
        }
        return result;
    }

    /**
     * Generates an Initial vector of length 16
     * @return iv : byte[]
     */
    public static byte[] generateIv() {
        byte[] iv = new byte[16];
        return iv;
    }

    /**
     * Generate a random ivParameterSpec based on a previously generated iv byte array
     * @param iv : byte[]
     * @return IvParameterSpec
     */
    public static IvParameterSpec generateIvParameterSpec(byte[] iv) {
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    /**
     * Encrypt an input with a specified key and input vector
     * @param input : String
     * @param key : SecretKey
     * @param iv : IvParameterSpec
     * @return String
     * @throws NoSuchAlgorithmException : no such algorithm exception
     * @throws IllegalBlockSizeException : illegal blocksize exception
     * @throws InvalidKeyException : invalid key exception
     * @throws BadPaddingException : bad padding exception
     * @throws InvalidAlgorithmParameterException : invalid algorithm parameter exception
     * @throws NoSuchPaddingException : no such padding exception
     */
    public static String encrypt(String input, SecretKey key, IvParameterSpec iv) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException {

        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        byte[] cipherText = cipher.doFinal(input.getBytes());

        String encryptedString = Base64.getEncoder().encodeToString(cipherText);

        return encryptedString;
    }

    /**
     * decrypt a cipher input given the appropriate key and input vector
     * @param input : String
     * @param key : SecretKey
     * @param iv : IvParameterSpec
     * @return String
     * @throws NoSuchAlgorithmException : no such algorithm exception
     * @throws IllegalBlockSizeException : illegal blocksize exception
     * @throws InvalidKeyException : invalid key exception
     * @throws BadPaddingException : bad padding exception
     * @throws InvalidAlgorithmParameterException : invalid algorithm parameter exception
     * @throws NoSuchPaddingException : no such padding exception
     */
    public static String decrypt(String input, SecretKey key, IvParameterSpec iv) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException {

        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        byte[] plainText = cipher.doFinal(Base64.getDecoder().decode(input));

        String decryptedString = new String(plainText);

        return decryptedString;
    }

    /**
     * Test all the methods of the class
     * @throws NoSuchAlgorithmException : no such algorithm exception
     * @throws IllegalBlockSizeException : illegal blocksize exception
     * @throws InvalidKeyException : invalid key exception
     * @throws BadPaddingException : bad padding exception
     * @throws InvalidAlgorithmParameterException : invalid algorithm parameter exception
     * @throws NoSuchPaddingException : no such padding exception
     */
    public static void test() throws NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, InvalidAlgorithmParameterException, NoSuchPaddingException {
        System.out.println(" ENCRYPTION ");
        String input = "jarvis";

        SecretKey key = PasswordEncrypterDecrypter.generateKey();
        byte[] iv = PasswordEncrypterDecrypter.generateIv();
        IvParameterSpec ivParameterSpec = PasswordEncrypterDecrypter.generateIvParameterSpec(iv);

        System.out.println(" SAVE TO DB ");
        String cipherPassword = PasswordEncrypterDecrypter.encrypt(input, key, ivParameterSpec);
        String s = PasswordEncrypterDecrypter.secretKey2String(key);
        String ivString = PasswordEncrypterDecrypter.array2String(iv);
        System.out.println("password : " + cipherPassword + " , key : " + s + " , iv : " + ivString);

        System.out.println(" DECRYPTION ");
        byte[] iv2Array = PasswordEncrypterDecrypter.string2Array(ivString);
        IvParameterSpec iv2 = new IvParameterSpec(iv2Array);
        SecretKey key2 = PasswordEncrypterDecrypter.string2SecretKey(s);
        String clearPassword = PasswordEncrypterDecrypter.decrypt(cipherPassword, key2, iv2);
        System.out.println("password : " + clearPassword);
    }
}
