package com.android.base.utils.security;

import android.os.Build;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import androidx.annotation.Nullable;


public class SimpleRsa {

    private static final String ALGORITHM = "RSA";
    private static final String TRANSFORMATION = "RSA/ECB/PKCS1Padding";

    /**
     * 得到公钥
     *
     * @param algorithm 算法
     * @param bysKey    key
     * @return 公钥
     */
    private static PublicKey getPublicKeyFromX509(String algorithm, String bysKey) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException {
        byte[] decodedKey = Base64.decode(bysKey, Base64.DEFAULT);
        X509EncodedKeySpec x509 = new X509EncodedKeySpec(decodedKey);
        KeyFactory keyFactory;
        //适配Android P及以后版本，否则报错NoSuchAlgorithmException
        //https://android-developers.googleblog.com/2018/03/cryptography-changes-in-android-p.html
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            keyFactory = KeyFactory.getInstance(algorithm);
        } else {
            keyFactory = KeyFactory.getInstance(algorithm, "BC");
        }
        return keyFactory.generatePublic(x509);
    }

    /**
     * 使用公钥加密
     *
     * @param content 加密字符串
     * @return 加密后的字符串
     */
    @Nullable
    public static String encryptByPublic(String content, String RSA_PUBLIC_KEY) {
        try {
            PublicKey pubKey = getPublicKeyFromX509(ALGORITHM, RSA_PUBLIC_KEY);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            byte[] plaintext = content.getBytes(StandardCharsets.UTF_8);
            byte[] output = cipher.doFinal(plaintext);
            return new String(Base64.encode(output, Base64.DEFAULT));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 使用公钥解密
     *
     * @param content 密文
     * @return 解密后的字符串
     */
    @Nullable
    public static String decryptByPublic(String content, String RSA_PUBLIC_KEY) {
        try {
            PublicKey pubkey = getPublicKeyFromX509(ALGORITHM, RSA_PUBLIC_KEY);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, pubkey);
            InputStream ins = new ByteArrayInputStream(Base64.decode(content, Base64.DEFAULT));
            ByteArrayOutputStream writer = new ByteArrayOutputStream();
            byte[] buf = new byte[128];
            int offset;
            while ((offset = ins.read(buf)) != -1) {
                byte[] block;
                if (buf.length == offset) {
                    block = buf;
                } else {
                    block = new byte[offset];
                    System.arraycopy(buf, 0, block, 0, offset);
                }
                writer.write(cipher.doFinal(block));
            }
            return new String(writer.toByteArray(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            return null;
        }
    }

}  