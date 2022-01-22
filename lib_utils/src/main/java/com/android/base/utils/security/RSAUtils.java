package com.android.base.utils.security;

import android.util.Base64;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import androidx.annotation.Nullable;

/**
 * @see <a href='https://android-developers.googleblog.com/2018/03/cryptography-changes-in-android-p.html'>cryptography-changes-in-android-p.html<a/>
 * @see <a href='https://www.jianshu.com/p/7841eae98d16'>Android使用RSA加密和解密cryptography-changes-in-android-p.html<a/>
 * @see <a href='https://stackoverflow.com/questions/12471999/rsa-encryption-decryption-in-android/12474193'>rsa-encryption-decryption-in-android/12474193<a/>
 * @see <a href='https://proandroiddev.com/secure-data-in-android-encrypting-large-data-dda256a55b36'>secure-data-in-android-encrypting-large-data-dda256a55b36<a/>
 */
@SuppressWarnings("WeakerAccess,unused")
public final class RSAUtils {

    private final static String KEY_PAIR = "RSA";

    /**
     * 默认
     */
    public static final String TRANSFORMATION_DEFAULT = KEY_PAIR;

    /**
     * JDK标准
     */
    public static final String TRANSFORMATION_ECB_PKCS1PADDING = "RSA/ECB/PKCS1Padding";

    /**
     * Android标准
     */
    public static final String TRANSFORMATION_ECB_NOPADDING = "RSA/ECB/NoPadding";

    ///////////////////////////////////////////////////////////////////////////
    // 加解密
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 用公钥加密，每次加密的字节数，不能超过密钥的长度值减去 11。
     *
     * @param data      需加密数据的byte数据
     * @param publicKey 公钥
     * @return 加密后的byte型数据
     */
    @Nullable
    public static byte[] encryptData(String transformation, byte[] data, PublicKey publicKey) {
        try {
            Cipher cipher = Cipher.getInstance(transformation);
            // 编码前设定编码方式及密钥
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            // 传入编码数据并返回编码结果
            return cipher.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 用私钥解密
     *
     * @param encryptedData 经过 encryptedData() 加密返回的 byte 数据
     * @param privateKey    私钥
     */
    @Nullable
    public static byte[] decryptData(String transformation, byte[] encryptedData, PrivateKey privateKey) {
        try {
            Cipher cipher = Cipher.getInstance(transformation);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return cipher.doFinal(encryptedData);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 用公钥解密
     *
     * @param encryptedData 经过 encryptedData() 加密返回的 byte 数据
     * @param publicKey     公钥
     */
    @Nullable
    public static String decryptData(String transformation, byte[] encryptedData, PublicKey publicKey) {
        try {
            Cipher cipher = Cipher.getInstance(transformation);
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            return new String(cipher.doFinal(encryptedData), StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // 生成新的私钥/密钥
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 随机生成 RSA 密钥对(默认密钥长度为 1024)
     */
    public static KeyPair generateRSAKeyPair() {
        return generateRSAKeyPair(1024);
    }

    /**
     * 随机生成 RSA 密钥对
     *
     * @param keyLength 密钥长度，范围：512～2048，一般1024
     */
    public static KeyPair generateRSAKeyPair(int keyLength) {
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance(KEY_PAIR);
            kpg.initialize(keyLength);
            return kpg.genKeyPair();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // 根据key创建 PublicKey 或 PrivateKey
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 通过公钥 byte[](publicKey.getEncoded()) 将公钥还原，适用于 RSA 算法。
     *
     * @throws NoSuchAlgorithmException NoSuchAlgorithm
     * @throws InvalidKeySpecException  InvalidKeySpec
     */
    public static PublicKey getPublicKey(byte[] keyBytes) throws NoSuchAlgorithmException, InvalidKeySpecException {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_PAIR);
        return keyFactory.generatePublic(keySpec);
    }

    /**
     * 通过私钥 byte[] 将私钥还原，适用于 RSA 算法。
     *
     * @throws NoSuchAlgorithmException NoSuchAlgorithm
     * @throws InvalidKeySpecException  InvalidKeySpec
     */
    public static PrivateKey getPrivateKey(byte[] keyBytes) throws NoSuchAlgorithmException, InvalidKeySpecException {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_PAIR);
        return keyFactory.generatePrivate(keySpec);
    }

    /**
     * 使用 N、e 值还原公钥
     *
     * @throws NoSuchAlgorithmException NoSuchAlgorithm
     * @throws InvalidKeySpecException  InvalidKeySpec
     */
    public static PublicKey getPublicKey(String modulus, String publicExponent) throws NoSuchAlgorithmException, InvalidKeySpecException {
        BigInteger bigIntModulus = new BigInteger(modulus);
        BigInteger bigIntPrivateExponent = new BigInteger(publicExponent);
        RSAPublicKeySpec keySpec = new RSAPublicKeySpec(bigIntModulus, bigIntPrivateExponent);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_PAIR);
        return keyFactory.generatePublic(keySpec);
    }

    /**
     * 使用 N、d 值还原私钥
     *
     * @throws NoSuchAlgorithmException NoSuchAlgorithm
     * @throws InvalidKeySpecException  InvalidKeySpec
     */
    public static PrivateKey getPrivateKey(String modulus, String privateExponent) throws NoSuchAlgorithmException, InvalidKeySpecException {
        BigInteger bigIntModulus = new BigInteger(modulus);
        BigInteger bigIntPrivateExponent = new BigInteger(privateExponent);
        RSAPublicKeySpec keySpec = new RSAPublicKeySpec(bigIntModulus, bigIntPrivateExponent);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_PAIR);
        return keyFactory.generatePrivate(keySpec);
    }

    /**
     * 从字符串中加载公钥
     *
     * @param publicKeyStr Base64 编码的公钥数据字符串
     * @throws Exception 加载公钥时产生的异常
     */
    public static PublicKey loadPublicKey(String publicKeyStr) throws Exception {
        try {
            byte[] buffer = Base64.decode(publicKeyStr, Base64.DEFAULT);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_PAIR);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
            return keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
            throw new Exception("公钥非法" + e.getLocalizedMessage());
        } catch (NullPointerException e) {
            throw new Exception("公钥数据为空");
        }
    }

    /**
     * 从字符串中加载私钥，加载时使用的是 PKCS8EncodedKeySpec（PKCS#8编码的Key指令）。
     *
     * @param privateKeyStr Base64 编码的私钥数据字符串
     */
    public static PrivateKey loadPrivateKey(String privateKeyStr) throws Exception {
        try {
            byte[] buffer = Base64.decode(privateKeyStr, Base64.DEFAULT);
            // X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_PAIR);
            return keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e) {
            throw new Exception("私钥非法");
        } catch (NullPointerException e) {
            throw new Exception("私钥数据为空");
        }
    }

    /**
     * 从文件中输入流中加载公钥
     *
     * @param in 公钥输入流
     * @throws Exception 加载公钥时产生的异常
     */
    public static PublicKey loadPublicKey(InputStream in) throws Exception {
        try {
            return loadPublicKey(readKey(in));
        } catch (IOException e) {
            throw new Exception("公钥数据流读取错误");
        } catch (NullPointerException e) {
            throw new Exception("公钥输入流为空");
        }
    }

    /**
     * 从文件中加载私钥
     */
    public static PrivateKey loadPrivateKey(InputStream in) throws Exception {
        try {
            return loadPrivateKey(readKey(in));
        } catch (IOException e) {
            throw new Exception("私钥数据读取错误");
        } catch (NullPointerException e) {
            throw new Exception("私钥输入流为空");
        }
    }

    /**
     * 读取密钥信息
     *
     * <pre>
     * --------------------
     * CONTENT
     * --------------------
     * </pre>
     */
    private static String readKey(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String readLine;
        StringBuilder sb = new StringBuilder();
        while ((readLine = br.readLine()) != null) {
            if (readLine.charAt(0) == '-') {
                continue;
            }
            sb.append(readLine);
            sb.append('\r');
        }
        return sb.toString();
    }

}