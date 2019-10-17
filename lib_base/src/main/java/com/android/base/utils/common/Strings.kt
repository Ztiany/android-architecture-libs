@file:JvmName("Strings")

package com.android.base.utils.common

import java.util.regex.Pattern

private const val CHINA_PHONE_REG = "^1\\d{10}$"
private const val ID_CARD_REG = "[1-9]\\d{13,16}[a-zA-Z0-9]{1}"
private const val EMAIL_REG = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*"
private const val DIGIT_REG = "-?[1-9]\\d+\""
private const val URL_REG = "(https?://(w{3}\\.)?)?\\w+\\.\\w+(\\.[a-zA-Z]+)*(:\\d{1,5})?(/\\w*)*(\\??(.+=.*)?(&.+=.*)?)?"
private const val CHINESE_REGEX = "^[\u4E00-\u9FA5]+$"
private const val DECIMALS_REG = "-?[1-9]\\d+(\\.\\d+)?"
private const val DIGITAL_LETTER_ONLY_REG = "^[A-Za-z0-9]+$"
private const val CONTAINS_DIGITAL_REG = ".*[0-9]+.*"
private const val CONTAINS_LETTERS_REG = ".*[a-zA-Z]+.*"
private const val CONTAINS_LOWERCASE_LETTERS_REG = "^.*[a-z]+.*$"
private const val CONTAINS_UPPERCASE_LETTERS_REG = "^.*[A-Z]+.*$"

/**
 * 验证中国的手机号
 *
 * @return 验证成功返回 true，验证失败返回 false
 */
fun isChinaPhoneNumber(mobile: String?): Boolean {
    return !isEmpty(mobile) && Pattern.matches(CHINA_PHONE_REG, mobile)
}

/**
 * 只包含 [1-9][a-z][A-Z]
 */
fun containsDigitalLetterOnly(text: String?): Boolean {
    return !isEmpty(text) && Pattern.matches(DIGITAL_LETTER_ONLY_REG, text)
}

/**是否包含字母*/
fun containsLetter(text: String?): Boolean {
    return !isEmpty(text) && Pattern.matches(CONTAINS_LETTERS_REG, text)
}

/**
 * 是否包含小写字母
 */
fun containsLowercaseLetter(text: String): Boolean {
    return !isEmpty(text) && Pattern.matches(CONTAINS_LOWERCASE_LETTERS_REG, text)
}

/**
 * 是否包含大写字母
 */
fun containsUppercaseLetter(text: String): Boolean {
    return !isEmpty(text) && Pattern.matches(CONTAINS_UPPERCASE_LETTERS_REG, text)
}

/**
 * 是否包含数字
 */
fun containsDigital(text: String?): Boolean {
    return !isEmpty(text) && Pattern.matches(CONTAINS_DIGITAL_REG, text)
}

/**
 * 验证身份证号码，居民身份证号码15位或18位，最后一位可能是数字或字母。
 */
fun isIdCard(idCard: String?): Boolean {
    return !isEmpty(idCard) && Pattern.matches(ID_CARD_REG, idCard)
}

/**
 * 验证Email
 */
fun isEmail(email: String?): Boolean {
    return !isEmpty(email) && Pattern.matches(EMAIL_REG, email)
}

/**
 * 验证整数（正整数和负整数）
 */
fun isDigit(digit: String?): Boolean {
    return !isEmpty(digit) && Pattern.matches(DIGIT_REG, digit)
}

/**
 * 验证整数和浮点数（正负整数和正负浮点数）
 */
fun isDecimals(decimals: String?): Boolean {
    return !isEmpty(decimals) && Pattern.matches(DECIMALS_REG, decimals)
}

/**
 * 是否为纯中文
 */
fun isChinese(chinese: String?): Boolean {
    return !isEmpty(chinese) && Pattern.matches(CHINESE_REGEX, chinese)
}

/**
 * 验证URL地址
 */
fun isURL(url: String?): Boolean {
    return !isEmpty(url) && Pattern.matches(URL_REG, url)
}

fun isLengthIn(string: String?, min: Int, max: Int): Boolean {
    val length = string?.length ?: 0
    return length in min..max
}

fun isEmpty(str: String?): Boolean {
    return str == null || str.isEmpty()
}