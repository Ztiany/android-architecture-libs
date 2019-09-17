package com.android.base.utils.android.views

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.transition.Transition
import android.view.View
import android.view.Window
import androidx.annotation.AnimRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import com.android.base.utils.android.compat.AndroidVersion


fun Activity.startActivityWithTransition(intent: Intent, share: View) {
    if (AndroidVersion.atLeast(21)) {
        val options = ActivityOptions.makeSceneTransitionAnimation(this, share, share.transitionName)
        startActivity(intent, options.toBundle())
    } else {//小于5.0，使用makeScaleUpAnimation
        val options = ActivityOptionsCompat.makeScaleUpAnimation(share, share.width / 2, share.height / 2, 0, 0)
        ActivityCompat.startActivity(this, intent, options.toBundle())
    }
}

fun Activity.setTransition(type: Transition) {
    if (AndroidVersion.atLeast(21)) {
        with(window) {
            requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
            enterTransition = type
            exitTransition = type
        }
    }
}

fun AppCompatActivity.finishWithAnimation(@AnimRes enterAnim: Int, @AnimRes exitAnim: Int) {
    supportFinishAfterTransition()
    overridePendingTransition(enterAnim, exitAnim)
}