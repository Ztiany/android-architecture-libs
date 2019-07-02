package com.android.base.utils.security;


import android.util.Base64;

/**
 * Reference: https://github.com/zhengxiaopeng/Rocko-Android-Demos
 */
abstract class CipherStrategy {

    final static String CHARSET = "UTF-8";

    /**
     * 将加密内容的 Base64 编码转换为二进制内容
     */
    protected byte[] decodeConvert(String str) {
        return Base64.decode(str, Base64.DEFAULT);
    }

    /**
     * 对加密后的二进制结果转换为 Base64 编码
     */
    protected String encodeConvert(byte[] bytes) {
        return new String(Base64.encode(bytes, Base64.DEFAULT));
    }

    /**
     * 对字符串进行加密
     *
     * @param content 需要加密的字符串
     * @return
     */
    public abstract String encrypt(String content);

    /**
     * 对字符串进行解密
     *
     * @param encryptContent 加密内容的 Base64 编码
     * @return
     */
    public abstract String decrypt(String encryptContent);

}
