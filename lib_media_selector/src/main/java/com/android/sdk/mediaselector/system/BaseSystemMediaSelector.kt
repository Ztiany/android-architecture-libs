package com.android.sdk.mediaselector.system

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.android.base.foundation.common.ActFragWrapper
import com.android.sdk.mediaselector.common.*
import timber.log.Timber
import java.io.File

internal const val REQUEST_CAMERA = 10711
internal const val REQUEST_ALBUM = 10712
internal const val REQUEST_FILE = 10713
internal const val REQUEST_UCROP = 10714

private const val INSTRUCTOR_KEY = "system_instructor_key"

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2020-08-06 18:12
 */
internal abstract class BaseSystemMediaSelector : SystemMediaSelector {

    protected val mediaSelectorCallback: ResultListener

    private val actFragWrapper: ActFragWrapper

    protected lateinit var currentInstructor: Instructor

    val context: Context
        get() = actFragWrapper.context

    protected val lifecycleOwner: LifecycleOwner

    constructor(activity: AppCompatActivity, resultListener: ResultListener) {
        actFragWrapper = ActFragWrapper.create(activity)
        lifecycleOwner = activity
        mediaSelectorCallback = resultListener
    }

    constructor(fragment: Fragment, resultListener: ResultListener) {
        mediaSelectorCallback = resultListener
        lifecycleOwner = fragment
        actFragWrapper = ActFragWrapper.create(fragment)
    }

    protected fun startActivityForResult(intent: Intent?, code: Int) {
        actFragWrapper.startActivityForResult(intent, code, null)
    }

    @CallSuper
    override fun onSaveInstanceState(outState: Bundle) {
        if (::currentInstructor.isInitialized) {
            outState.putParcelable(INSTRUCTOR_KEY, currentInstructor)
        }
    }

    @CallSuper
    override fun onRestoreInstanceState(outState: Bundle?) {
        if (!::currentInstructor.isInitialized) {
            outState?.getParcelable<Instructor>(INSTRUCTOR_KEY)?.let {
                currentInstructor = it
            }
        }
        if (::currentInstructor.isInitialized) {
            currentInstructor.setMediaSelector(this)
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Result
    ///////////////////////////////////////////////////////////////////////////
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode != REQUEST_CAMERA && requestCode != REQUEST_ALBUM &&
                requestCode != REQUEST_FILE && requestCode != REQUEST_UCROP) {
            return
        }

        Timber.d("onActivityResult() called with: requestCode = [$requestCode], resultCode = [$resultCode], data = [$data]")
        if (resultCode != Activity.RESULT_OK) {
            mediaSelectorCallback.onCancel()
            return
        }

        when (requestCode) {
            REQUEST_CAMERA -> processCameraResult()
            REQUEST_ALBUM -> processSystemPhotoResult(data)
            REQUEST_FILE -> processFileResult(data)
            REQUEST_UCROP -> processUCropResult(data)
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Crop
    ///////////////////////////////////////////////////////////////////////////

    protected fun toCrop(src: String) {
        MediaUtils.toUCrop(
                actFragWrapper.context,
                actFragWrapper.fragment,
                src,
                currentInstructor.cropOptions,
                REQUEST_UCROP)
    }

    private fun processUCropResult(data: Intent?) {
        val uCropResult = MediaUtils.getUCropResult(data)
        Timber.d("processCameraResult() called with: resultCode = [], data = [$uCropResult]")
        if (uCropResult == null) {
            mediaSelectorCallback.onTakeFail()
        } else {
            val absolutePath = MediaUtils.getAbsolutePath(context, uCropResult)
            if (!TextUtils.isEmpty(absolutePath)) {
                mediaSelectorCallback.onTakeSuccess(newUriList(absolutePath))
            } else {
                mediaSelectorCallback.onTakeFail()
            }
            Timber.d("processCameraResult() called with: resultCode = [], data = [$absolutePath]")
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Camera
    ///////////////////////////////////////////////////////////////////////////

    override fun takePhotoFromCamera(): Instructor {
        return Instructor(this, Instructor.CAMERA)
    }

    fun takePhotoFromCamera(instructor: Instructor): Boolean {
        currentInstructor = instructor
        if (!MediaUtils.hasCamera(context)) {
            return false
        }
        val targetFile = File(instructor.cameraPhotoSavePath)
        val intent = MediaUtils.makeCaptureIntent(context, targetFile, MediaSelectorConfiguration.getAuthority(context))
        try {
            startActivityForResult(intent, REQUEST_CAMERA)
            return true
        } catch (e: Exception) {
            Timber.d(e, "takePhotoFromCamera error")
        }
        return false
    }

    private fun processCameraResult() {
        //需要裁减，可以裁减则进行裁减，否则直接返回
        if (currentInstructor.needCrop()) {
            //检测图片是否被保存下来
            val photoPath = File(currentInstructor.cameraPhotoSavePath)
            if (!photoPath.exists()) {
                mediaSelectorCallback.onTakeFail()
                return
            }
            val cameraPhotoSavePath = currentInstructor.cameraPhotoSavePath
            toCrop(cameraPhotoSavePath)
            return
        }

        //检测图片是否被保存下来
        if (!File(currentInstructor.cameraPhotoSavePath).exists()) {
            mediaSelectorCallback.onTakeFail()
        } else {
            mediaSelectorCallback.onTakeSuccess(newUriList(currentInstructor.cameraPhotoSavePath))
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Album
    ///////////////////////////////////////////////////////////////////////////

    override fun takePhotoFromSystem(): Instructor {
        return Instructor(this, Instructor.ALBUM)
    }

    fun takePhotoFormSystem(instructor: Instructor): Boolean {
        currentInstructor = instructor
        return doTakePhotoFormSystem()
    }

    protected abstract fun doTakePhotoFormSystem(): Boolean

    protected abstract fun processSystemPhotoResult(data: Intent?)

    ///////////////////////////////////////////////////////////////////////////
    // File
    ///////////////////////////////////////////////////////////////////////////

    override fun takeFileFromSystem(): Instructor {
        return Instructor(this, Instructor.FILE)
    }

    fun takeFile(instructor: Instructor): Boolean {
        currentInstructor = instructor
        return doTakeFile()
    }

    protected abstract fun doTakeFile(): Boolean

    protected abstract fun processFileResult(data: Intent?)

}