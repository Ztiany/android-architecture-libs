package com.android.base.utils.security;

import java.io.UnsupportedEncodingException;

/**
 * Reference: https://github.com/zhengxiaopeng/Rocko-Android-Demos
 */
public class AESCipherStrategy extends CipherStrategy {

    private String key;

    public AESCipherStrategy(String key) {
        this.key = key;
    }

    @Override
    public String encrypt(String content) {
        byte[] encryptByte = new byte[0];
        try {
            encryptByte = AESUtils.encryptData(content.getBytes(CHARSET), key);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodeConvert(encryptByte);
    }

    @Override
    public String decrypt(String encryptContent) {
        byte[] encryptByte = decodeConvert(encryptContent);
        byte[] decryptByte = AESUtils.decryptData(encryptByte, key);
        String result = "";
        try {
            result = new String(decryptByte, CHARSET);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

}