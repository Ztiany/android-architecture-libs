package com.android.base.architecture.app

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.os.Build
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Process
import com.android.base.CrashProcessor
import com.android.base.utils.android.AppUtils
import timber.log.Timber
import java.io.File
import java.io.PrintStream
import java.lang.Thread.UncaughtExceptionHandler
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * 全局异常处理
 */
internal class CrashHandler private constructor(
        private val context: Context
) : UncaughtExceptionHandler {

    private var _crashProcessor: CrashProcessor? = null

    fun setCrashProcessor(crashProcessor: CrashProcessor) {
        _crashProcessor = crashProcessor
    }

    override fun uncaughtException(thread: Thread, ex: Throwable) {
        val crashProcessor = _crashProcessor
        if (crashProcessor != null) {
            crashProcessor.uncaughtException(thread, ex)
        } else {
            // 收集异常信息，写入到sd卡
            restoreCrash(thread, ex)
            //退出
            killProcess()
        }
    }

    private fun restoreCrash(thread: Thread, ex: Throwable) {
        ex.printStackTrace(System.err)
        // 收集异常信息，写入到sd卡
        val externalFilesDir = context.getExternalFilesDir(null) ?: return
        val dir = File(externalFilesDir.toString() + File.separator + "crash")

        if (!dir.exists()) {
            val mkdirs = dir.mkdirs()
            if (!mkdirs) {
                Timber.e("CrashHandler create dir fail")
                return
            }
        }

        try {
            @SuppressLint("SimpleDateFormat") val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val name = dateFormat.format(Date(System.currentTimeMillis())) + ".log"
            val targetFile = File(dir, name)
            if (!targetFile.exists()) {
                targetFile.createNewFile()
            }
            val err = PrintStream(targetFile)
            err.println("--------------------------------AppInfo--------------------------------")
            err.println("AndroidVersion: " + AppUtils.getAppVersionName())
            err.println()
            err.println()
            err.println("--------------------------------SystemInfo:--------------------------------")
            err.println("Product: " + Build.PRODUCT)
            err.println("CPU_ABI: " + Build.CPU_ABI)
            err.println("TAGS: " + Build.TAGS)
            err.println("VERSION_CODES.BASE:" + VERSION_CODES.BASE)
            err.println("MODEL: " + Build.MODEL)
            err.println("SDK: " + VERSION.SDK_INT)
            err.println("VERSION.RELEASE: " + VERSION.RELEASE)
            err.println("DEVICE: " + Build.DEVICE)
            err.println("DISPLAY: " + Build.DISPLAY)
            err.println("BRAND: " + Build.BRAND)
            err.println("BOARD: " + Build.BOARD)
            err.println("FINGERPRINT: " + Build.FINGERPRINT)
            err.println("ID: " + Build.ID)
            err.println("MANUFACTURER: " + Build.MANUFACTURER)
            err.println("USER: " + Build.USER)
            err.println()
            err.println()
            err.println("--------------------------------CrashContent--------------------------------")
            ex.printStackTrace(err)
            err.println()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun killProcess() {
        Process.killProcess(Process.myPid())
    }

    companion object {
        fun register(application: Application): CrashHandler {
            val crashHandler = CrashHandler(application)
            Thread.setDefaultUncaughtExceptionHandler(crashHandler)
            return crashHandler
        }
    }

}