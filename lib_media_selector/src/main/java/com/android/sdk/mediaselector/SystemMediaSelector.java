package com.android.sdk.mediaselector;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * 通过系统相册或者系统相机获取照片
 * <pre>
 *     1. 默认的 FileProvider 的 Authorities 为 {@code PackageName+ ".file.provider"}
 * </pre>
 *
 * @author Ztiany
 * Email: 1169654504@qq.com
 * Date : 2017-08-09 10:50
 */
public class SystemMediaSelector {

    private static final String TAG = SystemMediaSelector.class.getSimpleName();

    private static final int REQUEST_CAMERA = 196;
    private static final int REQUEST_CROP = 197;
    private static final int REQUEST_ALBUM = 198;
    private static final int REQUEST_FILE = 199;
    private static final int REQUEST_LIBRARY_CROP = 200;

    private static final String POSTFIX = ".file.provider";
    private String mAuthority;

    private final MediaSelectorCallback mMediaSelectorCallback;
    private Activity mActivity;
    private Fragment mFragment;

    private final CropOptions mDefaultOptions = new CropOptions();
    private String mCropTitle;
    private boolean mNeedCrop;
    private CropOptions mCropOptions;

    /*使用内部的裁剪库（推荐，系统裁剪兼容性差）*/
    private boolean mUseInnerCrop = true;

    /*用于保存图片*/
    private String mSavePhotoPath;
    /*某些手机，拍照和裁剪不能是同一个路径*/
    private String mSavePhotoPathForCropCamera;

    public SystemMediaSelector(Activity activity, MediaSelectorCallback systemMediaSelectorCallback) {
        mActivity = activity;
        mMediaSelectorCallback = systemMediaSelectorCallback;
        init();
    }

    public SystemMediaSelector(Fragment fragment, MediaSelectorCallback systemMediaSelectorCallback) {
        mFragment = fragment;
        mMediaSelectorCallback = systemMediaSelectorCallback;
        init();
    }

    private void init() {
        mAuthority = getContext().getPackageName().concat(POSTFIX);
        mDefaultOptions.setAspectX(0);
        mDefaultOptions.setAspectY(0);
        mDefaultOptions.setOutputX(0);
        mDefaultOptions.setOutputY(0);
    }

    private Context getContext() {
        if (mFragment != null) {
            return mFragment.getContext();
        } else {
            return mActivity;
        }
    }

    private void startActivityForResult(Intent intent, int code) {
        if (mFragment != null) {
            mFragment.startActivityForResult(intent, code);
        } else {
            mActivity.startActivityForResult(intent, code);
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // setter
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 默认的authority 为"包名.fileProvider"
     *
     * @param authority 指定FileProvider的authority
     */
    public void setAuthority(String authority) {
        mAuthority = authority;
    }

    private CropOptions getCropOptions() {
        return mCropOptions == null ? mDefaultOptions : mCropOptions;
    }

    public void setUseInnerCrop(boolean useInnerCrop) {
        mUseInnerCrop = useInnerCrop;
    }

    ///////////////////////////////////////////////////////////////////////////
    // take photo from camera
    ///////////////////////////////////////////////////////////////////////////

    public boolean takePhotoFromCamera(String savePath) {
        mSavePhotoPath = savePath;
        mNeedCrop = false;
        return toCamera(mSavePhotoPath);
    }

    /**
     * 为了保证裁裁剪图片不出问题，务必指定 CropOptions 中的各个参数不要为 0，否则可能出现问题（比如魅族手机如果指定 OutputX 和 OutputY 为 0，则只会裁减出一个像素）。
     */
    public boolean takePhotoFromCameraAndCrop(String savePath, @Nullable CropOptions cropOptions, String cropTitle) {
        mSavePhotoPath = savePath;
        mSavePhotoPathForCropCamera = Utils.addFilePostfix(mSavePhotoPath, "camera");
        mNeedCrop = true;
        mCropOptions = cropOptions;
        mCropTitle = cropTitle;
        return toCamera(mSavePhotoPathForCropCamera);
    }

    private boolean toCropPhotoFromCamera() {
        File targetFile = new File(mSavePhotoPath);
        File srcFile = new File(mSavePhotoPathForCropCamera);
        Intent intent = Utils.makeCropIntentNoCovering(getContext(), srcFile, targetFile, mAuthority, getCropOptions(), mCropTitle);
        try {
            startActivityForResult(intent, REQUEST_CROP);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "toCropPhotoFromAlbum error", e);
        }
        return false;
    }

    private boolean toCamera(String savePhotoPath) {
        if (!Utils.hasCamera(getContext())) {
            return false;
        }
        File targetFile = new File(savePhotoPath);
        Intent intent = Utils.makeCaptureIntent(getContext(), targetFile, mAuthority);
        try {
            startActivityForResult(intent, REQUEST_CAMERA);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "takePhotoFromCamera error", e);
        }
        return false;
    }

    ///////////////////////////////////////////////////////////////////////////
    // take photo from album
    ///////////////////////////////////////////////////////////////////////////

    public boolean takePhotoFormAlbum() {
        mNeedCrop = false;
        try {
            startActivityForResult(Utils.makeAlbumIntent(), REQUEST_ALBUM);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean takePhotoFormAlbumAndCrop(String savePhotoPath, @Nullable CropOptions cropOptions, String cropTitle) {
        mNeedCrop = true;
        mSavePhotoPath = savePhotoPath;
        mCropOptions = cropOptions;
        mCropTitle = cropTitle;
        try {
            startActivityForResult(Utils.makeAlbumIntent(), REQUEST_ALBUM);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean toCropPhotoFromAlbum(Uri uri) {
        File targetFile = new File(mSavePhotoPath);
        File srcFile = new File(Utils.getAbsolutePath(getContext(), uri));
        Intent intent = Utils.makeCropIntentNoCovering(getContext(), srcFile, targetFile, mAuthority, getCropOptions(), mCropTitle);
        try {
            startActivityForResult(intent, REQUEST_CROP);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "toCropPhotoFromAlbum error", e);
        }
        return false;
    }

    ///////////////////////////////////////////////////////////////////////////
    // take photo from album
    ///////////////////////////////////////////////////////////////////////////

    public boolean takeFile() {
        return takeFile(null);
    }

    public boolean takeFile(String mimeType) {
        Intent intent = Utils.makeFilesIntent(mimeType);
        try {
            startActivityForResult(intent, REQUEST_FILE);
        } catch (Exception e) {
            Log.e(TAG, "takeFile error", e);
            return false;
        }
        return true;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Process Result
    ///////////////////////////////////////////////////////////////////////////

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CAMERA) {
            processCameraResult(resultCode, data);
        } else if (requestCode == REQUEST_CROP) {
            processCropResult(resultCode, data);
        } else if (requestCode == REQUEST_ALBUM) {
            processAlbumResult(resultCode, data);
        } else if (requestCode == REQUEST_FILE) {
            processFileResult(resultCode, data);
        } else if (requestCode == REQUEST_LIBRARY_CROP) {
            processUCropResult(data);
        }
    }

    private void processUCropResult(Intent data) {
        Uri uCropResult = Utils.getUCropResult(data);
        if (uCropResult == null) {
            mMediaSelectorCallback.onTakeFail();
        } else {
            mMediaSelectorCallback.onTakeSuccess(Utils.getAbsolutePath(getContext(), uCropResult));
        }
    }

    private void processFileResult(int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            Uri uri = data.getData();
            if (uri == null) {
                mMediaSelectorCallback.onTakeFail();
            } else {
                String absolutePath = Utils.getAbsolutePath(getContext(), uri);
                mMediaSelectorCallback.onTakeSuccess(absolutePath);
            }
        } else {
            mMediaSelectorCallback.onTakeFail();
        }
    }

    private void processAlbumResult(int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (data != null && data.getData() != null) {
                Uri uri = data.getData();
                if (mNeedCrop) {
                    if (mUseInnerCrop) {
                        Utils.toUCrop(getContext(), mFragment, Utils.getAbsolutePath(getContext(), uri), mSavePhotoPath, getCropOptions(), REQUEST_LIBRARY_CROP);
                    } else {
                        boolean success = toCropPhotoFromAlbum(uri);
                        if (!success) {
                            mMediaSelectorCallback.onTakeSuccess(Utils.getAbsolutePath(getContext(), uri));
                        }
                    }
                } else {
                    mMediaSelectorCallback.onTakeSuccess(Utils.getAbsolutePath(getContext(), uri));
                }
            } else {
                mMediaSelectorCallback.onTakeFail();
            }
        }
    }

    private void processCropResult(int resultCode, @SuppressWarnings("unused") Intent data) {
        //有时候，系统裁减的结果可能没有保存到我们指定的目录，而是通过data返回了
        if (resultCode == Activity.RESULT_OK) {
            if (new File(mSavePhotoPath).exists()) {
                mMediaSelectorCallback.onTakeSuccess(mSavePhotoPath);
            } else if (data != null && data.getData() != null) {
                String realPathFromURI = Utils.getAbsolutePath(getContext(), data.getData());
                if (TextUtils.isEmpty(realPathFromURI)) {
                    mMediaSelectorCallback.onTakeFail();
                } else {
                    mMediaSelectorCallback.onTakeSuccess(realPathFromURI);
                }
            } else {
                mMediaSelectorCallback.onTakeFail();
            }
        }
    }

    private void processCameraResult(int resultCode, @SuppressWarnings("unused") Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            //需要裁减，可以裁减则进行裁减，否则直接返回
            if (mNeedCrop) {
                //检测图片是否被保存下来
                File photoPath = new File(mSavePhotoPathForCropCamera);
                if (!photoPath.exists()) {
                    mMediaSelectorCallback.onTakeFail();
                    return;
                }

                if (mUseInnerCrop) {
                    Utils.toUCrop(getContext(), mFragment, mSavePhotoPathForCropCamera, mSavePhotoPath, getCropOptions(), REQUEST_LIBRARY_CROP);
                } else {
                    boolean success = toCropPhotoFromCamera();
                    if (!success) {
                        photoPath.renameTo(new File(mSavePhotoPath));
                        mMediaSelectorCallback.onTakeSuccess(mSavePhotoPath);
                    }
                }

            } else {
                //检测图片是否被保存下来
                if (!new File(mSavePhotoPath).exists()) {
                    mMediaSelectorCallback.onTakeFail();
                    return;
                }
                mMediaSelectorCallback.onTakeSuccess(mSavePhotoPath);
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Interface
    ///////////////////////////////////////////////////////////////////////////

    public interface MediaSelectorCallback {

        default void onTakeSuccess(String path) {
        }

        default void onTakeFail() {
        }

    }

}