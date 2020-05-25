# 说明

## 微信登录

微信登录需要在`包名.wxapi` 包中添加一个名为 `WXEntryActivity` 的 Activity，并继承该 module 提供的 `AbsWXEntryActivity`。

```java
/**
 * 微信分享、登录回调
 *
 * @author Ztiany
 */
@SuppressWarnings("all")
public class WXEntryActivity extends AbsWXEntryActivity {


}
```

manifest 配置参考

```xml
        <!--微信分享回调-->
        <activity
            android:name="应用包名.wxapi.WXEntryActivity"
            android:exported="true"
            android:screenOrientation="portrait"/>
```

## 微信支付

微信支付需要在`包名.wxapi` 包中添加一个名为 `WXPayEntryActivity` 的 Activity，并继承该 module 提供的 `AbsWeChatPayEntryActivity`。

```java
/**
 * 微信分享、登录回调
 *
 * @author Ztiany
 */
@SuppressWarnings("all")
public class WXPayEntryActivity extends AbsWeChatPayEntryActivity {

}
```

manifest 配置参考

```xml
        <!--微信分享回调-->
        <activity
            android:name="应用包名.wxapi.WXPayEntryActivity"
            android:exported="true"
            android:screenOrientation="portrait"/>
```

## 支付宝支付

- sdk 版本：alipaySdk-15.6.2-20190416165100-noUtdid

## QQ

manifest 配置参考

```xml
        <activity
            android:name="com.tencent.tauth.AuthActivity"
            android:launchMode="singleTask"
            android:noHistory="true">
            <intent-filter>
                <action android:name="android.intent.action.VIEW"/>

                <category android:name="android.intent.category.DEFAULT"/>
                <category android:name="android.intent.category.BROWSABLE"/>
                
                <!--1101491530 为你得 AppId-->
                <data android:scheme="tencent1101491530"/>
            </intent-filter>
        </activity>
        
        <activity
            android:name="com.tencent.connect.common.AssistActivity"
            android:configChanges="orientation|keyboardHidden|screenSize"
            android:theme="@android:style/Theme.Translucent.NoTitleBar"/>
```