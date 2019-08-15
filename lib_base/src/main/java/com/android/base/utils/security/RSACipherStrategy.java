package com.android.base.utils.security;

import java.io.InputStream;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Reference: https://github.com/zhengxiaopeng/Rocko-Android-Demos
 */
public class RSACipherStrategy extends CipherStrategy {

    private PublicKey mPublicKey;
    private PrivateKey mPrivateKey;

    public void initPublicKey(String publicKeyContentStr) {
        try {
            mPublicKey = RSAUtils.loadPublicKey(publicKeyContentStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initPublicKey(InputStream publicKeyIs) {
        try {
            mPublicKey = RSAUtils.loadPublicKey(publicKeyIs);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initPrivateKey(String privateKeyContentStr) {
        try {
            mPrivateKey = RSAUtils.loadPrivateKey(privateKeyContentStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initPrivateKey(InputStream privateIs) {
        try {
            mPrivateKey = RSAUtils.loadPrivateKey(privateIs);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String encrypt(String content) {
        if (mPublicKey == null) {
            throw new NullPointerException("PublicKey is null, please initialize it first");
        }
        byte[] encryptByte = RSAUtils.encryptData(content.getBytes(), mPublicKey);

        return encodeConvert(encryptByte);
    }

    @Override
    public String decrypt(String encryptContent) {
        if (mPrivateKey == null) {
            throw new NullPointerException("PrivateKey is null, please initialize it first");
        }
        byte[] encryptByte = decodeConvert(encryptContent);
        byte[] decryptByte = RSAUtils.decryptData(encryptByte, mPrivateKey);

        return new String(decryptByte);
    }

}