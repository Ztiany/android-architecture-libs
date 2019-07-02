package com.android.sdk.qrcode.zxing;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;

import com.android.sdk.qrcode.QRCodeView;
import com.android.sdk.qrcode.Debug;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

public class ZXingView extends QRCodeView {

    private MultiFormatReader mMultiFormatReader;

    public ZXingView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ZXingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initMultiFormatReader();
    }

    private void initMultiFormatReader() {
        mMultiFormatReader = new MultiFormatReader();
        mMultiFormatReader.setHints(QRCodeDecoder.HINTS);
    }

    @Override
    public String processData(byte[] data, int width, int height) {
        String result = null;
        Result rawResult = null;

        try {
            PlanarYUVLuminanceSource source;
            Rect rect = getFramingRectInPreview(width, height);
            Debug.log("rect:" + rect);
            Debug.log("data width * height:" + width + "*" + height);
            if (rect != null) {
                source = new PlanarYUVLuminanceSource(data, width, height, rect.left, rect.top, rect.width(), rect.height(), false);
            } else {
                source = new PlanarYUVLuminanceSource(data, width, height, 0, 0, width, height, false);
            }
            rawResult = mMultiFormatReader.decodeWithState(new BinaryBitmap(new HybridBinarizer(source)));
            Debug.log("rawResult:" + rawResult);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mMultiFormatReader.reset();
        }
        if (rawResult != null) {
            result = rawResult.getText();
        }
        return result;
    }

}