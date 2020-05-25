package com.android.sdk.social.wechat;


import com.tencent.mm.opensdk.modelbase.BaseResp;

import androidx.annotation.NonNull;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-11-07 17:35
 */
class WeChatLoginException extends Exception {

    private int mErrorCode;
    private String mErrMsg;

    WeChatLoginException(int errorCode, String errMsg) {
        mErrorCode = errorCode;
        mErrMsg = errMsg;
    }

    @Override
    public String getMessage() {
        return getMessageFormBaseResp();
    }

    @NonNull
    @Override
    public String toString() {
        return getMessageFormBaseResp() + "---" + super.toString();
    }

    private String getMessageFormBaseResp() {
        String message = "未知错误";

        switch (mErrorCode) {
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                message = "发送取消";
                break;
            case BaseResp.ErrCode.ERR_SENT_FAILED:
                message = "发送失败";
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                message = "发送被拒绝";
                break;
            case BaseResp.ErrCode.ERR_UNSUPPORT:
                message = "不支持错误";
                break;
            case BaseResp.ErrCode.ERR_COMM:
                message = "一般错误";
                break;
        }
        return message + " errMsg = " + mErrMsg;
    }

}
