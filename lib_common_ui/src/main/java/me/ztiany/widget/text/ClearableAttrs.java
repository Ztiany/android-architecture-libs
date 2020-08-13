package me.ztiany.widget.text;

import android.graphics.Bitmap;

public class ClearableAttrs {

    private Bitmap mClearBitmap;
    private Bitmap mPasswordVisibleBitmap;
    private Bitmap mPasswordInvisibleBitmap;
    private boolean mPasswordVisibleEnable;
    private boolean mContentClearableEnable;

    public Bitmap getClearBitmap() {
        return mClearBitmap;
    }

    public void setClearBitmap(Bitmap clearBitmap) {
        mClearBitmap = clearBitmap;
    }

    public Bitmap getPasswordVisibleBitmap() {
        return mPasswordVisibleBitmap;
    }

    public void setPasswordVisibleBitmap(Bitmap passwordVisibleBitmap) {
        mPasswordVisibleBitmap = passwordVisibleBitmap;
    }

    public Bitmap getPasswordInvisibleBitmap() {
        return mPasswordInvisibleBitmap;
    }

    public void setPasswordInvisibleBitmap(Bitmap passwordInvisibleBitmap) {
        mPasswordInvisibleBitmap = passwordInvisibleBitmap;
    }

    public boolean isPasswordVisibleEnable() {
        return mPasswordVisibleEnable;
    }

    public void setPasswordVisibleEnable(boolean passwordVisibleEnable) {
        mPasswordVisibleEnable = passwordVisibleEnable;
    }

    public boolean isContentClearableEnable() {
        return mContentClearableEnable;
    }

    public void setContentClearableEnable(boolean contentClearableEnable) {
        mContentClearableEnable = contentClearableEnable;
    }

}