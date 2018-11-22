package com.eastrobot.tinklocker;

import com.google.crypto.tink.*;
import com.google.crypto.tink.aead.AeadConfig;
import com.google.crypto.tink.aead.AeadFactory;
import com.google.crypto.tink.aead.AeadKeyTemplates;
import com.google.crypto.tink.proto.KeyTemplate;
import com.google.crypto.tink.proto.Keyset;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Created by carxiong on 19/11/2018.
 */
public class TinkUtils {
    private static boolean isInited = false;
    private static final Logger LOGGER = Logger.getLogger(TinkUtils.class.getName());
    private static  KeyTemplate dekTemplate = AeadKeyTemplates.AES256_GCM;
    //    public static void main(String[] args) throws GeneralSecurityException, IOException {
//        init1();
//    }
    static {
        try {
            Config.register(AeadConfig.LATEST);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
    }

    public static byte[] getEncryptByte(byte[] needEncryptByteArray, byte[] key) throws GeneralSecurityException, IOException {

        KeysetHandle keysetHandle = KeysetHandle.generateNew(AeadKeyTemplates.AES128_GCM);
        Aead aeadEncryption = AeadFactory.getPrimitive(keysetHandle);
        byte[] cipherTextBytes = aeadEncryption.encrypt(needEncryptByteArray,key);
        return cipherTextBytes;
    }

    public static byte[] getDecryptByte(byte[] needDecryptByteArray, byte[] key) throws GeneralSecurityException, IOException {

        KeysetHandle keysetHandle = KeysetHandle.generateNew(AeadKeyTemplates.AES128_GCM);
        Aead aeadDecryption = AeadFactory.getPrimitive(keysetHandle);
        byte[] decryptedCipherTextBytes = aeadDecryption.decrypt(needDecryptByteArray,key);
        return decryptedCipherTextBytes;
    }
    public static byte[] getEncryptByte(byte[] needEncryptByteArray, Keyset keyset) throws GeneralSecurityException, IOException {

        KeysetHandle handle = KeysetHandle.generateNew(AeadKeyTemplates.AES128_GCM);
        byte[] content = keyset.toByteArray();
        OutputStream stream = new ByteArrayOutputStream();
        stream.write(content);
        CleartextKeysetHandle.write(handle, JsonKeysetWriter.withOutputStream(stream));
        Aead aeadEncryption = AeadFactory.getPrimitive(handle);
        byte[] cipherTextBytes = aeadEncryption.encrypt(needEncryptByteArray,null);
        return cipherTextBytes;
    }

    public static byte[] getDecryptByte(byte[] needDecryptByteArray, Keyset keyset) throws GeneralSecurityException, IOException {

        KeysetHandle handle = KeysetHandle.generateNew(AeadKeyTemplates.AES128_GCM);
        byte[] content = keyset.toByteArray();
        OutputStream stream = new ByteArrayOutputStream();
        stream.write(content);
        CleartextKeysetHandle.write(handle, JsonKeysetWriter.withOutputStream(stream));
        Aead aeadDecryption = AeadFactory.getPrimitive(handle);
        byte[] decryptedCipherTextBytes = aeadDecryption.decrypt(needDecryptByteArray,null);
        return decryptedCipherTextBytes;
    }

    private static void init1() throws GeneralSecurityException, IOException {
//        KeysetHandle keysetHandle = KeysetHandle.generateNew(
//                AeadKeyTemplates.AES128_GCM);
//        String keysetFilename = "my_keyset.json";
//        CleartextKeysetHandle.write(keysetHandle, JsonKeysetWriter.withFile(
//                new File(keysetFilename)));
//
//        Aead aead = AeadFactory.getPrimitive(keysetHandle);

        String plainText = "Text that is going to be sent over an insecure channel and must be encrypted at all costs!";

        // Initialize Tink configuration
        Config.register(AeadConfig.LATEST);

        // GENERATE key
        // TODO key should only be generated once and then stored in a secure location.
        KeysetHandle keysetHandle = KeysetHandle.generateNew(AeadKeyTemplates.AES128_GCM);

        // ENCRYPTION
        Aead aeadEncryption = AeadFactory.getPrimitive(keysetHandle);
        byte[] cipherTextBytes = aeadEncryption.encrypt(plainText.getBytes(StandardCharsets.UTF_8), null);
        // conversion of raw bytes to BASE64 representation
        String cipherText = new String(Base64.getEncoder().encode(cipherTextBytes));

        // DECRYPTION
        Aead aeadDecryption = AeadFactory.getPrimitive(keysetHandle);
        byte[] decryptedCipherTextBytes = aeadDecryption.decrypt(Base64.getDecoder().decode(cipherText), null);
        String decryptedCipherText = new String(decryptedCipherTextBytes, StandardCharsets.UTF_8);

        LOGGER.log(Level.INFO, () -> String.format("Decrypted and original plain text are the same: %b", decryptedCipherText.compareTo(plainText) == 0));

    }


}
