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

public class PasswordEncrypterDecrypter {
    private static final String algorithm = "AES/CBC/PKCS5Padding";
    private static final int n = 128;

    public static SecretKey generateKey() throws NoSuchAlgorithmException {
        KeyGenerator kg = KeyGenerator.getInstance("AES");
        kg.init(n);
        SecretKey key = kg.generateKey();
        return key;
    }

    public static String secretKey2String(SecretKey key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    public static SecretKey string2SecretKey(String key) {
        byte[] decodedKey = Base64.getDecoder().decode(key);
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
    }

    public static String array2String(byte[] array) {
        return Arrays.toString(array);
    }

    public static byte[] string2Array(String s) {
        String[] strings = s.replace("[", "").replace("]", "").split(", ");
        byte[] result = new byte[strings.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = Byte.parseByte(strings[i]);
        }
        return result;
    }

    public static byte[] generateIv() {
        byte[] iv = new byte[16];
        return iv;
    }

    public static IvParameterSpec generateIvParameterSpec(byte[] iv) {
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    public static String encrypt(String input, SecretKey key, IvParameterSpec iv) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException {

        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        byte[] cipherText = cipher.doFinal(input.getBytes());

        String encryptedString = Base64.getEncoder().encodeToString(cipherText);

        return encryptedString;
    }

    public static String decrypt(String input, SecretKey key, IvParameterSpec iv) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException {

        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        byte[] plainText = cipher.doFinal(Base64.getDecoder().decode(input));

        String decryptedString = new String(plainText);

        return decryptedString;
    }
}
