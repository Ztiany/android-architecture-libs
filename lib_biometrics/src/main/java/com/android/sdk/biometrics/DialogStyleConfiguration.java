package com.android.sdk.biometrics;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2020-03-10 14:48
 */
public class DialogStyleConfiguration {

    private CharSequence mTitle;

    private CharSequence mSubtitle;

    private CharSequence mDescription;

    private CharSequence mCancelAction;

    public CharSequence getTitle() {
        return mTitle;
    }

    public void setTitle(CharSequence title) {
        mTitle = title;
    }

    public CharSequence getSubtitle() {
        return mSubtitle;
    }

    public void setSubtitle(CharSequence subtitle) {
        mSubtitle = subtitle;
    }

    public CharSequence getDescription() {
        return mDescription;
    }

    public void setDescription(CharSequence description) {
        mDescription = description;
    }

    public CharSequence getCancelAction() {
        return mCancelAction;
    }

    public void setCancelAction(CharSequence cancelAction) {
        mCancelAction = cancelAction;
    }

}
