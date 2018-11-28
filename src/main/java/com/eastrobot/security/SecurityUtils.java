package com.eastrobot.security;

import com.eastrobot.security.aescbc.AESCBC;
import com.eastrobot.security.aescbc.exception.AESException;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.nio.file.Files;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by carxiong on 21/11/2018.
 */
public class SecurityUtils {
    private SecurityUtils() {
    }

    public static byte[] byteMerger(byte[]... values) {
        int lengthByte = 0;
        for (int i = 0; i < values.length; i++) {
            lengthByte += values[i].length;
        }
        byte[] allByte = new byte[lengthByte];
        int countLength = 0;
        for (int i = 0; i < values.length; i++) {
            byte[] b = values[i];
            System.arraycopy(b, 0, allByte, countLength, b.length);
            countLength += b.length;
        }
        return allByte;
    }

    public static void encryptFile(String inputFilePath, String outFilePath) throws IOException, AESException, NoSuchPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        String sKey = UUIDUtils.generate16Uuid();
        String ivParameter = UUIDUtils.generate16Uuid();
        AESCBC aes = new AESCBC(sKey, ivParameter);
        byte[] needEncryptByteArray = file2ByteArray(inputFilePath);
        byte[] encryptByteArray = aes.encryptRetrurnByte(needEncryptByteArray);
        byte[] sKeyBytes = sKey.getBytes("UTF8");
        byte[] ivParameterBytes = ivParameter.getBytes("UTF8");
        byte[] mergedBytes = SecurityUtils.byteMerger(encryptByteArray, sKeyBytes, ivParameterBytes);
        createFile(outFilePath, mergedBytes);
    }

    public static void decryptFile(String inputFilePath, String outFilePath) throws IOException, AESException, NoSuchPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        byte[] fileByteArray = file2ByteArray(inputFilePath);
        byte[] needDecryptByteArray = new byte[fileByteArray.length - 32];
        System.arraycopy(fileByteArray, 0, needDecryptByteArray, 0, needDecryptByteArray.length);
        byte[] sKeyBytes1 = new byte[16];
        System.arraycopy(fileByteArray, needDecryptByteArray.length, sKeyBytes1, 0, sKeyBytes1.length);
        byte[] ivParameterBytes2 = new byte[16];
        System.arraycopy(fileByteArray, needDecryptByteArray.length + 16, ivParameterBytes2, 0, ivParameterBytes2.length);


        String sKey1 = new String(sKeyBytes1, "UTF8");
        String ivParameter1 = new String(ivParameterBytes2, "UTF8");
        AESCBC aesDecrypt = new AESCBC(sKey1, ivParameter1);
        byte[] decryptByteArray = aesDecrypt.decrypt(needDecryptByteArray);
        createFile(outFilePath, decryptByteArray);
    }

    public static void createFile(String path, byte[] content) throws IOException, AESException {
        if (StringUtils.isBlank(path)) {
            throw new AESException(path + " the path must not be null");
        }
        File file = new File(path);
        if (file.exists()) {
            Files.delete(file.toPath());
        }
        try (FileOutputStream fos = new FileOutputStream(path);) {

            fos.write(content);
        } catch (IOException e) {
            throw e;
        }
    }

    public static byte[] file2ByteArray(String filePath) throws IOException {

        InputStream in = new FileInputStream(filePath);
        byte[] data = toByteArray(in);
        in.close();
        return data;
    }

    public static byte[] toByteArray(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024 * 4];
        int n = 0;
        while ((n = in.read(buffer)) != -1) {
            out.write(buffer, 0, n);
        }
        return out.toByteArray();
    }

}
