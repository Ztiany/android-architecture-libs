package com.android.sdk.social.wechat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.android.sdk.social.common.Status;
import com.android.sdk.social.common.Utils;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import timber.log.Timber;


@SuppressWarnings("unused")
public class WeChatManager {

    private static WeChatManager sWeChatManager;
    private final IWXAPI mWxApi;

    private static String sAppId;
    private static String sAppSecret;

    /**
     * @param context   上下文
     * @param appId     app id
     * @param appSecret 密钥，如果要进行微信登录，则需要提供。
     */
    public static synchronized void initWeChatSDK(Context context, String appId, String appSecret) {
        if (sWeChatManager != null) {
            throw new UnsupportedOperationException("WeChatManager has already been initialized");
        }
        sAppId = appId;
        sAppSecret = appSecret;
        sWeChatManager = new WeChatManager(context);
    }

    private static synchronized void destroy() {
        sWeChatManager.currentState = null;
        sWeChatManager.mWxApi.unregisterApp();
        sWeChatManager.mWxApi.detach();
        sWeChatManager = null;
    }

    static String getAppId() {
        Utils.requestNotNull(sAppId, "weChat app id");
        return sAppId;
    }

    static String getAppSecret() {
        Utils.requestNotNull(sAppSecret, "weChat appSecret");
        return sAppSecret;
    }

    private WeChatManager(Context context) {
        mWxApi = WXAPIFactory.createWXAPI(context.getApplicationContext(), getAppId(), false);
        mWxApi.registerApp(getAppId());
        sWeChatManager = this;
    }

    public static synchronized WeChatManager getInstance() {
        if (sWeChatManager == null) {
            throw new UnsupportedOperationException("WeChatManager has not been initialized");
        }
        return sWeChatManager;
    }

    ///////////////////////////////////////////////////////////////////////////
    // 通用
    ///////////////////////////////////////////////////////////////////////////

    static boolean handleIntent(Intent intent, IWXAPIEventHandler iwxapiEventHandler) {
        WeChatManager weChatManager = sWeChatManager;
        if (weChatManager != null) {
            return weChatManager.mWxApi.handleIntent(intent, iwxapiEventHandler);
        } else {
            Timber.w("WeChatManager handleIntent called, but WeChatManager has not been initialized");
        }
        return false;
    }

    @SuppressWarnings("unused")
    public boolean isInstalledWeChat() {
        return mWxApi.isWXAppInstalled();
    }

    static void handleOnWxEntryResp(BaseResp baseResp) {
        Timber.d("handleOnWxEntryResp type = " + baseResp.getType() + "errStr = " + baseResp.errStr);
        if (ConstantsAPI.COMMAND_SENDAUTH == baseResp.getType()) {
            handAuthResp(baseResp);
        } else if (ConstantsAPI.COMMAND_PAY_BY_WX == baseResp.getType()) {
            handleOnWxEntryPayResp(baseResp);
        } else if (ConstantsAPI.COMMAND_LAUNCH_WX_MINIPROGRAM == baseResp.getType()) {
            handleMiniProgramResp(baseResp);
        } else if (ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX == baseResp.getType()) {
            handleSendMessageResp(baseResp);
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // 小程序
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 跳转小程序
     *
     * @param userName 小程序原始id
     * @param path     拉起小程序页面的可带参路径，不填默认拉起小程序首页
     */
    public void navToMinProgram(String userName, String path, boolean isRelease) {
        WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
        req.userName = userName;
        req.path = path;
        // 可选打开开发版，体验版和正式版
        req.miniprogramType = isRelease ? WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE : WXLaunchMiniProgram.Req.MINIPROGRAM_TYPE_PREVIEW;
        mWxApi.sendReq(req);
    }

    private static void handleMiniProgramResp(BaseResp baseResp) {
        if (baseResp instanceof WXLaunchMiniProgram.Resp) {
            WXLaunchMiniProgram.Resp launchMiniProResp = (WXLaunchMiniProgram.Resp) baseResp;
            //对应小程序组件 <button open-type="launchApp"> 中的 app-parameter 属性
            String extMsg = launchMiniProResp.extMsg;
            Timber.d("handleMiniProgramResp = " + baseResp.errStr);
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // 登录与认证
    ///////////////////////////////////////////////////////////////////////////

    private static void handAuthResp(BaseResp baseResp) {
        WeChatManager weChatManager = sWeChatManager;

        if (weChatManager == null || !(baseResp instanceof SendAuth.Resp)) {
            return;
        }

        SendAuth.Resp resp = (SendAuth.Resp) baseResp;

        if (BaseResp.ErrCode.ERR_OK == resp.errCode) {
            Timber.i("WeChatManager handleOnResp success, resp = " + resp);
            if (resp.state.equals(weChatManager.currentState)) {
                weChatManager.handWeChatLoginResp(resp);
            } else {
                Timber.w("WeChatManager handleChatLoginResp called, but state is not matched");
            }
        } else {
            Timber.w("WeChatManager handleOnResp fail, resp = " + resp);
            int requestType = weChatManager.mRequestType;
            if (requestType == REQUEST_TYPE_CODE) {
                weChatManager.mAuthCode.postValue(Status.error(new WeChatLoginException(resp.errCode, resp.errStr)));
            } else if (requestType == REQUEST_TYPE_USER_INFO) {
                weChatManager.mUserInfo.postValue(Status.error(new WeChatLoginException(resp.errCode, resp.errStr)));
            }
        }
    }

    private int mRequestType;
    private String currentState;

    private static final int REQUEST_TYPE_CODE = 1;
    private static final int REQUEST_TYPE_USER_INFO = 2;

    private final MutableLiveData<Status<WXUser>> mUserInfo = new SingleLiveData<>();
    private final MutableLiveData<Status<String>> mAuthCode = new SingleLiveData<>();

    /**
     * 默认的state，用于保持请求和回调的状态，授权请求后原样带回给第三方。该参数可用于防止csrf攻击（跨站请求伪造攻击），
     * 建议第三方带上该参数，可设置为简单的随机数加session进行校验
     */
    private static final String DEFAULT_STATE = "wechat_sdk_login";

    /**
     * 固定的请求域，应用授权作用域，如获取用户个人信息则填写snsapi_userinfo
     */
    private static final String GET_USER_INFO_SCOPE = "snsapi_userinfo";

    @NonNull
    @SuppressWarnings("WeakerAccess")
    public LiveData<Status<WXUser>> loginResult() {
        return mUserInfo;
    }

    @NonNull
    public LiveData<Status<String>> authResult() {
        return mAuthCode;
    }

    /**
     * 请求微信登录，通过 {@link #loginResult() } 获取结果
     *
     * @param state 第三方程序发送时用来标识其请求的唯一性的标志，由第三方程序调用 sendReq 时传入，由微信终端回传，state 字符串长度不能超过 1K
     */
    public void requestChatLogin(String state) {
        mRequestType = REQUEST_TYPE_USER_INFO;
        doRequest(state);
    }

    /**
     * 请求获取微信授权码，通过 {@link #authResult()} 获取结果
     *
     * @param state 第三方程序发送时用来标识其请求的唯一性的标志，由第三方程序调用 sendReq 时传入，由微信终端回传，state 字符串长度不能超过 1K
     */
    public void requestAuthCode(String state) {
        mRequestType = REQUEST_TYPE_CODE;
        doRequest(state);
    }

    private void doRequest(String state) {
        SendAuth.Req req = new SendAuth.Req();
        req.scope = GET_USER_INFO_SCOPE;
        req.state = TextUtils.isEmpty(state) ? DEFAULT_STATE : state;
        currentState = req.state;
        mWxApi.sendReq(req);
    }

    @SuppressLint("CheckResult")
    private void handWeChatLoginResp(SendAuth.Resp resp) {
        if (mRequestType == REQUEST_TYPE_CODE) {

            mAuthCode.postValue(Status.success(resp.code));

        } else if (mRequestType == REQUEST_TYPE_USER_INFO) {

            mUserInfo.postValue(Status.loading());
            Timber.i("handWeChatLoginResp called, requesting user info.......");
            WeChatLoginImpl.doWeChatLogin(resp)
                    .subscribe(
                            wxUser -> {
                                Timber.i("handWeChatLoginResp success and result = " + wxUser);
                                processRequestUserInfoResult(wxUser);
                            },
                            throwable -> {
                                Timber.i("handWeChatLoginResp fail and result = " + throwable);
                                mUserInfo.postValue(Status.error(throwable));
                            });
        }
    }

    private void processRequestUserInfoResult(WXUser wxUser) {
        if (wxUser != null) {
            if (wxUser.getErrcode() != 0) {
                mUserInfo.postValue(Status.error(new WeChatLoginException(wxUser.getErrcode(), wxUser.getErrmsg())));
            } else {
                mUserInfo.postValue(Status.success(wxUser));
            }
        } else {
            mUserInfo.postValue(Status.error(new WeChatLoginException(BaseResp.ErrCode.ERR_COMM, null)));
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // 支付
    ///////////////////////////////////////////////////////////////////////////

    private SingleLiveData<Status> mWXPayResultData = new SingleLiveData<>();

    public void doPay(PayInfo payInfo) {
        PayReq req = new PayReq();
        req.appId = payInfo.getAppId();
        req.nonceStr = payInfo.getNonceStr();
        req.partnerId = payInfo.getPartnerId();
        req.prepayId = payInfo.getPrepayId();
        req.packageValue = payInfo.getPackage();
        req.timeStamp = payInfo.getTimestamp();
        req.sign = payInfo.getSign();
        mWxApi.sendReq(req);
    }

    public LiveData<Status> getWXPayResultData() {
        return mWXPayResultData;
    }

    private static void handleOnWxEntryPayResp(BaseResp baseResp) {
        WeChatManager weChatManager = sWeChatManager;
        if (weChatManager == null) {
            return;
        }
        if (baseResp.errCode == 0) {//成功展示成功页面
            weChatManager.mWXPayResultData.postValue(Status.success());
        } else if (baseResp.errCode == -1) {//错误可能的原因：签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、其他异常等。
            weChatManager.mWXPayResultData.postValue(Status.error(new WeChatPayException(baseResp.errStr)));
        } else if (baseResp.errCode == -2) {//用户取消无需处理。发生场景：用户不支付了，点击取消，返回APP。
            weChatManager.mWXPayResultData.postValue(Status.cancel());
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // 分享
    ///////////////////////////////////////////////////////////////////////////

    public boolean share(WeChatShareInfo.ShareContent content) {
        try {
            SendMessageToWX.Req baseReq = WeChatShareInfo.buildReq(content);
            mWxApi.sendReq(baseReq);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void handleSendMessageResp(BaseResp baseResp) {
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                //todo success
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                //todo canceled
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
            case BaseResp.ErrCode.ERR_SENT_FAILED:
            case BaseResp.ErrCode.ERR_UNSUPPORT:
            case BaseResp.ErrCode.ERR_COMM:
            case BaseResp.ErrCode.ERR_BAN:
                //todo error
                break;
        }
    }

}