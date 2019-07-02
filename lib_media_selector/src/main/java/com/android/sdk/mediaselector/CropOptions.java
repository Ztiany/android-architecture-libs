package com.android.sdk.mediaselector;

import java.io.Serializable;

/**
 * 裁剪配置类
 * Author: JPH
 * Date: 2016/7/27 13:19
 */
public class CropOptions implements Serializable {

    private int aspectX = 1;
    private int aspectY = 1;
    private int outputX;
    private int outputY;

    public CropOptions() {

    }

    int getAspectX() {
        return aspectX;
    }

    /**
     * 裁剪宽度比例 与aspectY组合，如16:9
     *
     * @param aspectX
     * @return
     */
    public CropOptions setAspectX(int aspectX) {
        this.aspectX = aspectX;
        return this;
    }

    int getAspectY() {
        return aspectY;
    }

    /**
     * 高度比例 与aspectX组合，如16:9
     *
     * @param aspectY
     * @return
     */
    public CropOptions setAspectY(int aspectY) {
        this.aspectY = aspectY;
        return this;
    }

    int getOutputX() {
        return outputX;
    }

    /**
     * 输出图片的宽度
     *
     * @param outputX
     * @return
     */
    public CropOptions setOutputX(int outputX) {
        this.outputX = outputX;
        return this;
    }

    int getOutputY() {
        return outputY;
    }

    /**
     * 输入图片的高度
     *
     * @param outputY
     * @return
     */
    public CropOptions setOutputY(int outputY) {
        this.outputY = outputY;
        return this;
    }


}
