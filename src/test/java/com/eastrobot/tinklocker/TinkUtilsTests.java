/*
 * Power by www.xiaoi.com
 */
package com.eastrobot.tinklocker;

import com.eastrobot.security.Security;
import com.eastrobot.security.SecurityUtils;
import com.eastrobot.security.UUIDUtils;
import com.eastrobot.security.aescbc.AESCBC;
import org.junit.Test;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.*;
import java.io.ByteArrayOutputStream;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.Random;
import java.util.UUID;

/**
 */
public class TinkUtilsTests {

    @Test
    public void test1() throws IOException, GeneralSecurityException {

        String fileBasePath = "C:\\Users\\carxiong\\Desktop\\tinktest\\";
        String fileName = "下一代实时流数据处理平台介绍yangxujun.pdf";
        String fileEncryptName = "encrypt_" + fileName;
        String fileDecryptName = "decrypt_" + fileName;
        byte[] key = null;//fileName.getBytes(StandardCharsets.UTF_8);
        byte[] needEncryptByteArray = file2ByteArray(fileBasePath + fileName);
        byte[] encryptByteArray = TinkUtils.getEncryptByte(needEncryptByteArray, key);
        createFile(fileBasePath + fileEncryptName, encryptByteArray);
//        byte[] needDecryptByteArray = file2ByteArray(fileBasePath + fileEncryptName);
//        byte[] decryptByteArray = TinkUtils.getDecryptByte(needDecryptByteArray,key);
//        createFile(fileBasePath + fileDecryptName, decryptByteArray);
    }

    private void createFile(String path, byte[] content) throws IOException {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
        FileOutputStream fos = new FileOutputStream(path);
        fos.write(content);
        fos.close();
    }

    private byte[] file2ByteArray(String filePath) throws IOException {

        InputStream in = new FileInputStream(filePath);
        byte[] data = toByteArray(in);
        in.close();

        return data;
    }

    private byte[] toByteArray(InputStream in) throws IOException {

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024 * 4];
        int n = 0;
        while ((n = in.read(buffer)) != -1) {
            out.write(buffer, 0, n);
        }
        return out.toByteArray();
    }

    @Test
    public void test2() throws Exception {
        String fileBasePath = "C:\\Users\\carxiong\\Desktop\\tinktest\\";
        String fileName = "下一代实时流数据处理平台介绍yangxujun.pdf";
        String fileEncryptName = "encrypt_" + fileName;
        String fileDecryptName = "decrypt_" + fileName;
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        KeySpec spec = new PBEKeySpec(Base64.getEncoder().encodeToString(fileName.getBytes("utf-8")).toCharArray(), salt, 65536, 256); // AES-256
        SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] key = f.generateSecret(spec).getEncoded();
//        byte[] key = fileName.getBytes(StandardCharsets.UTF_8);
        byte[] needEncryptByteArray = file2ByteArray(fileBasePath + fileName);
        byte[] encryptByteArray = Security.encrypt(needEncryptByteArray, key);
        createFile(fileBasePath + fileEncryptName, encryptByteArray);
//        byte[] needDecryptByteArray = file2ByteArray(fileBasePath + fileEncryptName);
//        byte[] decryptByteArray = TinkUtils.getDecryptByte(needDecryptByteArray,key);
//        createFile(fileBasePath + fileDecryptName, decryptByteArray);
    }

    @Test
    public void test3() throws Exception {
        String fileBasePath = "C:\\Users\\carxiong\\Desktop\\read\\";
        String fileName = "[www.java1234.com]设计模式_可复用面向对象软件的基础.pdf";
        String fileEncryptName = "encrypt_" + fileName;
        String fileDecryptName = "decrypt_" + fileName;
        long time = System.currentTimeMillis();
        SecurityUtils.encryptFile(fileBasePath + fileName,fileBasePath + fileEncryptName);
        System.out.println(System.currentTimeMillis()-time);
        SecurityUtils.decryptFile(fileBasePath + fileEncryptName,fileBasePath + fileDecryptName);
        System.out.println(System.currentTimeMillis()-time);
    }




    @Test
    public void test4() throws Exception {
        String fileName = "下一代实时流数据处理平台介绍yangxujun.pdf";
        String fileName1 = "下一代实时流数据处理平台介绍yangxujun1.pdf";
        String fileName2 = "下一代实时流数据处理平台介绍yangxujun2.pdf";
        String fileName3 = "下一代实时流数据处理平台介绍yangxujun3.pdf";
        System.out.println(Base64.getEncoder().encodeToString(fileName.getBytes("UTF8")));
        System.out.println(Base64.getEncoder().encodeToString(fileName1.getBytes("UTF8")));
        System.out.println(Base64.getEncoder().encodeToString(fileName2.getBytes("UTF8")));
        System.out.println(Base64.getEncoder().encodeToString(fileName3.getBytes("UTF8")));
    }

    @Test
    public void test5() throws Exception {
        UUID uuid = UUID.randomUUID();
        String str = uuid.toString();
        String uuidStr = str.replace("-", "");
        System.out.println(uuidStr);

        int first = new Random(10).nextInt(8) + 1;
        System.out.println(first);
        int hashCodeV = UUID.randomUUID().toString().hashCode();
        if (hashCodeV < 0) {//有可能是负数
            hashCodeV = -hashCodeV;
        }
        // 0 代表前面补充0
        // 4 代表长度为4
        // d 代表参数为正数型
        String ss = first + String.format("%015d", hashCodeV);
        System.out.println(ss);
        byte[] ssByte = ss.getBytes("UTF8");
        for (byte d : ssByte) {
            System.out.println(d);
        }

    }



    // private

    public static void main(String[] args) throws UnsupportedEncodingException {

    }
}
