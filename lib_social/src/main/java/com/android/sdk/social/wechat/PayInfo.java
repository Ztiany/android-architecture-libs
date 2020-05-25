package com.android.sdk.social.wechat;


import androidx.annotation.NonNull;

@SuppressWarnings("unused,WeakerAccess")
public class PayInfo {

    private String mAppId;
    private String mPartnerId;
    private String mPrepayId;
    private String mPackage;
    private String mNonceStr;
    private String mTimestamp;
    private String mSign;

    public String getAppId() {
        return mAppId;
    }

    public void setAppId(String appId) {
        mAppId = appId;
    }

    public String getPartnerId() {
        return mPartnerId;
    }

    public void setPartnerId(String partnerId) {
        mPartnerId = partnerId;
    }

    public String getPrepayId() {
        return mPrepayId;
    }

    public void setPrepayId(String prepayId) {
        mPrepayId = prepayId;
    }

    public String getPackage() {
        return mPackage;
    }

    public void setPackage(String aPackage) {
        mPackage = aPackage;
    }

    public String getNonceStr() {
        return mNonceStr;
    }

    public void setNonceStr(String nonceStr) {
        mNonceStr = nonceStr;
    }

    public String getTimestamp() {
        return mTimestamp;
    }

    public void setTimestamp(String timestamp) {
        mTimestamp = timestamp;
    }

    public String getSign() {
        return mSign;
    }

    public void setSign(String sign) {
        mSign = sign;
    }

    @NonNull
    @Override
    public String toString() {
        return "PayInfo{" +
                "mAppId='" + mAppId + '\'' +
                ", mPartnerId='" + mPartnerId + '\'' +
                ", mPrepayId='" + mPrepayId + '\'' +
                ", mPackage='" + mPackage + '\'' +
                ", mNonceStr='" + mNonceStr + '\'' +
                ", mTimestamp='" + mTimestamp + '\'' +
                ", mSign='" + mSign + '\'' +
                '}';
    }

}
