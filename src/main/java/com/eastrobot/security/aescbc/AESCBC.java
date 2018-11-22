package com.eastrobot.security.aescbc;


import com.eastrobot.security.aescbc.exception.AESException;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by carxiong on 20/11/2018.
 */
public class AESCBC {
    private static final String CIPHERMODEPADDING = "AES/CBC/PKCS5Padding";// AES/CBC/PKCS7Padding
    private SecretKeySpec skforAES = null;
    private IvParameterSpec IV;

    private AESCBC() {
    }

    // key必须为16,32,64
    public AESCBC(String sKey, String ivParameter) throws UnsupportedEncodingException, AESException {
        validateKeyIv(sKey, ivParameter);
        byte[] skAsByteArray;
        try {
            skAsByteArray = sKey.getBytes("UTF8");
            skforAES = new SecretKeySpec(skAsByteArray, "AES");
        } catch (UnsupportedEncodingException e) {
            throw e;
        }
        byte[] iv = ivParameter.getBytes();
        IV = new IvParameterSpec(iv);
    }

    private void validateKeyIv(String sKey, String ivParameter) throws AESException {
        int sKeyLength = sKey.length();
        if (sKeyLength != 16) {
            throw new AESException("sKey length must be 16/32/64");
        }
        if (ivParameter.length() != sKeyLength) {
            throw new AESException("ivParameter length is not equaled to sKey length");
        }
    }

    public String encrypt(byte[] plaintext) throws NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        byte[] ciphertext = encrypt(CIPHERMODEPADDING, skforAES, IV, plaintext);
        return Base64Encoder.encode(ciphertext);
    }

    public String decrypt(String cipherTextBase64) throws NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        byte[] s = Base64Decoder.decodeToBytes(cipherTextBase64);
        return new String(decrypt(CIPHERMODEPADDING, skforAES, IV, s));
    }

    public byte[] encryptRetrurnByte(byte[] plainText) throws NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        return encrypt(CIPHERMODEPADDING, skforAES, IV, plainText);
    }

    public byte[] decrypt(byte[] plainText) throws NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        return decrypt(CIPHERMODEPADDING, skforAES, IV, plainText);
    }

    private byte[] encrypt(String cmp, SecretKey sk, IvParameterSpec IV, byte[] msg) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher c = Cipher.getInstance(cmp);
        c.init(Cipher.ENCRYPT_MODE, sk, IV);
        return c.doFinal(msg);

    }

    private byte[] decrypt(String cmp, SecretKey sk, IvParameterSpec IV, byte[] ciphertext) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher c = Cipher.getInstance(cmp);
        c.init(Cipher.DECRYPT_MODE, sk, IV);
        return c.doFinal(ciphertext);
    }
}
