package com.android.sdk.mediaselector.custom

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
import com.android.sdk.mediaselector.common.MediaUtils
import com.android.sdk.mediaselector.common.ResultListener
import com.android.sdk.mediaselector.common.newUriList
import com.bilibili.boxing.Boxing
import com.bilibili.boxing.model.config.BoxingConfig
import com.bilibili.boxing.model.entity.BaseMedia
import com.bilibili.boxing_impl.ui.BoxingActivity
import com.ztiany.mediaselector.R
import timber.log.Timber
import java.util.ArrayList

private const val REQUEST_BOXING = 10715;
internal const val REQUEST_UCROP = 10716
private const val INSTRUCTOR_KEY = "custom_instructor_key"

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2020-08-11 10:09
 */
internal abstract class BaseMediaSelector : MediaSelector {

    private val actFragWrapper: ActFragWrapper

    protected val mediaSelectorCallback: ResultListener

    protected val lifecycleOwner: LifecycleOwner

    protected lateinit var currentInstructor: Instructor

    val context: Context
        get() = actFragWrapper.context

    constructor(activity: AppCompatActivity, resultListener: ResultListener) {
        actFragWrapper = ActFragWrapper.create(activity)
        mediaSelectorCallback = resultListener
        lifecycleOwner = activity
    }

    constructor(fragment: Fragment, resultListener: ResultListener) {
        mediaSelectorCallback = resultListener
        actFragWrapper = ActFragWrapper.create(fragment)
        lifecycleOwner = fragment
    }

    final override fun takeMedia(): Instructor = Instructor(this)

    fun start(instructor: Instructor): Boolean {
        currentInstructor = instructor
        return configAndStart()
    }

    private fun configAndStart(): Boolean {
        val boxingConfig = createBoxingConfig()
        if (currentInstructor.isNeedCamera) {
            boxingConfig.needCamera(R.drawable.ic_boxing_camera)
        }
        return start(boxingConfig)
    }

    private fun start(boxingConfig: BoxingConfig): Boolean {
        Timber.d(" -start(boxingConfig)")
        val fragment = actFragWrapper.fragment
        return try {
            if (fragment != null) {
                val boxing = Boxing.of(boxingConfig).withIntent(fragment.requireContext(), BoxingActivity::class.java)
                boxing.start(fragment, REQUEST_BOXING)
            } else {
                val boxing = Boxing.of(boxingConfig).withIntent(actFragWrapper.context, BoxingActivity::class.java)
                boxing.start(actFragWrapper.context as Activity, REQUEST_BOXING)
            }
            true
        } catch (e: Exception) {
            Timber.e(e, "start(boxingConfig) ")
            e.printStackTrace()
            false
        }
    }

    private fun createBoxingConfig(): BoxingConfig {
        return if (currentInstructor.takingType == Instructor.PICTURE) {
            if (currentInstructor.isMulti) {
                BoxingConfig(BoxingConfig.Mode.MULTI_IMG).withMaxCount(currentInstructor.maxCount)
            } else {
                BoxingConfig(BoxingConfig.Mode.SINGLE_IMG)
            }
        } else {
            BoxingConfig(BoxingConfig.Mode.VIDEO)
        }.also {
            it.mediaFilter = currentInstructor.mediaFilter
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Result
    ///////////////////////////////////////////////////////////////////////////

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Timber.d("onActivityResult() called with: requestCode = [$requestCode], resultCode = [$resultCode], data = [$data]")
        if (requestCode != REQUEST_BOXING && requestCode != REQUEST_UCROP) {
            return
        }

        if (resultCode != Activity.RESULT_OK) {
            mediaSelectorCallback.onCancel()
            return
        }
        if (data == null) {
            mediaSelectorCallback.onCancel()
            return
        }

        if (requestCode == REQUEST_BOXING) {
            handleResult(data)
        } else if (requestCode == REQUEST_UCROP) {
            processUCropResult(data)
        }
    }

    private fun handleResult(data: Intent) {
        val medias = Boxing.getResult(data)
        if (medias.isNullOrEmpty()) {
            mediaSelectorCallback.onCancel()
            return
        }

        if (medias.size > 1) {
            handleMultiResult(medias)
        } else {
            handleSingleResult(medias[0])
        }
    }

    abstract fun handleSingleResult(baseMedia: BaseMedia)

    abstract fun handleMultiResult(medias: ArrayList<BaseMedia>)

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
        if (uCropResult == null) {
            mediaSelectorCallback.onTakeFail()
        } else {
            val absolutePath = MediaUtils.getAbsolutePath(context, uCropResult)
            if (!TextUtils.isEmpty(absolutePath)) {
                mediaSelectorCallback.onTakeSuccess(newUriList(absolutePath))
            } else {
                mediaSelectorCallback.onTakeFail()
            }
        }
    }

}