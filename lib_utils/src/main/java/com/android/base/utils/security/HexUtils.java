package com.android.base.utils.security;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2019-10-23 15:09
 */
public class HexUtils {

    /**
     * 字节数组转成16进制字符串
     *
     * @return 16进制字符串
     */
    public static String byte2hex(byte[] bytes) { // 一个字节的数，
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        String tmp;
        for (byte theByte : bytes) {
            // 整数转成十六进制表示
            tmp = (Integer.toHexString(theByte & 0XFF));
            if (tmp.length() == 1) {
                sb.append("0");
            }
            sb.append(tmp);
        }
        return sb.toString().toUpperCase(); // 转成大写
    }

    /**
     * 将hex字符串转换成字节数组
     *
     * @param inputString 16进制的字符串
     * @return 字节数组
     */
    public static byte[] hex2byte(String inputString) {
        if (inputString == null || inputString.length() < 2) {
            return new byte[0];
        }
        inputString = inputString.toLowerCase();
        int l = inputString.length() / 2;
        byte[] result = new byte[l];
        for (int i = 0; i < l; ++i) {
            String tmp = inputString.substring(2 * i, 2 * i + 2);
            result[i] = (byte) (Integer.parseInt(tmp, 16) & 0xFF);
        }
        return result;
    }

}
