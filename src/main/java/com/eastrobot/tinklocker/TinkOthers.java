package com.eastrobot.tinklocker;

import com.google.crypto.tink.mac.MacConfig;
import com.google.crypto.tink.proto.*;
import com.google.crypto.tink.proto.Keyset.Key;
import com.google.protobuf.ByteString;
import com.google.protobuf.MessageLite;

import java.io.UnsupportedEncodingException;

/**
 * Created by carxiong on 20/11/2018.
 */
public class TinkOthers {


    public  static Keyset createKeyset(String keyValue) throws Exception {
        Keyset keyset =
                createKeyset(
                        createKey(
                                createHmacKeyData(keyValue.getBytes("UTF-8"), 16),
                                42,
                                KeyStatusType.ENABLED,
                                OutputPrefixType.TINK));
        return keyset;
    }
    /** @return a keyset from a list of keys. The first key is primary. */
    public static Keyset createKeyset(Key primary, Key... keys) throws Exception {
        Keyset.Builder builder = Keyset.newBuilder();
        builder.addKey(primary).setPrimaryKeyId(primary.getKeyId());
        for (Key key : keys) {
            builder.addKey(key);
        }
        return builder.build();
    }
    /** @return a key with some specified properties. */
    public static Key createKey(
            KeyData keyData, int keyId, KeyStatusType status, OutputPrefixType prefixType)
            throws Exception {
        return Key.newBuilder()
                .setKeyData(keyData)
                .setStatus(status)
                .setKeyId(keyId)
                .setOutputPrefixType(prefixType)
                .build();
    }
    /** @return a {@code KeyData} containing a {@code HmacKey}. */
    public static KeyData createHmacKeyData(byte[] keyValue, int tagSize) throws Exception {
        return createKeyData(
                createHmacKey(keyValue, tagSize),
                MacConfig.HMAC_TYPE_URL,
                KeyData.KeyMaterialType.SYMMETRIC);
    }
    /** @return a {@code KeyData} from a specified key. */
    public static KeyData createKeyData(MessageLite key, String typeUrl, KeyData.KeyMaterialType type)
            throws Exception {
        return KeyData.newBuilder()
                .setValue(key.toByteString())
                .setTypeUrl(typeUrl)
                .setKeyMaterialType(type)
                .build();
    }
    public static HmacKey createHmacKey(byte[] keyValue, int tagSize) throws Exception {
        HmacParams params =
                HmacParams.newBuilder().setHash(HashType.SHA256).setTagSize(tagSize).build();

        return HmacKey.newBuilder()
                .setParams(params)
                .setKeyValue(ByteString.copyFrom(keyValue))
                .build();
    }
}
