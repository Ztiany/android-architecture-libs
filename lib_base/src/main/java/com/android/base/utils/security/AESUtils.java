package com.android.base.utils.security;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import androidx.annotation.Nullable;


/**
 * AES 对称加密
 */
@SuppressWarnings("unused")
public class AESUtils {

    //算法/模式/填充
    public static String AES = "AES";
    public static String AES_CBC_ISO10126PADDING = "AES/CBC/ISO10126Padding";
    public static String AES_CBC_NOPADDING = "AES/CBC/NoPadding";
    public static String AES_CBC_PKCS5PADDING = "AES/CBC/PKCS5Padding";
    public static String AES_CFB_ISO10126PADDING = "AES/CFB/ISO10126Padding";
    public static String AES_CFB_NOPADDING = "AES/CFB/NoPadding";
    public static String AES_CFB_PKCS5PADDING = "AES/CFB/PKCS5Padding";
    public static String AES_CTR_ISO10126PADDING = "AES/CTR/ISO10126Padding";
    public static String AES_CTR_NOPADDING = "AES/CTR/NoPadding";
    public static String AES_CTR_PKCS5PADDING = "AES/CTR/PKCS5Padding";
    public static String AES_CTS_ISO10126PADDING = "AES/CTS/ISO10126Padding";
    public static String AES_CTS_NOPADDING = "AES/CTS/NoPadding";
    public static String AES_CTS_PKCS5PADDING = "AES/CTS/PKCS5Padding";
    public static String AES_ECB_ISO10126PADDING = "AES/ECB/ISO10126Padding";
    public static String AES_ECB_NOPADDING = "AES/ECB/NoPadding";
    public static String AES_ECB_PKCS5PADDING = "AES/ECB/PKCS5Padding";
    public static String AES_OFB_ISO10126PADDING = "AES/OFB/ISO10126Padding";
    public static String AES_OFB_NOPADDING = "AES/OFB/NoPadding";
    public static String AES_OFB_PKCS5PADDING = "AES/OFB/PKCS5Padding";

    private static final int LIMIT_LEN = 16;

    private static SecretKeySpec generateAESKey(String algorithm, String password) {
        byte[] passwordData = password.getBytes();
        if (passwordData.length != LIMIT_LEN) {
            throw new IllegalArgumentException("password 长度必须等于16");
        }
        return new SecretKeySpec(passwordData, algorithm);
    }

    @Nullable
    public static byte[] encryptData(byte[] content, String algorithm, String password) {
        try {
            SecretKeySpec key = generateAESKey(algorithm, password);
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return cipher.doFinal(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    public static byte[] decryptData(byte[] content, String algorithm, String password) {
        try {
            SecretKeySpec key = generateAESKey(algorithm, password);
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.DECRYPT_MODE, key);
            return cipher.doFinal(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}