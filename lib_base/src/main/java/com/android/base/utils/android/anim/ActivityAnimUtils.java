package com.android.base.utils.android.anim;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.support.annotation.AnimRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.view.View;
import android.view.Window;

import com.android.base.utils.android.compat.AndroidVersion;

public class ActivityAnimUtils {

    private ActivityAnimUtils() {
        throw new UnsupportedOperationException("no need instantiation");
    }

    public static void startActivity(Activity activity, Intent intent, View share) {
        if (AndroidVersion.atLeast(21)) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(activity, share, share.getTransitionName());
            activity.startActivity(intent, options.toBundle());
        } else {//小于5.0，使用makeScaleUpAnimation
            ActivityOptionsCompat options = ActivityOptionsCompat.makeScaleUpAnimation(share, share.getWidth() / 2, share.getHeight() / 2, 0, 0);
            ActivityCompat.startActivity(activity, intent, options.toBundle());
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Enter And Exit Transition
    ///////////////////////////////////////////////////////////////////////////

    public static final int TYPE_EXPLODE = 1;
    public static final int TYPE_SLIDE = 2;
    public static final int TYPE_FADE = 3;

    /**
     * @param activity context
     * @param type     {@link #TYPE_EXPLODE},{@link #TYPE_SLIDE},{@link #TYPE_FADE}
     */
    private static void setTransition(Activity activity, int type) {
        if (AndroidVersion.atLeast(21)) {
            Window window = activity.getWindow();
            window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            switch (type) {
                case TYPE_EXPLODE:
                    window.setEnterTransition(new Explode());
                    window.setExitTransition(new Explode());
                    break;
                case TYPE_SLIDE:
                    window.setEnterTransition(new Slide());
                    window.setExitTransition(new Slide());
                    break;
                case TYPE_FADE:
                    window.setEnterTransition(new Fade());
                    window.setExitTransition(new Fade());
                    break;
            }
        }
    }

    public static void finishWithAnimation(AppCompatActivity activity, @AnimRes int enterAnim, @AnimRes int exitAnim) {
        activity.supportFinishAfterTransition();
        activity.overridePendingTransition(enterAnim, exitAnim);
    }

}
