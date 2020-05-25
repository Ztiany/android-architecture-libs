package com.android.sdk.social.wechat;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import androidx.annotation.NonNull;

@SuppressWarnings("unused")
public class WXUser extends AuthResult {

    /**
     * 普通用户的标识，对当前开发者帐号唯一
     */
    private String openid;

    private String nickname;

    /**
     * 普通用户性别，1为男性，2为女性
     */
    private int sex;

    private String province;
    private String city;
    private String country;

    /**
     * 用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空
     */
    private String headimgurl;

    /**
     * 开发者最好保存unionID信息，以便以后在不同应用之间进行用户信息互通。
     */
    private String unionid;

    private List<String> privilege;

    public String getOpenid() {
        return openid;
    }

    public String getNickname() {
        return nickname;
    }

    public int getSex() {
        return sex;
    }

    public String getProvince() {
        return province;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public String getUnionid() {
        return unionid;
    }

    public List<String> getPrivilege() {
        return privilege;
    }

    @NotNull
    @NonNull
    @Override
    public String toString() {
        return "WXUser{" +
                "openid='" + openid + '\'' +
                ", nickname='" + nickname + '\'' +
                ", sex=" + sex +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", headimgurl='" + headimgurl + '\'' +
                ", unionid='" + unionid + '\'' +
                ", privilege=" + privilege +
                ", errcode=" + getErrcode() +
                ", errmsg=" + getErrmsg() +
                '}';
    }

}