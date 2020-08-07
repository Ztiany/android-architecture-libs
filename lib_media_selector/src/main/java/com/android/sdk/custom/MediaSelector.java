package com.android.sdk.custom;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.android.sdk.common.CropOptions;
import com.bilibili.boxing.Boxing;
import com.bilibili.boxing.BoxingCrop;
import com.bilibili.boxing.BoxingMediaLoader;
import com.bilibili.boxing.model.config.BoxingConfig;
import com.bilibili.boxing.model.config.BoxingCropOption;
import com.bilibili.boxing.model.entity.BaseMedia;
import com.bilibili.boxing.utils.BoxingFileHelper;
import com.bilibili.boxing.utils.BoxingLog;
import com.bilibili.boxing_impl.ui.BoxingActivity;
import com.ztiany.mediaselector.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

/**
 * 基于 Boxing 的多媒体文件选择器
 * <pre>
 *     FileProvider 的 Authorities为 {@code PackageName+ ".file.provider"} 才能正常工作
 * </pre>
 */
public class MediaSelector {

    static {
        BoxingMediaLoader.getInstance().init(new BoxingGlideLoader());
        BoxingCrop.getInstance().init(new BoxingUcrop());
    }

    private static final int REQUEST_CODE_SINGLE = 100;//选择单图
    private static final int REQUEST_CODE_MULTI = 200;//选择多图

    private final Callback mCallback;
    private Activity mActivity;
    private Fragment mFragment;

    public MediaSelector(Activity contextWrapper, Callback callback) {
        mCallback = callback;
        mActivity = contextWrapper;
    }

    public MediaSelector(Fragment contextWrapper, Callback callback) {
        mCallback = callback;
        mFragment = contextWrapper;
    }

    //Function
    public void takeMultiPicture(boolean needCamera, int selectCount) {
        BoxingConfig boxingConfig = new BoxingConfig(BoxingConfig.Mode.MULTI_IMG);
        if (needCamera) {
            boxingConfig.needCamera(R.drawable.ic_boxing_camera);
        }
        boxingConfig.withMaxCount(selectCount);
        start(boxingConfig, REQUEST_CODE_MULTI);
    }

    public void takeSinglePicture(boolean needCamera) {
        BoxingConfig singleImgConfig = new BoxingConfig(BoxingConfig.Mode.SINGLE_IMG);
        if (needCamera) {
            singleImgConfig.needCamera(R.drawable.ic_boxing_camera);
        }
        start(singleImgConfig, REQUEST_CODE_SINGLE);
    }

    public void takeSinglePictureWithCrop(boolean needCamera, CropOptions cropOptions) {
        //裁减的图片放在App内部缓存目录
        String cachePath = BoxingFileHelper.getCacheDir(getContext());
        if (TextUtils.isEmpty(cachePath)) {
            BoxingLog.d("takeSinglePictureWithCrop fail because getCacheDir fail");
            return;
        }

        //裁剪信息
        Uri destUri = new Uri.Builder()
                .scheme("file")
                .appendPath(cachePath)
                .appendPath(String.format(Locale.US, "%s.jpg", System.currentTimeMillis()))
                .build();

        BoxingCropOption cropOption = new BoxingCropOption(destUri)
                .aspectRatio(cropOptions.getAspectX(), cropOptions.getAspectY())
                .withMaxResultSize(cropOptions.getOutputX(), cropOptions.getOutputY());

        //获取图片配置
        BoxingConfig singleCropImgConfig = new BoxingConfig(BoxingConfig.Mode.SINGLE_IMG).withCropOption(cropOption);

        if (needCamera) {
            singleCropImgConfig.needCamera(R.drawable.ic_boxing_camera);
        }

        //去获取图片
        start(singleCropImgConfig, REQUEST_CODE_SINGLE);
    }

    private Context getContext() {
        if (mFragment != null) {
            return mFragment.getContext();
        } else {
            return mActivity;
        }
    }

    private void start(BoxingConfig boxingConfig, int requestCode) {
        if (mFragment != null) {
            Boxing boxing = Boxing.of(boxingConfig).withIntent(mFragment.getContext(), BoxingActivity.class);
            boxing.start(mFragment, requestCode);
        } else if (mActivity != null) {
            Boxing boxing = Boxing.of(boxingConfig).withIntent(mActivity, BoxingActivity.class);
            boxing.start(mActivity, requestCode);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_SINGLE) {
                processSingle(data);
            } else if (requestCode == REQUEST_CODE_MULTI) {
                processMultiResult(data);
            }
        }
    }

    private void processSingle(Intent data) {
        final ArrayList<BaseMedia> medias = Boxing.getResult(data);
        if (medias != null && !medias.isEmpty()) {
            mCallback.onTakePictureSuccess(medias.get(0).getPath());
        }
    }

    private void processMultiResult(Intent data) {
        final ArrayList<BaseMedia> medias = Boxing.getResult(data);
        if (medias != null) {
            List<String> strings = new ArrayList<>();
            for (BaseMedia media : medias) {
                strings.add(media.getPath());
            }
            if (!strings.isEmpty()) {
                mCallback.onTakeMultiPictureSuccess(strings);
            }
        }
    }

    public interface Callback {
        default void onTakeMultiPictureSuccess(@NonNull List<String> pictures) {
        }

        default void onTakePictureSuccess(@NonNull String picture) {
        }
    }

}
