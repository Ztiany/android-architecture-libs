@file:JvmName("Strings")

package com.android.base.utils.common

import java.util.regex.Pattern


private const val CHINA_PHONE_REG = "^1\\d{10}$"
private const val ID_CARD_REG = "[1-9]\\d{13,16}[a-zA-Z0-9]{1}"
private const val EMAIL_REG = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*"
private const val DIGIT_REG = "\\-?[1-9]\\d+\""
private const val URL_REG = "(https?://(w{3}\\.)?)?\\w+\\.\\w+(\\.[a-zA-Z]+)*(:\\d{1,5})?(/\\w*)*(\\??(.+=.*)?(&.+=.*)?)?"
private const val CHINESE_REGEX = "^[\u4E00-\u9FA5]+$"
private const val DECIMALS_REG = "\\-?[1-9]\\d+(\\.\\d+)?"
private const val LETTERS_REG = ".*[a-zA-Z]++.*"
private const val DIGITAL_REG = ".*[0-9]++.*"
private const val DIGITAL_LETTER_ONLY_REG = "^[A-Za-z0-9]+$"

/**
 * 验证中国的手机号
 *
 * @return 验证成功返回 true，验证失败返回 false
 */
fun isChinaPhoneNumber(mobile: String): Boolean {
    return !isEmpty(mobile) && Pattern.matches(CHINA_PHONE_REG, mobile)
}

fun containsLetter(text: String): Boolean {
    return !isEmpty(text) && Pattern.matches(LETTERS_REG, text)
}

fun containsDigital(text: String): Boolean {
    return !isEmpty(text) && Pattern.matches(DIGITAL_REG, text)
}

fun containsDigtalLetterOnly(text: String): Boolean {
    return !isEmpty(text) && Pattern.matches(DIGITAL_LETTER_ONLY_REG, text)
}

/**
 * 验证身份证号码
 *
 * @param idCard 居民身份证号码15位或18位，最后一位可能是数字或字母
 * @return 验证成功返回true，验证失败返回false
 */
fun isIdCard(idCard: String): Boolean {
    return !isEmpty(idCard) && Pattern.matches(ID_CARD_REG, idCard)
}

/**
 * 验证Email
 *
 * @param email email地址，格式：zhangsan@sina.com，zhangsan@xxx.com.cn，xxx代表邮件服务商
 * @return 验证成功返回true，验证失败返回false
 */
fun isEmail(email: String): Boolean {
    return !isEmpty(email) && Pattern.matches(EMAIL_REG, email)
}

/**
 * 验证整数（正整数和负整数）
 *
 * @param digit 一位或多位0-9之间的整数
 * @return 验证成功返回true，验证失败返回false
 */
fun isDigit(digit: String): Boolean {
    return !isEmpty(digit) && Pattern.matches(DIGIT_REG, digit)
}

/**
 * 验证整数和浮点数（正负整数和正负浮点数）
 *
 * @param decimals 一位或多位0-9之间的浮点数，如：1.23，233.30
 * @return 验证成功返回true，验证失败返回false
 */
fun isDecimals(decimals: String): Boolean {
    return !isEmpty(decimals) && Pattern.matches(DECIMALS_REG, decimals)
}

/**
 * 验证中文
 *
 * @param chinese 中文字符
 * @return 验证成功返回true，验证失败返回false
 */
fun isChinese(chinese: String): Boolean {
    return !isEmpty(chinese) && Pattern.matches(CHINESE_REGEX, chinese)
}

/**
 * 验证URL地址
 *
 * @param url url
 * @return 验证成功返回true，验证失败返回false
 */
fun isURL(url: String): Boolean {
    return !isEmpty(url) && Pattern.matches(URL_REG, url)
}

/**
 * 获取字符串的字符个数
 */
fun getCharLength(string: String): Int {
    return if (isEmpty(string)) {
        0
    } else string.trim { it <= ' ' }.toCharArray().size
}

fun isCharLength(string: String?, length: Int): Boolean {
    return null != string && (isEmpty(string) && length == 0 || string.trim { it <= ' ' }.toCharArray().size == length)
}

fun isLengthIn(string: String?, min: Int, max: Int): Boolean {
    val length = string?.length ?: 0
    return length in min..max
}

fun isEmpty(str: CharSequence?): Boolean {
    return str == null || str.toString().trim { it <= ' ' }.isEmpty()
}
