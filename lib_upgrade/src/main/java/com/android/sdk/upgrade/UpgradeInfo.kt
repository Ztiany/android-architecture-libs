package com.android.sdk.upgrade

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2019-10-29 19:17
 */
data class UpgradeInfo(
    val isNewVersion: Boolean,
    val isForce: Boolean,
    val versionName: String,
    val downloadUrl: String,
    val description: String,
    val digitalAbstract: String,
    val raw: Any?,
)