@file:JvmName("WebViews")

package com.android.base.utils.android.views

import android.graphics.Bitmap
import android.graphics.Canvas
import android.webkit.WebView

fun captureBitmapFromWebView(webView: WebView): Bitmap {
    val snapShot = webView.capturePicture()
    val bmp = Bitmap.createBitmap(snapShot.width, snapShot.height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bmp)
    snapShot.draw(canvas)
    return bmp
}
